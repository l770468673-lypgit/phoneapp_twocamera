package com.estone.bank.estone_appsmartlock.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.ui.CameraActivity;
import com.estone.bank.estone_appsmartlock.ui.CameraPicActivity;
import com.estone.bank.estone_appsmartlock.ui.CameraVideoActivity;
import com.estone.bank.estone_appsmartlock.ui.PlayVideoActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecyVideoAdapter extends RecyclerView.Adapter<RecyVideoAdapter.ViewHolder> {

    private String TAG = "RecyPicAdapter";
    private List<String> mDate;
    private Context mContext;

    private String mVideourl;

    public RecyVideoAdapter(CameraVideoActivity cameraPicActivity, String picDic) {
        this.mContext = cameraPicActivity;
        this.mDate = new ArrayList<>();
        this.mVideourl = picDic;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cameralist_picitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String surl = mDate.get(position);
        LUtils.d(TAG, "surl" + surl);
        ///storage/emulated/0/com.estone.bank.estone_appsmartlock/videorecord/20190125152714.mp4
        //File.separator/storage/emulated/0/com.estone.bank.estone_appsmartlock/snapshot/
        LUtils.d(TAG, "FunPath.PATH_PHOTO + File.separator" + FunPath.PATH_VIDEO + File.separator);

        Bitmap videoThumbnail = getVideoThumbnail(surl);
        holder.piclist_itemlist.setImageBitmap(videoThumbnail);
        holder.imageviewplay.setVisibility(View.VISIBLE);
        String replace = surl.replace(mVideourl, "");
        final String subyear = replace.substring(0, 4); //  日期
        final String submonth = replace.substring(4, 6); //  日期
        final String subday = replace.substring(6, 8); //  日期
        final String subdayaft = replace.substring(8, 10); //  上下午
        final String subdahours = replace.substring(10, 12); // 时间


        holder.todays.setText(subyear + "-" + submonth + "-" + subday);

        //FunPath.PATH_PHOTO + File.separator
        //String subyearMD = replace.substring(0, 8);
        holder.imageviewplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Intent intent = new Intent("android.intent.action.VIEW");
                //                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                String type = "video/*";
                ////              Uri uri = Uri.fromFile(EstoneVideoFile);
                //                Uri uri = Uri.fromFile(new File(surl));
                //                intent.setDataAndType(uri, type);
                //                mContext.startActivity(intent);
                //                LUtils.e(TAG, "------------uri------" + uri.toString());

                Intent intentplay = new Intent(mContext, PlayVideoActivity.class);
                intentplay.putExtra("videoUrl", surl);
                intentplay.putExtra("subyearMD", subyear + "-" + submonth + "-" + subday);
                intentplay.putExtra("subdayaft", subdayaft);
                intentplay.putExtra("subdahours", subdahours);
                mContext.startActivity(intentplay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDate != null ? mDate.size() : 0;
    }

    public void setDate(List<String> date) {
        mDate = date;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView piclist_itemlist, imageviewplay;
        private TextView todays;
        private RelativeLayout constlayout;
        int position = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            piclist_itemlist = itemView.findViewById(R.id.piclist_itemlist);
            constlayout = itemView.findViewById(R.id.constlayout);
            imageviewplay = itemView.findViewById(R.id.imageviewplay);

            todays = itemView.findViewById(R.id.todays);


            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            LUtils.d(TAG, "width===" + screenWidth);
            LUtils.d(TAG, "height===" + screenHeight);

            // 视频窗口全屏显示
            GridLayoutManager.LayoutParams lpWnd = (GridLayoutManager.LayoutParams) constlayout.getLayoutParams();
            lpWnd.width = screenWidth / 2 - 40;
            lpWnd.height = screenWidth / 2 - 40;
            constlayout.setLayoutParams(lpWnd);

        }
    }


    //得到视频缩略图
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
