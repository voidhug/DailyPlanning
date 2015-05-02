package voidhug.test;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import voidhug.test.alarm.Alarm;
import voidhug.test.bean.TaskBean;
import voidhug.test.constant.MenuItemId;
import voidhug.test.constant.TaskConstant;
import voidhug.test.dao.DataBaseUtil;
import voidhug.test.date.DateUtils;


/**
 * Created by voidhug on 15/4/23.
 */
public class List_0 extends Fragment {


    private Button add_task_btn;
    private Cursor todayTaskCur;

    private List<Map<String, Object>> taskItemList = null; // List保存今天任务信息
    private static TodayTaskListViewAdapter adapter = null; // 今天列表视图adapter
    private ListView taskList = null; // 今天任务信息列表视图

    private int todayClickViewId = 0;
    private int todayClickPosition = 0;


    private int alarmHour;
    private int alarmMinute;
    private int currentHour;
    private int currentMinute;
    private String time = "";
    private int timeAlertValue = TaskConstant.NO_TIME_ALERT;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_0, container, false);
        initTodayAdapter();
        taskList = (ListView) view.findViewById(R.id.task_list_view);
        taskList.setAdapter(adapter);

        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todayClickViewId = (int) id;
                todayClickPosition = position;
                return false;
            }
        });


        taskList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderIcon(android.R.drawable.ic_menu_more);
                menu.setHeaderTitle(R.string.task_long_click_menu_title);
                menu.add(0, MenuItemId.TASK_ITEM_LONG_CLICK_MENU_EDIT, 0, R.string.task_long_click_menu_edit);
                menu.add(0, MenuItemId.TASK_ITEM_LONG_CLICK_MENU_DELETE, 0, R.string.task_long_click_menu_delete);
            }
        });

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentValues values = new ContentValues();
                if (Integer.parseInt(taskItemList.get(position).get(TaskBean.IF_COMPLETE).toString()) == TaskConstant.TASK_NOT_COMPLETE) {
                    values.put(TaskBean.IF_COMPLETE, TaskConstant.TASK_COMPLETE);
                    int rows = DataBaseUtil.update(List_0.this.getActivity(), TaskBean.TABLE_NAME, values, TaskBean.ID + "=" + id, null);
                    if (rows > 0) {
                        Map<String, Object> map = taskItemList.get(position);
                        map.put(TaskBean.IF_COMPLETE, TaskConstant.TASK_COMPLETE + "");
                        taskItemList.set(position, map);
                        adapter.notifyDataSetChanged();
                        cancelAlarm((int) id);
                        Toast.makeText(List_0.this.getActivity(), "活动完成", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(List_0.this.getActivity(), "数据库更新失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        add_task_btn = (Button) view.findViewById(R.id.add_task_btn);
        add_task_btn.setOnClickListener(new AddTaskListener());
        return view;
    }


    private void initTodayAdapter() {
        taskItemList = new ArrayList<Map<String, Object>>();
        initTodayListItem();
        adapter = new TodayTaskListViewAdapter();
    }


    private void initTodayListItem() {
        todayTaskCur = DataBaseUtil.query(List_0.this.getActivity(),
                TaskBean.TABLE_NAME, null, TaskBean.DATETIME + " LIKE ?",
                new String[] { DateUtils.now() + "%" }, null, null, TaskBean.ID
                        + " ASC");

        for (todayTaskCur.moveToFirst(); !todayTaskCur.isAfterLast(); todayTaskCur.moveToNext()) {
            Map<String, Object> map = TaskBean.generateTask(todayTaskCur);
            taskItemList.add(map);
        }

        DataBaseUtil.closeDatabase();

    }



    private class AddTaskListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int[] nowHourAndMinute = DateUtils.getNowHourAndMinute();
            currentHour = nowHourAndMinute[0];
            currentMinute = nowHourAndMinute[1];
            alarmHour = nowHourAndMinute[0];
            alarmMinute = nowHourAndMinute[1];
            time = DateUtils.formatTime(alarmHour, alarmMinute);
            timeAlertValue = TaskConstant.NO_TIME_ALERT;

            final View addTaskView = getViewById(R.layout.add_task_dialog_layout);
            final EditText taskContent = (EditText) addTaskView.findViewById(R.id.et_task_info);
            final EditText taskPosition = (EditText) addTaskView
                    .findViewById(R.id.et_task_position);
            TimePicker timePicker = (TimePicker) addTaskView.findViewById(R.id.tp_task_time);
            timePicker.setCurrentHour(alarmHour);
            timePicker.setCurrentMinute(alarmMinute);
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    time = DateUtils.formatTime(hourOfDay, minute);
                    alarmHour = hourOfDay;
                    alarmMinute = minute;
                }
            });
            final CheckBox timeAlertCB = (CheckBox) addTaskView
                    .findViewById(R.id.cb_time_alert);
            timeAlertCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (timeAlertCB.isChecked()) {
                        timeAlertValue = TaskConstant.TIME_ALERT;
                    } else {
                        timeAlertValue = TaskConstant.NO_TIME_ALERT;
                    }
                }
            });




            new AlertDialog.Builder(List_0.this.getActivity())
                    .setTitle(R.string.add_task_dialog_title)
                    .setIcon(R.drawable.add_task_dialog_icon)
                    .setView(addTaskView)
                    .setPositiveButton(R.string.save_task_dialog_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String taskContentStr = taskContent.getText().toString().trim();
                            String taskPositionStr = taskPosition.getText().toString().trim();
                            if (taskContentStr.equals("")) {
                                Toast.makeText(List_0.this.getActivity(), "活动名称不能为空", Toast.LENGTH_SHORT).show();
                            } else if (alarmHour < currentHour || ((alarmHour == currentHour) && (alarmMinute < currentMinute))) {
                                Toast.makeText(List_0.this.getActivity(), "时间无效", Toast.LENGTH_SHORT).show();
                            } else {
                                String datetime = DateUtils.now() + " " + time;
                                ContentValues values = new ContentValues();
                                values.put(TaskBean.TASK_NAME, taskContentStr);
                                values.put(TaskBean.DATETIME, datetime);
                                values.put(TaskBean.IF_COMPLETE, TaskConstant.TASK_NOT_COMPLETE);
                                values.put(TaskBean.POSITION_NAME, taskPositionStr);
                                values.put(TaskBean.TIME_ALERT_FLAG, timeAlertValue);

                                long id = DataBaseUtil.insert(
                                        List_0.this.getActivity(),
                                        TaskBean.TABLE_NAME,
                                        TaskBean.ID,
                                        values);

                                if (id == -1) {
                                    Toast.makeText(
                                            List_0.this.getActivity(),
                                            "数据库插入数据失败",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } else {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(TaskBean.TASK_NAME,
                                            taskContentStr);
                                    map.put(TaskBean.DATETIME,
                                            datetime);
                                    map.put(TaskBean.POSITION_NAME,
                                            taskPositionStr);
                                    map.put(TaskBean.TIME_ALERT_FLAG,
                                            timeAlertValue + "");

                                    map.put(TaskBean.IF_COMPLETE,
                                            TaskConstant.TASK_NOT_COMPLETE
                                                    + "");
                                    map.put(TaskBean.ID, id + "");
                                    refreshTaskAfterInsert(map);
                                    if (timeAlertValue == TaskConstant.TIME_ALERT) {
                                        registerAlarm((int) id, alarmHour, alarmMinute);
                                    }

                                }
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        }
    }

    private View getViewById(int resourceId) {
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        return inflater.inflate(resourceId, null);
    }

    private class TodayTaskListViewAdapter extends BaseAdapter {

        final class TaskListItemView {
            public TextView taskInfo;//
            public ImageView timeAlert;
            public TextView taskTimeAndPositionInfo;
            public ImageView taskcompleteState;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            TaskListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new TaskListItemView();
                convertView = LayoutInflater.from(List_0.this.getActivity()).inflate(R.layout.today_task_item, null);
                listItemView.taskInfo = (TextView) convertView.findViewById(R.id.today_task_info);
                listItemView.timeAlert = (ImageView) convertView.findViewById(R.id.today_time_alert);
                listItemView.taskTimeAndPositionInfo = (TextView) convertView.findViewById(R.id.today_task_time_position_textview);
                listItemView.taskcompleteState = (ImageView) convertView.findViewById(R.id.today_task_complete_state);
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
            if (Integer.parseInt(((String) taskItemList.get(position).get(TaskBean.TIME_ALERT_FLAG))) == TaskConstant.NO_TIME_ALERT) {
                listItemView.timeAlert.setVisibility(View.INVISIBLE);
            } else {
                listItemView.timeAlert.setVisibility(View.VISIBLE);
            }

            if (TaskConstant.TASK_NOT_COMPLETE == Integer.parseInt(taskItemList.get(position).get(TaskBean.IF_COMPLETE).toString())) {
                listItemView.taskcompleteState.setBackgroundResource(R.drawable.theme_checked);
                listItemView.taskTimeAndPositionInfo.setTextColor(0xff000000);

                listItemView.taskInfo.setTextColor(0xff000000);
            } else {
                listItemView.taskcompleteState.setBackgroundResource(R.drawable.theme_checked_disable);
                listItemView.taskTimeAndPositionInfo.setTextColor(0xff808080);
                listItemView.taskInfo.setTextColor(0xff808080);
            }
            return convertView;
        }
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MenuItemId.TASK_ITEM_LONG_CLICK_MENU_DELETE:
                deleteTodayTaskItem();
                return true;
            case MenuItemId.TASK_ITEM_LONG_CLICK_MENU_EDIT:
                todayTaskCur = DataBaseUtil.query(List_0.this.getActivity(),
                        TaskBean.TABLE_NAME, null, TaskBean.ID + "="
                                + todayClickViewId, null, null, null, null);
                Map<String, Object> map = null;
                for (todayTaskCur.moveToFirst(); !todayTaskCur.isAfterLast(); todayTaskCur
                        .moveToNext()) {
                    map = TaskBean.generateTask(todayTaskCur);
                }
                DataBaseUtil.closeDatabase();


                final View editTaskView = getViewById(R.layout.add_task_dialog_layout);
                final EditText taskContent = (EditText) editTaskView
                        .findViewById(R.id.et_task_info);
                taskContent.setText(map.get(TaskBean.TASK_NAME).toString());
                final EditText taskPosition = (EditText) editTaskView
                        .findViewById(R.id.et_task_position);
                taskPosition.setText(map.get(TaskBean.POSITION_NAME).toString());

                int[] minuteAndHour = DateUtils.getHourAndMinuteByDateTime(map.get(
                        TaskBean.DATETIME).toString());

                TimePicker timePicker = (TimePicker) editTaskView
                        .findViewById(R.id.tp_task_time);
                timePicker.setCurrentHour(minuteAndHour[0]);
                timePicker.setCurrentMinute(minuteAndHour[1]);
                int[] nowHourAndMinute = DateUtils.getNowHourAndMinute();
                currentHour = nowHourAndMinute[0];
                currentMinute = nowHourAndMinute[1];
                alarmHour = minuteAndHour[0];
                alarmMinute = minuteAndHour[1];
                final int alarmHourAfterUpdate = alarmHour;
                final int alarmMinuteAfterUpdate = alarmMinute;
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        time = DateUtils.formatTime(hourOfDay, minute);
                        alarmHour = hourOfDay;
                        alarmMinute = minute;
                    }
                });


                final CheckBox timeAlertCB = (CheckBox) editTaskView
                        .findViewById(R.id.cb_time_alert);

                timeAlertValue = Integer.parseInt(map.get(TaskBean.TIME_ALERT_FLAG)
                        .toString());
                if (timeAlertValue == TaskConstant.TIME_ALERT) {
                    timeAlertCB.setChecked(true);
                } else {
                    timeAlertCB.setChecked(false);
                }

                timeAlertCB
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         boolean isChecked) {
                                // TODO Auto-generated method stub
                                if (timeAlertCB.isChecked()) {
                                    timeAlertValue = TaskConstant.TIME_ALERT;
                                } else {
                                    timeAlertValue = TaskConstant.NO_TIME_ALERT;
                                }
                            }
                        });


                new AlertDialog.Builder(List_0.this.getActivity())
                        .setTitle(R.string.edit_task_dialog_title)
                        .setIcon(android.R.drawable.ic_menu_edit)
                        .setView(editTaskView)
                        .setPositiveButton(R.string.save_task_dialog_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String taskContentStr = taskContent.getText().toString().trim();
                                String taskPositionStr = taskPosition.getText().toString().trim();
                                if (taskContentStr.equals("")) {
                                    Toast.makeText(List_0.this.getActivity(), "活动名称不能为空", Toast.LENGTH_SHORT).show();
                                } else if (alarmHour < currentHour || ((alarmHour == currentHour) && (alarmMinute < currentMinute))) {
                                    Toast.makeText(List_0.this.getActivity(), "时间无效", Toast.LENGTH_SHORT).show();
                                    alarmHour = alarmHourAfterUpdate;
                                    alarmMinute = alarmMinuteAfterUpdate;
                                } else {

                                    String datetime = DateUtils.now() + " " + time;
                                    ContentValues values = new ContentValues();
                                    values.put(TaskBean.TASK_NAME, taskContentStr);
                                    values.put(TaskBean.DATETIME, datetime);
                                    values.put(TaskBean.IF_COMPLETE, TaskConstant.TASK_NOT_COMPLETE);
                                    values.put(TaskBean.POSITION_NAME, taskPositionStr);
                                    values.put(TaskBean.TIME_ALERT_FLAG, timeAlertValue);

                                    deleteTodayTaskItemAfterUpdate();

                                    DataBaseUtil.insert(
                                            List_0.this.getActivity(),
                                            TaskBean.TABLE_NAME,
                                            TaskBean.ID + "=" + todayClickViewId,
                                            values);

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(TaskBean.TASK_NAME,
                                            taskContentStr);
                                    map.put(TaskBean.DATETIME,
                                            datetime);
                                    map.put(TaskBean.POSITION_NAME,
                                            taskPositionStr);
                                    map.put(TaskBean.TIME_ALERT_FLAG,
                                            timeAlertValue + "");

                                    map.put(TaskBean.IF_COMPLETE,
                                            TaskConstant.TASK_NOT_COMPLETE
                                                    + "");
                                    map.put(TaskBean.ID, todayClickViewId + "");

                                    refreshTodayTaskAfterUpdate(todayClickPosition, map);
                                    Toast.makeText(List_0.this.getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                    cancelAlarm(todayClickViewId);

                                    if (timeAlertValue == TaskConstant.TIME_ALERT) {
                                        registerAlarm(todayClickViewId, alarmHour, alarmMinute);
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel_dialog_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
        }
        return super.onContextItemSelected(item);
    }




    private void refreshTodayTaskAfterUpdate(int position,
                                             Map<String, Object> map) {
        //
        taskItemList.set(position, map);
        adapter.notifyDataSetChanged();
    }

    private void deleteTodayTaskItemAfterUpdate() {
        DataBaseUtil.delete(List_0.this.getActivity(), TaskBean.TABLE_NAME, TaskBean.ID + "=" + todayClickViewId, null);
    }

    private void deleteTodayTaskItem() {
        DataBaseUtil.delete(List_0.this.getActivity(), TaskBean.TABLE_NAME, TaskBean.ID + "=" + todayClickViewId, null);
        Toast.makeText(List_0.this.getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
        taskItemList.remove(todayClickPosition);
        cancelAlarm(todayClickViewId);
        adapter.notifyDataSetChanged();
    }

    private void refreshTaskAfterInsert(Map<String, Object> map) {
        taskItemList.add(map);
        adapter.notifyDataSetChanged();
    }

    private void cancelAlarm(int id) {
        Intent intent = new Intent(Alarm.ALARM_CANCEL_ACTION);
        intent.putExtra(Alarm.ID, id);
        List_0.this.getActivity().sendBroadcast(intent);
    }

    private void registerAlarm(int id, int hourOfDay, int minute) {
        Intent intent = new Intent(Alarm.ALARM_REGISTRATION_DETAIL_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(Alarm.ID, id);
        bundle.putInt(Alarm.HOUR, hourOfDay);
        bundle.putInt(Alarm.MINUTE, minute);
        intent.putExtra(Alarm.ALARM_REGISTRATION_BUNDLE_TAG, bundle);
        List_0.this.getActivity().sendBroadcast(intent);
    }



}
