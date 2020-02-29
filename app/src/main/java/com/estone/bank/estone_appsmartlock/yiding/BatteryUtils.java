package com.estone.bank.estone_appsmartlock.yiding;

import android.widget.ImageView;

import com.estone.bank.estone_appsmartlock.R;


/**
 * 显示电量
 * Created by Bingo on 2017/11/28.
 */

public class BatteryUtils {

    public static void showBattery(ImageView view, String battery) {
        if (view != null) {
            if (battery != null) {
                try {
                    // 0~100
                    // 80%以上四格，50%以上三格，30%以上2格，30%以下一格
                    int b = Integer.parseInt(battery);
                    if (b >= 80) {
                        view.setBackgroundResource(R.drawable.battery_4);
                    } else if (b < 80 && b > 50) {
                        view.setBackgroundResource(R.drawable.battery_3);
                    } else if (b < 50 && b > 30) {
                        view.setBackgroundResource(R.drawable.battery_2);
                    } else {
                        view.setBackgroundResource(R.drawable.battery_1);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
