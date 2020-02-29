package com.estone.bank.estone_appsmartlock.ui.binds;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.anton46.stepsview.StepsView;
import com.basic.G;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.ListAdapterFunDevice;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.utils.DiaUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.WifIUtils;
import com.lib.MsgContent;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnAddSubDeviceResultListener;
import com.lib.funsdk.support.OnFunDeviceListener;
import com.lib.funsdk.support.OnFunDeviceWiFiConfigListener;
import com.lib.funsdk.support.config.JsonConfig;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunLoginType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BindXMCameraActivity extends BaseActivity implements OnFunDeviceListener, AdapterView.OnItemClickListener, OnFunDeviceWiFiConfigListener, View.OnClickListener, OnAddSubDeviceResultListener
        //        , AdapterView.OnItemSelectedListener
{


    public static String TAG = "BindXMCameraActivity";
    final static private String[] FROM = {
            "DEVICE_SN",
            "DEVICE_LAN_IP"
    };
    final static private String[] stepsLables = {"", "", ""};

    private List<FunDevice> deviceList = new ArrayList<>();

    private List<Map<String, String>> deviceMapList = new ArrayList<>();

    private LinearLayout mlinlny_firststep;
    private LinearLayout mrouter_msglly;
    private LinearLayout Linly_step2;
//    private ScrollView scrool_height;
    private LinearLayout listviewlly;
    private RelativeLayout relativelayout_noconnect;
    private Button mScanButton, mbutton_scannext, mbuttonconn_gateway;//, mdevicesbtnRefresh;
    private StepsView mStepsView;
    private ImageView iamge_loaddate_anim;
    private final int MESSAGE_REFRESH_DEVICE_STATUS = 0x100;
    private final int MESSAGE_REFRESH_DEVICE_QUERY = 0x101;
    private EditText Mwifipassword, Mwifiname;
    private ListView mDevlisyview;
    private ListAdapterFunDevice mAdapterFunDevice;
    private int GPS_REQUEST_CODE = 6001;
    private ImageView camera_activity_back;
    private RelativeLayout ralyhelp;
    private TextView help_nolistener, help_nolisteneralert;
    private AnimationDrawable mAnimaition;
    private FunDevice mSelectDevice;

    private boolean isNextStep = false;
    private boolean search = true;

    @Override
    public void onAddSubDeviceFailed(FunDevice funDevice, MsgContent msgContent) {

    }

    @Override
    public void onAddSubDeviceSuccess(FunDevice funDevice, MsgContent msgContent) {
        if (msgContent.str.equals(JsonConfig.AddSubDevice)) {
            JSONObject jsonObject;
            jsonObject = (JSONObject) JSONObject.parse(G.ToString(msgContent.pData));
            String mSubDeviceSN = (String) jsonObject.get("RFDeviceSN");
            System.out.println("zyy------------mSubDeviceSN    " + mSubDeviceSN);
            //            Toast.makeText(this, "添加子设备成功", Toast.LENGTH_SHORT).show();
        }
    }


    class simpleHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH_DEVICE_STATUS:
                    FunSupport.getInstance().requestAllLanDeviceStatus();

                    break;
                case MESSAGE_REFRESH_DEVICE_QUERY:
                    FunSupport.getInstance().requestLanDeviceList();

                    break;
            }
        }

    }

    private simpleHandler mHandler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplications.addActivityToBeDestory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_camera);
        mHandler1 = new simpleHandler();
        initview();

        LUtils.d(TAG, "------------onCreate--------");

    }

    private void loadDate() {
        // 刷新设备列表
        refreshLanDeviceList();
        // Demo，如果是进入设备列表就切换到本地模式,退出时切换回NET模式
        FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_INTENTT);
        FunSupport.getInstance().registerOnFunDeviceWiFiConfigListener(this);
        FunSupport.getInstance().registerOnAddSubDeviceResultListener(this);
        // 监听设备类事件
        FunSupport.getInstance().registerOnFunDeviceListener(this);
        // 打开之后进行一次搜索
        requestToGetLanDeviceList();
    }

    private void initview() {
        mStepsView = ((StepsView) findViewById(R.id.stepsView))
                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
                .setLabels(stepsLables);
        mStepsView.setCompletedPosition(0);

        mScanButton = (Button) findViewById(R.id.button_scan);
        ralyhelp = (RelativeLayout) findViewById(R.id.ralyhelp);
        camera_activity_back = (ImageView) findViewById(R.id.camera_activity_back);
        help_nolistener = (TextView) findViewById(R.id.help_nolistener);
        help_nolisteneralert = (TextView) findViewById(R.id.help_nolisteneralert);
        iamge_loaddate_anim = findViewById(R.id.iamge_loaddate_anim);
        mbutton_scannext = (Button) findViewById(R.id.button_scannext);
        mbuttonconn_gateway = (Button) findViewById(R.id.buttonconn_gateway);
        mlinlny_firststep = (LinearLayout) findViewById(R.id.linlny_firststep);
        mrouter_msglly = (LinearLayout) findViewById(R.id.router_msglly);
        Linly_step2 = (LinearLayout) findViewById(R.id.Linly_step2);
        mlinlny_firststep.setVisibility(View.VISIBLE);
        listviewlly = (LinearLayout) findViewById(R.id.listviewlly);

        relativelayout_noconnect = (RelativeLayout) findViewById(R.id.relativelayout_noconnect);
        Mwifipassword = findViewById(R.id.wifipassword);
        Mwifiname = findViewById(R.id.wifiname);

        mDevlisyview = findViewById(R.id.listView_camera_in_lan);
        mAdapterFunDevice = new ListAdapterFunDevice(BindXMCameraActivity.this);

        mDevlisyview.setAdapter(mAdapterFunDevice);
        mDevlisyview.setOnItemClickListener(this);
        //        mDevlisyview.setOnItemSelectedListener(this);
        mScanButton.setOnClickListener(this);
        camera_activity_back.setOnClickListener(this);
        ralyhelp.setOnClickListener(this);
        mbutton_scannext.setOnClickListener(this);
        mbuttonconn_gateway.setOnClickListener(this);
        Mwifiname.setText(WifIUtils.getConnectWifiSSID(BindXMCameraActivity.this));
        LUtils.d(TAG, "ssid==" + WifIUtils.getConnectWifiSSID(BindXMCameraActivity.this));
        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);

        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }


    private void requestToGetLanDeviceList() {
        if (search) {
            mHandler1.sendEmptyMessage(MESSAGE_REFRESH_DEVICE_QUERY);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        FunSupport.getInstance().removeOnFunDeviceWiFiConfigListener(this);
        LUtils.d(TAG, "------------onStop--------");
    }

    @Override
    protected void onDestroy() {
        LUtils.d(TAG, "------------onDestroy--------");
        // 注销设备事件监听
        FunSupport.getInstance().removeOnFunDeviceListener(this);

        // 切换回网络访问
        FunSupport.getInstance().setLoginType(FunLoginType.LOGIN_BY_INTENTT);

        super.onDestroy();
    }

    @Override
    public void onLanDeviceListChanged() {

        if (isNextStep) {
            deviceList = FunSupport.getInstance().getLanDeviceList();
            // 设备数目大于0
            if (deviceList.size() > 0) {
                BindXMCameraActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterFunDevice.setDateDevs(deviceList);
                        mHandler1.removeMessages(MESSAGE_REFRESH_DEVICE_QUERY);
                        search = false;
                    }
                });
                listviewlly.setVisibility(View.VISIBLE);
                if (mAnimaition != null) {
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE); //加载动画
                }
                LUtils.d(TAG, "-----------deviceList----------" + deviceList.size());
                Linly_step2.setVisibility(View.GONE);
                mbutton_scannext.setVisibility(View.GONE);
                mrouter_msglly.setVisibility(View.GONE);
                mbuttonconn_gateway.setVisibility(View.VISIBLE);
            }
            requestToGetLanDeviceList();
        } else {
            LUtils.d(TAG, "-----isNextStep==---" + isNextStep);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onDeviceListChanged() {

    }

    @Override
    public void onDeviceStatusChanged(FunDevice funDevice) {
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
    public void onDeviceWiFiConfigSetted(FunDevice funDevice) {
        FunSupport.getInstance().requestLanDeviceList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        LUtils.d(TAG, "mSelectDevice==" + mSelectDevice.toString());
        isNextStep = false;
        LUtils.d(TAG, "position==" + position);
        mSelectDevice = deviceList.get(position);
        mAdapterFunDevice.changeListItemColor(position);
        //        mAdapterFunDevice.notifyDataSetChanged();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_scan:
                checkWIFInet();

                //                refreshLanDeviceList();
                //                requestToGetLanDeviceList();
                break;
            case R.id.ralyhelp:
                DiaUtils.HelpDialog(this);
                break;
            case R.id.camera_activity_back:
                finish();
                break;
            case R.id.buttonconn_gateway:
                if (mSelectDevice != null) {
                    Intent intent = new Intent(this, BindGateWayAndLock.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bindcammSelectDevice", mSelectDevice);
                    bundle.putString(BindFinalActivity.EXTRA_CAM_SN, mSelectDevice.getDevSn());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    FunSupport.getInstance().requestDeviceAdd(mSelectDevice);
                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                } else {
                    Toast.makeText(BindXMCameraActivity.this, "请选择需要绑定的设备", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.button_scannext:

                String wifiname = Mwifiname.getText().toString().trim();
                String wifipassword = Mwifipassword.getText().toString().trim();
                if (wifipassword.length() >= 8) {
                    if (wifiname.length() >= 1) {
                        mAnimaition.setOneShot(false);
                        mAnimaition.start();//启动
                        iamge_loaddate_anim.setVisibility(View.VISIBLE);
                        WifIUtils.startQuickSetting(BindXMCameraActivity.this, wifiname, wifipassword);
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                WifIUtils.stopQuickSetting();
                            }
                        }, 7000);
                    } else {
                        Toast.makeText(BindXMCameraActivity.this, "请确认SSID长度是否正确", Toast.LENGTH_SHORT).show();
                    }
                    ;
                } else {
                    Toast.makeText(BindXMCameraActivity.this, "请确认密码长度是否正确", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            //做需要做的事情，比如再次检测是否打开GPS了 或者定位
            openGPSSettings();
        }
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (checkGPSIsOpen()) {
            inputWifiinformation();
        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })

                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    }

    private void checkWIFInet() {
        if (WifIUtils.isWifiConnect(BindXMCameraActivity.this)) {

            relativelayout_noconnect.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 23) {
                openGPSSettings();
            }

        } else {
            //无连接
            Toast.makeText(this, "WIFI未连接", Toast.LENGTH_LONG).show();
            relativelayout_noconnect.setVisibility(View.VISIBLE);
        }

    }

    private void inputWifiinformation() {
        mlinlny_firststep.setVisibility(View.GONE);
        Linly_step2.setVisibility(View.VISIBLE);
        mScanButton.setVisibility(View.GONE);
        help_nolistener.setVisibility(View.GONE);
        help_nolisteneralert.setVisibility(View.VISIBLE);
        mbutton_scannext.setVisibility(View.VISIBLE);
        mrouter_msglly.setVisibility(View.VISIBLE);

        isNextStep = true;
        loadDate();
    }


    private void refreshLanDeviceList() {
        deviceList.addAll(FunSupport.getInstance().getLanDeviceList());
        //        mAdapterFunDevice.notifyDataSetChanged();
        //        for (int i = 0; i < deviceList.size(); i++) {
        //            FunSupport.getInstance().requestDeviceAdd(deviceList.get(i));
        //        }
        LUtils.d(TAG, "refreshLanDeviceList()==deviceList==" + deviceList.toString());
        // 延时100毫秒更新设备消息
        mHandler1.removeMessages(MESSAGE_REFRESH_DEVICE_STATUS);
        if (deviceList.size() > 0) {
            mHandler1.sendEmptyMessageDelayed(MESSAGE_REFRESH_DEVICE_STATUS, 1000);
        }
    }

}
