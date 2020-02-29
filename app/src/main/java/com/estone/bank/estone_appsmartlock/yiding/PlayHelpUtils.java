package com.estone.bank.estone_appsmartlock.yiding;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lib.funsdk.support.FunPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.firmwarelib.nativelibs.command.DevHelper;
import cn.firmwarelib.nativelibs.rx.RxSchedulerHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 播放视频帮助工具类
 * Created by Bingo on 2018/1/4.
 */

public class PlayHelpUtils {

    private static final String TAG = "PlayHelpUtils";

    private static MediaPlayer mediaPlayer;

    private static void Log(String msg) {
        Log.i(TAG, msg);
    }


    /**
     * 保存图片到本地
     *
     * @param filePath       文件的总体路径
     * @param firstImageName 文件的名字
     * @param videoBitmap    文件源
     * @return
     */
    private static Observable<Boolean> saveFirstImageToSdcard(final String filePath, final String firstImageName, final Bitmap videoBitmap) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(saveFileToSD(filePath, firstImageName, ConversionTool.Bitmap2Bytes(videoBitmap)));
                e.onComplete();
            }
        }).compose(RxSchedulerHelper.INSTANCE.<Boolean>ioToMain());
    }

    /**
     * 录制视频事件
     *
     * @param isChecked
     * @param Sn
     */
    public static String videoRecord(String localPath, boolean isChecked, String Sn, Bitmap videoBitmap) {
//        String fileName = getCurrentDate() + ".avi";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = sdf.format(new Date());
        fileName = fileName + System.currentTimeMillis();
        String firstImageName = getCurrentDate() + ".avi";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (isChecked) {
                // 开始录制
                try {
                    if (Sn != null) {
//                        String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()
//                                + localPath + Sn + "/video/";
                        String videofile = FunPath.PATH_VIDEO + File.separator + Sn+File.separator;
                        File file = new File(videofile);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        /**
                         * 保存视频的第一帧
                         */
                        saveFirstImageToSdcard(videofile, firstImageName, videoBitmap).subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) {
                            }
                        }).isDisposed();

                        // Native.StartAviRecord(filePath + fileName, 15);
                        DevHelper.Companion.getDevHelper().devStartAviRecord(videofile + firstImageName);
                        return videofile + firstImageName;
                    }else {
                        LUtils.d(TAG, "Sn == null==");
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 停止录制
                // Log("停止录制-->>");
                // Native.StopAviRecord();
                DevHelper.Companion.getDevHelper().devStopAviRecord();
            }
        } else {
            Log("error-->>当前的sdcard状态不可用...");
        }
        return null;
    }


    //------------------------------------------------------------------------------------------//

    /**
     * 截取图片保存到本地指定文件夹
     *
     * @param localPath
     * @param equipmentName
     * @param bitmap
     * @return
     */
    private static boolean screenshotsImage(String localPath, String equipmentName, Bitmap bitmap) {
        byte[] imageByte = ConversionTool.Bitmap2Bytes(bitmap);
        if (imageByte != null) {
            String url = "https://s3.cn-north-1.amazonaws.com.cn/enxuncnnorth1/"
                    + equipmentName + "/" + screenshotsImageDate() + ".jpg";
            try {
                Log("-->>screenshotsImage" + equipmentName);
                return saveFileToSD(localPath, equipmentName, url, imageByte);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 往SD卡写入文件的方法
     *
     * @param localPath
     * @param equipmentName
     * @param url
     * @param bytes
     * @return
     * @throws Exception
     */
    private static Boolean saveFileToSD(String localPath, String equipmentName, String url, byte[] bytes) throws Exception {
        Log("" + "equipmentName:" + equipmentName);
        Log("" + "url:" + url);
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + localPath + equipmentName + "/";
            // 以设备名称创建目录
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (url != null && url.contains("/")) {
                String[] dirList = url.split("/");
                // 年目录名称 // 月目录名称 // 日目录名称
                File dateFileName = new File(filePath + dirList[dirList.length - 2] + "/");
                Log("" + "dateFileName:" + filePath + dirList[dirList.length - 2] + "/");
                if (!dateFileName.exists()) {
                    dateFileName.mkdirs();
                }
                // 文件的名称
                String fileName = dirList[dirList.length - 1];
                Log("" + "fileName:" + fileName);

                File file = new File(dateFileName + "/", fileName);
                if (!file.exists()) {
                    //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                    FileOutputStream output = new FileOutputStream(file);
                    output.write(bytes);
                    //将bytes写入到输出流中
                    output.close();
                    //关闭输出流
                    Log("" + "图片已成功保存到" + file.getAbsolutePath());
                    return true;
                } else {
                    Log("" + "文件已经存在");
                    return false;
                }
            }
        } else {
            Log("" + "SD卡不存在或者不可读写");
            return false;
        }
        return false;
    }

    /**
     * 保存图片到本地
     *
     * @param localPath 文件的总体路径
     * @param fileName  文件的名字
     * @param bytes     文件源
     * @return
     * @throws Exception
     */
    private static Boolean saveFileToSD(String localPath, String fileName, byte[] bytes) throws Exception {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(localPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(localPath, fileName);
            if (!file.exists()) {
                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(file);
                output.write(bytes);
                //将bytes写入到输出流中
                output.close();
                //关闭输出流
                // Log("" + "图片已成功保存到" + file.getAbsolutePath());
                return true;
            } else {
                // Log("" + "文件已经存在");
                return false;
            }
        } else {
            // Log("" + "SD卡不存在或者不可读写");
            return false;
        }
    }


    /**
     * 截取图片的时间
     *
     * @return 20171111/113020
     */
    private static String screenshotsImageDate() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd/HHmmss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            return formatter.format(curDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 录制视频
     * 获取当期时间
     *
     * @return 20171111-113020
     */
    private static String getCurrentDate() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            return formatter.format(curDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
