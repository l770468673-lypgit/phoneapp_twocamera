package com.estone.bank.estone_appsmartlock.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.costum.CircleImageView;
import com.estone.bank.estone_appsmartlock.fragments.AboutMineFragment;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_HeadPic;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.photoview.ClipImageActivity;
import com.estone.bank.estone_appsmartlock.utils.Base64BitmapUtils;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersionalDateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PersionalDateActivity";
    private ImageView imageback;
    private RelativeLayout myhead_pic;
    private RelativeLayout rey01;
    private TextView phonenumber;

    //请求相机
    private static final int REQUEST_CAPTURE = 11100;
    //请求相册
    private static final int REQUEST_PICK = 11101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 11102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 11103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 11104;
    //头像1
    private CircleImageView head_image1;

    //头像2
    //    private ImageView headImage2;
    //调用照相机返回图片临时文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persionaldate);


        initview(savedInstanceState);
        repeat();
        String mUser_pic_head = getIntent().getStringExtra("mUser_pic_head");

        //  String string = ShareUtil.getString(Contants.USER_PIC_HEAD);
        Log.i(TAG, "mUser_pic_head ----" + "" + mUser_pic_head);
        if (mUser_pic_head != null) {
            Glide.with(PersionalDateActivity.this).load(mUser_pic_head.toString())
                    .placeholder(R.mipmap.housedetal_uesrdefaultpic)
                    .dontAnimate().skipMemoryCache(true).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    into(head_image1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void repeat() {

        String str = "****";
        if (StringUtils.isNotBlank(str)) {
            StringBuilder sb = new StringBuilder("15689123357");
            sb.replace(3, 7, str);
//            System.err.println("========" + sb.toString());
            phonenumber.setText(sb.toString());
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
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }

    private void initview(Bundle savedInstanceState) {
        imageback = findViewById(R.id.imageback);
        phonenumber = findViewById(R.id.phonenumber);
        rey01 = findViewById(R.id.persionalrey01rey01);
        myhead_pic = findViewById(R.id.myhead_pic);
        head_image1 = findViewById(R.id.head_image1);
        myhead_pic.setOnClickListener(this);
        imageback.setOnClickListener(this);
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myhead_pic:
                type = 1;
                uploadHeadImage();
                break;
            case R.id.imageback:
            case R.id.persionalrey01rey01:
                finish();
                break;

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
                    if (ContextCompat.checkSelfPermission(PersionalDateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(PersionalDateActivity.this, new String[]{
                                        Manifest.permission.CAMERA},
                                WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    } else {
                        //跳转到调用系统相机
                        gotoCarema();
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
                    if (ContextCompat.checkSelfPermission(PersionalDateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请READ_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(PersionalDateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                READ_EXTERNAL_STORAGE_REQUEST_CODE);
                    } else {
                        //跳转到调用系统图库
                        gotoPhoto();
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

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    if (type == 1) {
                        head_image1.setImageBitmap(bitMap);
                        LUtils.d(TAG, "----------//剪切图片返回------------");
                        UptoBackground(bitMap);

                        Message message = AboutMineFragment.mHandler.obtainMessage(AboutMineFragment.MAINHANDLER_BITMAP_MSG);
                        Bundle bundle = new Bundle();
                        String s = Base64BitmapUtils.bitmapToBase64(bitMap);
                        bundle.putString("bitmap", s);
                        message.setData(bundle);
                        AboutMineFragment.mHandler.sendMessage(message);

                    }
//                    else {
                        //   headImage2.setImageBitmap(bitMap);
//                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void UptoBackground(Bitmap bitMap) {
//        String s = null;
        //        byte[] array = ByteBuffer.allocate(bitMap.getByteCount()).array();
        //        String imageBase64_1 = Base64.getEncoder().encodeToString(array);
        //        Base64BitmapUtils.savePNG_After(bitMap, Environment.getExternalStorageDirectory() + "/pic_pic.jpg");
        //        String  s= Base64BitmapUtils.getImgStr(Environment.getExternalStorageDirectory() + "/pic_pic.jpg");
        String sbase64 = Base64BitmapUtils.bitmapToBase64(bitMap);
        //        Bitmap bitmap = Base64BitmapUtils.base64ToBitmap(sbase64);

//        LUtils.showLong(TAG, "  sbase64 =" + sbase64.toString());

        Call<Bean_HeadPic> responsHeadCall = HttpManager.getInstance().getHttpClient().changeUserHeadPic
                (Contants.ACTIONID94, MyApplications.getRegistXMId(), sbase64);
        responsHeadCall.enqueue(new Callback<Bean_HeadPic>() {
            @Override
            public void onResponse(Call<Bean_HeadPic> call, Response<Bean_HeadPic> response) {
                if (response.body() != null) {
//                    String status = response.body().getStatus();
                    int errcode = response.body().getErrorCode();
                    if (errcode == 0) {
                        ToastUtils.showToast(PersionalDateActivity.this, "图片更换成功");
                        LUtils.d(TAG, "response.body().getStatus()" + response.body().getExtra().getImageUrl());
                        //                      ShareUtil.putString(Contants.USER_PIC_HEAD,response.body().getExtra().getImageUrl());
                    } else {
                        ToastUtils.showToast(PersionalDateActivity.this, "图片更换失败");
                        LUtils.d(TAG, "response.body().getStatus()" + response.body().toString());
                        LUtils.d(TAG, "rresponse.body().getErrorCode()" + response.body().getErrorCode());
                    }
                }
            }

            @Override
            public void onFailure(Call<Bean_HeadPic> call, Throwable t) {
                LUtils.showLong(TAG, "onFailure=");
            }
        });

    }


    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}
