package com.estone.bank.estone_appsmartlock.ui;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.utils.DiaUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;
import com.example.common.DialogInputPasswd;
import com.example.common.UIFactory;
import com.lib.FunSDK;
import com.lib.funsdk.support.FunDevicePassword;
import com.lib.funsdk.support.FunError;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.OPPTZControl;
import com.lib.funsdk.support.config.OPPTZPreset;
import com.lib.funsdk.support.config.SystemInfo;
import com.lib.funsdk.support.models.FunDevType;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunStreamType;
import com.lib.funsdk.support.utils.TalkManager;
import com.lib.funsdk.support.widget.FunVideoView;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Demo: 监控类设备播放控制等
 *
 * @author Administrator
 */
@SuppressLint("ClickableViewAccessibility")
public class CameraActivity
        extends BaseActivity
        implements OnClickListener,
        OnFunDeviceOptListener,
        OnPreparedListener,
        OnErrorListener,
        OnInfoListener
        //        Switch.OnCheckedChangeListener
{
    private static String TAG = "CameraActivity";

    private FunDevice mFunDevice;

    private RelativeLayout mLayoutVideoWnd;
    private FunVideoView mFunVideoView;
    private TextView mTextStreamType;
    private TextView cameraviewshowhome;
    private Chronometer timeadd;
    private ImageView mBtnStream;
    private boolean isreaStream = true;
    private Button mBtnCapture;
    //    private Button mBtnRecord = null;
    private ImageButton mBtnScreenRatio;
    private Button takevideo;
    //    private Button mBtnFishEyeInfo = null;
    //    private View mSplitView = null;
    private RelativeLayout mLayoutRecording;

    private LinearLayout mLayoutControls;
    //    private LinearLayout mLayoutChannel  ;
    private Button mBtnVoiceTalk;
    private ImageView mBtnVoice;
    private boolean hasVoice = true;
    private ImageButton mBtnDevCapture;
    private ImageButton mBtnDevRecord;

    private ImageView btnsoundschange, addkeyl_activity_back;

    private TextView mTextVideoStat;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    private String preset = null;
    private int mChannelCount;
    private boolean isGetSysFirst = true;

    private LinearLayout lly_back;

    private final int MESSAGE_PLAY_MEDIA = 0x100;

    //    private final int MESSAGE_AUTO_HIDE_CONTROL_BAR = 0x102;
    private final int MESSAGE_TOAST_SCREENSHOT_PREVIEW = 0x103;
    private final int MESSAGE_OPEN_VOICE = 0x104;

    // 自动隐藏底部的操作控制按钮栏的时间
    //    private final int AUTO_HIDE_CONTROL_BAR_DURATION = 10000;


    private boolean booltakevideo = true;
    private TalkManager mTalkManager = null;

    private boolean mCanToPlay = false;

    public String NativeLoginPsw; //本地密码

    //    private LinearLayout btnOpenDoor;
    private AnimationDrawable mAnimaition;
    private Bean_AllDevices.InfosBean mSendinfosBean;

    private boolean toobarisshow = true;

    private cameraHandler mHandler;


    class cameraHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PLAY_MEDIA: {
                    playRealMedia();
                }
                break;

                case MESSAGE_TOAST_SCREENSHOT_PREVIEW: {
                    String path = (String) msg.obj;
                    toastScreenShotPreview(path);
                }
                break;
                case MESSAGE_OPEN_VOICE: {
                    mFunVideoView.setMediaSound(true);
                }
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mHandler = new cameraHandler();
        String funDeviceId = getIntent().getStringExtra("FUN_DEVICE_SN");
        if (funDeviceId != null) {
            mFunDevice = getFunDeviceFromSN(funDeviceId);
        }

        LUtils.d(TAG, "funDeviceId==null" + funDeviceId);
        LUtils.d(TAG, "mServicesdeviceById==null" + mFunDevice);

        Bundle extras = getIntent().getExtras();
        mSendinfosBean = (Bean_AllDevices.InfosBean) extras.getSerializable("mSendinfosBean");
        //        mFunDevice = FunSupport.getInstance().findDeviceById(funDeviceId);
        File imgPath = new File(FunPath.PATH_PHOTO + File.separator + mFunDevice.getDevMac());
        if (imgPath != null && !imgPath.exists()) {
            FunPath.makeRootDirectory(imgPath.getPath());
            LUtils.d(TAG, "开始创建   " + imgPath);
        } else {
            LUtils.d(TAG, "imgPath 已经有了   " + imgPath);
        }
        File videoPath = new File(FunPath.PATH_VIDEO + File.separator + mFunDevice.getDevMac());
        if (videoPath != null && !videoPath.exists()) {
            FunPath.makeRootDirectory(videoPath.getPath());
            LUtils.d(TAG, "开始创建   " + videoPath);
        } else {
            LUtils.d(TAG, "imgPath 已经有了   " + videoPath);
        }

        FunSupport.getInstance().setDevWakeUp(mFunDevice);
        final ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle(mFunDevice.getDevSn());

        }
        initview();
