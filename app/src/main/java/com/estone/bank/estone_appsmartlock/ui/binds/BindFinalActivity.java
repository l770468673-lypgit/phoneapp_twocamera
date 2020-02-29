package com.estone.bank.estone_appsmartlock.ui.binds;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Base64BitmapUtils;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.google.gson.Gson;
import com.jeanboy.cropview.cropper.CropperHandler;
import com.jeanboy.cropview.cropper.CropperManager;
import com.jeanboy.cropview.cropper.CropperParams;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;

import java.io.IOException;

import cn.firmwarelib.nativelibs.retrofit_okhttp.RequestManager;
import cn.firmwarelib.nativelibs.retrofit_okhttp.interfaces.HttpResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BindFinalActivity extends BaseActivity implements View.OnClickListener, CropperHandler {
    final static public String EXTRA_CAM_SN = "CAM_SN";
    final static public String EXTRA_CAM_TAG = "CAM_TAG";
    final static public String TAG = "BindFinalActivity";
    final static public String EXTRA_GATEWAY_SN = "GATEWAY_SN";
    final static public String EXTRA_DEVID_SN = "DEVID_SN";


    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 11140;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 11150;
    //调用照相机返回图片临时文件


    final static private String[] stepsLables = {
            "",
            "",
            ""
    };
    private String camSN;
    private String camTAG;
    private String DevIdSn;
    private String gateWaySN;


    private Button button_uppics;

    private EditText mNameEditText;
    private ImageView imageviewim_roombg;
    private EditText meditText_houseid;
    private Button mButton_confirm;
    private ImageView final_activity_back;
    // 1: qq, 2: weixin

    private Bitmap mBitMap;
    private FunDevice mFunDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplications.addActivityToBeDestory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_final);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        StepsView stepsView = ((StepsView) findViewById(R.id.stepsView))
                .setBarColorIndicator(getResources().getColor(R.color.colorPrimaryLight))
                .setLabelColorIndicator(getResources().getColor(R.color.a595959))
                .setProgressColorIndicator(getResources().getColor(R.color.colorAccentLight))
                .setLabels(stepsLables);
        stepsView.setCompletedPosition(2);

        Bundle extras = getIntent().getExtras();
        mFunDevice = (FunDevice) extras.getSerializable("bindcammSelectDevice");
        camSN = extras.getString(BindFinalActivity.EXTRA_CAM_SN);
        camTAG = extras.getString(BindFinalActivity.EXTRA_CAM_TAG);
        gateWaySN = extras.getString(EXTRA_GATEWAY_SN);
        DevIdSn = extras.getString(EXTRA_DEVID_SN);
        final_activity_back = findViewById(R.id.final_activity_back);
        final_activity_back.setOnClickListener(this);
        if (camSN != null) {
            String substring = camSN.substring(0, camSN.length() - 5);
            LUtils.d(TAG,"substring====="+substring);
            ((TextView) findViewById(R.id.textView_cam_sn)).setText(substring);
        }
        if (gateWaySN != null) {
            ((TextView) findViewById(R.id.textView_gateway_sn)).setText(gateWaySN);
        }
        if (DevIdSn != null) {
            ((TextView) findViewById(R.id.textView_lock_sn)).setText(DevIdSn);
        }
        CropperManager.getInstance().build(this);
        mNameEditText = findViewById(R.id.editText_name);
        imageviewim_roombg = findViewById(R.id.imageviewim_roombg);
        button_uppics = findViewById(R.id.button_uppics);
        meditText_houseid = findViewById(R.id.editText_houseid);
        mButton_confirm = findViewById(R.id.button_confirm);
        mButton_confirm.setOnClickListener(this);
        button_uppics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.final_activity_back:
                finish();
                break;
            case R.id.button_confirm:
                String houseid = meditText_houseid.getText().toString();
                String sName = mNameEditText.getText().toString();
                if (houseid.length() > 1 && sName.length() > 1) {
                    commit(houseid, sName);
                } else {
                    Toast.makeText(BindFinalActivity.this, "请保持上方内容不为空", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.button_uppics:

                uploadHeadImage();
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    //houseid,sName,sAddress
    private void commit(String houseid, String sName) {
        String mSbase64;
        if (mBitMap != null) {
            mSbase64 = Base64BitmapUtils.bitmapToBase64(mBitMap);
        } else {
            mSbase64 = "";
        }
        Call<ResponsHead> responsHeadCall = HttpManager.getInstance().getHttpClient().
                bindDevices(Contants.ACTIONID11,
                        DevIdSn,
                        camSN.substring(0, camSN.length() - 5),
                        gateWaySN,
                        MyApplications.getRegistXMId(),
                        houseid,
                        sName,
                        null,
                        mSbase64);
        responsHeadCall.enqueue(new Callback<ResponsHead>() {
            @Override
            public void onResponse(Call<ResponsHead> call, Response<ResponsHead> response) {
                if (response.body() != null) {
                    ResponsHead body = response.body();
                    if (body != null) {
                        if (body.getErrorCode() == 0) {
                            if (!"CAMERA_YD".equals(camTAG)) {
                                commitBindDevices();
                            }

                            Toast.makeText(
                                    BindFinalActivity.this,
                                    "绑定成功",
                                    Toast.LENGTH_SHORT
                            ).show();
                            toAddEquipment( camSN.substring(0, camSN.length() - 5));
                            MyApplications.clearActivitiesDestoryList();
                            finish();
                        } else if (body.getErrorCode() == 1101) {
                            if (!"CAMERA_YD".equals(camTAG)) {
                                FunSupport.getInstance().requestDeviceRemove(mFunDevice);
                            }
                            LUtils.d(TAG, "已经绑定过n==" + mFunDevice);
                            Toast.makeText(
                                    BindFinalActivity.this,
                                    "绑定失败，已经绑定过",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else if (body.getErrorCode() == 1102) {
                            if (!"CAMERA_YD".equals(camTAG)) {
                                FunSupport.getInstance().requestDeviceRemove(mFunDevice);
                            }
                            Toast.makeText(
                                    BindFinalActivity.this,
                                    "数据库访问失败 ",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsHead> call, Throwable t) {

            }
        });

    }
    /**
     * 添加设备到个人中心
     * @param substring
     */
    private void toAddEquipment(String substring) {
        RequestManager.getInstance().addEquipment(substring, "8", new HttpResponseListener() {
            @Override
            public void onResponseSuccess(int statusCode, Object object) {
                if (statusCode == 200) {
//                    showToast("添加设备 成功");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(BindFinalActivity.this.getBaseContext());
//                    builder.setTitle("温馨提醒");
//                    builder.setMessage("你已经成功添加设备");
//                    builder.setPositiveButton("继续添加", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
////                            et_sn.setText("");
//                        }
//                    });
//                    builder.setNegativeButton("返回上一页", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//                    builder.show();
                }

                //  LogUtil.INSTANCE.d();
                LUtils.d(TAG, " 添加设备 statusCode:" + statusCode + "    Json:" + new Gson().toJson(object));
            }

            @Override
            public void onResponseError(Throwable e) {
                //                showToast("添加设备 失败 ：" + e.getMessage());
                LUtils.d(TAG, " 解绑设备 异常:" + e.getMessage());
            }
        });
    }

    private void commitBindDevices() {
        FunSupport.getInstance().requestDeviceAdd(mFunDevice);
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
                    if (ContextCompat.checkSelfPermission(BindFinalActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(BindFinalActivity.this, new String[]{
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
                    if (ContextCompat.checkSelfPermission(BindFinalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请READ_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(BindFinalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropperManager.getInstance().handlerResult(requestCode, resultCode, data);
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
            mBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            imageviewim_roombg.setImageBitmap(mBitMap);
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
