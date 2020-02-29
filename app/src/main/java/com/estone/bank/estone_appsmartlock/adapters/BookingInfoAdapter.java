package com.estone.bank.estone_appsmartlock.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.https.beans.User_AllDevices2;
import com.estone.bank.estone_appsmartlock.utils.LUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 */
public class BookingInfoAdapter extends RecyclerView.Adapter<BookingInfoAdapter.ViewHolder> {
    private static final String TAG = "BookingInfoRecyclerView";
    private int maxCount = 0;


    private OnItemClickedListener onItemClickedListener;
    private OnItemViewClickListener onItemViewClickedListener;
    private OnItemLongClickedListener onItemlongClickedListener;
    private Context mContext;
    private List<User_AllDevices2.InfosBean> mdates;

    //    private int isposition;

    public BookingInfoAdapter(Context context) {
        this.mdates = new ArrayList<>();
        this.mContext = context;

    }

    public void setData(List<User_AllDevices2.InfosBean> list) {
        mdates = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booking_info2, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        User_AllDevices2.InfosBean infosBean = mdates.get(position);

        if (infosBean.isLongcheck()) {
            holder.lly_edit.setVisibility(View.VISIBLE);
        } else {
            holder.lly_edit.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);
        LUtils.d(TAG, "infosBean.getDevId()==" + infosBean.getDevId());
        holder.nameTextView.setText(infosBean.getReserveName());
        String idNumber = infosBean.getIdNumber();
        String str = "**********";
        StringBuilder sb = new StringBuilder(idNumber);
        sb.replace(6, 16, str);
        holder.idTextView.setText(sb);
        holder.phoneTextView.setText(infosBean.getMobile());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date checkInStart = new Date(infosBean.getCheckInStart());
        Date checkInEnd = new Date(infosBean.getCheckInEnd());
        holder.fromTextView.setText(simpleDateFormat.format(checkInStart));
        holder.toTextView.setText(simpleDateFormat.format(checkInEnd));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClickedListener.OnItemViewClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                onItemlongClickedListener.onItemlongClickedListeners(position);
                return true;
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.OnItemDeleteClicked(position);

            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.OnItemEditClicked(position);
            }
        });

        holder.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.OnPhoneNumberClicked(holder.phoneTextView.getText().toString());
            }
        });

        //预定信息状态判断
        if (System.currentTimeMillis() > infosBean.getCheckInStart()
                && System.currentTimeMillis() < infosBean.getCheckInEnd()
                && infosBean.isCheckIn()) {
            holder.textView_ischech.setText("已入住");
            //入住时间内，且已check in
        } else if (System.currentTimeMillis() > infosBean.getCheckInEnd() && infosBean.isCheckIn()) {
            infosBean.setClicked(false);
            //入住时间结束，且已check in
            holder.textView_ischech.setText("已退房");
//            holder.textView_ischech.setTextColor(mContext.getResources().getColor(R.color.c6c6c6));
            holder.fromTextView.setTextColor(mContext.getResources().getColor(R.color.c6c6c6));
            holder.toTextView.setTextColor(mContext.getResources().getColor(R.color.c6c6c6));
        } else if (System.currentTimeMillis() > infosBean.getCheckInStart() && System.currentTimeMillis() < infosBean.getCheckInEnd() && !infosBean.isCheckIn()) {
            //
            holder.textView_ischech.setText("已到时,未入住");
        } else if (System.currentTimeMillis() < infosBean.getCheckInStart()) {
            //
            holder.textView_ischech.setText("未到时");
        } else if (System.currentTimeMillis() > infosBean.getCheckInEnd() && !infosBean.isCheckIn()) {
            //超过入住时间但未入住
            infosBean.setClicked(false);
            holder.textView_ischech.setText("过期未入住");
            holder.fromTextView.setTextColor(mContext.getResources().getColor(R.color.c6c6c6));
            holder.toTextView.setTextColor(mContext.getResources().getColor(R.color.c6c6c6));
//            holder.textView_ischech.setTextColor(mContext.getResources().getColor(R.color.c6c6c6));
        }

    }

    @Override
    public int getItemCount() {
        if (maxCount != 0) {
            return mdates.size() < maxCount ? mdates.size() : maxCount;
        }
        return mdates.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        TextView nameTextView;
        TextView phoneTextView;
        TextView idTextView;
        TextView fromTextView;
        TextView textView_ischech;
        TextView toTextView;
        ImageView headImageView;
        Button editButton;
        //        Button editButton2;
        Button deleteButton;
        //        Button deleteButton2;
        boolean isExpanded;
        //        private LinearLayout lly_edit_delect2;
        private LinearLayout lly_edit;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //            lly_edit_delect2 = view.findViewById(R.id.lly_edit_delect2);
            lly_edit = view.findViewById(R.id.lly_edit);
            textView_ischech = view.findViewById(R.id.textView_ischech);
            nameTextView = view.findViewById(R.id.textView_bookingInfo_name);
            phoneTextView = view.findViewById(R.id.textView_bookingInfo_tel);
            idTextView = view.findViewById(R.id.textView_bookingInfo_id);
            fromTextView = view.findViewById(R.id.textView_bookingInfo_from);
            toTextView = view.findViewById(R.id.textView_bookingInfo_to);
            headImageView = view.findViewById(R.id.imageView_head);
            editButton = view.findViewById(R.id.button_edit);
            //            editButton2 = view.findViewById(R.id.button_edit2);
            deleteButton = view.findViewById(R.id.button_delete);
            //            deleteButton2 = view.findViewById(R.id.button_delete2);

            isExpanded = false;
        }


        @Override
        public String toString() {
            return "";
        }
    }


    public void setOnItemLongClickedListener(OnItemLongClickedListener onItemlongClickedListener) {
        this.onItemlongClickedListener = onItemlongClickedListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;

    }

    public void setOnItemViewClickedListener(OnItemViewClickListener onItemViewClickedListener) {
        this.onItemViewClickedListener = onItemViewClickedListener;

    }


    public interface OnItemViewClickListener {

        void OnItemViewClick(int posstion);
    }


    public interface OnItemLongClickedListener {
        void onItemlongClickedListeners(int position);
    }

    public interface OnItemClickedListener {
        void OnItemDeleteClicked(int position);

        void OnItemEditClicked(int position);

        void OnPhoneNumberClicked(String phoneNumber);
    }
}
