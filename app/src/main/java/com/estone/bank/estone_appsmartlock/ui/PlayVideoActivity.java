package com.estone.bank.estone_appsmartlock.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.RecyVideoAdapter;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;

import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;

public class PlayVideoActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "PlayVideoActivity";
    private SurfaceView sv_main_surface;
    private SeekBar mProgress;
    private TextView textprogress, text_videomsg;

    private ImageView video_back_pic, video_back_stop, image_stop_start, play_ImageView;
    private MediaPlayer mediaPlayer;
    private String mVideoUrl1;
    private String aftOrMoring;
    public boolean stop = false;

    private boolean mPlaying;
    private Formatter mMFormatter;
    private StringBuilder mMFormatBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        mVideoUrl1 = getIntent().getStringExtra("videoUrl");
        //将长度转换为时间
        mMFormatBuilder = new StringBuilder();
        mMFormatter = new Formatter(mMFormatBuilder, Locale.getDefault());

        intview();
     /*
            参数二： 代表当前seekBar滑动的进度
            参数三：  true 表示手指拖拽    false 表示 设置进度
            当拖动条发生变化时调用该方法
             */
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            //当用户开始滑动滑块时调用该方法（即按下鼠调用一次）
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            //当用户结束对滑块滑动时,调用该方法（即松开鼠标）
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                int progress = seekBar.getProgress();
                //在当前位置播放
                //                mediaPlayer.seekTo(progress);
                mediaPlayer.start();
            }
        });

    }


    public void playmusic(String view) {

        if (mediaPlayer == null) {
            //01.播放内存中的音频
            //mediaPlayer = MediaPlayer.create(this, R.raw.f);
            //播放内存卡中文件
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(this, Uri.parse(view));
                //准备
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //**重点: 把捕捉到的画面放到SurfaceView中**         mediaPlayer.setDisplay(sfv_movie_surface.getHolder());
            mediaPlayer.setDisplay(sv_main_surface.getHolder());
            mediaPlayer.start();

            //获取音乐的总时长
            int duration = mediaPlayer.getDuration();
            LUtils.d(TAG, "获取音乐的总时长==" + duration);
            String s = stringForTime(duration);
            LUtils.d(TAG, "获取音乐的总时长= s ==" + s);
            //将进度条设置最大值为：音乐的总时长
            textprogress.setText(s);
            mProgress.setMax(duration);
            //启动线程
            mPlaying = mediaPlayer.isPlaying();
            new MyThread().start();
        } else if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }


    //将长度转换为时间
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mMFormatBuilder.setLength(0);
        if (hours > 0) {
            return mMFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mMFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //启动线程的一个方法
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (mProgress.getProgress() <= mProgress.getMax()) {
                //获取音乐当前播放的位置
                if (null != mediaPlayer) {
                    if (mPlaying) {//如果不在播放状态，则停止更新
                        // 播放器进度条，防止界面报错
                        Log.d(TAG, "播放器停止播放,跳过获取位置");
                        int position = mediaPlayer.getCurrentPosition();
                        LUtils.d(TAG, "postioninp----" + position);
                        mProgress.setProgress(position);
                    }

                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mPlaying = false;
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                finish();

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void intview() {

        String subyearMD = getIntent().getStringExtra("subyearMD");
        String subdayaft = getIntent().getStringExtra("subdayaft");
        String subdahours = getIntent().getStringExtra("subdahours");
        mProgress = findViewById(R.id.download_progress);
        sv_main_surface = findViewById(R.id.play_video);
        textprogress = findViewById(R.id.textprogress);
        video_back_pic = findViewById(R.id.video_back_pic);
        text_videomsg = findViewById(R.id.text_videomsg);
        video_back_stop = findViewById(R.id.video_back_stop);
        image_stop_start = findViewById(R.id.image_stop_start);
        play_ImageView = findViewById(R.id.play_ImageView);
        int i = Integer.parseInt(subdayaft);
        if (i >= 12) {
            aftOrMoring = "下午";
        } else {
            aftOrMoring = "上午";
        }
        text_videomsg.setText(subyearMD + "  " + aftOrMoring + "  " + subdayaft + ":" + subdahours);
        video_back_pic.setOnClickListener(this);
        video_back_stop.setOnClickListener(this);
        image_stop_start.setOnClickListener(this);
        play_ImageView.setOnClickListener(this);

        mProgress.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        Bitmap videoThumbnail = RecyVideoAdapter.getVideoThumbnail(mVideoUrl1);

        play_ImageView.setImageBitmap(videoThumbnail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_back_stop:
            case R.id.video_back_pic:
                mPlaying = false;
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    mediaPlayer.stop();
                }
                finish();
                break;
            case R.id.image_stop_start:
                play_ImageView.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                playmusic(mVideoUrl1);
                if (stop) {
                    image_stop_start.setBackgroundResource(R.mipmap.playvideo_play);
                    stop = false;
                } else {
                    image_stop_start.setBackgroundResource(R.mipmap.playvideo_stop);
                    stop = true;
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LUtils.d(TAG, "==onDestroy======");
        if (mediaPlayer != null) {

            mediaPlayer.release();
        }
    }
}
