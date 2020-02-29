package com.estone.bank.estone_appsmartlock.yiding;

import android.os.Environment;

import java.io.File;

import cn.firmwarelib.nativelibs.NetWork.UrlManganger;
import cn.firmwarelib.nativelibs.utils.RxSchedulerHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Bingo on 2017/11/7.
 */

public class FileSdcardHelper {

    private final static String TAG = "SDFileHelper";
    // 本地图片视频问价存放路径,以后打包不同客户版的的时候,更改这个路径即可
    public final static String app_sd = "/" + UrlManganger.appType + "/";



    /**
     * 删除单独一个文件路径
     *
     * @param filePath
     * @return
     */
    public static Observable<Boolean> deleteImageFile(final String filePath) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteFile(filePath));
                e.onComplete();
            }
        }).compose(RxSchedulerHelper.<Boolean>ioToMain());
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的路径
     * @return
     */
    private static boolean deleteFile(String filePath) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(filePath);
                if (file.isFile() && file.exists()) {
                    return file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }
}
