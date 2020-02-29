package com.estone.bank.estone_appsmartlock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.PushHelper;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.receivers.MsgYTCastReceiver;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;

import cn.firmwarelib.nativelibs.config.SdkConfig;
import cn.firmwarelib.nativelibs.retrofit_okhttp.RequestManager;
import cn.firmwarelib.nativelibs.retrofit_okhttp.interfaces.HttpResponseListener;

//cea8622547ea15b24d0976cd4eb92d9  release
//fd0b4172a16468e6487672e1011df977   debug
//fd0b4172a16468e6487672e1011df977
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mlogin_logined;
    private TextView mlogin_forgetpass, mlogin_willregist;
    private String TAG = "LoginActivity";
    private EditText mlogin_phonenum, mpassword;
    private RelativeLayout mlogin_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginfragment);
        initFid();

    }

    private void initFid() {
        mlogin_logined = findViewById(R.id.login_loginedyt);
        mlogin_forgetpass = findViewById(R.id.login_forgetpass);
        mlogin_willregist = findViewById(R.id.login_willregist);
        mlogin_forgetpass = findViewById(R.id.login_forgetpass);
        mlogin_phonenum = findViewById(R.id.login_phonenum);
        mpassword = findViewById(R.id.password);
        mlogin_back = findViewById(R.id.login_back_fii);
        mlogin_forgetpass.setOnClickListener(this);
        mlogin_willregist.setOnClickListener(this);
        mlogin_logined.setOnClickListener(this);
        mlogin_back.setOnClickListener(this);
        //        FunSupport.getInstance().login("killsmm", "zxl870319");
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_forgetpass:
                intent.setClass(LoginActivity.this, ForGetPassActivity.class);
                startActivity(intent);
                break;
            case R.id.login_back:
                finish();
                break;
            case R.id.login_willregist:
                intent.setClass(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_loginedyt:
                if (TextUtils.isEmpty(mlogin_phonenum.getText().toString().trim()) || TextUtils.isEmpty(mpassword.getText().toString())) {
                    ToastUtils.showToast(LoginActivity.this, "请输入账号，密码");
                } else {
                    RequestManager.getInstance().login(mlogin_phonenum.getText().toString().trim(), mpassword.getText().toString().trim(), "devid", new HttpResponseListener() {
                        @Override
                        public void onResponseSuccess(int statusCode, Object o) {
                            LUtils.d(TAG, "statusCode==" + statusCode);

                            intent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onResponseError(Throwable throwable) {
                            LUtils.d(TAG, "throwable==" + throwable.getMessage());
                        }
                    });
                }

                break;
        }

    }
}
