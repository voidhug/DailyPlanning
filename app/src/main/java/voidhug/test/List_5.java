package voidhug.test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;
import voidhug.test.bean.User;
import voidhug.test.constant.RsgisterConstant;
import voidhug.test.util.CommonUtils;

/**
 * Created by voidhug on 15/5/3.
 */
public class List_5 extends Fragment implements View.OnClickListener{

    private EditText et_username, et_password;
    private Button btn_login;
    private TextView btn_register;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_register = (TextView) view.findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(RsgisterConstant.ACTION_REGISTER_SUCCESS_FINISH);
        this.getActivity().registerReceiver(receiver, filter);

        return view;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && RsgisterConstant.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
                List_5.this.getActivity().finish();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment list_4 = new List_4();
            fragmentManager.beginTransaction().replace(R.id.content_frame, list_4).commit();
        } else {
            boolean isNetConnected = CommonUtils.isNetworkAvailable(List_5.this.getActivity());
            if(!isNetConnected){
                ShowToast(R.string.network_tips);
                return;
            }
            login();
        }
    }

    private void login() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(List_5.this.getActivity());
        progress.setMessage("正在登陆...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        User bu = new User();
        bu.setUsername(name);
        bu.setPassword(password);
        bu.login(List_5.this.getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ShowToast(R.string.login_success);
                progress.dismiss();
                Intent intent = new Intent(List_5.this.getActivity(), Task.class);
                startActivity(intent);
                List_5.this.getActivity().finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                progress.dismiss();
                ShowToast(R.string.login_failure);
            }
        });
    }


    private void ShowToast(int i) {
        String s = getResources().getString(i);
        Toast.makeText(List_5.this.getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}
