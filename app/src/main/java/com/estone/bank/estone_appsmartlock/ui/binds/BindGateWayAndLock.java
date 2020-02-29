package com.estone.bank.estone_appsmartlock.ui.binds;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_GateWayBind;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_GateWayLine;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lib.funsdk.support.models.FunDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindGateWayAndLock extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BindGateWayAndLock";

    private ImageView gateway_activity_back, gatewayload_anim;
    private TextView bind_gatewayid;
    private TextView bind_lockid;
    private TextView imageView117;
    private TextView hasstuyds;
    private Button mButton_refresh;
    private Button mButton_confirm;
    final static private String[] stepsLables = {
            "",
            "",
            ""
    };
    private String mGatewayId;
    private String mDevId;
    private String mCameraID;
    private String mCAM_TAG;
    private AnimationDrawable mAnimaition;
    private BindGateWayHandler mHandler;
    private FunDevice mMFunDevice;

    class BindGateWayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 30021:
                    mAnimaition.stop();
                    gatewayload_anim.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplications.addActivityToBeDestory(this);
        mHandler = new BindGateWayHandler();
        setContentView(R.layout.bindgatewayandlock);

        Bundle extras = getIntent().getExtras();
        mMFunDevice = (FunDevice) extras.getSerializable("bindcammSelectDevice");
        mCameraID = extras.getString(BindFinalActivity.EXTRA_CAM_SN);
        mCAM_TAG = extras.getString(BindFinalActivity.EXTRA_CAM_TAG);
        LUtils.d(TAG, "mCameraID         ===" + mCameraID);
        LUtils.d(TAG, "mCAM_TAG         ===" + mCAM_TAG);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        gateway_activity_back = findViewById(R.id.gatewaylock_activity_back);
        gatewayload_anim = findViewById(R.id.gatewayload_anim);
        bind_gatewayid = findViewById(R.id.bind_gatewayid);
        imageView117 = findViewById(R.id.imageView117);
        hasstuyds = findViewById(R.id.hasstuyds);
        bind_lockid = findViewById(R.id.bind_lockid);
        mButton_confirm = findViewById(R.id.button_confirm);
        mButton_refresh = findViewById(R.id.button_refresh);
        mButton_confirm.setOnClickListener(this);
        mButton_refresh.setOnClickListener(this);
        gateway_activity_back.setOnClickListener(this);
        StepsView stepsView = ((StepsView) findViewById(R.id.stepsView))
                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
                .setLabels(stepsLables);
        stepsView.setCompletedPosition(1);
        gatewayload_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) gatewayload_anim.getBackground();
        mAnimaition.setOneShot(false);
        loadGatewayBindStuts(mCameraID);

    }

    private void loadGatewayBindStuts(String stringExtra) {
        mAnimaition.start();
        gatewayload_anim.setVisibility(View.VISIBLE);
        String substring = stringExtra.substring(0, stringExtra.length() - 5);
        LUtils.d(TAG, "substring         ===" + substring);
        Call<Bean_GateWayBind> bean_headPicCall = HttpManager.getInstance().getHttpClient().queryGateWayBind(Contants.ACTIONID18, substring);
        bean_headPicCall.enqueue(new Callback<Bean_GateWayBind>() {
            @Override
            public void onResponse(Call<Bean_GateWayBind> call, Response<Bean_GateWayBind> response) {
                if (response.body() != null) {
                    Bean_GateWayBind body = response.body();
                    if (body.getErrorCode() == 0) {
                        mAnimaition.stop();
                        gatewayload_anim.setVisibility(View.GONE);
                        Bean_GateWayBind.ExtraBean extra = body.getExtra();
                        mDevId = extra.getDevId();
                        mGatewayId = extra.getGatewayId();
                        bind_lockid.setText(mDevId);
                        bind_gatewayid.setText(extra.getGatewayId());
                        QueryGatewayisOnlice(mGatewayId);
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean_GateWayBind> call, Throwable t) {

            }
        });

    }

    private void QueryGatewayisOnlice(String gatewayId) {
        mAnimaition.start();
        gatewayload_anim.setVisibility(View.VISIBLE);
        Call<Bean_GateWayLine> bean_userInfoCall = HttpManager.getInstance().getHttpClient().queryGtewayIdStutstion(Contants.ACTIONID21, gatewayId);

        bean_userInfoCall.enqueue(new Callback<Bean_GateWayLine>() {
            @Override
            public void onResponse(Call<Bean_GateWayLine> call, Response<Bean_GateWayLine> response) {
                if (response.body() != null) {
                    int errorCode = response.body().getErrorCode();
                    if (errorCode == 0) {
                        Bean_GateWayLine.ExtraBean extra = response.body().getExtra();
                        String wifi = extra.getWifi();
                        if (wifi.equals("1")) {
                            imageView117.setVisibility(View.VISIBLE);
                            imageView117.setBackground(getResources().getDrawable(R.drawable.gatewayonlicestuts));
                            imageView117.setTextColor(getResources().getColor(R.color.bgColor_actionsheet_cancel_nor));
                            imageView117.setText("在线");
                            hasstuyds.setText(R.string.hasgatewaymsg);
                            mButton_confirm.setVisibility(View.VISIBLE);
                            mButton_refresh.setVisibility(View.GONE);
                        } else {
                            imageView117.setVisibility(View.VISIBLE);
                            imageView117.setText("离线");
                            imageView117.setBackground(getResources().getDrawable(R.drawable.gatewaynotonlicestuts));
                            imageView117.setTextColor(getResources().getColor(R.color.bgColor_actionsheet_cancel_nor));
                            mButton_refresh.setVisibility(View.VISIBLE);
                            mButton_confirm.setVisibility(View.GONE);
                            hasstuyds.setText(R.string.nohasgatewaymsg);
                        }
                        mHandler.sendEmptyMessageDelayed(30021, 1700);
                    } else {
                        mHandler.sendEmptyMessageDelayed(30021, 1700);
                        Toast.makeText(BindGateWayAndLock.this, "请求失败", Toast.LENGTH_SHORT).show();
                        //                        imageView117.setVisibility(View.VISIBLE);
                        //                        imageView117.setText("离线");
                        //                        imageView117.setBackgroundColor(R.drawable.gatewaynotonlicestuts);
                        //                        imageView117.setTextColor(getResources().getColor(R.color.little_grey));
                        //                        mButton_refresh.setVisibility(View.VISIBLE);
                        //                        mButton_confirm.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean_GateWayLine> call, Throwable t) {
                Toast.makeText(BindGateWayAndLock.this, "请求失败", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(30021, 1700);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gatewaylock_activity_back:
                finish();
                break;
            case R.id.button_refresh:
                QueryGatewayisOnlice(mGatewayId);
                break;
            case R.id.button_confirm:
                Intent intent = new Intent(getBaseContext(), BindFinalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bindcammSelectDevice", mMFunDevice);
                bundle.putString(BindFinalActivity.EXTRA_CAM_SN, mCameraID);
                bundle.putString(BindFinalActivity.EXTRA_CAM_TAG, mCAM_TAG);
                bundle.putString(BindFinalActivity.EXTRA_GATEWAY_SN, mGatewayId);
                bundle.putString(BindFinalActivity.EXTRA_DEVID_SN, mDevId);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);

                break;
        }
    }
}
