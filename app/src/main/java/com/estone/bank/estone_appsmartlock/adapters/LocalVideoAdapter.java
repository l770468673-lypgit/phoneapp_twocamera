package com.estone.bank.estone_appsmartlock.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.https.beans.LocalVideoInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.LocalViewHolder> {
    private Context context;
    private List<LocalVideoInfo> mDatas;
    private MediaPlayer player;
    //    记录哪个位置播放视频了
    private int currentPosition = -1;


    public LocalVideoAdapter(Context context) {
        this.context = context;
        this.mDatas = new ArrayList<>();
        player = new MediaPlayer();


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });
    }


    public void destoryPlayer() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public LocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_local_video, parent, false);
        LocalViewHolder holder = new LocalViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(LocalViewHolder holder, int position) {
        final LocalVideoInfo info = mDatas.get(position);



        holder.mTitleTv.setText(info.getPath());
        //  获取播放时间
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(new Date(info.getDuration()));
        holder.mDurationTv.setText("播放时长：" + time);
        //  获取视频的大小
        long mSize = info.getSize() / 1024 / 1024;
        holder.mSizeTv.setText("视频大小：" + mSize + "M");
        //  获取视频缩略图，显示缩略图
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(info.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
        holder.mThumbIv.setImageBitmap(thumbnail);


        holder.mThumbIv.setTag(position);
        //  判断当前位置是否是需要播放视频的位置。


        holder.mThumbIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) v.getTag();
                currentPosition = pos;
                notifyDataSetChanged();
            }
        });


        if (currentPosition == position) {
            if (player.isPlaying()) {
                player.stop();
            }
            //     说明当前正在被创建的view，应该播放视频
            //     隐藏ImageView，展示SurfaceView
            holder.mSurfaceView.setVisibility(View.VISIBLE);
            holder.mThumbIv.setVisibility(View.INVISIBLE);
            //   获取SurfaceHolder对象
            SurfaceHolder surfaceHolder = holder.mSurfaceView.getHolder();


            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    player.reset();   //重置媒体播放器
                    player.setDisplay(holder);  //设置播放器播放的位置
                    try {
                        player.setDataSource(info.getPath());
                        player.prepare();   //本地视频使用同步，网络视频使用异步
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                }
            });
        } else {
            //     不需要播放视频
            holder.mSurfaceView.setVisibility(View.INVISIBLE);
            holder.mThumbIv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void setdate(List<LocalVideoInfo> date) {
        this.mDatas = date;
    }


    class LocalViewHolder extends RecyclerView.ViewHolder {
        SurfaceView mSurfaceView;
        ImageView mThumbIv;
        TextView mTitleTv, mDurationTv, mSizeTv;

        public LocalViewHolder(View itemView) {
            super(itemView);
            mSurfaceView = (SurfaceView) itemView.findViewById(R.id.item_surface);
            mThumbIv = (ImageView) itemView.findViewById(R.id.item_iv);
            mTitleTv = (TextView) itemView.findViewById(R.id.item_tv_title);
            mDurationTv = (TextView) itemView.findViewById(R.id.item_tv_time);
            mSizeTv = (TextView) itemView.findViewById(R.id.item_tv_size);
        }
    }
}
