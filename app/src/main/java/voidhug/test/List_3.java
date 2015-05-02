package voidhug.test;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import voidhug.test.bean.HeartMessage;
import voidhug.test.dao.DataBaseUtil;
import voidhug.test.date.DateUtils;

/**
 * Created by voidhug on 15/5/2.
 */
public class List_3 extends Fragment {

    private List<HeartMessage> messages = new ArrayList<HeartMessage>();

    private int messageDirection = HeartMessage.MESSAGE_FROM;
    private ListView chatHistoryLv;
    private Button sendBtn;
    private EditText textEditor;
    private Cursor databaseCur;
    private HeartAdapter chatHistoryAdapter;
    private TextView heartListViewEmptyTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_3, container, false);
        chatHistoryLv = (ListView) view.findViewById(R.id.chatting_history_lv);
        setAdapterForThis();
        sendBtn = (Button) view.findViewById(R.id.send_button);
        textEditor = (EditText) view.findViewById(R.id.text_editor);

        sendBtn.setOnClickListener(l);

        chatHistoryLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                //执行删除操作
                showCustomMessage("确认删除","确认删除本条心语",(int)id,position);
                return false;
            }
        });
        return view;
    }

    // 设置adapter
    private void setAdapterForThis() {
        initMessages();
        chatHistoryAdapter = new HeartAdapter();
        chatHistoryLv.setAdapter(chatHistoryAdapter);
        heartListViewEmptyTV = new TextView(this.getActivity());
        heartListViewEmptyTV.setGravity(Gravity.CENTER);
        heartListViewEmptyTV.setTextSize(20);	//设置字体大小
        heartListViewEmptyTV.setTextColor(0xff000000);
        List_3.this.getActivity().addContentView(heartListViewEmptyTV, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        chatHistoryLv.setEmptyView(heartListViewEmptyTV);
    }

    private void initMessages() {
        //查询数据库
        databaseCur = DataBaseUtil.query(List_3.this.getActivity(), HeartMessage.TABLE_NAME,
                null, null, null, null, null, HeartMessage.ID+" ASC");
        for(databaseCur.moveToFirst(); !databaseCur.isAfterLast();databaseCur.moveToNext()) {
            HeartMessage hm = HeartMessage.generateHeartMessage(databaseCur);
            hm.setDirection(messageDirection);
            messages.add(hm);
            changeMessageDirection();
        }
        DataBaseUtil.closeDatabase();
    }

    private View.OnClickListener l = new View.OnClickListener() {

        public void onClick(View v) {

            if (v.getId() == sendBtn.getId()) {
                String str = textEditor.getText().toString();
                String sendStr;
                if (str != null
                        && (sendStr = str.trim().replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "")
                        .replaceAll("\f", "")) != "") {
                    sendMessage(sendStr, messageDirection);
                    changeMessageDirection();
                }
                textEditor.setText("");
            }
        }

        // 模拟发送消息
        private void sendMessage(String sendStr, int flag) {
            ContentValues values = new ContentValues();
            String datetime = DateUtils.nowDetail();
            values.put(HeartMessage.DATE, datetime);
            values.put(HeartMessage.HEART_CONTENT, sendStr);
            long id = DataBaseUtil.insert(List_3.this.getActivity(), HeartMessage.TABLE_NAME, HeartMessage.ID, values);

            if(id != -1) {
                HeartMessage hm = new HeartMessage((int)id, datetime, sendStr, flag);
                messages.add(hm);
                chatHistoryAdapter.notifyDataSetChanged();
            }
        }
    };

    private void changeMessageDirection() {
        if(messageDirection == HeartMessage.MESSAGE_FROM) {
            messageDirection = HeartMessage.MESSAGE_TO;
        } else {
            messageDirection = HeartMessage.MESSAGE_FROM;
        }
    }

    public class HeartAdapter extends BaseAdapter {
        public HeartAdapter() {
        }

        public int getCount() {
            return messages.size();
        }

        public Object getItem(int position) {
            return messages.get(position);
        }

        public long getItemId(int position) {
            return messages.get(position).getHeartId();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            HeartMessage message = messages.get(position);
            if (convertView == null
                    || (holder = (ViewHolder) convertView.getTag()).flag != message
                    .getDirection()) {

                holder = new ViewHolder();
                if (message.getDirection() == HeartMessage.MESSAGE_FROM) {
                    holder.flag = HeartMessage.MESSAGE_FROM;

                    convertView = LayoutInflater.from(List_3.this.getActivity()).inflate(
                            R.layout.heart_item_from, null);
                } else {
                    holder.flag = HeartMessage.MESSAGE_TO;
                    convertView = LayoutInflater.from(List_3.this.getActivity()).inflate(
                            R.layout.heart_item_to, null);
                }

                holder.text = (TextView) convertView
                        .findViewById(R.id.heart_content_itv);
                convertView.setTag(holder);
            }
            holder.text.setText(message.getContent());

            return convertView;
        }

        // 优化listview的Adapter
        class ViewHolder {
            TextView text;
            int flag;
        }

    }

    private void showCustomMessage(String pTitle, final String pMsg, final int id, final int position) {
        final Dialog lDialog = new Dialog(List_3.this.getActivity(),
                android.R.style.Theme_Translucent_NoTitleBar);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(R.layout.iphone_alert_dialog_layout);
        ((TextView) lDialog.findViewById(R.id.dialog_title_delete)).setText(pTitle);
        ((TextView) lDialog.findViewById(R.id.dialog_message)).setText(pMsg);
        ((Button) lDialog.findViewById(R.id.cancel))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // write your code to do things after users clicks
                        // CANCEL
                        lDialog.dismiss();
                    }
                });
        ((Button) lDialog.findViewById(R.id.ok))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // write your code to do things after users clicks OK
                        lDialog.dismiss();
                        int rows = DataBaseUtil.delete(List_3.this.getActivity(), HeartMessage.TABLE_NAME, HeartMessage.ID+"="+id, null);
                        if(rows > 0) {
                            //更新listview
                            messages.remove(position);
                            chatHistoryAdapter.notifyDataSetChanged();
                            Toast.makeText(List_3.this.getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(List_3.this.getActivity(), "数据库更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        lDialog.show();

    }

}
