package voidhug.test;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import voidhug.test.bean.TaskBean;
import voidhug.test.constant.TaskConstant;
import voidhug.test.dao.DataBaseUtil;
import voidhug.test.date.DateUtils;


/**
 * Created by voidhug on 15/4/24.
 */

public class List_1 extends Fragment {

    private int taskId = 0;
    private int curPosition = 0;
    private Button buttonQueryDateForPlay;
    private Calendar calendar = Calendar.getInstance();
    private EditText queryDate;
    private Cursor taskCur;
    private List<Map<String, Object>> taskItemList = null;
    private static TodayTaskListViewAdapter adapter = null;
    private ListView taskList = null;
    private DatePickerDialog dialog;

    String s;


    private class TodayTaskListViewAdapter extends BaseAdapter {


        final class TaskListItemView {
            public TextView taskInfo;//
            public TextView taskTimeAndPositionInfo;
            public Button buttonDeleteTask;
        }
        @Override
        public int getCount() {
            return taskItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.parseInt(taskItemList.get(position).get(TaskBean.ID).toString());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TaskListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new TaskListItemView();
                convertView = LayoutInflater.from(List_1.this.getActivity()).inflate(R.layout.history_task_item, null);
                listItemView.taskInfo = (TextView) convertView.findViewById(R.id.history_task_info);
                listItemView.taskTimeAndPositionInfo = (TextView) convertView.findViewById(R.id.history_task_time_position_textview);
                listItemView.buttonDeleteTask = (Button) convertView.findViewById(R.id.history_task_delete_btn);
                convertView.setTag(listItemView);
            } else {
                listItemView = (TaskListItemView) convertView.getTag();
            }
            listItemView.taskInfo.setText((String) taskItemList.get(position).get(TaskBean.TASK_NAME));
            String positionName = (String) taskItemList.get(position).get(TaskBean.POSITION_NAME);
            String timeAndPosition = "截止时间:" + DateUtils.getTaskTime((String)taskItemList.get(position).get(TaskBean.DATETIME));

            if (!positionName.equals("")) {
                timeAndPosition += ("\n地点:" + positionName);
            }
            listItemView.taskTimeAndPositionInfo.setText(timeAndPosition);
            listItemView.buttonDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskId = Integer.parseInt(taskItemList
                            .get(position).get(TaskBean.ID).toString());
                    curPosition = position;

                    showDeleteMessage("确认删除", "确认删除此活动？");
                }
            });

            return convertView;
        }
    }

    private void showDeleteMessage(String pTitle, final String pMsg) {
        final Dialog lDialog = new Dialog(List_1.this.getActivity(),
                android.R.style.Theme_Translucent_NoTitleBar);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(R.layout.iphone_alert_dialog_layout);
        ((TextView) lDialog.findViewById(R.id.dialog_title_delete))
                .setText(pTitle);
        ((TextView) lDialog.findViewById(R.id.dialog_message))
                .setText(pMsg);
        (lDialog.findViewById(R.id.cancel))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        lDialog.dismiss();
                    }
                });
        (lDialog.findViewById(R.id.ok))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        deleteTaskItem();
                        lDialog.dismiss();
                    }
                });
        lDialog.show();


    }

    private void deleteTaskItem() {
        DataBaseUtil.delete(List_1.this.getActivity(), TaskBean.TABLE_NAME, TaskBean.ID + "=" + taskId, null);
        Toast.makeText(List_1.this.getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        taskItemList.remove(curPosition);
        adapter.notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_1, container, false);
        queryDate = (EditText) view.findViewById(R.id.queryDate);
        buttonQueryDateForPlay = (Button) view.findViewById(R.id.button3);
        buttonQueryDateForPlay.setOnClickListener(new buttonQueryDateForPlay());
        return view;
    }

    private class buttonQueryDateForPlay implements View.OnClickListener {

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                s = "" + year + "-";
                s += (month < 10) ? ("0" + (month + 1) + "-") : ((month + 1) + "-");
                s += (dayOfMonth < 10) ? ("0" + dayOfMonth) : (dayOfMonth);
                queryDate.setText(s);
                showForList();
                taskList = (ListView) List_1.this.getActivity().findViewById(R.id.history_task_list_view);
                taskList.setAdapter(adapter);
                dialog.dismiss();
            }
        };

        @Override
        public void onClick(View v) {
            dialog = new DatePickerDialog(List_1.this.getActivity(),
                                                            dateListener, calendar.get(Calendar.YEAR),
                                                            calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    private void showForList () {
        taskItemList = new ArrayList<Map<String, Object>>();
        taskCur = DataBaseUtil.query(List_1.this.getActivity(),
                TaskBean.TABLE_NAME, null, TaskBean.DATETIME + " LIKE ?",
                new String[]{ s + "%"}, null, null, TaskBean.ID
                        + " ASC");
        for (taskCur.moveToFirst(); !taskCur.isAfterLast(); taskCur.moveToNext()) {
            Map<String, Object> map = TaskBean.generateTask(taskCur);
            taskItemList.add(map);
        }

        DataBaseUtil.closeDatabase();
        adapter = new TodayTaskListViewAdapter();

    }


}
