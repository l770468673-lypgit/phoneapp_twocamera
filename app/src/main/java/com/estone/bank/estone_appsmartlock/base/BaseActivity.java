package com.estone.bank.estone_appsmartlock.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estone.bank.estone_appsmartlock.R;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null &&
                    intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_base);
    }


    /**
     * 将Disposable添加
     *
     * @param disposable
     */
    public void addSubscription(Disposable disposable) {
        if (compositeDisposable == null || (!compositeDisposable.isDisposed())) {
            compositeDisposable = new CompositeDisposable();
        }
        if (disposable != null && !disposable.isDisposed()) {
            compositeDisposable.add(disposable);
        }
    }
    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑
     * 防止Rx造成的内存泄漏
     */
    public void unDisposable() {
        // 保证activity结束时取消所有正在执行的订阅
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }


}
