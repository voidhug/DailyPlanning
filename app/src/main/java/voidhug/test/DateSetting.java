package voidhug.test;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.DatePicker;

import java.util.Date;

import voidhug.test.util.Utils;

/**
 * Created by voidhug on 15/5/2.
 */
public class DateSetting extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences share = getSharedPreferences("INIT", Context.MODE_PRIVATE);
        String str = share.getString("SET", "0");

        if ("0".equals(str)) {
            showDialog(2);
        }

        addPreferencesFromResource(R.xml.preference);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 在欢迎界面设置BACK键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
        }
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        if (preference.getKey().equals("SET")) {
            showDialog(1);
        }
        return false;
    }

    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case 1 :
                Date date = new Date();
                DatePickerDialog dpd = new DatePickerDialog(this,
                        onDateSetListener, date.getYear() + 1900,
                        date.getMonth(), date.getDate());

                return dpd;

            case 2 :
                return new AlertDialog.Builder(this).setTitle("您还没有定义第一周").create();
            default :
                break;
        }
        return super.onCreateDialog(id);
    }


    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            String dateString = Integer.toString(year) + "-"
                    + Integer.toString(monthOfYear) + "-"
                    + Integer.toString(dayOfMonth);
            int setInt = Utils.getDayOfYear(year, monthOfYear, dayOfMonth);

            SharedPreferences share = getSharedPreferences("INIT",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();
            editor.putString("SET", Integer.toString(setInt));
            editor.putString("SET_DATE", dateString);
            editor.commit();
//            Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
//            DateSetting.this.sendBroadcast(intent);

        }
    };


}
