package com.estone.bank.estone_appsmartlock.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_login;
import com.estone.bank.estone_appsmartlock.https.beans.bean_WX_XMlogin;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Base64BitmapUtils;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunLoginListener;
import com.mob.wrappers.ShareSDKWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.firmwarelib.nativelibs.retrofit_okhttp.RequestManager;
import cn.firmwarelib.nativelibs.retrofit_okhttp.interfaces.HttpResponseListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends BaseActivity implements View.OnClickListener,
        PlatformActionListener,
        OnFunLoginListener {

    private static final String TAG = "SplashActivity";
    private int MSG_ID_WX_LOGIN_BACK = 111222;

    public static splashHandler mHandler;
    private Button msplash_wxlogin;
    private Bitmap mBitmapCover;
    private Button msplash_numlogin;
    private RelativeLayout mRecyanimation;
    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;
    private String mMUnionid1;
    private String mWxusermHeadimgurl = null;

    AlertDialog mPermissionDialog;
    private int PERMISSION_CODE = 1000;
    List<String> mPermissionList = new ArrayList<>();
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            //            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    private Platform mWechat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new splashHandler();

        mWechat = ShareSDK.getPlatform(Wechat.NAME);
        mWechat.setPlatformActionListener(this);

        initPermission();
        //        ShareUtil.putString(Contants.IS_FIRST_LOGIN, "LOGIN");
        //        MyApplications.setRegistXMId("oAuO41T9E4wiiHm2GNHMINqzX_5Q");
        //        MyApplications.setRegistXMPass("62065847");
        //        loginToXM("oAuO41T9E4wiiHm2GNHMINqzX_5Q", "62065847");
        //        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        //        startActivity(intent);
    }

    private void initPermission() {
        mPermissionList.clear();
        //逐个判断是否还有未通过的权限
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限到mPermissionList中
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
        } else {
            //权限已经都通过了，可以将程序继续打开了
            initFid();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissionDismiss = false;//有权限没有通过
        if (PERMISSION_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    LUtils.d(TAG, "grantResults[i]==" + i);
                    hasPermissionDismiss = true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss) {//如果有没有被允许的权限
            showPermissionDialog();

        } else {
            //权限已经都通过了，可以将程序继续打开了
            initFid();
        }


    }


    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + "com.estone.bank.estone_appsmartlock");
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            SplashActivity.this.finish();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        FunSupport.getInstance().removeOnFunLoginListener(this);
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailed(Integer errCode) {

    }

    @Override
    public void onLogout() {

    }


    class splashHandler extends Handler {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111223:
                    //  注册或登录
                    if (mBitmapCover != null) {
                        String s = Base64BitmapUtils.bitmapToBase64(mBitmapCover);
                        Call<bean_WX_XMlogin> beanunionid = HttpManager.getInstance().getHttpClient().
                                registToXM(Contants.ACTIONID97, mMUnionid1, s);
                        LUtils.d(TAG, "mUnionid1  ==" + mMUnionid1);
                        regist(beanunionid, mMUnionid1);
                    } else {
                        LUtils.d(TAG, "头像为空 ");
                        Call<bean_WX_XMlogin> beanunionid = HttpManager.getInstance().getHttpClient().
                                registToXM(Contants.ACTIONID97, mMUnionid1, null);
                        regist(beanunionid, mMUnionid1);
                    }
                    break;
                case 111222:
                    //  微信第三方登录成功
                    Bundle data = msg.getData();
                    mMUnionid1 = data.getString("mUnionid1");
                    mWxusermHeadimgurl = data.getString("wxusermHeadimgurl");

                    if (mWxusermHeadimgurl != null) {
                        Glide.with(SplashActivity.this).load(mWxusermHeadimgurl).asBitmap().dontAnimate().skipMemoryCache(true).
                                diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mBitmapCover = resource;
                                mHandler.sendEmptyMessage(111223);
                            }
                        });
                    }
                    break;
            }
        }

    }


    private void initFid() {
        msplash_numlogin = findViewById(R.id.splash_numlogin);
        mRecyanimation = findViewById(R.id.recyanimation);
        iamge_loaddate_anim = findViewById(R.id.spaiamge_loaddate_anim);
        msplash_wxlogin = findViewById(R.id.splash_wxlogin);
        msplash_wxlogin.setOnClickListener(this);
        msplash_numlogin.setOnClickListener(this);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.2f, 1f, 1.2f, 1f);//默认从（0,0）
        scaleAnimation.setDuration(1800);
        mRecyanimation.startAnimation(scaleAnimation);
        FunSupport.getInstance().registerOnFunLoginListener(this);
        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);

        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);


