package voidhug.test;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import voidhug.test.date.DateDay;

/**
 * Created by voidhug on 15/5/2.
 */
public class List_2 extends Fragment implements android.view.View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView tv_weekseq,tv_weekday;
    private Button btn_classedit;
    private GridView gridview;
    private DateDay dateday;


    private final int[]array = {R.drawable.mon_icon,R.drawable.tue_icon,R.drawable.wed_icon,R.drawable.thu_icon,R.drawable.fri_icon};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_weekseq = (TextView) view.findViewById(R.id.tv_weekseq);
        tv_weekday = (TextView) view.findViewById(R.id.tv_weekday);
        tv_weekseq.setTextColor(getResources().getColor(R.color.purple));
        tv_weekday.setTextColor(getResources().getColor(R.color.blue));
        btn_classedit = (Button) view.findViewById(R.id.btn_classedit);
        btn_classedit.setOnClickListener(this);


        gridview = (GridView) view.findViewById(R.id.gridview);

        String[] from = {"imageview"};
        int[] to = { R.id.cell_imageview};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this.getActivity(), getData(), R.layout.cell, from, to);
        gridview.setAdapter(simpleAdapter);
        gridview.setOnItemClickListener(this);


    }

    public ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> date = new ArrayList<HashMap<String,Object>>();
        for(int i = 0; i < array.length; ++i){
            HashMap<String, Object> map=new HashMap<String, Object>();
            map.put("imageview", array[i]);
            date.add(map);
        }
        return date;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_classedit :
                Intent intent=new Intent(this.getActivity(), ScheduleInsert.class);
                List_2.this.startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        intent.setClass(this.getActivity(), ScheduleShow.class);
        intent.putExtra("WeekDay", position);
        List_2.this.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        dateday = null;
        dateday = new DateDay(List_2.this.getActivity());
        tv_weekseq.setText(dateday.getWeedSeq()+"周 ");
        tv_weekday.setText(dateday.getWeekDayCH());
    }



}
