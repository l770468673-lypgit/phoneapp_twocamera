package com.estone.bank.estone_appsmartlock.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.application.PushHelper;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.ViewPagerAdapter;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.fragments.AboutMineFragment;
import com.estone.bank.estone_appsmartlock.fragments.PropertiesFragment;
import com.estone.bank.estone_appsmartlock.https.beans.AppUpdate;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.receivers.MsgYTCastReceiver;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.firmwarelib.nativelibs.config.SdkConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private static String TAG = "MainActivity";
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    ArrayList<Fragment> mMFragmentList;
    RadioGroup main_rg;
    RadioButton Mmain_rbhome, Mmain_rbmine;
    private String mMUser_pic_head;
    private String mDownloadedAppPath;
    private static final int NO_3 = 0x3;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;
    private Notification mBuild;
    private Intent mIntentNotifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //        initPermission();
        LUtils.d(TAG, " onCreate  ");
        mMUser_pic_head = ShareUtil.getString("USER_PIC_HEAD");
        LUtils.d(TAG, "  mUser_pic_head   " + mMUser_pic_head);
        initPagerDATE(mMUser_pic_head);

        checuAppUpdate();
        LUtils.d(TAG, " FunPath.PATH_CAPTURE_TEMP " + FunPath.PATH_CAPTURE_TEMP);
        if (SdkConfig.getInstance().IS_PUSH()) {//判断是否需要TMS 推送
            PushHelper.register(this);
            SdkConfig.getInstance().setPushPath(MsgYTCastReceiver.class.getName());//设置获取推送地址
            SdkConfig.getInstance().setSDK_PREFIX("my_babai_hisi_");//设置固件版本前缀
        }
    }

    private void checuAppUpdate() {

        Call<AppUpdate> appUpdateCall = HttpManager.getInstance().getHttpClient().upDateApp(Contants.ACTIONID102, "1");
        appUpdateCall.enqueue(new Callback<AppUpdate>() {
            @Override
            public void onResponse(Call<AppUpdate> call, Response<AppUpdate> response) {
                AppUpdate body = response.body();
                if (body != null) {
                    int errorCode = body.getErrorCode();
                    if (errorCode == 0) {
                        final AppUpdate.ExtraBean extra = body.getExtra();
                        String versionCode = extra.getVersionCode();
                        int localVersion = MyApplications.getLocalVersion(MainActivity.this);
                        int iversionCode = Integer.parseInt(versionCode);
                        LUtils.d(TAG, "localVersion==" + localVersion);
                        LUtils.d(TAG, "iversionCode==" + iversionCode);
                        if (iversionCode > localVersion) {
                            // 开始下载
                            LUtils.d(TAG, "开始下载");
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("发现新版本" + extra.getVersionCode())
                                    .setMessage("是否升级？")
                                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_LONG).show();
                                            new AppDownloadTask().execute(extra.getUrl(),
                                                    extra.getApkFileName(), extra.getVersionCode());
                                        }
                                    }).create().show();

                        } else {
                            LUtils.d(TAG, "不下载");
                        }
                    } else {
                        LUtils.d(TAG, "body==null");
                    }
                } else {
                    LUtils.d(TAG, "请求失败 ");
                }
            }

            @Override
            public void onFailure(Call<AppUpdate> call, Throwable t) {

            }
        });
    }

    public class AppDownloadTask extends AsyncTask<String, Integer, Boolean> {
        private int mTotalBytes;


        //11//onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 初始化 NotificationCompat ；
            mBuilder = new NotificationCompat.Builder(MainActivity.this);
            mBuilder.setSmallIcon(R.mipmap.login_logo);
            mBuilder.setContentTitle("下载中....");
            mBuilder.setContentText("正在下载");
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //            mManager.notify(NO_3, mBuilder.build());
            mBuilder.setProgress(100, 0, false);

            mIntentNotifi = new Intent(Intent.ACTION_VIEW);
            mIntentNotifi.setAction(Intent.ACTION_VIEW);
            mIntentNotifi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntentNotifi.addCategory(Intent.CATEGORY_DEFAULT);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            InputStream is = null;
            OutputStream output = null;

            Log.e(TAG, "params[0]:" + params[0]);
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
                    Log.e(TAG, "connection failed:" + params[0]);
                    return Boolean.FALSE;
                }
                mTotalBytes = conn.getContentLength();
                Log.e(TAG, "mTotalBytes===:" + mTotalBytes);
                is = conn.getInputStream();
                File dir = new File(Contants.DOWNLOADED_APP_DIR);
                if (dir.exists()) {
                    File[] files = dir.listFiles();
                    for (File f : files) {
                        f.delete();
                    }
                } else {
                    dir.mkdirs();
                    Log.d(TAG, "create dir:" + dir.getAbsolutePath());
                }
                mDownloadedAppPath = Contants.DOWNLOADED_APP_DIR + params[1] + "_" + params[2] + ".apk";
                File file = new File(mDownloadedAppPath);
                Log.d(TAG, "mDownloadedAppPath ===" + mDownloadedAppPath);
                file.createNewFile();

                output = new FileOutputStream(file);
                byte[] buffer = new byte[(int) (1024 * 1024)];
                int current;
                int downloaded = 0;

                int downloaded_M = 0;
                while ((current = is.read(buffer)) != -1) {
                    output.write(buffer, 0, current);
                    downloaded += current;
                    if ((downloaded / (512 * 1024) > downloaded_M)) {
                        downloaded_M++;
                        publishProgress(downloaded);
                    }
                }
                output.flush();
                Log.d(TAG, "new app size:" + mTotalBytes + ", downloaded file length:" + file.length());
                return mTotalBytes == file.length() ? Boolean.TRUE : Boolean.FALSE;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception:", e);
                return Boolean.FALSE;
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    output = null;
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    is = null;
                }

            }
        }


        //2 onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mBuilder.setContentTitle("下载完成");
            mBuilder.setContentText("点击安装");
            mBuilder.setProgress(100, 100, false);
            if (aBoolean) {
                silentInstallApk(mDownloadedAppPath);
                //点击安装代码块
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LUtils.d(TAG, "download package:" + values[0]);
            LUtils.d(TAG, "onProgressUpdate ---mTotalBytes --------  ==" + mTotalBytes);
            float percent = (float) values[0] / (float) mTotalBytes;
            float progress = (float) Math.floor(percent * 100);
            LUtils.d(TAG, "download package:progress  ==" + (int) progress);
            mBuilder.setProgress(100, (int) progress, false);
            mBuilder.setContentText("下载进度：" + (int) progress + "%");
            //  mBuilder.setContentIntent(resultPendingIntent);
            mManager.notify(NO_3, mBuilder.build());


        }
    }


    private void silentInstallApk(String apkPath) {
        Log.i(TAG, "silentInstallApk: " + apkPath);
        File file = new File(apkPath);
        if (!file.exists()) {
            Log.e(TAG, "silentInstallApk File doesn't exist:" + apkPath);
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MainActivity.this, "com.estone.bank.estone_appsmartlock.provider", file);
            mIntentNotifi.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        mIntentNotifi.setDataAndType(uri, "application/vnd.android.package-archive");
        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, mIntentNotifi, 0);
        mBuild = mBuilder.setContentIntent(pi).build();
        mManager.notify(NO_3, mBuild);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LUtils.d(TAG, " onRestart  ");
        initPagerDATE(mMUser_pic_head);
        main_rg.getChildAt(0).performClick();//模拟点击第一个RB
        loginToXM(MyApplications.getRegistXMId(), MyApplications.getRegistXMPass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    //双击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LUtils.d(TAG, "onKeyDown: event -- " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //finish();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                break;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void initPagerDATE(String user_pic_head) {

        mMFragmentList = new ArrayList<>();
        mMFragmentList.add(PropertiesFragment.newInstance());
        mMFragmentList.add(AboutMineFragment.newInstance(user_pic_head));
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setList(mMFragmentList);
        LUtils.d(TAG, "mMFragmentList.size==" + mMFragmentList.size());
        //系统默认选中第一个,但是系统选中第一个不执行onNavigationItemSelected(MenuItem)方法,
        // 如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mViewPager.addOnPageChangeListener(this);
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewpager);
        main_rg = findViewById(R.id.main_rg);
        Mmain_rbhome = findViewById(R.id.main_rbhome);
        Mmain_rbmine = findViewById(R.id.main_rbmine);
        main_rg.setOnCheckedChangeListener(this);
        main_rg.getChildAt(0).performClick();//模拟点击第一个RB
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LUtils.d(TAG, "  onPageScrollStateChanged (position)==" + position);
        //ViewPager和BottomNaviationView联动,当ViewPager的某个页面被选中了,同时设置BottomNaviationView对应的tab按钮被选中
        switch (position) {
            case 0:
                main_rg.check(R.id.main_rbhome);
                break;
            case 1:
                main_rg.check(R.id.main_rbmine);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }


    /**
     * 登录雄迈
     *
     * @return 成功与否
     */

    private boolean loginToXM(String unionid, String nickname) {
        //        if (FunSupport.getInstance().login("killsmm", "zxl870319")) {
        if (FunSupport.getInstance().login(unionid, nickname)) {
            Log.i(TAG, "loginToXM: login succeed!");
            return true;
        } else {
            Log.i(TAG, "loginToXM: login fault!");
            return false;
        }
    }


    // main_rbhome, main_rbmine;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_rbhome:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.main_rbmine:
                mViewPager.setCurrentItem(1);
                break;
        }
    }


}
