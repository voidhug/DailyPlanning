package voidhug.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import voidhug.test.bean.Schedule;
import voidhug.test.dao.DBUtil;

/**
 * Created by voidhug on 15/5/2.
 */
public class WeekDayFragment extends Fragment {

    private int weekday = -1;
    private DBUtil dbUtil;


    private TextView tv_weekday,
            tv_classname_0,tv_classname_1,tv_classname_3,tv_classname_5,tv_classname_7,
            tv_roomno_0,tv_roomno_1,tv_roomno_3,tv_roomno_5,tv_roomno_7;
    private ArrayList<Schedule> schedulelist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        weekday = getArguments().getInt("1");
        return inflater.inflate(R.layout.app_schedule_show_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_weekday=(TextView) view.findViewById(R.id.tv_weekday);
        tv_classname_0=(TextView) view.findViewById(R.id.tv_classname_0);
        tv_classname_1=(TextView) view.findViewById(R.id.tv_classname_1);
        tv_classname_3=(TextView) view.findViewById(R.id.tv_classname_3);
        tv_classname_5=(TextView) view.findViewById(R.id.tv_classname_5);
        tv_classname_7=(TextView) view.findViewById(R.id.tv_classname_7);
        tv_roomno_0=(TextView) view.findViewById(R.id.tv_roomno_0);
        tv_roomno_1=(TextView) view.findViewById(R.id.tv_roomno_1);
        tv_roomno_3=(TextView) view.findViewById(R.id.tv_roomno_3);
        tv_roomno_5=(TextView) view.findViewById(R.id.tv_roomno_5);
        tv_roomno_7=(TextView) view.findViewById(R.id.tv_roomno_7);

        tv_weekday.setText(formatWeekDay(weekday));

        initClassValue();
    }

    private String formatWeekDay(int position) {
        String wdstr="";
        switch (position) {
            case 0 :
                wdstr="星期一";
                break;
            case 1 :
                wdstr="星期二";
                break;
            case 2 :
                wdstr="星期三";
                break;
            case 3 :
                wdstr="星期四";
                break;
            case 4 :
                wdstr="星期五";
                break;
        }
        return wdstr;
    }

    private void initClassValue() {
        dbUtil=new DBUtil(this.getActivity());
        schedulelist=dbUtil.ClassQry();
        dbUtil.close();
        initValue();
        for (Schedule schedule : schedulelist) {
            if(Integer.valueOf(schedule.getWeekday())==weekday){

                tv_classname_0.setText(schedule.getFirst());
                tv_roomno_0.setText(schedule.getSecond());

                tv_classname_1.setText(schedule.getThird());
                tv_roomno_1.setText(schedule.getFouth());

                tv_classname_3.setText(schedule.getFifth());
                tv_roomno_3.setText(schedule.getSixth());

                tv_classname_5.setText(schedule.getSeventh());
                tv_roomno_5.setText(schedule.getEighth());

                tv_classname_7.setText(schedule.getNinth());
                tv_roomno_7.setText(schedule.getTenth());
            }
        }
    }


    private void initValue() {
        tv_classname_0.setText("");
        tv_roomno_0.setText("");

        tv_classname_1.setText("");
        tv_roomno_1.setText("");

        tv_classname_3.setText("");
        tv_roomno_3.setText("");

        tv_classname_5.setText("");
        tv_roomno_5.setText("");

        tv_classname_7.setText("");
        tv_roomno_7.setText("");

    }
}
