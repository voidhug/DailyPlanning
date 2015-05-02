package voidhug.test.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import voidhug.test.bean.HeartMessage;
import voidhug.test.bean.TaskBean;

/**
 * Created by voidhug on 15/4/24.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "TimeMaster"; //数据库名称
    private static int DATABASE_VERSION = 2;  //数据库版本


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * SQLite语句，创建一个表
     * */
    private static String CREATE_TASK_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TaskBean.TABLE_NAME + "("
            + TaskBean.ID + " INTEGER PRIMARY KEY,"
            + TaskBean.DATETIME + " TEXT,"
            + TaskBean.TASK_NAME + " TEXT,"
            + TaskBean.POSITION_NAME + " TEXT,"
            + TaskBean.TIME_ALERT_FLAG + " INTEGER,"
            + TaskBean.IF_COMPLETE + " INTEGER)";


    //创建随想表
    private static String CREATE_HEART_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + HeartMessage.TABLE_NAME + "("
            + HeartMessage.ID + " INTEGER PRIMARY KEY,"
            + HeartMessage.DATE + " TEXT,"
            + HeartMessage.HEART_CONTENT + " TEXT)";

    //删除随想表
    private static String DROP_HEART_TABLE_SQL = "DROP TABLE IF EXISTS " + HeartMessage.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE_SQL);
        db.execSQL(CREATE_HEART_TABLE_SQL); //创建随想表
    }

    //删除任务表
    private static String DROP_TASK_TABLE_SQL = "DROP TABLE IF EXISTS " + TaskBean.TABLE_NAME;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TASK_TABLE_SQL); //删除任务表
        db.execSQL(DROP_HEART_TABLE_SQL); //删除随想表
        onCreate(db);

    }
}
