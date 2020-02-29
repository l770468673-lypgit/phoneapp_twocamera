package com.estone.bank.estone_appsmartlock.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.https.beans.bean_Alllookinfor;
import com.estone.bank.estone_appsmartlock.utils.Base64BitmapUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LockingInfoRecyclerViewAdapter extends RecyclerView.Adapter<LockingInfoRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "LockingAdapter";
    //    private final OnListFragmentInteractionListener mListener;
    //    private int maxCount = 0;
    private List<bean_Alllookinfor.InfosBean> list;
    private Context mContext;
    private LookOnItemClickedListener onItemClickedListener;
    private Bitmap bitmap1;

    //--
    public LockingInfoRecyclerViewAdapter(FragmentActivity activity) {
        this.list = new ArrayList<>();
        this.mContext = activity;

    }


    public void setData(List<bean_Alllookinfor.InfosBean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_locking_info, parent, false);
        String mUserralbitmap = ShareUtil.getString("userralbitmap");
        if (mUserralbitmap != null) {

            bitmap1 = Base64BitmapUtils.base64ToBitmap(mUserralbitmap);
        }

        //注意false
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {

        return list != null ? list.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final bean_Alllookinfor.InfosBean infosBean = list.get(position);
        //时间TextView赋值
        DateFormat format = SimpleDateFormat.getDateTimeInstance();
        holder.textView_lookinfo_time.setText(format.format(new Date(infosBean.getUi().getTime())));

        LUtils.d(TAG, "----infosBean.getUi() ------" + infosBean.getUi().toString());
        LUtils.d(TAG, "----infosBean.getUi() ------" + infosBean.getName());

//        switch (infosBean.getUi().getType()){
//            case "":
//
//                break;
//        }
        if ("adminid".equals(infosBean.getUi().getType())) {
            holder.icon_lookinfo_opentype.setText("管理员密码开门");
            holder.textView_lookInfo_name.setText("管理员密码");
            holder.icon_lookinfo_opentype.setTextColor(mContext.getResources().getColor(R.color.idopen));
            holder.icon_lookinfo.setImageResource(R.mipmap.housedetal_idcard_opendoor);
            holder.admin_tagpic.setVisibility(View.VISIBLE);
            holder.admin_tagpic.setImageResource(R.mipmap.lookinfor_adminpic);
        }
            if ("admincard".equals(infosBean.getUi().getType())) {
            holder.icon_lookinfo_opentype.setText("管理员卡号开门");
            holder.textView_lookInfo_name.setText("管理员卡片");
            holder.icon_lookinfo.setImageResource(R.mipmap.housedetal_passopendoor);
            holder.icon_lookinfo_opentype.setTextColor(mContext.getResources().getColor(R.color.passopen));
            holder.admin_tagpic.setVisibility(View.VISIBLE);
            holder.admin_tagpic.setImageResource(R.mipmap.lookinfor_adminpic);
        }
        if ("tempid".equals(infosBean.getUi().getType())) {
            holder.icon_lookinfo_opentype.setText("临时密码开门");
            holder.icon_lookinfo.setImageResource(R.mipmap.housedetal_passopendoor);
            holder.textView_lookInfo_name.setText("临时密码");
            holder.admin_tagpic.setVisibility(View.GONE);
            holder.icon_lookinfo_opentype.setTextColor(mContext.getResources().getColor(R.color.passopen));
        } else if ("tempcard".equals(infosBean.getUi().getType())) {
            holder.icon_lookinfo_opentype.setText("临时卡号开门");
            holder.icon_lookinfo.setImageResource(R.mipmap.housedetal_idcard_opendoor);
            holder.admin_tagpic.setVisibility(View.GONE);
            holder.textView_lookInfo_name.setText(infosBean.getName());
            holder.icon_lookinfo_opentype.setTextColor(mContext.getResources().getColor(R.color.idopen));
        }

//        if("tempcard".equals(infosBean.getUi().getType())&&(infosBean.getUi().getImgUrl()!=null)){
//            // 本次是签注
//            holder.imageView_lookinfohead.setTag(R.id.glide_tag, position);
//            LUtils.d(TAG, "-----imgUrl VISIBLE ！= null--------" + position + "000" + infosBean.getUi().getImgUrl());
//            holder.tv_canlookimg.setVisibility(View.VISIBLE);
//            holder.user_signpic.setVisibility(View.VISIBLE);
//            holder.textView_lookInfo_name.setText(infosBean.getName());
//            holder.user_signpic.setImageResource(R.mipmap.sign_lookinfror_pic);
//            Glide.with(mContext).load(infosBean.getUi().getImgUrl())
//                    .placeholder(R.mipmap.housedetal_uesrdefaultpic)
//                    .dontAnimate().skipMemoryCache(true).
//                    diskCacheStrategy(DiskCacheStrategy.NONE).
//                    into(holder.imageView_lookinfohead);
//        }

        //给ImageView设置唯一标记。  拍照的头像 -----  身份证头像
        if ((infosBean.getUi().getImgUrl().length() < 10) && (infosBean.getImg().length() < 10)) {
            //  签注拍照的图片长度小于10 并且身份证url 区小于10， 是 密码、 或者 管理员卡
            if ("tempid".equals(infosBean.getUi().getType())) {
                // 临时密码
                Glide.clear(holder.imageView_lookinfohead);
                holder.user_signpic.setVisibility(View.GONE);
                holder.tv_canlookimg.setVisibility(View.GONE); // 查看图标
                holder.imageView_lookinfohead.setTag(R.id.glide_tag, position);
                holder.imageView_lookinfohead.setImageResource(R.mipmap.housedetal_uesrdefaultpic);
            } else {
                Glide.clear(holder.imageView_lookinfohead);
                holder.imageView_lookinfohead.setImageBitmap(bitmap1);
                holder.imageView_lookinfohead.setTag(R.id.glide_tag, position);
                holder.user_signpic.setVisibility(View.GONE);
                holder.tv_canlookimg.setVisibility(View.GONE); // 查看图标
            }
            return;
        } else if ((infosBean.getUi().getImgUrl().length() > 10) && (infosBean.getImg().length() > 10)) {
            // //  签注拍照的图片长度大于10 , 并且身份证url 区大于10， 是签注
            holder.imageView_lookinfohead.setTag(R.id.glide_tag, position);
            LUtils.d(TAG, "-----imgUrl VISIBLE ！= null--------" + position + "000" + infosBean.getUi().getImgUrl());
            holder.tv_canlookimg.setVisibility(View.VISIBLE);
            holder.user_signpic.setVisibility(View.VISIBLE);
            holder.user_signpic.setImageResource(R.mipmap.sign_lookinfror_pic);
            Glide.with(mContext).load(infosBean.getUi().getImgUrl())
                    .placeholder(R.mipmap.housedetal_uesrdefaultpic)
                    .dontAnimate().skipMemoryCache(true).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    into(holder.imageView_lookinfohead);
        } else if ((infosBean.getUi().getImgUrl().length() < 10) && (infosBean.getImg().length() > 10)) {
            //签注拍照的图片长度小于10 , 并且身份证url 区大于10， 是 临时普通刷卡
            holder.imageView_lookinfohead.setTag(R.id.glide_tag, position);
            holder.user_signpic.setVisibility(View.GONE);
            holder.tv_canlookimg.setVisibility(View.GONE); // 查看图标

        } else if ((infosBean.getUi().getImgUrl().length() >10) && (infosBean.getImg().length() < 10)) {
            holder.textView_lookInfo_name.setText("二次签注");
//            holder.itemView.setVisibility(View.GONE);
        }
        Object tag = holder.imageView_lookinfohead.getTag(R.id.glide_tag);
        if (tag != null && (int) tag != position) {
            //如果tag不是Null,并且同时tag不等于当前的position。
            //说明当前的viewHolder是复用来的
            //Cancel any pending loads Glide may have for the view
            //and free any resources that may have been loaded for the view.
            Glide.clear(holder.imageView_lookinfohead);
            holder.tv_canlookimg.setVisibility(View.GONE);
        }

//        if (infosBean.getUi().getImgUrl() != null) {
//
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {

                    if (infosBean.getUi().getImgUrl().length() > 10) {
                        LUtils.d(TAG, "-----imgUrl VISIBLE ！= null--------" + infosBean.getUi().getSn());
                        onItemClickedListener.lookOnItemClicked(infosBean.getUi().getSn(), infosBean.getUi().getImgUrl());
                    }
                }
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder != null) {
            Glide.clear(holder.imageView_lookinfohead);
        }
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //        public TextView dateTextView;
        public TextView icon_lookinfo_opentype;
        public TextView tv_canlookimg;
        public TextView textView_lookInfo_cardid;
        public TextView textView_lookInfo_name, textView_lookinfo_time;
        public ImageView imageView_lookinfohead, admin_tagpic, user_signpic;
        public ImageView icon_lookinfo;
        public LinearLayout lly_edit_delect1;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //dateTextView = (TextView) view.findViewById(R.id.textView_date);
            textView_lookInfo_name = (TextView) view.findViewById(R.id.textView_lookInfo_name);
            textView_lookInfo_cardid = (TextView) view.findViewById(R.id.textView_lookInfo_cardid);
            //typeImageView = (ImageView) view.findViewById(R.id.imageView_type);
            imageView_lookinfohead = (ImageView) view.findViewById(R.id.imageView_lookinfohead);
            icon_lookinfo = (ImageView) view.findViewById(R.id.icon_lookinfo);
            admin_tagpic = (ImageView) view.findViewById(R.id.admin_tagpic);
            user_signpic = (ImageView) view.findViewById(R.id.user_signpic);
            textView_lookinfo_time = (TextView) view.findViewById(R.id.textView_lookinfo_time);
            icon_lookinfo_opentype = (TextView) view.findViewById(R.id.icon_lookinfo_opentype);
            tv_canlookimg = (TextView) view.findViewById(R.id.tv_canlookimg);
            lly_edit_delect1 = (LinearLayout) view.findViewById(R.id.lly_edit_delect1);
        }

        @Override
        public String toString() {
            return "";
        }
    }

    public void setOnItemClickedListener(LookOnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;

    }

    public interface LookOnItemClickedListener {
        void lookOnItemClicked(String position, String iurl);


    }

}
