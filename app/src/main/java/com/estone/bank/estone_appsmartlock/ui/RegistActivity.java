package com.estone.bank.estone_appsmartlock.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.utils.LUtils;

import cn.firmwarelib.nativelibs.retrofit_okhttp.RequestManager;
import cn.firmwarelib.nativelibs.retrofit_okhttp.interfaces.HttpResponseListener;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    static private final String TAG = "RegistActivity";
    private EditText mreg_phonenum, mimgregpasswordyanzhengmg, mokregpassword;
    private ImageView mmoremessage_activity_back;
    private Button mlogin_regist;
private RelativeLayout mlogin_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regists);
        mreg_phonenum = findViewById(R.id.reg_phonenum);
        mlogin_back = findViewById(R.id.login_back);
        mimgregpasswordyanzhengmg = findViewById(R.id.imgregpasswordyanzhengmg);
        mokregpassword = findViewById(R.id.okregpassword);
        mmoremessage_activity_back = findViewById(R.id.moremessage_activity_back);
        mlogin_regist = findViewById(R.id.login_regist);

        mlogin_regist.setOnClickListener(this);
        mlogin_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_regist:

                RequestManager.getInstance().register(mreg_phonenum.getText().toString().trim(), mokregpassword.getText().toString().trim(),
                      "", "", "", new HttpResponseListener() {
                            @Override
                            public void onResponseSuccess(int i, Object o) {
                                LUtils.d(TAG, "code===" + i);
                            }

                            @Override
                            public void onResponseError(Throwable throwable) {
                                LUtils.d(TAG, "throwable===" + throwable.getMessage());
                            }
                        });
                break;
            case R.id.moremessage_activity_back:
            case R.id.login_back:
                finish();
                break;
        }
    }
}
