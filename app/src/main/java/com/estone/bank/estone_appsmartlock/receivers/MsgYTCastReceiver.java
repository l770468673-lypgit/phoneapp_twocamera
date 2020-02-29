package com.estone.bank.estone_appsmartlock.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import cn.firmwarelib.nativelibs.bean.PushInfo;
import cn.firmwarelib.nativelibs.broadcastreceiver.PushMesReciver;
import cn.firmwarelib.nativelibs.utils.LogUtil;

public class MsgYTCastReceiver extends PushMesReciver {

    @Override
    public void pushMes(Context context, PushInfo pushInfo) {

        LogUtil.INSTANCE.i("收到推送消息，已经JSON");
        Toast.makeText(context,"收到推送消息，已经JSON:"+ JSON.toJSONString(pushInfo), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pushMes(Context context, Object pushInfo) {
        LogUtil.INSTANCE.i("收到推送消息，没有JSON");
        Toast.makeText(context,"收到推送消息，没有JSON:"+ JSON.toJSONString(pushInfo), Toast.LENGTH_SHORT).show();
    }
}
