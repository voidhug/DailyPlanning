package voidhug.test.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import voidhug.test.bean.Schedule;

/**
 * Created by voidhug on 15/5/2.
 */
public class DBUtil extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "class_db";
    private static int DATABASE_VERSION = 12;

    private static final String SCHEDULE_TABLE="schedule_table";
    private static final String WEEKSEQ="weekseq";
    private static final String WEEKDAY="weekday";
    private static final String CLASSTYPE="classtype";
    private static final String FIRST="first";
    private static final String SECOND="second";
    private static final String THIRD="third";
    private static final String FOUTH="fouth";
    private static final String FIFTH="fifth";
    private static final String SIXTH="sixth";
    private static final String SEVENTH="seventh";
    private static final String EIGHTH="eighth";
    private static final String NINTH="ninth";
    private static final String TENTH="tenth";


    public DBUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立课程表的SQL语句
        String classsql=SCHEDULE_TABLE+"("+WEEKDAY+" INTEGER primary key autoincrement, "
                + WEEKSEQ+ " text, " +CLASSTYPE+ " text, "
                +FIRST+ " text, "+SECOND+ " text, "+THIRD+ " text, "+FOUTH+ " text, "+FIFTH+ " text, "
                +SIXTH+ " text, "+SEVENTH+ " text, "+EIGHTH+ " text, "+NINTH+ " text, "+TENTH+ " text "
                +")";
        try {
            db.execSQL("create table if not exists "+classsql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public long insertClass(Schedule schedule) {

        SQLiteDatabase db = this.getWritableDatabase();
		/* 将新增的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(WEEKSEQ, schedule.getWeekseq());
        cv.put(WEEKDAY, schedule.getWeekday());
        cv.put(CLASSTYPE, schedule.getClasstype());
        cv.put(FIRST, schedule.getFirst());
        cv.put(SECOND, schedule.getSecond());
        cv.put(THIRD, schedule.getThird());
        cv.put(FOUTH, schedule.getFouth());
        cv.put(FIFTH, schedule.getFifth());
        cv.put(SIXTH, schedule.getSixth());
        cv.put(SEVENTH, schedule.getSeventh());
        cv.put(EIGHTH, schedule.getEighth());
        cv.put(NINTH, schedule.getNinth());
        cv.put(TENTH, schedule.getTenth());
        long row = db.insert(SCHEDULE_TABLE, null, cv);
        db.close();
        return row;
    }

    public void updateClass(Schedule schedule) {

        SQLiteDatabase db = this.getWritableDatabase();

        String where = WEEKDAY + " = ?";
        String[] whereValue = {schedule.getWeekday()};
		/* 将新增的值放入ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(WEEKSEQ, schedule.getWeekseq());
        cv.put(WEEKDAY, schedule.getWeekday());
        cv.put(CLASSTYPE, schedule.getClasstype());
        cv.put(FIRST, schedule.getFirst());
        cv.put(SECOND, schedule.getSecond());
        cv.put(THIRD, schedule.getThird());
        cv.put(FOUTH, schedule.getFouth());
        cv.put(FIFTH, schedule.getFifth());
        cv.put(SIXTH, schedule.getSixth());
        cv.put(SEVENTH, schedule.getSeventh());
        cv.put(EIGHTH, schedule.getEighth());
        cv.put(NINTH, schedule.getNinth());
        cv.put(TENTH, schedule.getTenth());
        db.update(SCHEDULE_TABLE, cv, where, whereValue);
        db.close();
    }

    public ArrayList<Schedule> ClassQry() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Schedule> qryclasslist=new ArrayList<Schedule>();
        Schedule schedule;
        Cursor cursor=db.query(SCHEDULE_TABLE, null, null, null, null, null,null);
        while(cursor.moveToNext()){
            schedule=new Schedule();
            schedule.setWeekseq(cursor.getString(cursor.getColumnIndex(WEEKSEQ)));
            schedule.setWeekday(cursor.getString(cursor.getColumnIndex(WEEKDAY)));
            schedule.setClasstype(cursor.getString(cursor.getColumnIndex(CLASSTYPE)));
            schedule.setFirst(cursor.getString(cursor.getColumnIndex(FIRST)));
            schedule.setSecond(cursor.getString(cursor.getColumnIndex(SECOND)));
            schedule.setThird(cursor.getString(cursor.getColumnIndex(THIRD)));
            schedule.setFouth(cursor.getString(cursor.getColumnIndex(FOUTH)));
            schedule.setFifth(cursor.getString(cursor.getColumnIndex(FIFTH)));
            schedule.setSixth(cursor.getString(cursor.getColumnIndex(SIXTH)));
            schedule.setSeventh(cursor.getString(cursor.getColumnIndex(SEVENTH)));
            schedule.setEighth(cursor.getString(cursor.getColumnIndex(EIGHTH)));
            schedule.setNinth(cursor.getString(cursor.getColumnIndex(NINTH)));
            schedule.setTenth(cursor.getString(cursor.getColumnIndex(TENTH)));
            qryclasslist.add(schedule);
        }
        cursor.close();
        db.close();

        return qryclasslist;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
