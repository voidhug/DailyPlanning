package voidhug.test.date;



import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by voidhug on 15/4/24.
 */
public class DateUtils {

    public static String MIDDLE = "12:00";

    /**
     * 1.得到数组；2.得到日历实例；3.取得当前毫秒数，为日历实例设置时间；4.从日历实例中得到当前小时，当前分钟；5.返回数组；
     * */
    public static int[] getNowHourAndMinute() {
        int[] nowHourAndMinute = new int[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        nowHourAndMinute[0] = calendar.get(Calendar.HOUR_OF_DAY);
        nowHourAndMinute[1] = calendar.get(Calendar.MINUTE);
        return nowHourAndMinute;
    }

    /**
     * 1.得到一个空字符串；2.如果传入的时间为个位数(如4)，则time -> 04，接着看分钟，如分钟为4，则time -> 04:04；3.返回time这个字符串
     * */
    public static String formatTime(int hourOfDay, int minute) {
        String time = "";
        if (hourOfDay < 10) {
            time += ("0" + hourOfDay);
        } else {
            time += hourOfDay;
        }
        Log.i("ing", minute + "");
        if (minute < 10) {
            time += (":0" + minute);
        } else {
            time += (":" + minute);
        }
        return time;
    }

    /**
     * 1.得到格式化器；2.得到当前日期；3.格式化日期；
     * */
    public static String now() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }


    public static String getTaskTime(String datetime) {
        String[] splitStr = datetime.split(" ", 2);
        if (splitStr[1].equals(MIDDLE)) {
            return "中午12:00";
        } else {
            String[] time = splitStr[1].split(":");
            if (Integer.parseInt(time[0]) >= 12) {
                return "下午" + splitStr[1];
            } else {
                return "上午" + splitStr[1];
            }
        }
    }

    public static int[] getHourAndMinuteByDateTime(String datetime) {
        String[] split = datetime.split(" ", 2)[1].split("\\:");
        int[] hourAndMinute = new int[2];
        hourAndMinute[0] = Integer.parseInt(split[0]);
        hourAndMinute[1] = Integer.parseInt(split[1]);
        return hourAndMinute;
    }


    public static String getHeartMessageTimeByDateTime(String datetime) {
        int[] yearMonthDayHourAndMinute = getYearMonthDayHourMinuteAndSecondByDateTime(datetime);
        StringBuilder sb = new StringBuilder();
        sb.append(yearMonthDayHourAndMinute[0]+"年");
        sb.append(yearMonthDayHourAndMinute[1]>=10?yearMonthDayHourAndMinute[1]+"月":"0"+yearMonthDayHourAndMinute[1]+"月");
        sb.append(yearMonthDayHourAndMinute[2]>=10?yearMonthDayHourAndMinute[2]+"日":"0"+yearMonthDayHourAndMinute[2]+"日");
        sb.append(yearMonthDayHourAndMinute[3]>=10?yearMonthDayHourAndMinute[3]+":":"0"+yearMonthDayHourAndMinute[3]+":");
        sb.append(yearMonthDayHourAndMinute[4]>=10?yearMonthDayHourAndMinute[4]+":":"0"+yearMonthDayHourAndMinute[4]+":");
        sb.append(yearMonthDayHourAndMinute[5]>=10?yearMonthDayHourAndMinute[5]:"0"+yearMonthDayHourAndMinute[5]);
        return sb.toString();
    }

    public static int[] getYearMonthDayHourMinuteAndSecondByDateTime(String datetime) {
        int[] yearMonthDayHourAndMinute = new int[6];
        String[] spliteOnce = datetime.split(" ", 2);
        String[] spliteDate = spliteOnce[0].split("\\-");
        String[] spliteTime = spliteOnce[1].split("\\:");
        yearMonthDayHourAndMinute[0] = Integer.parseInt(spliteDate[0]);
        yearMonthDayHourAndMinute[1] = Integer.parseInt(spliteDate[1]);
        yearMonthDayHourAndMinute[2] = Integer.parseInt(spliteDate[2]);
        yearMonthDayHourAndMinute[3] = Integer.parseInt(spliteTime[0]);
        yearMonthDayHourAndMinute[4] = Integer.parseInt(spliteTime[1]);
        yearMonthDayHourAndMinute[5] = Integer.parseInt(spliteTime[2]);
        return yearMonthDayHourAndMinute;
    }

    public static String nowDetail() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }



}
