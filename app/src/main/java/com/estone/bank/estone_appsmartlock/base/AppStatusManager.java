package com.estone.bank.estone_appsmartlock.base;

public class AppStatusManager {


    public static AppStatusManager appStatusManager;

    private AppStatusManager(){}

    //单例模式
    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }



}