//        if (mFunDevice.devType == FunDevType.EE_DEV_LAMP_FISHEYE) {
//            // 鱼眼灯泡,设置鱼眼效果
//            mFunVideoView.setFishEye(true);
//        }
        mFunVideoView.setOnTouchListener(new OnVideoViewTouchListener());
        mFunVideoView.setOnPreparedListener(this);
        mFunVideoView.setOnErrorListener(this);
        mFunVideoView.setOnInfoListener(this);
        // 注册设备操作回调
        FunSupport.getInstance().registerOnFunDeviceOptListener(this);
        mTalkManager = new TalkManager(mFunDevice);
        mCanToPlay = false;
        // 如果设备未登录,先登录设备
        if (!mFunDevice.hasLogin() || !mFunDevice.hasConnected()) {
            loginDevice();
        } else {
            requestSystemInfo();
        }
        mBtnVoiceTalk.setEnabled(true);
        OpenVoiceChannel();
        mBtnVoice.setBackgroundResource(R.mipmap.camerapreview_takepic_sounds);
    }


    public void btnClick() {
        timeadd.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - timeadd.getBase()) / 1000 / 60);
        timeadd.setFormat("0" + String.valueOf(hour) + ":%s");
        timeadd.start();
    }


    private FunDevice getFunDeviceFromSN(String sn) {
        List<FunDevice> devices = FunSupport.getInstance().getDeviceList();
        LUtils.d(TAG, " List<FunDevice>==" + devices.size());
        for (FunDevice device : devices) {
            if (device.getDevSn().equals(sn)) {
                return device;
            }
        }
        return null;
    }

    public void stopClick() {
        timeadd.stop();
    }

    private void initview() {
        String funDeviceRoomid = getIntent().getStringExtra("FUN_DEVICE_ROOMID");
        String funDeviceRoomName = getIntent().getStringExtra("FUN_DEVICE_ROOMNAME");
        mLayoutVideoWnd = findViewById(R.id.layoutPlayWnd_relyout);
        btnsoundschange = findViewById(R.id.btnsoundschange);
        addkeyl_activity_back = findViewById(R.id.addkeyl_activity_back);
        cameraviewshowhome = findViewById(R.id.cameraviewshowhome);
        timeadd = findViewById(R.id.timeadd);
        lly_back = findViewById(R.id.lly_back);

        // 视频显示为小窗口
        RelativeLayout.LayoutParams lpWnd = (RelativeLayout.LayoutParams) mLayoutVideoWnd.getLayoutParams();
        lpWnd.height = UIFactory.dip2px(this, 250);
        lpWnd.topMargin = UIFactory.dip2px(this, 0);
        mLayoutVideoWnd.setLayoutParams(lpWnd);

        takevideo = findViewById(R.id.takevideo);
        mBtnStream = findViewById(R.id.btnStream);
        mBtnCapture = findViewById(R.id.btnCapture);
        mBtnScreenRatio = findViewById(R.id.btnScreenRatio);
        mLayoutRecording = findViewById(R.id.layout_recording);
        mTextVideoStat = findViewById(R.id.textVideoStat);
        mBtnVoiceTalk = findViewById(R.id.btnVoiceTalk);
        mBtnVoice = findViewById(R.id.Btn_Talk_Switch);
        mBtnDevCapture = findViewById(R.id.btnDevCapture);
        mBtnDevRecord = findViewById(R.id.btnDevRecord);
        mLayoutControls = findViewById(R.id.layoutFunctionControl);
        mTextStreamType = findViewById(R.id.textStreamStat);
        mFunVideoView = findViewById(R.id.funVideoView);
        mFunVideoView.setOnTouchListener(new OnVideoViewTouchListener());
        mBtnStream.setOnClickListener(this);
        mBtnCapture.setOnClickListener(this);
        mLayoutVideoWnd.setOnClickListener(this);
        mBtnScreenRatio.setOnClickListener(this);
        btnsoundschange.setBackgroundResource(R.drawable.spack_voice_anim);
        mTextVideoStat.setText("请等待...");
        takevideo.setText(R.string.video_pic);
        cameraviewshowhome.setText(funDeviceRoomName + "---" + funDeviceRoomid);
        mAnimaition = (AnimationDrawable) btnsoundschange.getBackground();
        mAnimaition.setOneShot(false);
        mBtnVoiceTalk.setOnClickListener(this);
        takevideo.setOnClickListener(this);
        mBtnVoiceTalk.setOnTouchListener(mIntercomTouchLs);
        mBtnVoice.setOnClickListener(this);
        mBtnDevCapture.setOnClickListener(this);
        mBtnDevRecord.setOnClickListener(this);
        addkeyl_activity_back.setOnClickListener(this);
    }

    private class OnVideoViewTouchListener implements OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            System.out.println("TTT-->>> event = " + event.getAction());
            if (event.getAction() == MotionEvent.ACTION_UP) {

                // 显示或隐藏视频操作菜单
                if (lly_back.getVisibility() == View.VISIBLE) {

                    lly_back.setVisibility(View.GONE);
                } else {

                    lly_back.setVisibility(View.VISIBLE);
                }
            }
            return false;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                } else {
                    finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        stopMedia();
        FunSupport.getInstance().removeOnFunDeviceOptListener(this);
        LUtils.d(TAG, "onDestroy()");
        if (null != mFunDevice) {
            FunSupport.getInstance().requestDeviceLogout(mFunDevice);
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        if (mCanToPlay) {
            FunSupport.getInstance().setDevWakeUp(mFunDevice);
            playRealMedia();
        }
        resumeMedia();
        super.onResume();
    }


    @Override
    protected void onPause() {

        stopTalk();
        CloseVoiceChannel(0);
        stopMedia();

        pauseMedia();
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        // 如果当前是横屏，返回时先回到竖屏
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        finish();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 检测屏幕的方向：纵向或横向
        if (newConfig.orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // 当前为横屏， 在此处添加额外的处理代码
            showAsLandscape();

        } else if (newConfig.orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            // 当前为竖屏， 在此处添加额外的处理代码
            showAsPortrait();
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() >= 1000 && v.getId() < 1000 + mChannelCount) {
            mFunDevice.CurrChannel = v.getId() - 1000;
            mFunVideoView.stopPlayback();
            playRealMedia();
        }
        switch (v.getId()) {
            case 1101: {
                startDevicesPreview();
            }
            break;
            case R.id.backBtnInTopLayout: {
                //
                onBackPressed();
            }
            case R.id.addkeyl_activity_back: {
                // 返回/退出

                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                finish();
                //                if (null != mFunDevice) {
                //                    FunSupport.getInstance().requestDeviceLogout(mFunDevice);
                //                }

            }
            break;
            case R.id.takevideo: {
                // 录制视频
                if (booltakevideo) { //true
                    tryToRecord();
                    takevideo.setText(R.string.stopvideo_pic);
                    btnClick();
                    booltakevideo = false;

                } else {
                    stopClick();
                    booltakevideo = true;
                    takevideo.setText(R.string.video_pic);

                    tryToRecord();
                }

            }
            break;

            case R.id.btnStream: // 切换码流
            {
                if (isreaStream) {
                    isreaStream = false;
                    mBtnStream.setBackgroundResource(R.mipmap.camerapreview_lookdefault);
                } else {
                    isreaStream = true;
                    mBtnStream.setBackgroundResource(R.mipmap.camerapreview_lookclear);
                }
                switchMediaStream();
            }
            break;
            case R.id.btnCapture: // 截图
            {
                tryToCapture();
            }
            break;

            case R.id.Btn_Talk_Switch: {
                if (hasVoice) {
                    hasVoice = false;
                    mBtnVoice.setBackgroundResource(R.mipmap.camerapreview_takepic_nosounds);
                    CloseVoiceChannel(500);
                } else {
                    hasVoice = true;
                    OpenVoiceChannel();
                    mBtnVoice.setBackgroundResource(R.mipmap.camerapreview_takepic_sounds);
                }

            }
            break;
            //        case R.id.btn_quit_voice:
            //            {
            //                CloseVoiceChannel(500);
            //            }
            //            break;
            case R.id.btnDevCapture: // 远程设备图像列表
            {
                //                startPictureList();
                //FunPath.PATH_PHOTO + File.separator
                startLocalPic(FunPath.PATH_PHOTO + File.separator + mFunDevice.getDevMac() + File.separator);
                //                ToastUtils.showToast(CameraActivity.this, "该功能暂未开放");
            }
            break;
            case R.id.btnDevRecord: // 远程设备录像列表
            {
                //                startRecordList();
                sratrVideoList();

            }
            break;
            case R.id.btnScreenRatio: // 横竖屏切换
            {
                switchOrientation();
            }
            break;
            case R.id.btnSettings: // 系统设置/系统信息
            {
                startDeviceSetup();
            }
            break;
            case R.id.btnSetPreset: {
                final EditText editText = new EditText(this);
                int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                editText.setInputType(inputType);
                new AlertDialog.Builder(this).setTitle(R.string.user_input_preset_number)
                        .setView(editText)
                        .setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                int i = 0;
                                String preset = editText.getText().toString();
                                if (TextUtils.isEmpty(preset)) {
                                    i = 1;
                                } else {
                                    i = Integer.parseInt(preset);
                                }
                                if (i > 200) {
                                    Toast.makeText(getApplicationContext(), R.string.user_input_preset_number_warn, Toast.LENGTH_SHORT).show();
                                } else {
                                    // 注意：如果是IPC/摇头机,channel = 0, 否则channel=-1，以实际使用设备为准，如果需要兼容，可以两条命令同时发送
                                    OPPTZControl cmd = new OPPTZControl(OPPTZControl.CMD_SET_PRESET, 0, i);
                                    FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmd);

                                    // for Demo, 为了兼容设备，cmd2和cmd一起发送，两条命令的差别是channel值不同
                                    OPPTZControl cmd2 = new OPPTZControl(OPPTZControl.CMD_SET_PRESET, -1, i);
                                    FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmd2);
                                }
                            }

                        })
                        .setNegativeButton(R.string.common_cancel, null).show();
            }
            break;
            case R.id.btnGetPreset: {
                OPPTZPreset oPPTZPreset = (OPPTZPreset) mFunDevice.getConfig(OPPTZPreset.CONFIG_NAME);
                if (null != oPPTZPreset) {
                    int[] ids = oPPTZPreset.getIds();
                    int index = 0;
                    preset = null;
                    Arrays.sort(ids);
                    if (ids != null && ids.length > 0) {
                        final String[] idStrs = new String[ids.length];
                        for (int i = 0; i < ids.length; i++) {
                            idStrs[i] = (Integer.toString(ids[i]));
                        }
                        alert = null;
                        builder = new AlertDialog.Builder(this);
                        alert = builder
                                .setTitle(R.string.user_select_preset)
                                .setSingleChoiceItems(idStrs, index, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        preset = idStrs[which];
                                    }
                                })
                                .setPositiveButton(R.string.common_skip, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (TextUtils.isEmpty(preset)) {
                                            preset = idStrs[0];
                                        }
                                        which = Integer.parseInt(preset);
                                        OPPTZControl cmd = new OPPTZControl(OPPTZControl.CMD_GO_TO_PRESET, 0, which);
                                        FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmd);
                                    }
                                })
                                .setNegativeButton(R.string.common_delete, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (TextUtils.isEmpty(preset)) {
                                            preset = idStrs[0];
                                        }
                                        which = Integer.parseInt(preset);
                                        OPPTZControl cmd = new OPPTZControl(OPPTZControl.CMD_CLEAR_PRESET, 0, which);
                                        FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmd);
                                    }
                                }).setNeutralButton(R.string.common_correct, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OPPTZControl cmd = new OPPTZControl(OPPTZControl.CMD_CORRECT, 0, 0);
                                        FunSupport.getInstance().requestDeviceCmdGeneral(mFunDevice, cmd);
                                    }
                                }).create();
                        alert.show();
                    }
                }
            }
            break;
            //            case R.id.btnFishEyeInfo: {
            //                // 显示鱼眼信息
            //                showFishEyeInfo();
            //            }
            //            break;

            //            case R.id.btnOpenDoor:
            //                openDoor();
            //                break;
            case R.id.layoutPlayWnd_relyout:
                if (toobarisshow) {
                    toobarisshow = false;
                    lly_back.setVisibility(View.VISIBLE);
                } else {
                    toobarisshow = true;
                    lly_back.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }

    private void sratrVideoList() {
        //FunPath.PATH_VIDEO + File.separator
        Intent oin = new Intent();
        oin.putExtra("IMAGE_LOCAL_VIDEO", FunPath.PATH_VIDEO + File.separator + mFunDevice.getDevMac() + File.separator);
        oin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        oin.setClass(this, CameraVideoActivity.class);
        startActivity(oin);
    }

    private void startLocalPic(String spic) {
        Intent oin = new Intent();
        oin.putExtra("IMAGE_LOCAL_PIC", spic);
        oin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        oin.setClass(CameraActivity.this, CameraPicActivity.class);
        startActivity(oin);

    }


    private void tryToRecord() {
        if (!mFunVideoView.isPlaying() || mFunVideoView.isPaused()) {
            ToastUtils.showToast(CameraActivity.this, R.string.media_record_failure_need_playing);
            return;
        }
        if (mFunVideoView.bRecord) {
            mFunVideoView.stopRecordVideo();
            mLayoutRecording.setVisibility(View.INVISIBLE);
            toastRecordSucess(mFunVideoView.getFilePath());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = sdf.format(new Date());
            mFunVideoView.startRecordVideo(FunPath.PATH_VIDEO + File.separator + mFunDevice.getDevMac() + File.separator + strDate + ".mp4");
            mLayoutRecording.setVisibility(View.VISIBLE);
            ToastUtils.showToast(CameraActivity.this, R.string.media_record_start);
        }

    }

    /**
     * 视频截图,并延时一会提示截图对话框
     */
    private void tryToCapture() {
        if (!mFunVideoView.isPlaying()) {
            ToastUtils.showToast(CameraActivity.this, R.string.media_capture_failure_need_playing);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(new Date());
        strDate = strDate + System.currentTimeMillis();
        final String path = mFunVideoView.captureImage(FunPath.PATH_PHOTO + File.separator + mFunDevice.getDevMac() + File.separator
                + strDate + ".jpg");    //图片异步保存
        if (!TextUtils.isEmpty(path)) {
            Message message = new Message();
            message.what = MESSAGE_TOAST_SCREENSHOT_PREVIEW;
            message.obj = path;
            mHandler.sendMessageDelayed(message, 200);            //此处延时一定时间等待图片保存完成后显示，也可以在回调成功后显示
        }
    }


    /**
     * 显示截图成功对话框
     *
     * @param path
     */
    private void toastScreenShotPreview(final String path) {
        View view = getLayoutInflater().inflate(R.layout.screenshot_preview, null, false);
        ImageView iv = view.findViewById(R.id.iv_screenshot_preview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(bitmap);
        new AlertDialog.Builder(this)
                .setTitle(R.string.device_socket_capture_preview)
                .setView(view)
                .setPositiveButton(R.string.device_socket_capture_save,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(path);
                                //                                File imgPath = new File(FunPath.PATH_PHOTO + File.separator + mFunDevice.getDevMac() + File.separator
                                //                                        + file.getName());
                                LUtils.d(TAG, "文件路径是  file  " + file);
                                LUtils.d(TAG, "文件路径是  path  " + path);
                                //                                if (imgPath.exists()) { // 是否存在
                                //                                    ToastUtils.showToast(CameraActivity.this, R.string.device_socket_capture_exist);
                                //                                } else {
                                //                                    FileUtils.copyFile(path, "" + imgPath);
                                //                                    LUtils.d(TAG, "文件新路径是 copyFile " + imgPath);
                                //                                }

                            }
                        })
                .setNegativeButton(R.string.device_socket_capture_delete,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FunPath.deleteFile(path);
                                ToastUtils.showToast(CameraActivity.this, R.string.device_socket_capture_delete_success);
                            }
                        })
                .show();
    }

    /**
     * 显示录像成功对话框
     *
     * @param path
     */
    private void toastRecordSucess(final String path) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.device_sport_camera_record_success)
                .setMessage(getString(R.string.media_record_stop) + path)
                .setPositiveButton(R.string.device_sport_camera_record_success_open,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                String type = "video/*";
                                //Uri uri = Uri.fromFile(EstoneVideoFile);
                                Uri uri = Uri.fromFile(new File(path));
                                intent.setDataAndType(uri, type);
                                startActivity(intent);
                                LUtils.d(TAG, "------------uri------" + uri.toString());
                                LUtils.d(TAG, "------------path------" + path);
                            }
                        })
                .setNegativeButton(R.string.device_sport_camera_record_success_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
    }


    private void showAsLandscape() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 隐藏底部的控制按钮区域
        mLayoutControls.setVisibility(View.GONE);
        //        btnOpenDoor.setVisibility(View.GONE);
        lly_back.setVisibility(View.GONE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        LUtils.d(TAG, "width===" + screenWidth);
        LUtils.d(TAG, "height===" + screenHeight);

        // 视频窗口全屏显示
        RelativeLayout.LayoutParams lpWnd = (RelativeLayout.LayoutParams) mLayoutVideoWnd.getLayoutParams();
        lpWnd.width = screenWidth;
        lpWnd.height = screenHeight;
        mLayoutVideoWnd.setLayoutParams(lpWnd);

        // 上面标题半透明背景
        //		mBtnScreenRatio.setText(R.string.device_opt_smallscreen);
    }

    private void showAsPortrait() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 还原上面标题栏背景
        // 视频显示为小窗口
        RelativeLayout.LayoutParams lpWnd = (RelativeLayout.LayoutParams) mLayoutVideoWnd.getLayoutParams();
        lpWnd.height = UIFactory.dip2px(this, 250);
        lpWnd.topMargin = UIFactory.dip2px(this, 0);
        // lpWnd.addRule(RelativeLayout.BELOW, mLayoutTop.getId());
        mLayoutVideoWnd.setLayoutParams(lpWnd);

        // 显示底部的控制按钮区域
        mLayoutControls.setVisibility(View.VISIBLE);
        //        btnOpenDoor.setVisibility(View.VISIBLE);
        lly_back.setVisibility(View.VISIBLE);

    }

    /**
     * 切换视频全屏/小视频窗口(以切横竖屏切换替代)
     */
    private void switchOrientation() {
        // 横竖屏切换
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            //             setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
    }

    /**
     * 打开设备配置
     */
    private void startDeviceSetup() {
        //		Intent intent = new Intent();
        //		intent.putExtra("FUN_DEVICE_ID", mFunDevice.getId());
        //		intent.setClass(this, ActivityGuideDeviceSetup.class);
        //		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //		startActivity(intent);
    }

    /***
     * 打开 多通道预览
     */
    private void startDevicesPreview() {
        //		Intent intent = new Intent();
        //		intent.putExtra("FUNDEVICE_ID", mFunDevice.getId());
        //		intent.setClass(this, ActivityGuideDevicePreview.class);
        //		startActivityForResult(intent, 0);
    }


    private void loginDevice() {
        //		showWaitDialog();
        // 设备登录
        FunSupport.getInstance().requestDeviceLogin(mFunDevice);
    }

    private void requestSystemInfo() {
        //		showWaitDialog();

        FunSupport.getInstance().requestDeviceConfig(mFunDevice, SystemInfo.CONFIG_NAME);
    }

    // 获取设备预置点列表
    private void requestPTZPreset() {
        FunSupport.getInstance().requestDeviceConfig(mFunDevice, OPPTZPreset.CONFIG_NAME, 0);
    }


    private void playRealMedia() {

        // 显示状态: 正在打开视频...
        mTextVideoStat.setText(R.string.media_player_opening);
        mTextVideoStat.setVisibility(View.VISIBLE);

        if (mFunDevice.isRemote) {
            mFunVideoView.setRealDevice(mFunDevice.getDevSn(), mFunDevice.CurrChannel);
        } else {
            String deviceIp = FunSupport.getInstance().getDeviceWifiManager().getGatewayIp();
            mFunVideoView.setRealDevice(deviceIp, mFunDevice.CurrChannel);
        }

        // 打开声音
        mFunVideoView.setMediaSound(true);

        // 设置当前播放的码流类型
        if (FunStreamType.STREAM_SECONDARY == mFunVideoView.getStreamType()) {
            mTextStreamType.setText(R.string.media_stream_secondary);
        } else {
            mTextStreamType.setText(R.string.media_stream_main);
        }
    }

    // 添加通道选择按钮
    @SuppressWarnings("ResourceType")
    private void addChannelBtn(int channelCount) {

    }

    private void stopMedia() {
        if (null != mFunVideoView) {
            mFunVideoView.stopPlayback();
            mFunVideoView.stopRecordVideo();
        }
    }

    private void pauseMedia() {
        if (null != mFunVideoView) {
            mFunVideoView.pause();
        }
    }

    private void resumeMedia() {
        if (null != mFunVideoView) {
            mFunVideoView.resume();
        }
    }

    private void switchMediaStream() {
        if (null != mFunVideoView) {
            if (FunStreamType.STREAM_MAIN == mFunVideoView.getStreamType()) {
                mFunVideoView.setStreamType(FunStreamType.STREAM_SECONDARY);
            } else {
                mFunVideoView.setStreamType(FunStreamType.STREAM_MAIN);
            }

            // 重新播放
            mFunVideoView.stopPlayback();
            playRealMedia();
        }
    }


    private OnTouchListener mIntercomTouchLs = new OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            try {

                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    startTalk();
                    mAnimaition.setOneShot(false);
                    mAnimaition.start();//启动
                    btnsoundschange.setVisibility(View.VISIBLE);
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    stopTalk();
                    mAnimaition.stop();
                    btnsoundschange.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    private void startTalk() {
        if (mTalkManager != null && mHandler != null && mFunVideoView != null) {
            mTalkManager.onStartThread();
            mTalkManager.setTalkSound(false);
        }
    }

    private void stopTalk() {
        if (mTalkManager != null && mHandler != null && mFunVideoView != null) {
            mTalkManager.onStopThread();
            mTalkManager.setTalkSound(true);
        }
    }

    private void OpenVoiceChannel() {
        mFunVideoView.setMediaSound(false);            //关闭本地音频
        mTalkManager.onStartTalk();
        mTalkManager.setTalkSound(true);
    }

    private void CloseVoiceChannel(int delayTime) {
        mTalkManager.onStopTalk();
        mHandler.sendEmptyMessageDelayed(MESSAGE_OPEN_VOICE, delayTime);
    }

    /**
     * 显示输入设备密码对话框
     */
    private void showInputPasswordDialog() {
        DialogInputPasswd inputDialog = new DialogInputPasswd(this,
                getResources().getString(R.string.device_login_input_password), "", R.string.common_confirm,
                R.string.common_cancel) {

            @Override
            public boolean confirm(String editText) {
                // 重新以新的密码登录
                if (null != mFunDevice) {
                    NativeLoginPsw = editText;

                    onDeviceSaveNativePws();

                    // 重新登录
                    loginDevice();
                }
                return super.confirm(editText);
            }

            @Override
            public void cancel() {
                super.cancel();

                // 取消输入密码,直接退出
                finish();
            }

        };

        inputDialog.show();
    }


    public void onDeviceSaveNativePws() {
        FunDevicePassword.getInstance().saveDevicePassword(mFunDevice.getDevSn(),
                NativeLoginPsw);
        // 库函数方式本地保存密码
        if (FunSupport.getInstance().getSaveNativePassword()) {
            FunSDK.DevSetLocalPwd(mFunDevice.getDevSn(), "admin", NativeLoginPsw);
            // 如果设置了使用本地保存密码，则将密码保存到本地文件
        }
    }

    @Override
    public void onDeviceLoginSuccess(final FunDevice funDevice) {
        System.out.println("TTT---->>>> loginsuccess");

        if (null != mFunDevice && null != funDevice) {
            if (mFunDevice.getId() == funDevice.getId()) {

                // 登录成功后立刻获取SystemInfo
                // 如果不需要获取SystemInfo,在这里播放视频也可以:playRealMedia();
                requestSystemInfo();
            }
        }
    }

    @Override
    public void onDeviceLoginFailed(final FunDevice funDevice, final Integer errCode) {
        // 设备登录失败
        DiaUtils.hideWaitDialog();
        ToastUtils.showToast(CameraActivity.this, FunError.getErrorStr(errCode));
        LUtils.d(TAG, "  onDeviceLoginFailed========FunError.getErrorStr(extra)=" + FunError.getErrorStr(errCode));
        // 如果账号密码不正确,那么需要提示用户,输入密码重新登录
        if (errCode == FunError.EE_DVR_PASSWORD_NOT_VALID) {
            showInputPasswordDialog();
        }
    }

    @Override
    public void onDeviceGetConfigSuccess(final FunDevice funDevice, final String configName, final int nSeq) {
        int channelCount = 0;
        if (SystemInfo.CONFIG_NAME.equals(configName)) {
            if (!isGetSysFirst) {
                return;
            }
            // 更新UI
            //此处为示例如何取通道信息，可能会增加打开视频的时间，可根据需求自行修改代码逻辑
            if (funDevice.channel == null) {
                FunSupport.getInstance().requestGetDevChnName(funDevice);
                requestSystemInfo();
                return;
            }
            channelCount = funDevice.channel.nChnCount;
            if (channelCount > 1) {
                mChannelCount = channelCount;
                addChannelBtn(channelCount);
            }
            DiaUtils.hideWaitDialog();
            // 设置允许播放标志
            mCanToPlay = true;
            isGetSysFirst = false;
            // 获取信息成功后,如果WiFi连接了就自动播放
            // 此处逻辑客户自定义
            playRealMedia();
            // 如果支持云台控制,在获取到SystemInfo之后,获取预置点信息,如果不需要云台控制/预置点功能功能,可忽略之
            if (mFunDevice.isSupportPTZ()) {
                requestPTZPreset();
            }
        } else if (OPPTZPreset.CONFIG_NAME.equals(configName)) {
            Toast.makeText(getApplicationContext(), R.string.user_set_preset_succeed, Toast.LENGTH_SHORT).show();
        } else if (OPPTZControl.CONFIG_NAME.equals(configName)) {
            Toast.makeText(getApplicationContext(), R.string.user_set_preset_succeed, Toast.LENGTH_SHORT).show();

        }
    }

    //    private String getType(int i) {
    //        switch (i) {
    //            case 0:
    //                return "P2P";
    //            case 1:
    //                return "Forward";
    //            case 2:
    //                return "IP";
    //            case 5:
    //                return "RPS";
    //            default:
    //                return "";
    //        }
    //    }


    @Override
    public void onDeviceGetConfigFailed(final FunDevice funDevice, final Integer errCode) {
        //        ToastUtils.showToast(CameraActivity.this, FunError.getErrorStr(errCode));
        //        LUtils.d(TAG, "  onDeviceGetConfigFailed   FunError.getErrorStr(errCode)=" + FunError.getErrorStr(errCode));
        //        LUtils.d(TAG, " (errCode)=" +errCode);
        if (errCode == -11406) {
            funDevice.invalidConfig(OPPTZPreset.CONFIG_NAME);
        }
    }


    @Override
    public void onDeviceSetConfigSuccess(final FunDevice funDevice, final String configName) {

    }


    @Override
    public void onDeviceSetConfigFailed(final FunDevice funDevice, final String configName, final Integer errCode) {
        if (OPPTZControl.CONFIG_NAME.equals(configName)) {
            Toast.makeText(getApplicationContext(), R.string.user_set_preset_fail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeviceChangeInfoSuccess(final FunDevice funDevice) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDeviceChangeInfoFailed(final FunDevice funDevice, final Integer errCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDeviceOptionSuccess(final FunDevice funDevice, final String option) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDeviceOptionFailed(final FunDevice funDevice, final String option, final Integer errCode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {

    }


    @Override
    public void onPrepared(MediaPlayer arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // 播放失败
        ToastUtils.showToast(CameraActivity.this, getResources().getString(R.string.media_play_error)
                + " : "
                + FunError.getErrorStr(extra));
        LUtils.d(TAG, "  onError========FunError.getErrorStr(extra)=" + FunError.getErrorStr(extra));
        if (FunError.EE_TPS_NOT_SUP_MAIN == extra
                || FunError.EE_DSS_NOT_SUP_MAIN == extra) {
            // 不支持高清码流,设置为标清码流重新播放
            if (null != mFunVideoView) {
                mFunVideoView.setStreamType(FunStreamType.STREAM_SECONDARY);
                playRealMedia();
            }
        }

        return true;
    }


    @Override
    public boolean onInfo(MediaPlayer arg0, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mTextVideoStat.setText(R.string.media_player_buffering);
            mTextVideoStat.setVisibility(View.VISIBLE);
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            mTextVideoStat.setVisibility(View.GONE);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        mFunDevice.CurrChannel = arg1;
        System.out.println("TTTT----" + mFunDevice.CurrChannel);
        if (mCanToPlay) {
            FunSupport.getInstance().setDevWakeUp(mFunDevice);
            playRealMedia();
        }
    }


    @Override
    public void onDeviceFileListGetFailed(FunDevice funDevice) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
