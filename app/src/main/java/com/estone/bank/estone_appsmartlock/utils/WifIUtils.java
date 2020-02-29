package com.estone.bank.estone_appsmartlock.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.estone.bank.estone_appsmartlock.R;
import com.lib.FunSDK;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.FunWifiPassword;
import com.lib.funsdk.support.utils.DeviceWifiManager;
import com.lib.funsdk.support.utils.MyUtils;
import com.lib.funsdk.support.utils.StringUtils;

public class WifIUtils {

    public static String TAG = "WifIUtils";
    public static Toast mToast = null;

    /**
     * 检查wifi是否处开连接状态
     *
     * @return
     */
    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }


    /**
     * 开始快速设置wifi
     */
    public static void startQuickSetting(Context context, String editTextSSID, String editTextPassword) {
        try {
            WifiManager wifiManage = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManage.getConnectionInfo();
            DhcpInfo wifiDhcp = wifiManage.getDhcpInfo();

            if (null == wifiInfo) {
                showToast(context, R.string.device_opt_set_wifi_info_error + "wifiInfo");
            }

//            String ssid = editTextSSID.getText().toString();
            if (StringUtils.isStringNULL(editTextSSID)) {
                showToast(context, com.example.funsdkdemo.R.string.device_opt_set_wifi_info_error + "ssid");
                return;
            }

            ScanResult scanResult = DeviceWifiManager.getInstance(context).getCurScanResult(editTextSSID);
            LUtils.d(TAG, "ssid==" + editTextSSID);
            LUtils.d(TAG, "scanResult==" + scanResult);
            if (null == scanResult) {
                showToast(context, "当前Wifi信息有误" );
                return;
            }

            int pwdType = MyUtils.getEncrypPasswordType(scanResult.capabilities);
//            String wifiPwd = editTextPassword.getText().toString().trim();

            if (pwdType != 0 && StringUtils.isStringNULL(editTextPassword)) {
                // 需要密码
                showToast(context, "当前Wifi信息有误");
                return;
            }

            StringBuffer data = new StringBuffer();
            data.append("S:").append(editTextSSID).append("P:").append(editTextPassword).append("T:").append(pwdType);

            String submask;
            if (wifiDhcp.netmask == 0) {
                submask = "255.255.255.0";
            } else {
                submask = MyUtils.formatIpAddress(wifiDhcp.netmask);
            }

            String mac = wifiInfo.getMacAddress();
            StringBuffer info = new StringBuffer();
            info.append("gateway:").append(MyUtils.formatIpAddress(wifiDhcp.gateway)).append(" ip:")
                    .append(MyUtils.formatIpAddress(wifiDhcp.ipAddress)).append(" submask:").append(submask)
                    .append(" dns1:").append(MyUtils.formatIpAddress(wifiDhcp.dns1)).append(" dns2:")
                    .append(MyUtils.formatIpAddress(wifiDhcp.dns2)).append(" mac:").append(mac)
                    .append(" ");


            FunSupport.getInstance().startWiFiQuickConfig(editTextSSID,
                    data.toString(), info.toString(),
                    MyUtils.formatIpAddress(wifiDhcp.gateway),
                    pwdType, 0, mac, -1);
            LUtils.d(TAG, "快速配置" + data);
            FunWifiPassword.getInstance().saveWifiPassword(editTextSSID, editTextPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showToast(Context context, String text) {
        if (null != text) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * 结束快速设置wifi
     */
    public static void stopQuickSetting() {
        FunSupport.getInstance().stopWiFiQuickConfig();
    }

    public static String getConnectWifiSSID(Context context) {
        try {
            WifiManager wifiManage = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            return wifiManage.getConnectionInfo().getSSID().replace("\"", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



}
