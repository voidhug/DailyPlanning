package voidhug.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import voidhug.test.TaskAlertActivity;
import voidhug.test.bean.TaskBean;

/**
 * Created by voidhug on 15/4/28.
 */
public class TestReceiver extends BroadcastReceiver {

    public static final String ALARM_ALERT_ACTION = "voidhug.test.ALARM_ALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(ALARM_ALERT_ACTION)) {
                Intent at_intent = new Intent(context, TaskAlertActivity.class);
                at_intent.putExtra(TaskBean.ID, intent.getIntExtra(TaskBean.ID, 1));
                at_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(at_intent);
            }
        }
    }
}
