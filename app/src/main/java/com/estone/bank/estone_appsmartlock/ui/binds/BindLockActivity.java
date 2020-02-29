//package com.estone.bank.estone_appsmartlock.ui.binds;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.anton46.stepsview.StepsView;
//import com.estone.bank.estone_appsmartlock.R;
//import com.estone.bank.estone_appsmartlock.apps.MyApplications;
//import com.estone.bank.estone_appsmartlock.utils.DiaUtils;
//import com.uuzuche.lib_zxing.activity.CaptureActivity;
//import com.uuzuche.lib_zxing.activity.CodeUtils;
//
//public class BindLockActivity extends AppCompatActivity implements View.OnClickListener {
//    final static public String EXTRA_CAM_SN = "CAM_SN";
//    final static public String EXTRA_GATEWAY_SN = "GATEWAY_SN";
//    final static private int REQUEST_CODE_SCAN_QRLOCK = 0;
//
//    private RelativeLayout rely_help;
//    private EditText editText_lock_sn;
////    final static private String[] stepsLables = {
////            "摄像头",
////            "网关",
////            "门锁",
////            "房间"
////    };
//    final static private String[] stepsLables = {
//            "",
//            "",
//            "",
//            ""
//    };
//
//    private String camSN;
//    private String gateWaySN;
//    private String lockSN;
//
//    private ImageView lock_activity_back;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_CODE_SCAN_QRLOCK:
//                    if (null != data) {
//                        Bundle bundle = data.getExtras();
//                        if (bundle == null) {
//                            return;
//                        }
//                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                            String result = bundle.getString(CodeUtils.RESULT_STRING);
//                            editText_lock_sn.setText(result);
//                        }
//                    }
//                    break;
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        MyApplications.addActivityToBeDestory(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bind_lock);
//        if (null != getSupportActionBar()) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        StepsView stepsView = ((StepsView) findViewById(R.id.stepsView))
//                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
//                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
//                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
//                .setLabels(stepsLables);
//        stepsView.setCompletedPosition(2);
//
//        camSN = getIntent().getStringExtra(EXTRA_CAM_SN);
//        gateWaySN = getIntent().getStringExtra(EXTRA_GATEWAY_SN);
//
//        rely_help = findViewById(R.id.rely_help);
//        editText_lock_sn = findViewById(R.id.editText_lock_sn);
//
//        lock_activity_back = findViewById(R.id.lock_activity_back);
//        rely_help.setOnClickListener(this);
//        lock_activity_back.setOnClickListener(this);
//        ((Button) findViewById(R.id.button_confirm)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lockSN = editText_lock_sn.getText().toString();
//                if (camSN != null && gateWaySN != null && !lockSN.equals("")) {
//                    Intent intent = new Intent(getBaseContext(), BindFinalActivity.class);
//                    intent.putExtra(BindFinalActivity.EXTRA_CAM_SN, camSN);
//                    intent.putExtra(BindFinalActivity.EXTRA_GATEWAY_SN, gateWaySN);
//                    intent.putExtra(BindFinalActivity.EXTRA_DEVID_SN, lockSN);
//                    startActivity(intent);
//
//                }
//            }
//        });
//
//                ImageButton scanQRButton = (ImageButton)findViewById(R.id.imageButton_scan_qrlock);
//
//                scanQRButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent_scan = new Intent(getBaseContext(), CaptureActivity.class);
//                        startActivityForResult(intent_scan, REQUEST_CODE_SCAN_QRLOCK);
//                    }
//                });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rely_help:
//                DiaUtils.HelpDialog(this);
//                break;
//            case R.id.lock_activity_back:
//                finish();
//                break;
//        }
//    }
//}
