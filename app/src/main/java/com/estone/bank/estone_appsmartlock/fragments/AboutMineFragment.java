package com.estone.bank.estone_appsmartlock.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.costum.CircleImageView;
import com.estone.bank.estone_appsmartlock.ui.PersionalDateActivity;
import com.estone.bank.estone_appsmartlock.ui.SplashActivity;
import com.estone.bank.estone_appsmartlock.utils.Base64BitmapUtils;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;
import com.estone.bank.estone_appsmartlock.utils.VersionCodeUtils;

public class AboutMineFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "AboutMineFragment";
    public static int MAINHANDLER_BITMAP_MSG = 9001;

    private CircleImageView mine_logo;
    private Button user_logout;
    private TextView MusermNickname;
    private TextView Msoftware_version;
    private String mUser_pic_head;
    private LinearLayout lly_userinformation;
    public static AboutMineFragmentHandler mHandler;

    public static AboutMineFragment newInstance(String user_pic_head) {

        AboutMineFragment fragment = new AboutMineFragment();
        Bundle args = new Bundle();
        args.putString("user_pic_head", user_pic_head);
        LUtils.d(TAG, "user_pic_head. ==" + user_pic_head);
        fragment.setArguments(args);
        return fragment;
    }

    public class AboutMineFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 9001:
                    String bitmap = msg.getData().getString("bitmap");
                    Bitmap bitmap1 = Base64BitmapUtils.base64ToBitmap(bitmap);
                    mUser_pic_head = bitmap;
                    mine_logo.setImageBitmap(bitmap1);

                    ShareUtil.putString("userralbitmap", bitmap);
                    break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_aboutmine_preference, container, false);
        mHandler = new AboutMineFragmentHandler();
        initview(inflate);
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mUser_pic_head = getArguments().getString("user_pic_head");
        }
        String wxusermNickname = ShareUtil.getString(Contants.WXUSERMNICKNAME);
        LUtils.d(TAG, "mUser_pic_head==" + mUser_pic_head);
        Log.d(TAG, "usermNickname==" + wxusermNickname);
        if (wxusermNickname != null) {
            MusermNickname.setText(wxusermNickname);
        } else {
            MusermNickname.setText("LoginUser");
        }


        Glide.with(getActivity()).load(mUser_pic_head).asBitmap().dontAnimate().skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                String sbitmap = Base64BitmapUtils.bitmapToBase64(resource);
                ShareUtil.putString("userralbitmap", sbitmap);
                mine_logo.setImageBitmap(resource);

            }
        });

        int localVersionCode = VersionCodeUtils.getLocalVersion(getActivity());
        String localVersionName = VersionCodeUtils.getLocalVersionName(getActivity());

        Msoftware_version.setText("V:" + localVersionName + "/" + localVersionCode);
    }

    private void initview(View inflate) {
        mine_logo = inflate.findViewById(R.id.mine_logo);
        user_logout = inflate.findViewById(R.id.user_logout);
        lly_userinformation = inflate.findViewById(R.id.lly_userinformation);
        MusermNickname = inflate.findViewById(R.id.usermNickname);
        Msoftware_version = inflate.findViewById(R.id.software_version);
        mine_logo.setOnClickListener(this);
        user_logout.setOnClickListener(this);
        lly_userinformation.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_logo:
            case R.id.lly_userinformation:
                Intent intent = new Intent(getActivity(), PersionalDateActivity.class);
                intent.putExtra("mUser_pic_head", mUser_pic_head);
                startActivity(intent);

                break;
            case R.id.user_logout:
                getActivity().finish();
                ShareUtil.removekey(Contants.IS_FIRST_LOGIN);
                Intent intentlogout = new Intent(getActivity(), SplashActivity.class);
                startActivity(intentlogout);
                break;
        }
    }
}
