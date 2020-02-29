package com.estone.bank.estone_appsmartlock.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.utils.LUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HousingInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HousingInfoFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    private LinearLayout background_1, background_2;
    private RelativeLayout background_3;
    private ImageView imagelight_1, imagelight_2;
    private TextView texttemp;
    private boolean istrue1 = true;
    private boolean istrue2 = true;
    private boolean istrue3 = true;
    private SeekBar mseekbar;
    private Button swimage_1, swimage_2;
    private Button conditioner;

    public static HousingInfoFragment newInstance() {
        HousingInfoFragment fragment = new HousingInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_housing_info, container, false);
        initView(v);
        return v;


    }

    private void initView(View v) {

        background_1 = v.findViewById(R.id.background_1);
        imagelight_1 = v.findViewById(R.id.imagelight_1);
        swimage_1 = v.findViewById(R.id.swimage_1);
        background_3 = v.findViewById(R.id.background_3);

        background_2 = v.findViewById(R.id.background_2);
        imagelight_2 = v.findViewById(R.id.imagelight_2);
        swimage_2 = v.findViewById(R.id.swimage_2);
        conditioner = v.findViewById(R.id.conditioner);

        mseekbar = v.findViewById(R.id.seekbar);
        texttemp = v.findViewById(R.id.texttemp);

        swimage_1.setOnClickListener(this);
        swimage_2.setOnClickListener(this);
        conditioner.setOnClickListener(this);
        mseekbar.setOnSeekBarChangeListener(this);
        swimage_1.setSelected(true);
        swimage_2.setSelected(true);
        conditioner.setSelected(true);

        texttemp.setText("17 ℃");
        tofalse();


    }

    private void tofalse() {
        background_2.setBackgroundResource(R.drawable.houseinfo_backgrounddefault);
        imagelight_2.setBackgroundResource(R.mipmap.housedetal_sleeproom_letoff);
        swimage_2.setText("关");
        swimage_2.setSelected(false);
        istrue2 = false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.swimage_1:
                if (istrue1) {
                    background_1.setBackgroundResource(R.drawable.houseinfo_backgrounddefault);
                    imagelight_1.setBackgroundResource(R.mipmap.housedetal_sleeproom_letoff);
                    swimage_1.setText("关");
                    swimage_1.setSelected(false);
                    istrue1 = false;
                } else {
                    istrue1 = true;
                    background_1.setBackgroundResource(R.drawable.houseinfo_backgroundselect);
                    imagelight_1.setBackgroundResource(R.mipmap.housedetal_sleeproom_letopen);
                    swimage_1.setText("开");
                    swimage_1.setSelected(true);
                }
                break;
            case R.id.swimage_2:
                if (istrue2) {
                    background_2.setBackgroundResource(R.drawable.houseinfo_backgrounddefault);
                    imagelight_2.setBackgroundResource(R.mipmap.housedetal_sleeproom_letoff);
                    swimage_2.setText("关");
                    swimage_2.setSelected(false);
                    istrue2 = false;
                } else {
                    istrue2 = true;
                    background_2.setBackgroundResource(R.drawable.houseinfo_backgroundselect);
                    imagelight_2.setBackgroundResource(R.mipmap.housedetal_sleeproom_letopen);
                    swimage_2.setText("开");
                    swimage_2.setSelected(true);
                }
                break;
            case R.id.conditioner:
                if (istrue3) {
                    mseekbar.setFocusable(false);
                    background_3.setBackgroundResource(R.drawable.houseinfo_backgrounddefault);
                    conditioner.setText("关");
                    conditioner.setSelected(false);
                    istrue3 = false;
                } else {
                    mseekbar.setFocusable(false);
                    istrue3 = true;
                    background_3.setBackgroundResource(R.drawable.houseinfo_backgroundselect);
                    conditioner.setText("开");
                    conditioner.setSelected(true);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值
        texttemp.setText(Integer.toString(progress) + " ℃");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        LUtils.e("------------", "开始滑动！");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        LUtils.e("------------", "停止滑动！");
    }
}
