package com.estone.bank.estone_appsmartlock.yiding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.ui.CameraActivity;
import com.estone.bank.estone_appsmartlock.ui.CameraPicActivity;
import com.estone.bank.estone_appsmartlock.ui.CameraVideoActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;
import com.lib.funsdk.support.FunPath;
import com.lidroid.xutils.BitmapUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.firmwarelib.nativelibs.NetWork.UrlManganger;
import cn.firmwarelib.nativelibs.bean.BaseBean.BaseObtain;
import cn.firmwarelib.nativelibs.bean.DeviceDetailBean;
import cn.firmwarelib.nativelibs.command.DevConstants;
import cn.firmwarelib.nativelibs.command.DevHelper;
import cn.firmwarelib.nativelibs.command.VoiceHelper;
import cn.firmwarelib.nativelibs.config.NetConfig;
import cn.firmwarelib.nativelibs.config.SdkConfig;
import cn.firmwarelib.nativelibs.retrofit_okhttp.RequestManager;
import cn.firmwarelib.nativelibs.retrofit_okhttp.interfaces.HttpResponseListener;
import cn.firmwarelib.nativelibs.utils.Encryption.AESUtiles;
import cn.firmwarelib.nativelibs.utils.LogUtil;
import io.reactivex.functions.Consumer;

