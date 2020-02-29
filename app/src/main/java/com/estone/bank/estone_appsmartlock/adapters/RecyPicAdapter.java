package com.estone.bank.estone_appsmartlock.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.LightingColorFilter;
import android.media.MediaMetadataRetriever;
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
import com.estone.bank.estone_appsmartlock.ui.CameraPicActivity;
import com.estone.bank.estone_appsmartlock.ui.CameraVideoActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;
import com.lib.funsdk.support.FunPath;
import com.lidroid.xutils.db.annotation.Id;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecyPicAdapter extends RecyclerView.Adapter<RecyPicAdapter.ViewHolder> {

    private String TAG = "RecyPicAdapter";
    private List<String> mDate;
    private Context mContext;

    private onItemPicClickListener mListener;
    private String mPicUrl;

    public RecyPicAdapter(CameraPicActivity cameraPicActivity, String image_local_pic) {
        this.mContext = cameraPicActivity;
        this.mDate = new ArrayList<>();
        this.mPicUrl = image_local_pic;
    }


    public void setOnItemPicListener(onItemPicClickListener mListener) {
        this.mListener = mListener;

    }

    public interface onItemPicClickListener {
        void itemListener(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cameralist_picitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String surl = mDate.get(position);
        LUtils.d(TAG, "surl==" + position + "===" + surl);

        // 包含
        Glide.with(mContext).load(surl)
                .placeholder(R.mipmap.camerapreview_takepic_default)
                .dontAnimate().
                into(holder.piclist_itemlist);
        String replace = surl.replace(mPicUrl, "");
        String subyearMD = replace.substring(0, 8); //  日期
        final String subyear = replace.substring(0, 4); //  日期
        final String submonth = replace.substring(4, 6); //  日期
        final String subday = replace.substring(6, 8); //  日期
        final String subdayaft = replace.substring(8, 10); //  上下午
        final String subdahours = replace.substring(10, 12); // 时间
        final String subdamin = replace.substring(12, 14); // 分
        holder.todays.setText(subyear + "-" + submonth + "-" + subday + "-" + subdayaft + ":" + subdahours + ":" + subdamin);
        //        holder.todays.setText(subyearMD);


        //FunPath.PATH_PHOTO + File.separator
        //String subyearMD = replace.substring(0, 8);
        holder.piclist_itemlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemListener(position);

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
}
