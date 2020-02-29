package com.estone.bank.estone_appsmartlock.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.basic.G;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.ui.CameraActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lib.Mps.XPMS_SEARCH_ALARMINFO_REQ;
import com.lib.funsdk.support.FunAlarmNotification;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceAlarmListener;
import com.lib.funsdk.support.OnFunDeviceAlarmPicListener;
import com.lib.funsdk.support.OnFunLoginListener;
import com.lib.funsdk.support.config.AlarmInfo;
import com.lib.funsdk.support.models.FunDevice;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServiceGuidePushAlarmNotification extends Service implements OnFunLoginListener, OnFunDeviceAlarmListener {

    private static final String TAG = "ServiceGuidePushAlarmNotification";

    private FunDevice inAlarmDevice;

    private NotificationManager mNotifyManager = null;

    private boolean mHasStarted = false;

    private List<String> mLinkedDevSn = new ArrayList<String>();

    private final int MESSAGE_CHECK_ALARM_STATUS = 0x100;

    //    private String alarmPicPath = "";

    private Bitmap alarmBitmap;

    private String mCurrDate = null;
    private ServicesHandler mHandler;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private class ServicesHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CHECK_ALARM_STATUS: {
                    checkMps();
                }
                break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new ServicesHandler();
        mNotifyManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        LUtils.d(TAG, "--------------- onCreate  -----");
        // 添加用户登录监听
        FunSupport.getInstance().registerOnFunLoginListener(this);
        // 添加设备报警监听
        FunSupport.getInstance().registerOnFunDeviceAlarmListener(this);

        FunSupport.getInstance().registerOnFunDeviceAlarmPicListener(
                new OnFunDeviceAlarmPicListener() {
                    @Override
                    public void onDeviceAlarmSearchPicSuccess(String path) {
                        alarmBitmap = BitmapFactory.decodeFile(path);
                        if (alarmBitmap != null) {
                            try {
                                //TODO 这里直接获取警报列表，第一条会有问题，好像是雄迈服务器的图片还没有准备好，所以先等几秒再获取。
                                Thread.sleep(2500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            notifyDeviceAlarm(inAlarmDevice);
                        }
                    }
                });
    }

    @Override
    public void onStart(Intent intent, int startId) {
        startMps();
        resetCheckInterval();
        LUtils.d(TAG, "------------onResume--- onStart  -----");
    }

    @Override
    public void onDestroy() {
        FunSupport.getInstance().removeOnFunDeviceAlarmListener(this);
        FunSupport.getInstance().removeOnFunLoginListener(this);
        super.onDestroy();
    }

    /**
     * 初始化消息推送
     */
    private void startMps() {
        if (!FunSupport.getInstance().hasLogin()) {
            // 只有用户登录了之后才进行消息推送监听
            return;
        }
        if (mHasStarted) {
            // 已经启动了,无需重复启动
            return;
        }
        checkLinkDevices();
        mHasStarted = true;
    }

    private void stopMps() {
        clearLinkDevices();
        mHasStarted = false;
    }

    private void checkMps() {
        if (!mHasStarted
                && FunSupport.getInstance().hasLogin()) {
            startMps();
            LUtils.d(TAG, " startMps();");
        } else if (mHasStarted
                && !FunSupport.getInstance().hasLogin()) {
            stopMps();
            LUtils.d(TAG, "==== stopMps();");
        } else if (mHasStarted) {
            checkLinkDevices();
            LUtils.d(TAG, " checkLinkDevices();");
        }

        resetCheckInterval();
    }

    /**
     * 获取消息通知是否使能
     *
     * @param devMac
     * @return
     */
    private boolean enabelNotification(String devMac) {
        return FunAlarmNotification.getInstance().getDeviceAlarmNotification(devMac);
    }

    //flush reason set to EsxFlushModeReasonInvalidBinLayout
    private void checkLinkDevices() {
        List<FunDevice> devList = new ArrayList<FunDevice>();
        devList.addAll(FunSupport.getInstance().getDeviceList());
        //        List<FunDevice> deviceList = FunSupport.getInstance().getDeviceList();
        //        for (int i = 0; i < deviceList.size(); i++) {
        //            FunSupport.getInstance().requestDeviceRemove(deviceList.get(i));
        //        }
        LUtils.i(TAG, "unlink : " + FunSupport.getInstance().getDeviceList());
        synchronized (mLinkedDevSn) {
            // Unlink已经不存在的设备
            for (int i = mLinkedDevSn.size() - 1; i >= 0; i--) {
                String devSn = mLinkedDevSn.get(i);
                if (null == FunSupport.getInstance().findDeviceBySn(devSn)
                    //					|| !enabelNotification(devSn) ) {
                        ) {
                    LUtils.i(TAG, "unlink : " + devSn);

                    // 取消设备报警(报警服务器)
                    FunSupport.getInstance().mpsUnLinkDevice(devSn);
                    // 从列表中移除
                    mLinkedDevSn.remove(i);
                }
            }

            // 添加还没有的Linked的设备
            for (FunDevice dev : devList) {
                String devSn = dev.getDevSn();
                if (!mLinkedDevSn.contains(devSn)
                    //						&& enabelNotification(devSn) ) {
                        ) {
                    LUtils.i(TAG, "link : " + devSn);
                    // 添加设备报警(报警服务器支持)
                    FunSupport.getInstance().mpsLinkDevice(dev);
                    mLinkedDevSn.add(devSn);
                }
            }
        }
    }

    private void clearLinkDevices() {
        synchronized (mLinkedDevSn) {
            for (String devSn : mLinkedDevSn) {
                FunSupport.getInstance().mpsUnLinkDevice(devSn);
            }
            mLinkedDevSn.clear();
        }
    }

    private void resetCheckInterval() {
        if (null != mHandler) {
            mHandler.removeMessages(MESSAGE_CHECK_ALARM_STATUS);
            mHandler.sendEmptyMessageDelayed(MESSAGE_CHECK_ALARM_STATUS, 10000);
        }
    }

    private void notifyDeviceAlarm(FunDevice funDev) {
        //mNotifyManager.cancel(funDev.getId());
        if (null == funDev) {
            return;
        }

        LUtils.i(TAG, "notifyDeviceAlarm : " + funDev.getDevSn() + ", " + funDev.getId());

        String title = getResources().getString(com.example.funsdkdemo.R.string.device_alarm_notification);

        Intent newIntent = new Intent(this, CameraActivity.class);
        newIntent.putExtra("FUN_DEVICE_ID", funDev.getId());
        newIntent.putExtra("FUN_DEVICE_SN", funDev.getDevSn());
        newIntent.putExtra("FUN_DEVICE_ROOMID", funDev.getId() + "");
        newIntent.putExtra("FUN_DEVICE_ROOMNAME", funDev.getDevSn());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                funDev.getId(), newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.bind_otherpic)
                .setTicker(title)
                .setContentTitle(getResources().getString(R.string.door_bell_rings))
                .setContentText(SimpleDateFormat.getDateInstance().format(System.currentTimeMillis()))
                .setContentIntent(pendingIntent)
                .setNumber(1)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(alarmBitmap));
        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
        notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
        mNotifyManager.notify(funDev.getId(), notification);
    }


    private void trySearchAlarm(Date date, FunDevice mFunDevice) {
        XPMS_SEARCH_ALARMINFO_REQ info = new XPMS_SEARCH_ALARMINFO_REQ();
        G.SetValue(info.st_00_Uuid, mFunDevice.getDevSn());

        if (null == date) {
            date = new Date();
        }

        initSearchInfo(info, date, -1);
        info.st_06_Number = 128;

        mCurrDate = String.format("%04d-%02d-%02d",
                info.st_02_StarTime.st_0_year,
                info.st_02_StarTime.st_1_month,
                info.st_02_StarTime.st_2_day);

        boolean b = FunSupport.getInstance().requestDeviceSearchAlarmInfo(mFunDevice, info);
        LUtils.e(TAG, "400040004000400040004000400040004000trySearchAlarm : b==" + b + "==mFunDevice" + mFunDevice);
    }

    private void initSearchInfo(XPMS_SEARCH_ALARMINFO_REQ info, Date date,
                                int channel) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        info.st_02_StarTime.st_0_year = c.get(Calendar.YEAR);
        info.st_02_StarTime.st_1_month = c.get(Calendar.MONTH) + 1;
        info.st_02_StarTime.st_2_day = c.get(Calendar.DATE);
        info.st_02_StarTime.st_4_hour = 0;
        info.st_02_StarTime.st_5_minute = 0;
        info.st_02_StarTime.st_6_second = 0;
        info.st_03_EndTime.st_0_year = c.get(Calendar.YEAR);
        info.st_03_EndTime.st_1_month = c.get(Calendar.MONTH) + 1;
        info.st_03_EndTime.st_2_day = c.get(Calendar.DATE);
        info.st_03_EndTime.st_4_hour = 23;
        info.st_03_EndTime.st_5_minute = 59;
        info.st_03_EndTime.st_6_second = 59;


        info.st_04_Channel = channel;
    }

    @Override
    public void onLoginSuccess() {
        // 用户登录成功, 启动报警推送消息检测
        resetCheckInterval();
        LUtils.d(TAG, "servers  用户登录成功");
    }

    @Override
    public void onLoginFailed(Integer errCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLogout() {
        // 用户登出后,立刻停止报警推送消息检测
        stopMps();
        LUtils.d(TAG, "==== onLogout();");
    }

    @Override
    public void onDeviceAlarmReceived(FunDevice funDevice) {
        // 收到设备报警
        LUtils.d(TAG, "onDeviceAlarmReceived: " + funDevice.getDevSn());
        inAlarmDevice = funDevice;
        try {
            //TODO 这里直接获取警报列表，第一条会有问题，好像是雄迈服务器的图片还没有准备好，所以先等2秒再获取。
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        trySearchAlarm(null, funDevice);
        notifyDeviceAlarm(inAlarmDevice);
    }

    //	// 搜索历史报警消息成功
    @Override
    public void onDeviceAlarmSearchSuccess(final FunDevice funDevice,
                                           List<AlarmInfo> infos) {
        try {
            File file = File.createTempFile("test", ".jpeg");
            LUtils.d(TAG, "00000000000000==" + infos.get(0));
            FunSupport.getInstance().getAlarmPic(funDevice, file.getPath(), infos.get(0).getPic());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeviceAlarmSearchFailed(FunDevice funDevice, int errCode) {
        // TODO Auto-generated method stub
        LUtils.d(TAG, "onDeviceAlarmSearchFailed : " + funDevice.getDevSn() + ", " + funDevice.getId());
    }

    @Override
    public void onDeviceLanAlarmReceived(FunDevice funDevice,
                                         AlarmInfo alarmInfo) {
        //		notifyDeviceAlarm(funDevice);
        LUtils.d(TAG, "onDeviceLanAlarmReceived : " + funDevice.getDevSn() + ", " + funDevice.getId());
    }
}
