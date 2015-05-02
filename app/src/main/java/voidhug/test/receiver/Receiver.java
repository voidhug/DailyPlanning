package voidhug.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import voidhug.test.alarm.Alarm;

/**
 * Created by voidhug on 15/4/28.
 */
public class Receiver extends BroadcastReceiver {

    //public static final String ALARM_ALERT_ACTION = "voidhug.test.ALARM_ALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Alarm.ALARM_REGISTRATION_DETAIL_ACTION)) {
                Alarm.enableAlarm(context, intent, action);
            }else if(action.equals(Alarm.ALARM_CANCEL_ACTION)) {
                Alarm.disableAlarm(context, intent);
            }

        }
    }
}
