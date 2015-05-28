package voidhug.test;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bmob.utils.BmobLog;

import cn.bmob.v3.listener.SaveListener;
import voidhug.test.bean.User;
import voidhug.test.constant.RsgisterConstant;
import voidhug.test.util.CommonUtils;

/**
 * Created by voidhug on 15/5/3.
 */
public class List_4 extends Fragment {

    Button btn_register;
    EditText et_username, et_password, et_email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_email = (EditText) view.findViewById(R.id.et_email);
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                register();
            }
        });
        return view;
    }


    private void register () {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_email.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }

        if (password.length() < 6){
            ShowToast(R.string.toast_simple_password_null);
            return;
        }

        if (!pwd_again.equals(password)) {
            ShowToast(R.string.toast_error_comfirm_password);
            return;
        }

        boolean isNetConnected = CommonUtils.isNetworkAvailable(List_4.this.getActivity());
        if(!isNetConnected){
            ShowToast(R.string.network_tips);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(List_4.this.getActivity());
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        final User bu = new User();
        bu.setUsername(name);
        bu.setPassword(password);

        bu.signUp(List_4.this.getActivity(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                progress.dismiss();
                ShowToast(R.string.toast_register_success);
                //发广播通知登陆页面退出
                List_4.this.getActivity().sendBroadcast(new Intent(RsgisterConstant.ACTION_REGISTER_SUCCESS_FINISH));
                // 启动主页
                Intent intent = new Intent(List_4.this.getActivity(), Task.class);
                startActivity(intent);
                List_4.this.getActivity().finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                BmobLog.i(arg1);
                ShowToast(R.string.toast_register_failure);
                progress.dismiss();
            }
        });


    }


    private void ShowToast(int i) {
        String s = getResources().getString(i);
        Toast.makeText(List_4.this.getActivity(), s, Toast.LENGTH_SHORT).show();
    }




}