//        String islogin = ShareUtil.getString(Contants.IS_FIRST_LOGIN);
//        if (islogin != null) {
//            mWechat.showUser(null);
//            mAnimaition.start();
//            iamge_loaddate_anim.setVisibility(View.VISIBLE);
//            msplash_wxlogin.setVisibility(View.GONE);
//
//            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);//第一个参数开始的透明度，第二个参数结束的透明度
//            alphaAnimation.setDuration(10000);//多长时间完成这个动作
//            mRecyanimation.startAnimation(alphaAnimation);
//
////            loginToXM(MyApplications.getRegistXMId(), MyApplications.getRegistXMPass());
//        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_wxlogin:

                mWechat.showUser(null);
                mAnimaition.start();
                iamge_loaddate_anim.setVisibility(View.VISIBLE);
                //userName:oAuO41T9E4wiiHm2GNHMINqzX_5Q
                //    psw:62065847
                //                loginToXM("oAuO41T9E4wiiHm2GNHMINqzX_5Q", "62065847");
                //                loginESTServers(Contants.ACTIONID95, "oAuO41T9E4wiiHm2GNHMINqzX_5Q");
                //                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                //                startActivity(intent);
                break;
            case R.id.splash_numlogin:
//                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                                startActivity(intent);

                RequestManager.getInstance().login("15689123357","15689123357", "devid", new HttpResponseListener() {
                    @Override
                    public void onResponseSuccess(int statusCode, Object o) {
                        LUtils.d(TAG, "statusCode==" + statusCode);
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onResponseError(Throwable throwable) {
                        LUtils.d(TAG, "throwable==" + throwable.getMessage());
                    }
                });
                break;

        }
    }

    // 微信登录返回
    @Override
    public void onComplete(Platform platform, int i,
                           final HashMap<String, Object> hashMap) {
        Iterator ite = hashMap.entrySet().iterator();
        //        mDialog2.dismiss();

        while (ite.hasNext()) {
            Map.Entry entry = (Map.Entry) ite.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            LUtils.d(TAG, key + "：-------------- " + value);
            LUtils.d(TAG, hashMap.toString());
        }
        //        iamge_loaddate_anim.setVisibility(View.GONE);
        //        mAnimaition.stop();
        ShareUtil.putString(Contants.IS_FIRST_LOGIN, "LOGIN");
        //        LUtils.d(TAG, "WXlogin_id==" + (String) hashMap.get("unionid"));
        //        LUtils.d(TAG, "WXlogin_nickname==" + (String) hashMap.get("nickname"));
        //        LUtils.d(TAG, "WXlogin_headimgurl===" + (String) hashMap.get("headimgurl"));
        ShareUtil.putString(Contants.WXUSERMNICKNAME, (String) hashMap.get("nickname"));
        Bundle bundle = new Bundle();
        bundle.putString("mUnionid1", (String) hashMap.get("unionid"));
        bundle.putString("wxusermHeadimgurl", (String) hashMap.get("headimgurl"));
        //        LUtils.d(TAG, "：-----wxusermHeadimgurl--------- " +  hashMap.get("headimgurl"));
        Message msgbundle = mHandler
                .obtainMessage(MSG_ID_WX_LOGIN_BACK);
        msgbundle.setData(bundle);
        mHandler.sendMessage(msgbundle);
    }


    private void regist(Call<bean_WX_XMlogin> unionid, final String uuid) {
        unionid.enqueue(new Callback<bean_WX_XMlogin>() {
            @Override
            public void onResponse(Call<bean_WX_XMlogin> call, Response<bean_WX_XMlogin> response) {
                if (response.body() != null) {
                    bean_WX_XMlogin body = response.body();
                    if (body.getErrorCode() == 0) {
                        bean_WX_XMlogin.ExtraBean extra = body.getExtra();
                        //                        MyApplications.setRegistXMId("oAuO41T9E4wiiHm2GNHMINqzX_5Q");
                        //                        MyApplications.setRegistXMPass("62065847");
                        MyApplications.setRegistXMId(extra.getAdminId());
                        MyApplications.setRegistXMPass(extra.getPassword());
                        loginToXM(extra.getAdminId(), extra.getPassword());
                        //  loginToXM("oAuO41T9E4wiiHm2GNHMINqzX_5Q", "62065847");
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        LUtils.d(TAG, "注册成功  mAdminId  ==" + extra.getAdminId());
                        LUtils.d(TAG, " 注册成功 mPassword ==" + extra.getPassword());

                        finish();
                    } else if (body.getErrorCode() == 9701) {
                        loginESTServerd(Contants.ACTIONID95, uuid);
                    } else if (body.getErrorCode() == 9702) {
                        if (uuid.contains("oAuO41TxcElPRF_neoNpPGPF1rFs"))
                            loginESTServerd(Contants.ACTIONID95, "oAuO41TxcElPRF_neoNpPGPF1rFd");
                    }
                } else {
                    LUtils.d(TAG, "response.body()=null         ==");
                }
            }

            @Override
            public void onFailure(Call<bean_WX_XMlogin> call, Throwable t) {
                //                Toast.makeText(SplashActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(Platform platform, final int i, final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // mDialog2.dismiss();
                mAnimaition.stop();
                iamge_loaddate_anim.setVisibility(View.GONE);
                LUtils.d(TAG, "微信登录失败!" + i + throwable);
                Toast.makeText(getBaseContext(), "微信登录失败!" + i + throwable, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancel(Platform platform, final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                mDialog2.dismiss();
                mAnimaition.stop();
                iamge_loaddate_anim.setVisibility(View.GONE);
                LUtils.d(TAG, "微信取消登录!" + i);
                Toast.makeText(getBaseContext(), "微信取消登录!" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 登录雄迈
     */

    private boolean loginToXM(String unionid, String nickname) {
        //        if(FunSupport.getInstance().login("killsmm","zxl870319")){
        if (FunSupport.getInstance().login(unionid, nickname)) {
            LUtils.i(TAG, "loginToXM: login succeed!");
            return true;
        } else {
            LUtils.i(TAG, "loginToXM: login fault!----");
            return false;
        }
    }

    private void loginESTServerd(String ACTIONID95, String XMACMINID) {

        Call<Bean_login> bean_loginCall = HttpManager.getInstance().getHttpClient().loginESTServer(ACTIONID95, XMACMINID);
        bean_loginCall.enqueue(new Callback<Bean_login>() {
            @Override
            public void onResponse(Call<Bean_login> call, Response<Bean_login> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
                        Bean_login.ExtraBean extra = response.body().getExtra();

                        //                        MyApplications.setRegistXMId("oAuO41T9E4wiiHm2GNHMINqzX_5Q");
                        //                        MyApplications.setRegistXMPass("62065847");
                        //                        MyApplications.setRegistXMId("oAuO41TxcElPRF_neoNpPGPF1rFd");
                        //                        MyApplications.setRegistXMPass("43841510");
                        MyApplications.setRegistXMId(extra.getAdminId());
                        MyApplications.setRegistXMPass(extra.getPassword());
                        loginToXM(extra.getAdminId(), extra.getPassword());
                        //                        loginToXM("oAuO41T9E4wiiHm2GNHMINqzX_5Q", "62065847");
                        LUtils.i(TAG, "getAdminId!----" + extra.getAdminId());
                        LUtils.i(TAG, "mImageUrl!----" + extra.getImageUrl());
                        LUtils.i(TAG, "getPassword!----" +  extra.getPassword());
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        //                        intent.putExtra("USER_PIC_HEAD", extra.getImageUrl());
                        startActivity(intent);
                        ShareUtil.putString("USER_PIC_HEAD", extra.getImageUrl());
                        finish();

                    } else {
                        LUtils.d(TAG, "response.body().getStatus()" + response.body().getStatus());
                        LUtils.d(TAG, "rresponse.body().getErrorCode()" + response.body().getErrorCode());
                        LUtils.d(TAG, "rresponse.body().getErrorCode()" + response.body().getExtra().getImageUrl()
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean_login> call, Throwable t) {

            }
        });
    }


}
