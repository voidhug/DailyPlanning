package voidhug.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

import voidhug.test.bean.Schedule;
import voidhug.test.dao.DBUtil;

/**
 * Created by voidhug on 15/5/2.
 */
public class ScheduleInsert extends Activity implements  android.view.View.OnClickListener, AdapterView.OnItemSelectedListener {
    private DBUtil dbUtil;
    private ArrayList<Schedule> schedulelist;

    private EditText et_classname_0,et_classname_1,et_classname_3,et_classname_5,et_classname_7,
            et_roomno_0,et_roomno_1,et_roomno_3,et_roomno_5,et_roomno_7;
    private ImageButton btn_insert,btn_cancle;
    private Spinner spinner_weekday;
    private String WEEK[] = {"星期一", "星期二", "星期三", "星期四", "星期五"};
    private boolean isExist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_schedule_insert);
        dbUtil=new DBUtil(this);
        schedulelist=dbUtil.ClassQry();
        initView();


    }


    private void initView() {
        // 设置按钮
        btn_insert = (ImageButton) findViewById(R.id.btn_insert);
        btn_cancle = (ImageButton) findViewById(R.id.btn_cancle);
        btn_insert.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);

        et_classname_0=(EditText) findViewById(R.id.et_classname_0);
        et_classname_1=(EditText) findViewById(R.id.et_classname_1);
        et_classname_3=(EditText) findViewById(R.id.et_classname_3);
        et_classname_5=(EditText) findViewById(R.id.et_classname_5);
        et_classname_7=(EditText) findViewById(R.id.et_classname_7);

        et_roomno_0=(EditText) findViewById(R.id.et_roomno_0);
        et_roomno_1=(EditText) findViewById(R.id.et_roomno_1);
        et_roomno_3=(EditText) findViewById(R.id.et_roomno_3);
        et_roomno_5=(EditText) findViewById(R.id.et_roomno_5);
        et_roomno_7=(EditText) findViewById(R.id.et_roomno_7);

        // 设置spinner的显示
        spinner_weekday = (Spinner) findViewById(R.id.spinner_weekday);
        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, WEEK);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_weekday.setAdapter(spinneradapter);
        spinner_weekday.setOnItemSelectedListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert :
                Schedule schedule=new Schedule();
                schedule.setWeekday(spinner_weekday.getSelectedItemPosition()+"");
                schedule.setFirst(et_classname_0.getText().toString());
                schedule.setSecond(et_roomno_0.getText().toString());
                schedule.setThird(et_classname_1.getText().toString());
                schedule.setFouth(et_roomno_1.getText().toString());
                schedule.setFifth(et_classname_3.getText().toString());
                schedule.setSixth(et_roomno_3.getText().toString());
                schedule.setSeventh(et_classname_5.getText().toString());
                schedule.setEighth(et_roomno_5.getText().toString());
                schedule.setNinth(et_classname_7.getText().toString());
                schedule.setTenth(et_roomno_7.getText().toString());
                if(isExist){
                    dbUtil.updateClass(schedule);
                }else{
                    dbUtil.insertClass(schedule);
                }

                dbUtil.close();
                finish();
                break;
            case R.id.btn_cancle :
                finish();
                overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
                break;
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果按下的是返回键，并且没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
            return false;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        initValue();
        for (Schedule schedule : schedulelist) {
            if(Integer.valueOf(schedule.getWeekday())==position){

                et_classname_0.setText(schedule.getFirst());
                et_roomno_0.setText(schedule.getSecond());

                et_classname_1.setText(schedule.getThird());
                et_roomno_1.setText(schedule.getFouth());

                et_classname_3.setText(schedule.getFifth());
                et_roomno_3.setText(schedule.getSixth());

                et_classname_5.setText(schedule.getSeventh());
                et_roomno_5.setText(schedule.getEighth());

                et_classname_7.setText(schedule.getNinth());
                et_roomno_7.setText(schedule.getTenth());

                isExist=true;
            }else{
                isExist=false;
            }
        }
    }

    private void initValue() {
        et_classname_0.setText("");
        et_roomno_0.setText("");

        et_classname_1.setText("");
        et_roomno_1.setText("");

        et_classname_3.setText("");
        et_roomno_3.setText("");

        et_classname_5.setText("");
        et_roomno_5.setText("");

        et_classname_7.setText("");
        et_roomno_7.setText("");

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
