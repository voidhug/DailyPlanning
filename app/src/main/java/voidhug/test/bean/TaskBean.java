package voidhug.test.bean;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by voidhug on 15/4/24.
 */
public class TaskBean {

    public static final String TABLE_NAME = "task";
    public static final String ID = "task_id";
    public static final String TASK_NAME = "task_name";
    public static final String DATETIME = "task_datetime";
    public static final String IF_COMPLETE = "if_complete";
    public static final String POSITION_NAME = "position_name";
    public static final String TIME_ALERT_FLAG = "time_alert";



    private String taskName;
    private String positionName;


    public static Map<String, Object> generateTask(Cursor cursor) {
        Map<String, Object> map = new HashMap<String, Object>();
        int id = cursor.getInt(cursor.getColumnIndex(ID));
        map.put(ID, id + "");
        String taskName = cursor.getString(cursor.getColumnIndex(TASK_NAME));
        map.put(TASK_NAME, taskName);
        String datetime = cursor.getString(cursor.getColumnIndex(DATETIME));
        map.put(DATETIME, datetime);
        String positionName = cursor.getString(cursor.getColumnIndex(POSITION_NAME));
        map.put(POSITION_NAME, positionName);
        int timeAlertFlag = cursor.getInt(cursor.getColumnIndex(TIME_ALERT_FLAG));
        map.put(TIME_ALERT_FLAG, timeAlertFlag+"");
        int ifComplete = cursor.getInt(cursor.getColumnIndex(IF_COMPLETE));
        map.put(IF_COMPLETE, ifComplete+"");

        return map;
    }
}
