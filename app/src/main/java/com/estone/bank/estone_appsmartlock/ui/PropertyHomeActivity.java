package com.estone.bank.estone_appsmartlock.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.Widgets.RatioImageView;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.fragments.BookingInfoFragment2;
import com.estone.bank.estone_appsmartlock.fragments.HousingInfoFragment;
import com.estone.bank.estone_appsmartlock.fragments.LockingInfoFragment;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;
import com.estone.bank.estone_appsmartlock.yiding.PlayerLiveActivity;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceListener;
import com.lib.funsdk.support.OnFunLoginListener;
import com.lib.funsdk.support.models.FunDevice;

import java.util.List;

//
public class PropertyHomeActivity extends BaseActivity implements View.OnClickListener, OnFunLoginListener, OnFunDeviceListener, RadioGroup.OnCheckedChangeListener

{
    private static final String TAG = "PropertyHomeActivity";
    private TextView textView_name, textView_address, properties_devstuts;
    private FrameLayout mfameLayout;
    private Bean_AllDevices.InfosBean mSendinfosBean;
    private String mCameraId;
    private FunDevice mFunDevice = null;
    private LinearLayout radiobutton_isonline;
    private RatioImageView imageView_main;
    private ImageView prepert_activity_back, mprepert_activity_showotherid;
    private ImageView mshowdtrefirst_image;
    private RadioGroup mradiogrop;
    private View mshowdtrefirst;
    private Button btnok;

