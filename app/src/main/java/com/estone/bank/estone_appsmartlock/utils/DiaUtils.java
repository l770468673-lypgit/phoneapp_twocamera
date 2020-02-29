package com.estone.bank.estone_appsmartlock.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.estone.bank.estone_appsmartlock.R;
import com.example.common.DialogWaitting;

public class DiaUtils {
    private  static DialogWaitting mWaitDialog = null;
    private static AlertDialog sMAlertDialog;

    public static void showWaitDialog(Context context) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(context);
        }
        mWaitDialog.show();


    }

    public  static void showWaitDialog(Context context,int resid) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(context);
        }
        mWaitDialog.show(resid);
    }

    public static void showWaitDialog(Context context,String text) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(context);
        }
        mWaitDialog.show(text);
    }

    public static void hideWaitDialog() {
        if (null != mWaitDialog) {
            mWaitDialog.dismiss();
        }
    }


    public  static void   HelpDialog(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View imgEntryView = inflater.inflate(R.layout.dialog_helpmsg, null);
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels/2;     // 屏幕宽度（像素）
//        int height = metric.heightPixels/2;   // 屏幕高度（像素）
//        LUtils.d(TAG, "widthPixels is " + width + "heightPixels is " + height);


        sMAlertDialog = new AlertDialog.Builder(context).create();
        ImageView img = (ImageView) imgEntryView.findViewById(R.id.imageview_showhelp);

        sMAlertDialog.setView(imgEntryView); // 自定义dialog
        sMAlertDialog.show();


        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                sMAlertDialog.cancel();
            }
        });
    }

}
