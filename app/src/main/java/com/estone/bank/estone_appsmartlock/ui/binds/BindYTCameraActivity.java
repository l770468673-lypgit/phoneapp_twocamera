package com.estone.bank.estone_appsmartlock.ui.binds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.anton46.stepsview.StepsView;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class BindYTCameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BindYTCameraActivity";
    private Button mbuttonconn_camerawifi;

    private final int REQUEST_CODE_SCAN = 0;
    final static private String[] stepsLables = {"", "", ""};
    private StepsView mStepsView;    private ImageView camera_activity_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_ytcamera);

        initView();
        MyApplications.addActivityToBeDestory(this);
    }

    private void initView() {

        mStepsView = ((StepsView) findViewById(R.id.stepsView))
                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
                .setLabels(stepsLables);
        mStepsView.setCompletedPosition(0);

        camera_activity_back = (ImageView) findViewById(R.id.camera_activity_back);

        mbuttonconn_camerawifi = findViewById(R.id.buttonconn_camerawifi);

        mbuttonconn_camerawifi.setOnClickListener(this);
        camera_activity_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonconn_camerawifi:
                Intent intent_scan = new Intent(BindYTCameraActivity.this, CaptureActivity.class);
                startActivityForResult(intent_scan, REQUEST_CODE_SCAN);
                break;
            case R.id.camera_activity_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN) {
            LUtils.d(TAG, "222222222REQUEST_CODE_SCAN  2222222222");
            if (resultCode == RESULT_OK) {
                LUtils.d(TAG, "RESULT_OK");
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String mResultdevid = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(BindYTCameraActivity.this, mResultdevid, Toast.LENGTH_LONG).show();
                        LUtils.d(TAG, "mResultdevid===" + mResultdevid);
                        //mmcameraid.setText(mResultdevid);
                        Intent intent = new Intent(this, BindQRNetActivity.class);
                        Bundle bundles = new Bundle();
                        bundles.putString(BindFinalActivity.EXTRA_CAM_SN, mResultdevid);
                        bundles.putString(BindFinalActivity.EXTRA_CAM_TAG, "CAMERA_YD");
                        intent.putExtras(bundles);

                        startActivity(intent);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(BindYTCameraActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        LUtils.d(TAG, "解析二维码失败===");
                    }
                }
            }
        }
    }

}
