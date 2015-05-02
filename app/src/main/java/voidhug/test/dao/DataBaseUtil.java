package voidhug.test.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by voidhug on 15/4/24.
 */
public class DataBaseUtil {

    private static SQLiteDatabase db = null;
    private static DatabaseHelper helper = null;

    /**
     * 1.得到DatabaseHelper对象；2.通过该对象得到一个可写的表；3.插入一条纪录；4.关闭数据库；5.返回行号；
     * */
    public static long insert(Context context, String tableName, String id, ContentValues values) {

        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        long rows = db.insert(tableName, id, values);
        closeDatabase();
        return rows;
    }


    public static Cursor query(Context context, String table, String[] columes, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
        return db.query(table, columes, selection, selectionArgs, groupBy, having, orderBy);
    }


    public static int update(Context context, String tableName, ContentValues values, String where, String[] whereArgs) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        int rows = db.update(tableName,values,where,whereArgs);
        closeDatabase();
        return rows;
    }



    public static int delete(Context context, String tableName, String where, String[] whereArgs) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        int rows = db.delete(tableName, where, whereArgs);
        closeDatabase();
        return rows;
    }

    public static void closeDatabase() {
        db.close();
    }
}
