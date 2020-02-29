package com.estone.bank.estone_appsmartlock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;

import java.util.ArrayList;
import java.util.List;

public class Recycleview_FruitAdapter extends RecyclerView.Adapter<Recycleview_FruitAdapter.ViewHolder> {
    private OnRcyItemClickListener mOnItemClickListener = null;

    private List<Integer> mdate;
    private Context mContext;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    private int isfirsts = 0;
    private String days;


    public Recycleview_FruitAdapter(FragmentActivity activity, String s) {
        this.mContext = activity;
        this.mdate = new ArrayList<>();
        this.days = s;

    }

    public void setOnItemClickLitener(OnRcyItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setDatas(List<Integer> datas) {
        this.mdate = datas;
        isClicks = new ArrayList<>();
        for (int i = 0; i < mdate.size(); i++) {
            isClicks.add(false);
        }
        this.notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.simplelist_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.datetime.setText(mdate.get(position) + "");
        isfirsts++;
        if (isfirsts == Integer.valueOf(days)) {
            isClicks.set(position, true);

        }

        if (isClicks.get(position)) {
            holder.datetime.setTextColor(Color.parseColor("#FFFFFF"));
            //            holder.datetime.setBackgroundResource(R.mipmap.housedetal_kongtiaotiaojie);
            holder.datetime.setBackgroundResource(R.drawable.circle_fill);

        } else {
            holder.datetime.setTextColor(Color.parseColor("#595959"));
            holder.datetime.setBackgroundResource(R.color.white);
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < isClicks.size(); i++) {
                        isClicks.set(i, false);
                    }
                    isClicks.set(position, true);
                    notifyDataSetChanged();

                    mOnItemClickListener.onItemClicks(holder.itemView, holder.datetime, position);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return  mdate != null ? mdate.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView datetime;

        public ViewHolder(View itemView) {
            super(itemView);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
        }
    }


    public interface OnRcyItemClickListener {
        void onItemClicks(View view, TextView textView, int position);
    }


}
