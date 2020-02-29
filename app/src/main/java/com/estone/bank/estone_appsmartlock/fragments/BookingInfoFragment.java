package com.estone.bank.estone_appsmartlock.fragments;//package com.estone.bank.estone_appsmartlock.fragments;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.AnimationDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.estone.bank.estone_appsmartlock.R;
//import com.estone.bank.estone_appsmartlock.adapters.BookingInfoRecyclerViewAdapter;
//import com.estone.bank.estone_appsmartlock.apps.MyApplications;
//import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
//import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
//import com.estone.bank.estone_appsmartlock.https.beans.User_AllDevices;
//import com.estone.bank.estone_appsmartlock.managers.HttpManager;
//import com.estone.bank.estone_appsmartlock.ui.AddUserActivity;
//import com.estone.bank.estone_appsmartlock.ui.ChangeUserActivity;
//import com.estone.bank.estone_appsmartlock.utils.Contants;
//import com.estone.bank.estone_appsmartlock.utils.DateUtils;
//import com.estone.bank.estone_appsmartlock.utils.LUtils;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
///**
// * A fragment representing a list of Items.
// * <p/>
// * interface.
// */
//public class BookingInfoFragment extends Fragment implements View.OnClickListener, BookingInfoRecyclerViewAdapter.OnItemClickedListener, BookingInfoRecyclerViewAdapter.OnItemLongClickedListener, BookingInfoRecyclerViewAdapter.OnItemViewClickListener {
//    // TODO: Customize parameter argument names
//    private static final String TAG = "BookingInfoFragment";
//
//
//    private TextView textView_title;
//    private LinearLayout mbooking_nullmsg;
//
//    private RecyclerView mrecycle;
//    private List<User_AllDevices.InfosBean> mInfos;
//    private BookingInfoRecyclerViewAdapter mAdapter;
//    public static BookHandler mHandler;
//    private Bean_AllDevices.InfosBean mSendinfosBean;
//    private ImageView bookinfor_anim;
//    private AnimationDrawable mAnimaition;
//    //User_AllDevices.InfosBean
//    @Override
//    public void OnItemDeleteClicked(int position) {
//        mAnimaition.start();
//        bookinfor_anim.setVisibility(View.VISIBLE);
//        User_AllDevices.InfosBean infosBean = mInfos.get(position);
//        String devId = infosBean.getDevId();
//        Call<ResponsHead> removebookinformation =
//                HttpManager.getInstance().getHttpClient().removebookinformation(Contants.ACTIONID05, devId, infosBean.getIdNumber(),
//                        infosBean.getCheckInStart(), infosBean.getCheckInEnd());
//        removebookinformation.enqueue(new Callback<ResponsHead>() {
//
//            @Override
//            public void onResponse(Call<ResponsHead> call, Response<ResponsHead> response) {
//                if (response.body() != null) {
//                    if (response.body().getErrorCode() == 0 && response.body().getStatus().equals("true")) {
//                        mAnimaition.stop();
//                        bookinfor_anim.setVisibility(View.GONE);
//                        loadBookInformation();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponsHead> call, Throwable t) {
//                //                Toast.makeText(getContext(), R.string.delect_failed, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    public void OnItemEditClicked(int position) {
//        User_AllDevices.InfosBean infosBean = mInfos.get(position);
//        Intent intent = new Intent(getActivity(), ChangeUserActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("User_AllDevicesInfosBean", infosBean);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }
//
//    @Override
//    public void OnPhoneNumberClicked(String phoneNumber) {
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + phoneNumber));
//        startActivity(intent);
//    }
//
//    @Override
//    public void onItemlongClickedListeners(int position) {
//
//        if (mInfos.size() > position + 1) { //2--0
//            // 长按不是最后一条
//            LUtils.d(TAG, "onItemlongClickedListeners- mInfos.size()--" + mInfos.size());
//            LUtils.d(TAG, "onItemlongClickedListeners- mInfos.size()--" + position);
//            for (int i = 0; i < mInfos.size(); i++) {
//                // 循环 数据源 设置 状态
//                if (i == position) {
//                    //   点击的位置 == 当前的数据位置 ，则设置当前的数据位置+1的数据 长按为 true, 显示下一个item的 布局
//                    User_AllDevices.InfosBean infosBean = mInfos.get(position + 1);
//                    infosBean.setLongcheck(true);
//                }
//            }
//            mAdapter.notifyDataSetChanged();
//        } else {
//            // 长按是最后一条
//            LUtils.d(TAG, "onItemlongClickedListeners-else mInfos.size()--" + mInfos.size());
//            LUtils.d(TAG, "onItemlongClickedListeners-else mInfos.size()--" + position);
//            for (int j = 0; j < mInfos.size(); j++) {
//                // 循环 数据源 设置 状态
//                if (j == position) {
//                    //  点击的位置 == 当前的数据位置 ，则设置当前的数据位置 的数据 长按为 true, 显示下一个item的 布局
//                    User_AllDevices.InfosBean infosBean = mInfos.get(position);
//                    infosBean.setLast(true);
//                }
//
//            }
//            mAdapter.notifyDataSetChanged();
//
//        }
//    }
//
//    @Override
//    public void OnItemViewClick(int posstion) {
//        LUtils.d(TAG, "OnItemViewClick- mInfos.size()--" + mInfos.size());
//        LUtils.d(TAG, "OnItemViewClick--  posstion-" + posstion);
//
//        for (int i = 0; i < mInfos.size(); i++) {
//            User_AllDevices.InfosBean infosBean = mInfos.get(i);
//            infosBean.setLongcheck(false);
//            infosBean.setLast(false);
//        }
//        //        mAdapter.setData(mInfos);
//        mAdapter.notifyDataSetChanged();
//    }
//
//    class BookHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//
//            }
//        }
//    }
//
//    public static BookingInfoFragment newInstance(Bean_AllDevices.InfosBean sendinfosBean) {
//        BookingInfoFragment fragment = new BookingInfoFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("sendinfosBean", sendinfosBean);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        mHandler = new BookHandler();
//        final View view = inflater.inflate(R.layout.fragment_booking_info_list, container, false);
//        initview(view);
//
//        return view;
//    }
//
//    private void initview(View view) {
//        mbooking_nullmsg = view.findViewById(R.id.booking_nullmsg);
//
//        textView_title = view.findViewById(R.id.textView_title);
//        mrecycle = view.findViewById(R.id.recycle);
//        bookinfor_anim = view.findViewById(R.id.bookinfor_anim);
//        textView_title.setOnClickListener(this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        mrecycle.setLayoutManager(linearLayoutManager);
//        if (mAdapter == null) {
//            mAdapter = new BookingInfoRecyclerViewAdapter(getActivity());
//        }
//        mAdapter.setOnItemClickedListener(this);
//        mAdapter.setOnItemLongClickedListener(this);
//        mAdapter.setOnItemViewClickedListener(this);
//        mSendinfosBean = (Bean_AllDevices.InfosBean) getArguments().getSerializable("sendinfosBean");
//        LUtils.d(TAG, mSendinfosBean.getDevId() + "initview");
//        bookinfor_anim.setBackgroundResource(R.drawable.load_date_anim);
//        mAnimaition = (AnimationDrawable) bookinfor_anim.getBackground();
//        mAnimaition.setOneShot(false);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        loadBookInformation();
//    }
//
//    private void loadBookInformation() {
//        mAnimaition.start();
//        bookinfor_anim.setVisibility(View.VISIBLE);
//        Call<User_AllDevices> user_allDevicesCall =
//                HttpManager.getInstance().getHttpClient().queryUserdevice
//                        (Contants.ACTIONID14, MyApplications.getRegistXMId(), DateUtils.getDayZero(System.currentTimeMillis()),
//                                System.currentTimeMillis() + 2678400000L, mSendinfosBean.getDevId());
//        //        (Contants.ACTIONID14, MyApplications.getRegistXMId(),1546601759000L,1546774559000L,
//        //                 mSendinfosBean.getDevId());
//        LUtils.d(TAG, "loadBookInformation   getDevId==" + mSendinfosBean.getDevId());
//        user_allDevicesCall.enqueue(new Callback<User_AllDevices>() {
//            @Override
//            public void onResponse(Call<User_AllDevices> call, Response<User_AllDevices> response) {
//                if (response.body() != null) {
//                    User_AllDevices body = response.body();
//                    mInfos = body.getInfos();
//                    LUtils.d(TAG, "mInfos.size=" + mInfos.size());
//                    if (mInfos.size() > 0) {
//                        mAnimaition.stop();
//                        bookinfor_anim.setVisibility(View.GONE);
//                        mbooking_nullmsg.setVisibility(View.GONE);
//                        mAdapter.setData(mInfos);
//                        mrecycle.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//                    } else {      mAnimaition.stop();
//                        bookinfor_anim.setVisibility(View.GONE);
//                        mbooking_nullmsg.setVisibility(View.VISIBLE);
//                        mAdapter.setData(mInfos);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User_AllDevices> call, Throwable t) {
//                mAnimaition.stop();
//                bookinfor_anim.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.textView_title:
//
//                LUtils.d(TAG, mSendinfosBean.getDevId() + "onClick");
//                Intent intent = new Intent(getActivity(), AddUserActivity.class);
//                intent.putExtra("mSendinfosBeangetDevId", mSendinfosBean.getDevId());
//                startActivity(intent);
//
//                break;
//        }
//    }
//}
