package com.estone.bank.estone_appsmartlock.managers;

import android.content.Context;

import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;


public class DbManager {
    private static final String TAG = "DbManager";
    private static DbManager sDbManager;
    private DbUtils mDb;

    public static DbManager getInstance(){

        return sDbManager;
    }

    public static void init(Context context) {
        sDbManager = new DbManager(context);

    }


    DbManager(Context context) {

        mDb = DbUtils.
                create(context, "facedoor.db", 2, new DbUtils.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbUtils dbUtils, int i, int i1) {
                        if (dbUtils != null) {
                            try {
                                dbUtils.dropTable(Bean_AllDevices.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }

                            try {
                                dbUtils.createTableIfNotExist(Bean_AllDevices.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }); // null for
    }

    public DbUtils getDb() {
        return mDb;
    }

}
