package com.estone.bank.estone_appsmartlock.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.Widgets.RatioImageView;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.ui.PropertyHomeActivity;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.yiding.PlayerLiveActivity;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;

import java.util.ArrayList;
import java.util.List;

public class PropertiesRecyclerViewAdapter extends RecyclerView.Adapter<PropertiesRecyclerViewAdapter.ViewHolder> {

    private static String TAG = "PropertiesRecyclerViewAdapter";
    private List<Bean_AllDevices.InfosBean> mData;
    private Context mContext;

    //    private reflashListher reflashListher;
    private OnRecLongClickListener mLinstener;

    //    public interface reflashListher {
    //        void refresh();
    //    }

    /**
     * 手动添加长按事件
     */
    public interface OnRecLongClickListener {
        void onRecLongClick(int position);
    }

    public void setOnRecLongClickListener(OnRecLongClickListener mOnItemClickListener) {
        this.mLinstener = mOnItemClickListener;
    }


    //    public void setOnreflashListher(reflashListher mOnItemClickListener) {
    //        this.reflashListher = mOnItemClickListener;
    //    }

    public PropertiesRecyclerViewAdapter(Context context) {
        this.mData = new ArrayList<>();
        this.mContext = context;
    }


    public void setData(List<Bean_AllDevices.InfosBean> infosBeanList) {
        mData = infosBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_properties, parent, false);

        return new ViewHolder(view);
    }

    private FunDevice getFunDeviceFromSN(String sn) {
        List<FunDevice> devices = FunSupport.getInstance().getDeviceList();
        for (FunDevice device : devices) {
            if (device.getDevSn().equals(sn)) {
                return device;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Bean_AllDevices.InfosBean infosBean = mData.get(position);
        holder.nameTextView.setText(infosBean.getRoomId());
        holder.addressTextView.setText(infosBean.getRoomName());
        String imgUrl = infosBean.getImgUrl();
        LUtils.d(TAG, "--" + imgUrl);
        FunDevice funDevice = getFunDeviceFromSN(infosBean.getCameraId());
        Glide.with(mContext).load(infosBean.getImgUrl())
                .placeholder(R.mipmap.pic_blackalph).error(R.mipmap.bind_otherpic).dontAnimate().skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mRatioImageView);
        if (funDevice != null) {
            holder.properfragment_devstuts.setText(funDevice.devStatus.getStatusResId());
        }

        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                Bean_AllDevices.InfosBean infosBean1 = mData.get(tag);
                Bundle bundle = new Bundle();
                bundle.putSerializable("sendinfosBean", infosBean1);
//                if (!testAllUpperCase(infosBean1.getDevId()) && infosBean1.getDevId().length() == 16) {
                    // XM 摄像头
                    LUtils.d(TAG, "getCameraId--------" + infosBean1.getCameraId() + "getDevId--------" + infosBean1.getDevId());
                    Intent intent = new Intent(mContext, PropertyHomeActivity.class);
                    //TODO add extras about the house here
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

//                } else {
//                Intent intent = new Intent(mContext, PlayerLiveActivity.class);
//                mContext.startActivity(intent);
//                }


            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int tag = (int) holder.cardView.getTag();
                mLinstener.onRecLongClick(tag); //长按事件
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RatioImageView mRatioImageView;
        private TextView nameTextView;
        private TextView addressTextView;
        private TextView properfragment_devstuts;
        private CardView cardView;


        public ViewHolder(View view) {
            super(view);
            mRatioImageView = view.findViewById(R.id.imageView_main);
            nameTextView = view.findViewById(R.id.textView_name);
            addressTextView = view.findViewById(R.id.textView_address);
            properfragment_devstuts = view.findViewById(R.id.properfragment_devstuts);
            cardView = view.findViewById(R.id.cardView_property);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameTextView.getText() + "'";
        }
    }


}