public class PlayerLiveActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private String TAG = "PlayerLiveActivity";
    //    private String mSN;
    public TextView title;

    public boolean IS_LOGIN = true;
    //    private String sn;//设备SN
    // 屏幕的高宽度
    private int startWidth = 0;
    private int startHeight = 0;
    private ImageView mcapimgs;
    private SurfaceView sfv_play;
    private SurfaceHolder surfaceHolder;
    protected int session; //>=0 或者 =-2 是属于正常，如果<0 是属于不正常
    private VoiceHelper voiceHelper;// 音频帮助类
    private ProgressBar pb_video;//缓存条
    private TextView tv_status;//播放失败的提示
    private RectF rectF;
    private boolean screenshots = false;//是否截图
    private int START_SETTING = 101;//去设置页面
    public boolean isSuccessInit = false;//是否初始化成功
    //    private ImageView ivBattery;//电池电量图标
    //    private TextView tvBattery;//电池电量
    private CheckBox startDeviceVoice;//手机播放声音
    private ImageView butSendVoice;
    private CheckBox cb_video_btm;//录屏
    private CheckBox cb_photo_btm;//截屏
    private ImageButton btopicact;//
    private ImageButton btovideoact;//

    private TextView tvAction;
    private Chronometer chronometer;
    private Bitmap videoBitmap;//录屏截取那个bitmap
    //    private View lineLock;//开锁按钮
    private String devType;//设备类型：01 门铃，02 猫眼，03门禁，04 一体锁 ，05 DSC
    //    public View lineRight;

    /*
     * 申请录音权限*/
    //申请录音权限
    private int GET_RECODE_AUDIO = 1;
    private String[] PERMISSION_AUDIO = {Manifest.permission.RECORD_AUDIO};
    private String[] PERMISSION_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String account;//登录账号
    public DeviceDetailBean deviceDetailBean;

    List<DeviceItemBean> list = new ArrayList<DeviceItemBean>();
    //    private DeviceItemBean itemBean2;
    private List<DeviceItemBean> mDeviceItemBeans;
    private String mFun_devSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_live);


        init();

    }

    private void initFile(String fun_DevSn) {

        File imgPath = new File(FunPath.PATH_PHOTO + File.separator + fun_DevSn);
        if (imgPath != null && !imgPath.exists()) {
            FunPath.makeRootDirectory(imgPath.getPath());
            LUtils.d(TAG, "开始创建   " + imgPath);
        } else {
            LUtils.d(TAG, "imgPath 已经有了   " + imgPath);
        }
        File videoPath = new File(FunPath.PATH_VIDEO + File.separator + fun_DevSn);
        if (videoPath != null && !videoPath.exists()) {
            FunPath.makeRootDirectory(videoPath.getPath());
            LUtils.d(TAG, "开始创建   " + videoPath);
        } else {
            LUtils.d(TAG, "imgPath 已经有了   " + videoPath);
        }


    }

    /**
     * 获取列表数据
     *
     * @param fun_DevSn
     */
    private void getListData(final String fun_DevSn) {
        LUtils.d(TAG, "getListData:"+fun_DevSn);
        RequestManager.getInstance().getEquipmentQuery(new HttpResponseListener() {
            @Override
            public void onResponseSuccess(int statusCode, Object object) {
                LUtils.d(TAG, "获取列表数据:" + JSON.toJSONString(object));
                BaseObtain object1 = (BaseObtain) object;
                list.clear();
                if (statusCode == 200) {
                    if (object1.data != null) {
                        mDeviceItemBeans = JSON.parseArray(JSON.toJSONString(object1.data), DeviceItemBean.class);
                        list.addAll(mDeviceItemBeans);
                        LUtils.d(TAG, "获取列表数据:" + mDeviceItemBeans.toString());
                        LUtils.d(TAG, "lis.size()==:" + list.size());
                        for (int i = 0; i < list.size(); i++) {
                            DeviceItemBean deviceItemBean = list.get(i);
                            String sn = deviceItemBean.getSn();
                            if (sn.equals(fun_DevSn)) {
//                                getDeviceData(sn+"AZREI");
                                getDeviceData(sn);
                                LUtils.d(TAG, "   getDeviceData(sn);:" + sn);
                            }

                        }
                        //                        itemBean2 = list.get(0);

                    }

                }

            }

            @Override
            public void onResponseError(Throwable e) {
                LUtils.d(TAG, "获取列表数据 失败:" + e.getMessage());
            }
        });
    }

    private void getDeviceData(final String itemBean) {
        RequestManager.getInstance().getEquipmentDetails(itemBean, new HttpResponseListener() {
            @Override
            public void onResponseSuccess(int statusCode, Object object) {
                LUtils.d(TAG, "设备详情：" + JSON.toJSONString(object));
                deviceDetailBean = (DeviceDetailBean) object;

                if (deviceDetailBean != null) {
                    Field[] fields = deviceDetailBean.getClass().getFields();
                    String mes = "";
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        String fieldName = field.getName();
                        try {
                            field.setAccessible(true);
                            Object o = field.get(deviceDetailBean);
                            if (!fieldName.equals("code")) {
                                mes += "\n\n   设备" + fieldName + ": " + o.toString();

                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {

                        }
                    }
                    LUtils.d(TAG, "AESUtiles.Decrypt(deviceDetailBean.getDid())=" + deviceDetailBean.getDid());
                    LUtils.d(TAG, "AESUtiles.Decrypt(deviceDetailBean.getInitstring())=" + deviceDetailBean.getInitstring());
                    LUtils.d(TAG, "AESUtiles.Decrypt(deviceDetailBean.appType())=" + deviceDetailBean.deviceType);

                    DevConfig.Companion.setDeviceDid(AESUtiles.Decrypt(deviceDetailBean.getDid()));
                    DevConfig.Companion.setDeviceInitString(AESUtiles.Decrypt(deviceDetailBean.getInitstring()));
                    DevConfig.Companion.setDeviceAppType(SdkConfig.appType);

                    doconnect(0, itemBean);
                    LUtils.d(TAG, "AESUtiles.Decrypt(deviceDetailBean.appType())=" + deviceDetailBean.deviceType);
                }
            }

            @Override
            public void onResponseError(Throwable e) {
                LUtils.d(TAG, " 固件信息 异常:" + e.getMessage());
            }
        });
    }

    private void doconnect(final int type, final String itemBean) {

        /**
         * 执行连接设备
         */
        addSubscription(DevHelper.Companion.getDevHelper().devInitP2P
                (AESUtiles.Decrypt(deviceDetailBean.getInitstring())).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                LUtils.d(TAG, "连接P2P设备：" + aBoolean + "    host_ip:" +
                        UrlManganger.host_ip + "   sn:" + itemBean + "    appType:" + UrlManganger.appType);
                if (aBoolean) {
                    addSubscription(DevHelper.Companion.getDevHelper().devActivateDevice(UrlManganger.host_ip, itemBean, UrlManganger.appType).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {

                            LUtils.d(TAG, "主动唤醒设备：" + aBoolean);
                            if (aBoolean) {
                                switch (type) {
                                    case 0:

                                        initview();
                                        initclick();


                                        break;
                                }
                            }
                        }
                    }));
                } else {
                    LUtils.d(TAG, "连接P2P设备失败：" + aBoolean);
                }
            }
        }));
    }

    /**
     * 初始化设备ok 可以点击操作
     */
    private void initVideoSuccess(boolean isSuccessInit) {
        startDeviceVoice.setClickable(isSuccessInit);//声音可以点击
        butSendVoice.setClickable(isSuccessInit);//发送声音可以点击
        cb_video_btm.setClickable(isSuccessInit);//录屏可以点击
        cb_photo_btm.setClickable(isSuccessInit);//截屏可以点击
        //        findViewById(R.id.iv_call_lock).setClickable(isSuccessInit);//可以开锁
    }

    private void loadDate() {
        verifyAudioPermissions();
        initVideoSuccess(false);
        initPlayer();
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        LUtils.d(TAG, "did:" + DevConfig.Companion.getDeviceDid());
        addSubscription(DevHelper.Companion.getDevHelper().devConnect(DevConfig.Companion.getDeviceDid()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer session) throws Exception {
                LUtils.d(TAG, "session:" + session);
                initVideoStatus(session);//session >=0 或者 =-2 是属于正常，如果<0 是属于不正常
            }

        }));
    }

    /**
     * 获取视频数据
     */
    private void doOnGetVideoData() {
        pb_video.setVisibility(View.GONE);
        LUtils.d(TAG, "开始获取视频数据..");
        // bitmapLock = ReentrantLock()
        rectF = new RectF();
        rectF.set(0f, 0f, (float) startWidth, (float) (startWidth * 0.5625));

        /**
         * 此处会不断返回一张张的图片视频数据,然后进行渲染
         */
        addSubscription(DevHelper.Companion.getDevHelper().devGetVideoFrame(session, this, new DevHelper.EquipmentOffline() {
            @Override
            public void eqvipemntOffline() {
                LUtils.d(TAG, "设备属于离线状态");
            }
        }).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {

                videoBitmap = bitmap;
                if (bitmap != null && !bitmap.isRecycled()) {
                    //进行截图
                    if (screenshots) {
                        LUtils.d(TAG, "截屏");
                        doOnScreenshots(bitmap);/////////////dddddddddddddddddddddddddddddddddddddd//////这个地方
                    }

                    if (surfaceHolder.getSurface().isValid()) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        canvas.drawBitmap(bitmap, null, rectF, null);
                        // 释放canvas锁，并且显示视图
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }


                } else {
                    LUtils.d(TAG, "获取视频数据:${t?.message}");
                }
            }
        }));

    }


    /**
     * 截取图片的Bitmap
     */
    private void doOnScreenshots(Bitmap bitmap) {
        screenshots = false;
        if (!bitmap.isRecycled()) {
            //            showImageDialog(bitmap);
        }
        if (bitmap == null) {

            LUtils.d(TAG, "bitmap: null");
        } else {
            mcapimgs.setImageBitmap(bitmap);
            bitmapToPath(bitmap);
            LUtils.d(TAG, "bitmap: != null");
        }
    }

    private void bitmapToPath(Bitmap bitmap) {
        //FunPath.PATH_VIDEO + File.separator + fun_DevSn
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = sdf.format(new Date());
            strDate = strDate + System.currentTimeMillis();

            FileOutputStream bao = new FileOutputStream(FunPath.PATH_PHOTO + File.separator + mFun_devSn + File.separator + strDate + ".jpg");
            LUtils.d(TAG, "baoPATH_VIDE==" + FunPath.PATH_PHOTO + File.separator + mFun_devSn + File.separator + strDate + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            bao.flush();
            bao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化视频连接状态
     */
    private void initVideoStatus(final int session) {
        this.session = session;
        if (session > 0) {
            addSubscription(DevHelper.Companion.getDevHelper().devInitPlayer(session).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean initResult) throws Exception {
                    LUtils.d(TAG, "初始化连接视频状态：" + initResult);
                    if (initResult) {
                        if (voiceHelper == null) {
                            voiceHelper = new VoiceHelper();
                        }
                        voiceHelper.init(session);
                        doOnGetVideoData();//获取视频数据
                        isSuccessInit = true;//成功初始化

                        voiceHelper.playPhoneVoice(session);//播放声音
                        // getBettery();//获取电量
                        sendVoice();//设备接受声音

                        initVideoSuccess(true);
                    } else {
                        initVideoFailed();
                    }

                }
            }));
        } else {
            initVideoFailed();
        }
    }

    /**
     * 初始化视频失败
     */
    private void initVideoFailed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pb_video.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
                //                showToast(getString(R.string.init_failed));
            }
        }, 1000);

    }

    /**
     * 设备接受声音
     */
    private void sendVoice() {

    }


    /**
     * 获取电量
     */
    private void getBettery() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccessInit)
                    addSubscription(DevHelper.Companion.getDevHelper().devGetBattery(session).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            //                            LUtils.d(TAG, "获取电量：" + s);
                            //                            doOnBattery(s);
                        }
                    }));
            }
        }, 1000);
    }

    /**
     * 操作电池电量显示逻辑
     *
     * @param battery 当前电池电量
     */
    private void doOnBattery(String battery) {
        if (battery.startsWith("0")) {

        }

        int batteryInt = 100;
        try {
            batteryInt = Integer.parseInt(battery);
            LUtils.d(TAG, "batteryInt===" + batteryInt);
            if (batteryInt > 0 && batteryInt <= 100) {
                // 显示电池电量百分比
                //                tvBattery.setVisibility(View.VISIBLE);
                //                tvBattery.setText(batteryInt + "%");
                // 防止固件返回错误的参数
                if (battery.equals("0") || battery.equals("00")) {
                    //                    tvBattery.setVisibility(View.GONE);
                    //                    ivBattery.setVisibility(View.GONE);
                } else {
                    //                    ivBattery.setVisibility(View.VISIBLE);
                    //                    BatteryUtils.showBattery(ivBattery, battery);
                    // 监测到当前的电池电量,提示电量不足
                    if (batteryInt <= 30) {
                        ToastUtils.showToast(this, "设备的电池电量不足30%,请及时充电.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    void verifyAudioPermissions() {

        ArrayList<String> listPermission = new ArrayList<>();
        //申请录音权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            listPermission.add(Manifest.permission.RECORD_AUDIO);
        }

        //写入文件权限

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (listPermission.size() > 0) {
            String[] array = (String[]) listPermission.toArray(new String[listPermission.size()]);
            ActivityCompat.requestPermissions(this, array, GET_RECODE_AUDIO);
        }
    }

    boolean isActionDown = false;
    boolean isNeedPlayVoice = false;//是否需要声音

    private void initclick() {
        //        lineRight = findViewById(R.id.lineRight);
        //        lineRight.setOnClickListener(this);

        cb_video_btm.setOnClickListener(this);//录屏
        btopicact.setOnClickListener(this);//
        btovideoact.setOnClickListener(this);//
        cb_photo_btm.setOnClickListener(this);//截屏
        findViewById(R.id.ib_volume).setOnClickListener(this);//是否播放设备声音
        butSendVoice.setOnClickListener(this);


        findViewById(R.id.iv_call_off).setOnClickListener(this);//挂断
        //        findViewById(R.id.iv_call_lock).setOnClickListener(this);//开锁

        LUtils.d(TAG, "是否播放声音：" + startDeviceVoice.isChecked());
        butSendVoice.setOnTouchListener(this);
        loadDate();
    }

    /**
     * 获取屏幕的尺寸
     */
    private void getDisplayMetrics() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        startWidth = metric.widthPixels;     // 屏幕宽度（像素）
        startHeight = metric.heightPixels;   // 屏幕高度（像素
    }

    private void initview() {
        setTitle("视频播放");
        //        title.setTextColor(ContextCompat.getColor(this, R.color.white));
        //        findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        getDisplayMetrics();//获取屏幕的尺寸
        sfv_play = findViewById(R.id.sfv_play);

        pb_video = findViewById(R.id.pb_video);
        tv_status = findViewById(R.id.tv_status);

        // 解决SurfaceView调用setZOrderOnTop(true)遮挡其他控件
        sfv_play.setZOrderOnTop(true);
        sfv_play.setZOrderMediaOverlay(true);
        surfaceHolder = sfv_play.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        //电池
        //        ivBattery = findViewById(R.id.iv_battery);
        //        tvBattery = findViewById(R.id.tv_battery);

        mcapimgs = findViewById(R.id.capimgs);

        //声音
        startDeviceVoice = findViewById(R.id.ib_volume);
        butSendVoice = findViewById(R.id.iv_voice_btm);
        tvAction = findViewById(R.id.tvAction);
        //录屏
        cb_video_btm = findViewById(R.id.cb_video_btm);
        //录屏时间
        chronometer = findViewById(R.id.chronometer);
        //截屏
        cb_photo_btm = findViewById(R.id.cb_photo_btm);
        btopicact = findViewById(R.id.btopicact);
        btovideoact = findViewById(R.id.btovideoact);

        //开锁按钮
        //        lineLock = findViewById(R.id.lineLock);
        //猫眼，门禁，一体锁 需要有开锁按钮
        //        if (!TextUtils.isEmpty(devType) && (devType.equals(DevConstants.DEV_TYPE.ONO_LOCK) || devType.equals(DevConstants.DEV_TYPE.CAT_EYES))) {
        //            lineLock.setVisibility(View.VISIBLE);
        //
        //        } else {
        //            lineLock.setVisibility(View.GONE);
        //        }
    }

    private void init() {
        //        account = NetConfig.getUserAccount();//登录账号
        Bundle extras = getIntent().getExtras();

        mFun_devSn = extras.getString("FUN_DEVICE_SN");
        initFile(mFun_devSn);
        getListData(mFun_devSn);
    }

    private void startLocalPic(String spic) {
        Intent oin = new Intent();
        oin.putExtra("IMAGE_LOCAL_PIC", spic);
        oin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        oin.setClass(PlayerLiveActivity.this, CameraPicActivity.class);
        startActivity(oin);

    }

    private void sratrVideoList(String videopath) {
        //FunPath.PATH_VIDEO + File.separator
        Intent oin = new Intent();
        oin.putExtra("IMAGE_LOCAL_VIDEO", videopath);
        oin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        oin.setClass(PlayerLiveActivity.this, CameraVideoActivity.class);
        startActivity(oin);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_call_off:
                finish();
                break;
            case R.id.btovideoact:
                sratrVideoList(FunPath.PATH_VIDEO + File.separator + mFun_devSn + File.separator);
                break;
            case R.id.btopicact:
                startLocalPic(FunPath.PATH_PHOTO + File.separator + mFun_devSn + File.separator);
                //                ToastUtils.showToast(CameraActivity.this, "该功能暂未开放");
                break;
            //            case R.id.lineRight://右侧按钮
            //                setOneCeClick(v);
            //                //                Intent intent = new Intent(this, AeviceSettingActivity.class);
            //                //                intent.putExtra("session", session);
            //                //                intent.putExtra("obj", itemBean);
            //                //                startActivityForResult(intent, START_SETTING);
            //                break;
            case R.id.cb_photo_btm://截屏
                LUtils.d(TAG, "截屏");
                screenshots = true;
                setOneCeClick(v);
                break;
            case R.id.ib_volume://播放设备声音
                if (voiceHelper != null)
                    if (startDeviceVoice.isChecked()) {
                        voiceHelper.playPhoneVoice(session);
                    } else {
                        voiceHelper.stopPhoneVoice();
                    }
                break;

            case R.id.cb_video_btm://录屏
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, PERMISSION_WRITE, GET_RECODE_AUDIO);
                    ToastUtils.showToast(PlayerLiveActivity.this, "请打开录音权限");
                } else {
                    Long currentTime = System.currentTimeMillis();
                    startOrStopRecord(currentTime, cb_video_btm);
                }
                break;
            //            case R.id.iv_call_lock://点击开锁
            //                setOneCeClick(v);
            //
            //                break;
        }
    }

    /**
     * 视频录制计时器
     *
     * @param isRecord
     */
    private void doOnVideoRecordTimer(boolean isRecord) {
        if (isRecord) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            chronometer.setVisibility(View.VISIBLE);
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            chronometer.setVisibility(View.GONE);
        }
    }

    /**
     * 录制视频
     */
    String recordPath = null;
    private Long lastTime = 0l;

    /**
     * 开始或者停止录制
     *
     * @param currentTime
     * @param but
     */
    private void startOrStopRecord(Long currentTime, final CheckBox but) {
        //  LogUtil.i("strMes:"+but);
        setOneCeClick(but);//处理点击事件并发
        LUtils.d(TAG, "--currentTime - lastTime------" + (currentTime - lastTime));
        if ((but.isChecked()) && currentTime - lastTime < 2000) {

            // ToastUtils.showToast(mContext, "to short");
            doOnVideoRecordTimer(but.isChecked());
            //Native.StopAviRecord();
            DevHelper.Companion.getDevHelper().devStopAviRecord();
            if (!TextUtils.isEmpty(recordPath)) {
                Log.d("PlayerLiveActivity", " isEmpty  recordPath：" + recordPath);
                but.setClickable(false);
                addSubscription(FileSdcardHelper.deleteImageFile(recordPath).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        LUtils.d(TAG, "删除成功");
                        recordPath = null;
                        but.setClickable(true);
                        lastTime = Long.valueOf(01);
                    }
                }));
            }

        } else {

            int time = 0;
            if (!but.isChecked()) {
                time = 350;
                lastTime = Long.valueOf(01);

                //                ToastUtils.showToast(PlayerLiveActivity.this, "录制完成：" + FileSizeUtil.getAutoFileOrFilesSize(recordPath));
                Log.d("PlayerLiveActivity", "文件目录是：" + recordPath);
            } else {
                lastTime = System.currentTimeMillis();
                time = 0;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recordPath = PlayHelpUtils.videoRecord(FileSdcardHelper.app_sd, but.isChecked(), mFun_devSn, videoBitmap);
                    Log.d("PlayerLiveActivity", " Handler  recordPath：" + recordPath);
                }
            }, time);

            doOnVideoRecordTimer(but.isChecked());
        }
    }

    /**
     * 回收资源
     */
    private void doOnRecycle() {
        rectF = null;
        // 注销P2P连接
        DevHelper.Companion.getDevHelper().devDeInitP2P();//断开P2P连接
        DevHelper.Companion.getDevHelper().devDisconnect(session);//断开设备连接
        if (voiceHelper != null) {
            voiceHelper.destroyVoice();//回收音频类资源
        }
        RingUtils.init().stopPlayingRings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doOnRecycle();
        unDisposable();
    }

    /**
     * 获取电脑
     */
    @Override
    protected void onResume() {
        super.onResume();
        getBettery();
        if (isNeedPlayVoice) {
            startDeviceVoice.performClick();
            isNeedPlayVoice = false;
        }
       /* if (isNeedSendVoice){
            butSendVoice.performClick();
            isNeedPlayVoice=false;
        }*/
    }


    @Override
    protected void onStop() {
        super.onStop();
        // 跳转页面需要关闭录制(此方法可以直接调用。没有开始录像也可以调用结束)
        // 切换到设备设置页面，不需要停止视频播放等操作，因为进入设置页面会立刻发送38命令读取设备信息
        // 设备会立刻停止播放视频
        // doOnVideoRecordTimer(false);
        //
        if (cb_video_btm != null) {
            if (cb_video_btm.isChecked()) {//停止录屏
                cb_video_btm.performClick();
            }
        }

        /*if (butSendVoice.getTag().equals("1")) {//停止发送声音
            butSendVoice.performClick();
            isNeedSendVoice=true;
        }*/
        if (startDeviceVoice != null) {
            if (startDeviceVoice.isChecked()) {//停止播放声音
                isNeedPlayVoice = true;
                startDeviceVoice.performClick();
            }
        }

    }

    /**
     * 设置并发事件
     *
     * @param view
     */
    public void setOneCeClick(final View view) {
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 800);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isActionDown) {
                    isActionDown = true;
                    LUtils.d(TAG, "按下");
                    if (ActivityCompat.checkSelfPermission(PlayerLiveActivity.this,
                            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PlayerLiveActivity.this, PERMISSION_AUDIO,
                                GET_RECODE_AUDIO);
                        // showToast("请打开录音权限");
                    } else {
                        butSendVoice.setTag("1");
                        voiceHelper.startSendVoice();
                        butSendVoice.setImageResource(R.drawable.voice_normal);
                        tvAction.setText("松手停止说话");

                        //说话的时候手机停止接受声音
                        if (startDeviceVoice.isChecked()) {
                            isNeedPlayVoice = true;
                            startDeviceVoice.performClick();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isActionDown = false;
                LUtils.d(TAG, "松开");
                butSendVoice.setTag("0");
                voiceHelper.stopSendVoice();
                butSendVoice.setImageResource(R.drawable.voice_btm);
                tvAction.setText("按住说话");
                if (isNeedPlayVoice) {
                    startDeviceVoice.performClick();
                    isNeedPlayVoice = false;
                }
                break;
        }
        return false;

    }
}
