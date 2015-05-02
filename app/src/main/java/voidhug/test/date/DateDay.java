package voidhug.test.date;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import voidhug.test.util.Utils;

/**
 * Created by voidhug on 15/5/2.
 */
public class DateDay {
    private String weekDay, SetStr;
    private int NowInt, SetInt;
    private Time timeNow = new Time();

    private static String[] WEEKDAYCH = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public DateDay(Context context) {

        SharedPreferences share = context.getSharedPreferences("INIT", Context.MODE_PRIVATE);
        SetStr = share.getString("SET", "0");
        timeNow.setToNow();
        NowInt = Utils.getDayOfYear(timeNow.year, timeNow.month, timeNow.monthDay);
        SetInt = Integer.parseInt(SetStr);

    }

    public String getWeedSeq() {
        if (SetStr.equals("0")) {
            weekDay = "未定义";
        } else {
            weekDay = ((NowInt - SetInt) / 7 + 1) + "";
        }
        return weekDay;// 获得"k"
    }

    public String getWeekDayCH() {
        return WEEKDAYCH[timeNow.weekDay];
    }

}