    private RadioButton mradiobutton_accredit_meun, mradiobutton_record_meun, mradiobutton_smart_meun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_home);
        Bundle extras = getIntent().getExtras();
        mSendinfosBean = (Bean_AllDevices.InfosBean) extras.getSerializable("sendinfosBean");
        initView();
        loadDateAndView();
    }


    private void initView() {
        mradiobutton_accredit_meun = findViewById(R.id.radiobutton_accredit_meun);
        btnok = findViewById(R.id.btnok);
        mshowdtrefirst = findViewById(R.id.showdtrefirst);
        mradiobutton_record_meun = findViewById(R.id.radiobutton_record_meun);
        imageView_main = findViewById(R.id.imageView_main);
        mradiobutton_smart_meun = findViewById(R.id.radiobutton_smart_meun);
        mradiogrop = findViewById(R.id.radiogrop);
        textView_name = findViewById(R.id.textView_name);
        //defaultimgselect = findViewById(R.id.defaultimgselect);
        mprepert_activity_showotherid = findViewById(R.id.prepert_activity_showotherid);
        prepert_activity_back = findViewById(R.id.prepert_activity_back);
        mshowdtrefirst_image = findViewById(R.id.showdtrefirst_image);
        properties_devstuts = findViewById(R.id.properties_devstuts);
        //        lly_xmactivity = findViewById(R.id.lly_xmactivity);
        textView_address = findViewById(R.id.textView_address);
        //        mnavigation_property = findViewById(R.id.navigation_property);
        radiobutton_isonline = findViewById(R.id.radiobutton_isonline);
        //        mproperty_viewpager = findViewById(R.id.property_viewpager);
        mfameLayout = findViewById(R.id.fameLayout);
        mprepert_activity_showotherid.setOnClickListener(this);
        radiobutton_isonline.setOnClickListener(this);
        prepert_activity_back.setOnClickListener(this);
        //        defaultimgselect.setOnClickListener(this);

        btnok.setOnClickListener(this);

        mradiogrop.setOnCheckedChangeListener(this);
        mradiogrop.getChildAt(0).performClick();//模拟点击第一个RB
        //创建拍照存储的临时文件
        Glide.with(PropertyHomeActivity.this).load(mSendinfosBean.getImgUrl()).asBitmap().dontAnimate().skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageView_main.setImageBitmap(resource);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FunSupport.getInstance().registerOnFunLoginListener(this);
        //        FunSupport.getInstance().registerOnFunDeviceListener(this);
        FunSupport.getInstance().requestDeviceList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadDateAndView() {
        textView_name.setText(mSendinfosBean.getRoomName());
        textView_address.setText(mSendinfosBean.getRoomId());
        //properties_devstuts.setText("查询中");
        mCameraId = mSendinfosBean.getCameraId();
        mFunDevice = getFunDeviceFromSN(mCameraId);

    }

    private FunDevice getFunDeviceFromSN(String sn) {
        List<FunDevice> devices = FunSupport.getInstance().getDeviceList();
        for (FunDevice device : devices) {
            if (device.getDevSn().equals(sn)) {
                return device;
            }
        }
        return null;
    }

    public static boolean testAllUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 97 && c <= 122) {
                return false;
            }
        }
        //str.charAt(index)
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //        case R.id.lly_xmactivity:
            //            break;
            case R.id.radiobutton_isonline:

                if (!testAllUpperCase(mSendinfosBean.getDevId()) && mSendinfosBean.getDevId().length() == 16) {
                    Intent intent = new Intent(this, CameraActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mSendinfosBean", mSendinfosBean);
                    intent.putExtra("FUN_DEVICE_ROOMID", mSendinfosBean.getRoomId() + "");
                    intent.putExtra("FUN_DEVICE_ROOMNAME", mSendinfosBean.getRoomName());
                    intent.putExtra("FUN_DEVICE_SN", mFunDevice.getDevSn());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    LUtils.d(TAG, "FUN_DEVICE_ROOMID===" + mSendinfosBean.getRoomId());
                } else {
//                    Toast.makeText(getBaseContext(), "猫眼设备无状态", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PlayerLiveActivity.class);
                    intent.putExtra("FUN_DEVICE_SN", mSendinfosBean.getCameraId());
                    startActivity(intent);
                }
                break;

            case R.id.prepert_activity_back:
                finish();
                break;
            case R.id.prepert_activity_showotherid:

                // 显示 对应的 网关id和锁的id
                //                String cameraId = mSendinfosBean.getCameraId();
                //                loadGatewayBindStuts(cameraId);
                Intent intentProperty = new Intent(PropertyHomeActivity.this, SettingHome.class);
                Bundle bundleProperty = new Bundle();

                bundleProperty.putSerializable("sendinfosBean", mSendinfosBean);
                // bundle.putString("StringcameraId", cameraId);
                intentProperty.putExtras(bundleProperty);
                startActivity(intentProperty);
                break;
            case R.id.btnok:
                ShareUtil.putString("isLoginFirst", "123123");
                mshowdtrefirst.setVisibility(View.GONE);
                mshowdtrefirst_image.setVisibility(View.GONE);
                btnok.setVisibility(View.GONE);

                radiobutton_isonline.setClickable(true);
                prepert_activity_back.setClickable(true);
                //defaultimgselect.setClickable(true);
                mradiobutton_accredit_meun.setClickable(true);
                mradiobutton_record_meun.setClickable(true);
                mradiobutton_smart_meun.setClickable(true);
                mradiogrop.setClickable(true);
                mfameLayout.setClickable(true);
                break;
        }
    }

    //
    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailed(Integer errCode) {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onDeviceListChanged() {
        this.mFunDevice = getFunDeviceFromSN(mCameraId);
        if (mFunDevice != null) {
            FunSupport.getInstance().requestDeviceStatus(mFunDevice);
        }
    }

    @Override
    public void onDeviceStatusChanged(FunDevice funDevice) {
        LUtils.i(TAG, "onDeviceStatusChanged: " + funDevice.getDevSn() + " status:" + funDevice.devStatus);
        String devSn = funDevice.getDevSn();
        if (mCameraId.equals(devSn)) {
            if (funDevice.devStatus != null) {
                if (("STATUS_OFFLINE").equals(funDevice.devStatus)) {
                    properties_devstuts.setText("离线");
                } else if (("STATUS_ONLINE").equals(funDevice.devStatus)) {
                    properties_devstuts.setText("在线");
                } else if (("STATUS_UNKNOWN").equals(funDevice.devStatus)) {
                    properties_devstuts.setText("查询中");
                }

            }
        }


    }

    @Override
    public void onDeviceAddedSuccess() {

    }

    @Override
    public void onDeviceAddedFailed(Integer errCode) {

    }

    @Override
    public void onDeviceRemovedSuccess() {

    }

    @Override
    public void onDeviceRemovedFailed(Integer errCode) {

    }

    @Override
    public void onAPDeviceListChanged() {

    }

    @Override
    public void onLanDeviceListChanged() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //    private RadioButton mradiobutton_accredit_meun, mradiobutton_record_meun, mradiobutton_smart_meun;
        if (checkedId == R.id.radiobutton_accredit_meun) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fameLayout, BookingInfoFragment2.newInstance(mSendinfosBean)).commit();
            mradiobutton_accredit_meun.setBackgroundResource(R.drawable.radiobutton_select_select);
            mradiobutton_accredit_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radioselect));

            mradiobutton_record_meun.setBackgroundResource(R.drawable.radiobutton_select_default);
            mradiobutton_record_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radiodefault));

            mradiobutton_smart_meun.setBackgroundResource(R.drawable.radiobutton_select_default);
            mradiobutton_smart_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radiodefault));

        }
        if (checkedId == R.id.radiobutton_record_meun) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fameLayout, LockingInfoFragment.newInstance(mSendinfosBean)).commit();

            mradiobutton_record_meun.setBackgroundResource(R.drawable.radiobutton_select_select);
            mradiobutton_record_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radioselect));

            mradiobutton_accredit_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radiodefault));
            mradiobutton_accredit_meun.setBackgroundResource(R.drawable.radiobutton_select_default);

            mradiobutton_smart_meun.setBackgroundResource(R.drawable.radiobutton_select_default);
            mradiobutton_smart_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radiodefault));

            if (ShareUtil.getString("isLoginFirst") == null) {
                radiobutton_isonline.setClickable(false);
                prepert_activity_back.setClickable(false);
                //                defaultimgselect.setClickable(false);
                mradiobutton_accredit_meun.setClickable(false);
                mradiobutton_record_meun.setClickable(false);
                mradiobutton_smart_meun.setClickable(false);
                mradiogrop.setClickable(false);
                mfameLayout.setClickable(false);
            }

        }
        if (checkedId == R.id.radiobutton_smart_meun) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fameLayout, HousingInfoFragment.newInstance()).commit();
            mradiobutton_smart_meun.setBackgroundResource(R.drawable.radiobutton_select_select);
            mradiobutton_smart_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radioselect));
            mradiobutton_record_meun.setBackgroundResource(R.drawable.radiobutton_select_default);
            mradiobutton_record_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radiodefault));
            mradiobutton_accredit_meun.setBackgroundResource(R.drawable.radiobutton_select_default);
            mradiobutton_accredit_meun.setTextColor(
                    getResources().getColor(R.color.btn_proper_radiodefault));
        }
    }


    public void show() {
        mshowdtrefirst.setVisibility(View.VISIBLE);
        mshowdtrefirst_image.setVisibility(View.VISIBLE);
        btnok.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mshowdtrefirst.setVisibility(View.GONE);
        mshowdtrefirst_image.setVisibility(View.GONE);
        btnok.setVisibility(View.GONE);
    }

}
