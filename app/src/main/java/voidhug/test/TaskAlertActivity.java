package voidhug.test;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import voidhug.test.alarm.Alarm;
import voidhug.test.bean.TaskBean;
import voidhug.test.dao.DataBaseUtil;
import voidhug.test.date.DateUtils;
import voidhug.test.service.AlertService;

/**
 * Created by voidhug on 15/4/28.
 */
public class TaskAlertActivity extends Activity {

    private Button confirmBtn;
    private int taskId;
    private Cursor databaseCur;
    private TextView alertViewContent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, AlertService.class);
        startService(i);



        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        km.newKeyguardLock("Tag For Debug").disableKeyguard();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(LayoutInflater.from(this).inflate(R.layout.task_alert_layout, null));
        alertViewContent = (TextView) findViewById(R.id.task_alert_dialog_content);
        confirmBtn = (Button) findViewById(R.id.task_alert_dialog_confirm_btn);
        confirmBtn.requestFocus();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Alarm.ALARM_CANCEL_ACTION);
                intent.putExtra(Alarm.ID, taskId);
                Alarm.disableAlarm(TaskAlertActivity.this, intent);
                Intent i= new Intent(TaskAlertActivity.this,AlertService.class);
                stopService(i);
                finish();

            }
        });

        taskId = getIntent().getIntExtra(TaskBean.ID, 1);
        String taskName = "";
        String taskPosition = "";
        String dateTime = "";
        databaseCur = DataBaseUtil.query(TaskAlertActivity.this,
                TaskBean.TABLE_NAME, new String[]{TaskBean.POSITION_NAME,
                        TaskBean.TASK_NAME, TaskBean.DATETIME}, TaskBean.ID
                        + "=" + taskId, null, null, null, null);

        for (databaseCur.moveToFirst(); !databaseCur.isAfterLast(); databaseCur
                .moveToNext()) {
            taskName = databaseCur.getString(databaseCur
                    .getColumnIndex(TaskBean.TASK_NAME));
            taskPosition = databaseCur.getString(databaseCur
                    .getColumnIndex(TaskBean.POSITION_NAME));
            dateTime = databaseCur.getString(databaseCur
                    .getColumnIndex(TaskBean.DATETIME));
        }
        DataBaseUtil.closeDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append("活动名称:" + taskName + "\n");
        sb.append("活动时间:" + DateUtils.getTaskTime(dateTime) + "\n");
        sb.append("活动地点:" + (taskPosition.equals("") ? "未知" : taskPosition));

        alertViewContent.setText(sb.toString());

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_HOME == keyCode
                || KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
