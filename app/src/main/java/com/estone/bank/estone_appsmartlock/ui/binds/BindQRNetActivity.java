package com.estone.bank.estone_appsmartlock.ui.binds;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.WifIUtils;

import cn.firmwarelib.nativelibs.command.DevHelper;
import io.reactivex.functions.Consumer;

public class BindQRNetActivity extends BaseActivity implements View.OnClickListener {

    private Button mbutton_scannext;
    private Button mbutton_tobindlock;
    private EditText mwifiname;
    private EditText mwifipassword;
    private ImageView mimageqr;
    private ImageView camera_activity_back;
    private String camSN;
    private String camTAG;
    final static private String[] stepsLables = {"", "", ""};
    private StepsView mStepsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_qrnet);

        initView();

        Bundle extras = getIntent().getExtras();
        camSN = extras.getString(BindFinalActivity.EXTRA_CAM_SN);
        camTAG = extras.getString(BindFinalActivity.EXTRA_CAM_TAG);
        MyApplications.addActivityToBeDestory(this);
    }

    private void initView() {
        camera_activity_back = (ImageView) findViewById(R.id.camera_activity_back);
        mwifipassword = findViewById(R.id.wifipassword);
        mbutton_tobindlock = findViewById(R.id.button_tobindlock);
        mwifiname = findViewById(R.id.wifiname);
        mbutton_scannext = findViewById(R.id.button_scannext);
        mimageqr = findViewById(R.id.imageqr);
        mStepsView = ((StepsView) findViewById(R.id.stepsView))
                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
                .setLabels(stepsLables);
        mStepsView.setCompletedPosition(0);
        camera_activity_back.setOnClickListener(this);
        mbutton_scannext.setOnClickListener(this);
        mbutton_tobindlock.setOnClickListener(this);
        mwifiname.setText(WifIUtils.getConnectWifiSSID(BindQRNetActivity.this));
        Log.e("TAG", "callBack: "+WifIUtils.getConnectWifiSSID(BindQRNetActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_scannext:
                String wifiname = mwifiname.getText().toString().trim();
                String wifipassword = mwifipassword.getText().toString().trim();
                if (wifipassword.length() >= 8) {
                    if (wifiname.length() >= 1) {
                        //                        mAnimaition.setOneShot(false);
                        //                        mAnimaition.start();//启动
                        //                        iamge_loaddate_anim.setVisibility(View.VISIBLE);
                        BuildQR(wifiname,wifipassword);
                        LUtils.d("BindQRNetActivity","wifiname===="+wifiname);
                        LUtils.d("BindQRNetActivity","wifipassword===="+wifipassword);
                    } else {
                        Toast.makeText(BindQRNetActivity.this, "请确认SSID长度是否正确", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BindQRNetActivity.this, "请确认密码长度是否正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.camera_activity_back:
                finish();
                break;
            case R.id.button_tobindlock:
                Intent intent = new Intent(this, BindGateWayAndLock.class);
                Bundle bundles = new Bundle();
                bundles.putString(BindFinalActivity.EXTRA_CAM_SN, camSN);
                bundles.putString(BindFinalActivity.EXTRA_CAM_TAG, camTAG);
                intent.putExtras(bundles);
                startActivity(intent);

                break;
        }
    }

    private void BuildQR(String wifiname, String wifipassword) {

        LUtils.d("BindQRNetActivity","wifiname===="+wifiname);
        LUtils.d("BindQRNetActivity","wifipassword===="+wifipassword);
        addSubscription(DevHelper.Companion.getDevHelper().getScanWifi
                //(ScanQRActivity.this, mname.getText().toString().trim(), mpass.getText().toString().trim(), "", new Consumer<Bitmap>() {
                        (BindQRNetActivity.this, wifiname, wifipassword, "", new Consumer<Bitmap>() {
                            @Override
                            public void accept(Bitmap bitmap) throws Exception {
                                mimageqr.setImageBitmap(bitmap);
                                mbutton_scannext.setVisibility(View.GONE);
                                mbutton_tobindlock.setVisibility(View.VISIBLE);

                            }
                        })
        );
    }
}
