package voidhug.test.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import voidhug.test.bean.TaskBean;
import voidhug.test.receiver.TestReceiver;

/**
 * Created by voidhug on 15/4/28.
 */
public class Alarm  {

    public static final String ALARM_REGISTRATION_DETAIL_ACTION = "voidhug.test.ALARM_REGISTRATION_DETAIL";
    public static final String ALARM_CANCEL_ACTION = "voidhug.test.ALARM_CANCEL";
    public static final String ID = "alarm_id";
    public static final String HOUR = "alarm_hour";
    public static final String MINUTE = "alarm_minute";
    public static final String ALARM_REGISTRATION_BUNDLE_TAG = "alarm_registration";



    public static void enableAlarm(Context context, Intent intent, String action) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (action.equals(ALARM_REGISTRATION_DETAIL_ACTION)) {
            Bundle bundle = intent.getBundleExtra(ALARM_REGISTRATION_BUNDLE_TAG);
            Intent alarmIntent = new Intent(TestReceiver.ALARM_ALERT_ACTION);
            alarmIntent.putExtra(TaskBean.ID, bundle.getInt(Alarm.ID));
            Calendar alarmCalendar = Calendar.getInstance();
            alarmCalendar.setTimeInMillis(System.currentTimeMillis());
            alarmCalendar.set(Calendar.HOUR_OF_DAY, bundle.getInt(Alarm.HOUR));
            alarmCalendar.set(Calendar.MINUTE, bundle.getInt(Alarm.MINUTE));
            alarmCalendar.set(Calendar.SECOND, 0);
            alarmCalendar.set(Calendar.MILLISECOND, 0);
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, bundle.getInt(Alarm.ID), alarmIntent, 0);

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmPendingIntent);
        }
    }

    public static void disableAlarm(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int taskId = intent.getIntExtra(Alarm.ID, 0);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
                taskId, new Intent(TestReceiver.ALARM_ALERT_ACTION), 0);
        alarmManager.cancel(alarmPendingIntent);
    }



}
