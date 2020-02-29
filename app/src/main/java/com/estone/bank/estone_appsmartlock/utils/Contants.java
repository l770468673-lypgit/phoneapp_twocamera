package com.estone.bank.estone_appsmartlock.utils;

import android.os.Environment;

import java.io.File;

public class Contants {
    public static final int MSG_UPDATE_PROPERTIES_LIST = 0x1001;

    public static String ACTIONID01 = "01";
    public static String ACTIONID05 = "05";
    public static String ACTIONID13 = "13";
    public static String ACTIONID12 = "12";
    public static String ACTIONID11 = "11";
    public static String ACMINNAME = "tiger";
    public static String ACTIONID14 = "14";
    public static String ACTIONID16 = "16";
    public static String ACTIONID97 = "97";
    public static String ACTIONID99 = "99";
    public static String ACTIONID94 = "94";
    public static String ACTIONID95 = "95";
    public static String ACTIONID93 = "93";
    public static String ACTIONID18 = "18"; // 查询绑定关系
    public static String ACTIONID24 = "24"; // 查个人信息
    public static String ACTIONID21 = "21";   // wifi是否连接
    public static String ACTIONID91 = "91";   //更换房间Name
    public static String ACTIONID30 = "30";   //提前退房
    public static String ACTIONID31 = "31";   //延期
    public static String ACTIONID102= "102";   //查询当前apk版本信息
    public static String IS_FIRST_LOGIN = "is_first_login";
    public static  String DOWNLOADED_APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;

    public static String WXUSERMNICKNAME = "wxusermnickname";

    public static String VIDEO_URL_INSDCARD = Environment.getExternalStorageDirectory().getPath() + "/EstoneVideo";

    //    public static String XMACMINID = "oAuO41TxcElPRF_neoNpPGPF1rFd";
    //    public static String XMPASSWORD = "43841510";

    public static long MS_PER_MONTH = 1000L * 3600L * 24L * 30L;

}
