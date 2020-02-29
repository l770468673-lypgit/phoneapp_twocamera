package com.estone.bank.estone_appsmartlock.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.estone.bank.estone_appsmartlock.utils.LUtils;

public abstract class BaseActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppStatus.APP_STATUS != AppStatus.APP_STATUS_NORMAL) { // 非正常启动流程，直接重新初始化应用界面
            LUtils.d("liunianprint:", "reInitApp");
            AppStatus.reInitApp(BaseActivity2.this);
            finish();
            return;
        } else { // 正常启动流程
            setUpViewAndData(savedInstanceState); // 子Activity初始化界面
        }

    }

    /**
     * 提供给子Activity设置界面的接口，不要在onCreate中初始化界面
     *
     * @param savedInstanceState
     */
    protected abstract void setUpViewAndData(@Nullable Bundle savedInstanceState);

}
