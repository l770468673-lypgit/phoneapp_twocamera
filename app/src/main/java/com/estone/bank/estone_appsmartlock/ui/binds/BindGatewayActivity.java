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
//
//public class BindGatewayActivity extends AppCompatActivity implements View.OnClickListener {
//    final static public String EXTRA_CAM_SN = "CAM_SN";
//    final static private int REQUEST_CODE_SCAN_QR = 0;
//    private String camSN;
//    private String gateWaySN;
//    private ImageView gateway_activity_back;
//
//    public static final String RESULT_TYPE = "result_type";
//    public static final String RESULT_STRING = "result_string";
//    public static final int RESULT_SUCCESS = 1;
//    public static final int RESULT_FAILED = 2;
//
//    final static private String[] stepsLables = {
//            "",
//            "",
//            "",
//            ""
//    };
//    private RelativeLayout rely_help;
////    final static private String[] stepsLables = {
////            "摄像头",
////            "网关",
////            "门锁",
////            "房间"
////    };
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_CODE_SCAN_QR:
//                    if (null != data) {
//                        Bundle bundle = data.getExtras();
//                        if (bundle == null) {
//                            return;
//                        }
//                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                            String result = bundle.getString(CodeUtils.RESULT_STRING);
//                            ((EditText) findViewById(R.id.editText_gateway_sn)).setText(result);
//                        }
//                    }
//                    break;
//            }
//
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        MyApplications.addActivityToBeDestory(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bind_gateway);
//        if (null != getSupportActionBar()) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        rely_help = (RelativeLayout) findViewById(R.id.rely_help);
//        gateway_activity_back = (ImageView) findViewById(R.id.gateway_activity_back);
//        rely_help.setOnClickListener(this);
//        gateway_activity_back.setOnClickListener(this);
//        StepsView stepsView = ((StepsView) findViewById(R.id.stepsView))
//                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
//                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
//                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
//                .setLabels(stepsLables);
//        stepsView.setCompletedPosition(1);
//
//
//        camSN = getIntent().getStringExtra(EXTRA_CAM_SN);
//
//        ((Button) findViewById(R.id.button_confirm)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                gateWaySN = ((EditText) findViewById(R.id.editText_gateway_sn)).getText().toString();
//                if (camSN != null && !gateWaySN.equals("")) {
//                    Intent intent = new Intent(getBaseContext(), BindLockActivity.class);
//                    intent.putExtra(BindLockActivity.EXTRA_CAM_SN, camSN);
//                    intent.putExtra(BindLockActivity.EXTRA_GATEWAY_SN, gateWaySN);
//                    startActivity(intent);
//
//                }
//            }
//        });
//
//        ImageButton scanQRButton = (ImageButton) findViewById(R.id.imageButton_scan_qr);
//        scanQRButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_scan = new Intent(getBaseContext(), CaptureActivity.class);
//                startActivityForResult(intent_scan, REQUEST_CODE_SCAN_QR);
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.rely_help:
//
//                DiaUtils.HelpDialog(this);
//                break;
//            case R.id.gateway_activity_back:
//
//                finish();
//                break;
//
//
//        }
//    }
//}
