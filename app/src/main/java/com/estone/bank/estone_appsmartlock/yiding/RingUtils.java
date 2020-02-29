package com.estone.bank.estone_appsmartlock.yiding;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * 铃声播放工具
 * Created by Bingo on 2017/11/27.
 */
public class RingUtils {

    private static RingUtils ringUtils;
    private MediaPlayer player;

    public static RingUtils init() {
        if (null == ringUtils) {
            synchronized (RingUtils.class) {
                if (null == ringUtils) {
                    ringUtils = new RingUtils();
                }
            }
        }
        return ringUtils;
    }


    /**
     * 开始播放声音
     *
     * @param context
     * @param ringAddress 铃声地址
     */
    public void startPlayingRings(Context context, String ringAddress) {
        try {
            if (player == null) {
                player = new MediaPlayer();
            }
            Uri uri;
            if (ringAddress == null || ringAddress.equals("")) {
                // 获取默认的铃声
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            } else {
                uri = Uri.parse(ringAddress);
            }
            player.setDataSource(context, uri);
            player.setLooping(false); // 不要循环
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (player == mp) {
                        if (!player.isPlaying()) {
                            player.start();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止铃声播放,停止计时器
     * (页面关闭调用,正常来说倒计时结束和接通关闭即可)
     * (用户接通调用)
     * (倒计时结束调用)
     */
    public void stopPlayingRings() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
}
