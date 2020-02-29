package com.estone.bank.estone_appsmartlock.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_HeadPic;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Base64BitmapUtils;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.jeanboy.cropview.cropper.CropperHandler;
import com.jeanboy.cropview.cropper.CropperManager;
import com.jeanboy.cropview.cropper.CropperParams;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingHome extends AppCompatActivity implements View.OnClickListener, CropperHandler {

    private TextView edit_lock, edit_gateway, edit_camera, edit_address, edit_houseid;
    private Bean_AllDevices.InfosBean mSendinfosBean;
    private Button settingbutton_uppics;
    private static String TAG = "SettingHome";
    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;
    private ImageView settingimageviewim_roombg;
    private ImageView setting_activity_back;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 21103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 21104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_home);
        mSendinfosBean = (Bean_AllDevices.InfosBean) getIntent().getExtras().getSerializable("sendinfosBean");

        initView();
        CropperManager.getInstance().build(this);

    }

    private void initView() {
        setting_activity_back = findViewById(R.id.setting_activity_back);
        settingbutton_uppics = findViewById(R.id.settingbutton_uppics);
        settingimageviewim_roombg = findViewById(R.id.settingimageviewim_roombg);
        iamge_loaddate_anim = findViewById(R.id.iamge_loaddate_anim);
        settingbutton_uppics.setOnClickListener(this);
        setting_activity_back.setOnClickListener(this);
        edit_lock = findViewById(R.id.edit_lock);

        edit_gateway = findViewById(R.id.edit_gateway);

        edit_camera = findViewById(R.id.edit_camera);

        edit_address = findViewById(R.id.edit_address);
        edit_address.setOnClickListener(this);
        edit_houseid = findViewById(R.id.edit_houseid);
        //创建拍照存储的临时文件

        Glide.with(SettingHome.this).load(mSendinfosBean.getImgUrl()).asBitmap().dontAnimate().skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                settingimageviewim_roombg.setImageBitmap(resource);
            }
        });

        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDevicesList();
    }

    public void loadDevicesList() {
        mAnimaition.start();//启动
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
        Call<Bean_AllDevices> tiger =
                HttpManager.getInstance().getHttpClient().queryListHome(Contants.ACTIONID13, MyApplications.getRegistXMId());
        tiger.enqueue(new Callback<Bean_AllDevices>() {
            @Override
            public void onResponse(Call<Bean_AllDevices> call, Response<Bean_AllDevices> response) {
                if (response.body() != null) {
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    Bean_AllDevices body = response.body();
                    List<Bean_AllDevices.InfosBean> infos = body.getInfos();
                    for (int i = 0; i < infos.size(); i++) {
                        Bean_AllDevices.InfosBean infosBean = infos.get(i);
                        if ((infosBean.getCameraId().equals(mSendinfosBean.getCameraId()))) {
                            edit_lock.setText(infosBean.getDevId());
                            edit_gateway.setText(infosBean.getGatewayId());
                            edit_camera.setText(infosBean.getCameraId());
                            edit_address.setText(infosBean.getRoomName());
                            edit_houseid.setText(infosBean.getRoomId());
                        }
                    }
                } else {
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Bean_AllDevices> call, Throwable t) {
                mAnimaition.stop();
                iamge_loaddate_anim.setVisibility(View.GONE);
                LUtils.d(TAG, "onFailure       madapter.setData(mInfos) ");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingbutton_uppics:
                uploadHeadImage();
                break;
            case R.id.setting_activity_back:
                finish();
                break;
            case R.id.edit_address:
                Intent intent = new Intent(SettingHome.this, SettingRoomId.class);
                intent.putExtra("housename", edit_address.getText().toString().trim());
                intent.putExtra("edit_houseid", edit_houseid.getText().toString().trim());
                intent.putExtra("edit_lock", edit_lock.getText().toString().trim());
                startActivity(intent);

                LUtils.d(TAG, "mEdit_houseid===" + edit_houseid.getText().toString().trim());
                LUtils.d(TAG, "mEdit_lock===" + edit_lock.getText().toString().trim());
                LUtils.d(TAG, "housename===" + edit_address.getText().toString().trim());

                break;
        }
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                CropperManager.getInstance().pickFromCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                CropperManager.getInstance().pickFromGallery();
            }
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(SettingHome.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(SettingHome.this, new String[]{
                                        Manifest.permission.CAMERA},
                                WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    } else {
                        //跳转到调用系统相机
                        CropperManager.getInstance().pickFromCamera();
                    }
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(SettingHome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请READ_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(SettingHome.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                READ_EXTERNAL_STORAGE_REQUEST_CODE);
                    } else {
                        //跳转到调用系统图库
                        CropperManager.getInstance().pickFromGallery();
                    }
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropperManager.getInstance().handlerResult(requestCode, resultCode, data);

    }

    private void changeHouseImg(Bitmap bitMap) {

        String sbase64 = Base64BitmapUtils.bitmapToBase64(bitMap);

        Call<Bean_HeadPic> bean_loginCall = HttpManager.getInstance().getHttpClient().changeRoompic
                (Contants.ACTIONID93, mSendinfosBean.getDevId(), mSendinfosBean.getRoomId(), sbase64);

        bean_loginCall.enqueue(new Callback<Bean_HeadPic>() {
            @Override
            public void onResponse(Call<Bean_HeadPic> call, Response<Bean_HeadPic> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
                        LUtils.d(TAG, "   response.body()  ==" + response.body().getErrorMsg());
                    } else {
                        LUtils.d(TAG, "   response.body() ==" + response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean_HeadPic> call, Throwable t) {

            }
        });

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public CropperParams getParams() {
        return new CropperParams(3, 1);
    }

    @Override
    public void onCropped(Uri uri) {
        LUtils.d("=====onCropped======", "======裁切成功=======" + uri);
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            settingimageviewim_roombg.setImageBitmap(bm);
            changeHouseImg(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCropCancel() {
        LUtils.d("=====onCropCancel====", "======裁切取消=====");
    }

    @Override
    public void onCropFailed(String msg) {
        LUtils.d("=====onCropFailed===", "=======裁切失败======" + msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CropperManager.getInstance().destroy();
    }

}
