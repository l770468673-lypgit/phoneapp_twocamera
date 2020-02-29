package com.estone.bank.estone_appsmartlock.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.base.AppStatus;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;

public class WelComeActivity extends BaseActivity {

    private ImageView mwelcomepic;
    private WelComeHandler mHandler;
    public Runnable toSplashActivity = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(WelComeActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    };

    //    public Runnable toMainActivity = new Runnable() {
    //        @Override
    //        public void run() {
    //            Intent intent = new Intent(WelComeActivity.this, MainActivity.class);
    //            startActivity(intent);
    //        }
    //    };


    class WelComeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppStatus.APP_STATUS = AppStatus.APP_STATUS_NORMAL; // App正常的启动，设置App的启动状态为正常启动
        setContentView(R.layout.activity_welcome);

        mwelcomepic = findViewById(R.id.welcomepic);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);//第一个参数开始的透明度，第二个参数结束的透明度
//        alphaAnimation.setDuration(2000);//多长时间完成这个动作
//        mwelcomepic.startAnimation(alphaAnimation);

        mHandler = new WelComeHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mHandler.postDelayed(toSplashActivity, 2000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
