package com.estone.bank.estone_appsmartlock.apps;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;

import com.estone.bank.estone_appsmartlock.managers.DbManager;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.services.ServiceGuidePushAlarmNotification;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;
import com.lib.funsdk.support.FunSupport;
import com.mob.MobApplication;
import com.mob.MobSDK;
import com.uuzuche.lib_zxing.DisplayUtil;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.firmwarelib.nativelibs.config.SdkConfig;
import cn.firmwarelib.nativelibs.utils.NetLibApplication;

public class MyApplications extends MobApplication {
    //MobApplication {

    static private final String TAG = "SmartLockApplication";
    private static List<Activity> lists = new ArrayList<>();

    public static void addActivityToBeDestory(Activity activity) {
        lists.add(activity);
    }

    public static void clearActivitiesDestoryList() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }

            lists.clear();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initDisplayOpinion(this);
        ShareUtil.initShared(this);
//
        setRegistXMId("oAuO41T9E4wiiHm2GNHMINqzX_5Q");
        setRegistXMPass("62065847");


        //        setRegistXMId("oAuO41TxcElPRF_neoNpPGPF1rFd");
        //        setRegistXMPass("43841510");
//          辰通
//                setRegistXMId("oAuO41Sibq7m7n7Sjz-nANRy3HNY");
//                setRegistXMPass("22378316");
//                WxLogin.initWx(this);
        DbManager.init(this);
        FunSupport.getInstance().init(this);
        HttpManager.getInstance();
        MobSDK.init(this);

        //yiding
        NetLibApplication.getInstance().init(this);

        SdkConfig.getInstance().initSdkConfig("ybell", "120.79.143.73", "8080", "2.0");
        SdkConfig.getInstance().setIS_PUSH(true);
        // 二维码
        ZXingLibrary.initDisplayOpinion(this);


        //打开alarm service
        //        ViewTarget.setTagId(R.id.glide_tag);
        Intent intent = new Intent(this, ServiceGuidePushAlarmNotification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        //        String fileContent = getFileContent(new File("storage/emulated/0/gateway_serial_number.txt"));
        //        Log.d(TAG, "fileContent ==" + fileContent);
    }

    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            LUtils.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    //读取指定目录下的所有TXT文件的文件内容
    protected String getFileContent(File file) {
        String content = "";
        if (file.isDirectory()) {    //检查此路径名的文件是否是一个目录(文件夹)
            Log.i(TAG, "The File doesn't not exist "
                    + file.getName().toString() + file.getPath().toString());
        } else {
            if (file.getName().endsWith(".txt")) {//文件格式为txt文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                                = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line;
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }
                        instream.close();        //关闭输入流
                    }
                } catch (java.io.FileNotFoundException e) {
                    Log.d(TAG, "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }
        return content;
    }


    public static void setRegistXMId(String xmid) {
        if (xmid != null) {
            ShareUtil.putString("XMACMINID", xmid);
        }

    }

    public static void setRegistXMPass(String xmpass) {

        if (xmpass != null) {
            ShareUtil.putString("XMPASSWORD", xmpass);
        }
    }

    public static String getRegistXMId() {

        return ShareUtil.getString("XMACMINID");
    }

    public static String getRegistXMPass() {

        return ShareUtil.getString("XMPASSWORD");
    }

    public static void initDisplayOpinion(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(context, dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(context, dm.heightPixels);
    }


}
