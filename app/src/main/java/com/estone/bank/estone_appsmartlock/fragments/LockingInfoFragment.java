package com.estone.bank.estone_appsmartlock.fragments;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnChangeLisener;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.LockingInfoRecyclerViewAdapter;
import com.estone.bank.estone_appsmartlock.adapters.Recycleview_FruitAdapter;
import com.estone.bank.estone_appsmartlock.adapters.Recycleview_FruitAdapter.OnRcyItemClickListener;
import com.estone.bank.estone_appsmartlock.costum.CircleImageView;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_userInfo;
import com.estone.bank.estone_appsmartlock.https.beans.bean_Alllookinfor;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.ui.PropertyHomeActivity;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.DateUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ShareUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class LockingInfoFragment extends Fragment
        implements View.OnClickListener,
        OnRcyItemClickListener, OnChangeLisener, OnSureLisener, LockingInfoRecyclerViewAdapter.LookOnItemClickedListener {

    private TextView mselectdate, mlly_setdate;
    private LinearLayout informationnull;
    private RecyclerView mlist_date, list_lookinformation;
    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;

    private static final String TAG = "LockingInfoFragment";
    private Recycleview_FruitAdapter mAdapter;
    private List<Integer> mIntegers;
    private LockingInfoRecyclerViewAdapter mAdapter1;
    private Bean_AllDevices.InfosBean mSendinfosBean;

    private OlHandler mHandler;
    private String mMMstart;
    private String mMMend;
    private PopupWindow mPopupWindow;
    private long mCurrentstart;
    private long mCurrentend;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locking_info_list, container, false);

        mHandler = new OlHandler();
        initview(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        informationnull.setVisibility(View.GONE);
        String isLoginFirst = ShareUtil.getString("isLoginFirst");
        LUtils.d(TAG, "isLoginFirst ==========" + isLoginFirst);
        if (isLoginFirst == null) {
            PropertyHomeActivity p = (PropertyHomeActivity) getActivity();
            p.show();
            list_lookinformation.setFocusable(false);
            LUtils.d(TAG, "i list_lookinformation.setFocusable(false);");
        } else {
            list_lookinformation.setFocusable(true);
            if (mCurrentstart != 0) {
                loaddateInfoemation(mCurrentstart, mCurrentend);
            } else {
                loaddateInfoemation(DateUtils.stringToDate(mMMstart, "yyyy年MM月dd日 HH:mm:ss").getTime(), DateUtils.stringToDate(mMMend, "yyyy年MM月dd日 HH:mm:ss").getTime());
            }

            LUtils.d(TAG, "  onResume ---loaddateInfoemation");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void lookOnItemClicked(String sn, final String ur) {
        mAnimaition.start();//启动
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
        Call<Bean_userInfo> bean_userInfoCall = HttpManager.getInstance().getHttpClient().queryInfoUser(Contants.ACTIONID24, sn);
        bean_userInfoCall.enqueue(new Callback<Bean_userInfo>() {

            @Override
            public void onResponse(Call<Bean_userInfo> call, Response<Bean_userInfo> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
                        Bean_userInfo.ExtraBean extra = response.body().getExtra();

                        initPopupWindow(extra.getAddress(),
                                extra.getBirth(),
                                extra.getIdNumber(),
                                extra.getImage(),
                                extra.getName(),
                                extra.getNation(),
                                extra.getSex(), ur);
                    } else {
                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        mAnimaition.stop();
                        iamge_loaddate_anim.setVisibility(View.GONE);
                    }
                } else {
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Bean_userInfo> call, Throwable t) {
                mAnimaition.stop();
                iamge_loaddate_anim.setVisibility(View.GONE);

            }
        });
    }


    protected void initPopupWindow(String address, String birth, String idNumber, String image, String name, String nation, String sex, String u) {
        View popupWindowView = getLayoutInflater().inflate(R.layout.dailog_showpersional, null);
        //内容，高度，宽度
        mPopupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_property_home, null),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        final CircleImageView mUserhead = popupWindowView
                .findViewById(R.id.image_userhead);
        final CircleImageView mCardhead = popupWindowView
                .findViewById(R.id.image_cardhead);
        TextView btnCancel = popupWindowView.findViewById(R.id.informationbtn_cancel);
        TextView username = popupWindowView.findViewById(R.id.username);
        TextView usersex = popupWindowView.findViewById(R.id.usersex);
        TextView userminzu = popupWindowView.findViewById(R.id.userminzu);
        //        TextView userbrith = popupWindowView.findViewById(R.id.userbrith);
        //        TextView useraddress = popupWindowView.findViewById(R.id.useraddress);
        TextView useridcard = popupWindowView.findViewById(R.id.useridcard);
        username.setText(name);
        usersex.setText(sex);
        userminzu.setText(nation);
        //        userbrith.setText(birth);
        //        useraddress.setText(address);
        String str = "**********";
        StringBuilder sb = new StringBuilder(idNumber);
        sb.replace(6, 16, str);

        useridcard.setText(sb);
        Glide.with(getActivity()).load(image)
                .placeholder(R.mipmap.housedetal_uesrdefaultpic)
                .dontAnimate().into(mCardhead);
        Glide.with(getActivity()).load(u)
                .placeholder(R.mipmap.housedetal_uesrdefaultpic)
                .dontAnimate().into(mUserhead);
        mAnimaition.stop();
        iamge_loaddate_anim.setVisibility(View.GONE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                dialog.dismiss();// 隐藏dialog
                mPopupWindow.dismiss();
            }
        });


    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    class OlHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 7001:
                    Bundle data = msg.getData();
                    List<bean_Alllookinfor.InfosBean> infos = (List<bean_Alllookinfor.InfosBean>) data.getSerializable("Serializableinfos");
                    mAdapter1.setData(infos);
                    mAdapter1.notifyDataSetChanged();
                    // mDialog2.dismiss();
                    informationnull.setVisibility(View.GONE);
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    break;
                case 7002:
                    informationnull.setVisibility(View.VISIBLE);
                    mAdapter1.setData(null);
                    mAdapter1.notifyDataSetChanged();
                    //                    mDialog2.dismiss();
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    break;
            }
        }
    }


    private void initview(View view) {
        mSendinfosBean = (Bean_AllDevices.InfosBean) getArguments().getSerializable("sendinfosBean");
        mlly_setdate = view.findViewById(R.id.lly_setdate);
        mselectdate = view.findViewById(R.id.selectdate);
        mlist_date = view.findViewById(R.id.list_date);
        informationnull = view.findViewById(R.id.informationnull);
        iamge_loaddate_anim = view.findViewById(R.id.iamge_loaddate_anim);
        list_lookinformation = view.findViewById(R.id.list_lookinformation);
        mselectdate.setOnClickListener(this);

        Date initcurrentDates = DateUtils.longToDate(System.currentTimeMillis(), "yyyy年MM月");
        LUtils.d(TAG, "==initcurrentDates=" + initcurrentDates);
        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
        String month = Formatmonth.format(initcurrentDates);
        String year = Formatyear.format(initcurrentDates);
        LUtils.d(TAG, "==month=" + month);
        int day = DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month));
        LUtils.d(TAG, "==day=" + day);
        mIntegers = initlISTdate(day);
        // DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month))
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mlist_date.setLayoutManager(linearLayoutManager);

        mlly_setdate.setText(DateUtils.longToString(System.currentTimeMillis(), "yyyy年MM月"));
        LUtils.d(TAG, "----------------------" + DateUtils.longToString(System.currentTimeMillis(), "yyyy年MM"));
        String sdays = DateUtils.longToString(System.currentTimeMillis(), "dd");
        mAdapter = new Recycleview_FruitAdapter(getActivity(), sdays);
        mlist_date.setAdapter(mAdapter);

        mAdapter.setDatas(mIntegers);
        mAdapter.setOnItemClickLitener(this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        list_lookinformation.setLayoutManager(linearLayoutManager2);

        mAdapter1 = new LockingInfoRecyclerViewAdapter(getActivity());
        list_lookinformation.setAdapter(mAdapter1);

        smoothMoveToPosition(mlist_date, Integer.parseInt(sdays));
        mMMstart = mlly_setdate.getText().toString().trim() + String.valueOf(sdays) + "日 " + "00:00:01";
        mMMend = mlly_setdate.getText().toString().trim() + String.valueOf(sdays) + "日 " + " 23:59:59";
        mAdapter1.setOnItemClickedListener(this);

        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);
    }


    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            //            mToPosition = position;
            //            mShouldScroll = true;
        }
    }


    private void loaddateInfoemation(long starts, Long ends) {
        mAnimaition.start();//启动
        iamge_loaddate_anim.setVisibility(View.VISIBLE);

        Call<bean_Alllookinfor> querylookinformation = HttpManager.getInstance().getHttpClient().
                querylookinformation(Contants.ACTIONID16,
                        mSendinfosBean.getDevId(), starts, ends
                );
        LUtils.d(TAG, "1111111---------starts---11111111" + DateUtils.longToString(starts, "yyyy-MM-dd HH:mm:ss"));
        LUtils.d(TAG, "11111-----------ends--1111111111" + DateUtils.longToString(ends, "yyyy-MM-dd HH:mm:ss"));

        LUtils.d(TAG, "11111----------- mSendinfosBean.getDevId()--1111111111" + mSendinfosBean.getDevId());
        querylookinformation.enqueue(new Callback<bean_Alllookinfor>() {
            @Override
            public void onResponse(Call<bean_Alllookinfor> call, Response<bean_Alllookinfor> response) {
                if (response.body() != null) {
                    bean_Alllookinfor body = response.body();
                    List<bean_Alllookinfor.InfosBean> infos = body.getInfos();
                    LUtils.d(TAG, "111111111111111--size =" + infos.size());
                    if (infos.size() != 0) {
                        LUtils.d(TAG, "mAdapter1=" + mAdapter1);
                        //                        mAdapter1.setData(infos);
                        //                        mAdapter1.notifyDataSetChanged();
                        Message message = new Message();
                        message.what = 7001;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Serializableinfos", (Serializable) infos);
                        message.setData(bundle);
                        mHandler.sendMessageDelayed(message, 500);
                    } else {
                        mHandler.sendEmptyMessageDelayed(7002, 500);
                    }
                } else {
                    mHandler.sendEmptyMessageDelayed(7002, 500);
                }
            }

            @Override
            public void onFailure(Call<bean_Alllookinfor> call, Throwable t) {

                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(7002, 500);
            }
        });
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LockingInfoFragment newInstance(Bean_AllDevices.InfosBean sendinfosBean) {
        LockingInfoFragment fragment = new LockingInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable("sendinfosBean", sendinfosBean);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_setdate:

                break;

            case R.id.selectdate:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
        }
    }

    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        // dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(this);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
    }

    private List<Integer> initlISTdate(int day1) {
        List<Integer> numlist = new ArrayList<>();
        for (int i = 1; i <= day1; i++) {
            numlist.add(i);
        }
        return numlist;
    }


    @Override
    public void onItemClicks(View view, TextView textView, int position) {
        int days = (position + 1);
        String trim = mlly_setdate.getText().toString().trim();
        String MMstart = trim + String.valueOf(days) + "  00:00:01";
        String MMend = trim + String.valueOf(days) + "  23:59:59";
        //        mlly_setdate.setText();
        LUtils.d(TAG, "MMstart==" + MMstart);
        LUtils.d(TAG, "MMend==" + MMend);

        mCurrentstart = DateUtils.getStringToDate(MMstart, "yyyy年MM月dd HH:mm:ss");
        mCurrentend = DateUtils.getStringToDate(MMend, "yyyy年MM月dd HH:mm:ss");
        LUtils.d(TAG, "currentstart          " + mCurrentstart);
        LUtils.d(TAG, "currentend          " + mCurrentend);

        loaddateInfoemation(mCurrentstart, mCurrentend);
    }

    @Override
    public void onChanged(Date date) {

    }

    @Override
    public void onSure(Date date, int Viewtype) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        String format1 = format.format(date);
        mlly_setdate.setText(format1);

        SimpleDateFormat sureFormatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat sureFormatyear = new SimpleDateFormat("yyyy");
        String month = sureFormatmonth.format(date);
        String year = sureFormatyear.format(date);

        int day = DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month));
        mAdapter.setDatas(initlISTdate(day));


    }


}
