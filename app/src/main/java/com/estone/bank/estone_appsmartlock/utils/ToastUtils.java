package com.estone.bank.estone_appsmartlock.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static Toast mToast = null;

    public static void showToast(Context context, String text) {
        if (null != text) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void showToast(Context context,int resid) {
        if (resid > 0) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

}
