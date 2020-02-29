package com.estone.bank.estone_appsmartlock.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.PropertiesRecyclerViewAdapter;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.ui.binds.BindXMCameraActivity;
import com.estone.bank.estone_appsmartlock.ui.binds.BindYTCameraActivity;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.MyDialog;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.models.FunDevice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PropertiesFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, PropertiesRecyclerViewAdapter.OnRecLongClickListener {
    private static final String TAG = "PropertiesFragment";
    private RecyclerView recyclerView;
    private LinearLayout fragmentperp_nullmsg;
    private ImageView adddevices;
    private PropertiesRecyclerViewAdapter madapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Bean_AllDevices.InfosBean> mInfos;
    private Button button_adddevices;
    private Button button_refreshdevices;
    private PreperHandler mHandler;
    private TextView mTextViewdialog_cancel;
    private TextView mTextViewdialog_ok;
    private MyDialog mMyDialog;
    private String mDevId;
    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;
    private String mCameraId;
    private String[] mGender;
    private AlertDialog.Builder mBuilder1;

    @Override
    public void onRecLongClick(int position) {
        Bean_AllDevices.InfosBean infosBean = mInfos.get(position);
        mDevId = infosBean.getDevId();
        mCameraId = infosBean.getCameraId();

        View view = getLayoutInflater().inflate(R.layout.remove_dialog, null);
        mMyDialog = new MyDialog(getActivity(), 0, 0, view, R.style.DialogTheme);
        mMyDialog.setCancelable(true);
        mMyDialog.show();
        mTextViewdialog_cancel = view.findViewById(R.id.dialog_cancel);
        mTextViewdialog_ok = view.findViewById(R.id.dialog_ok);
        mTextViewdialog_cancel.setOnClickListener(this);
        mTextViewdialog_ok.setOnClickListener(this);
        //        delectitem(mDevId);

    }

    private FunDevice getFunDeviceFromSN(List<FunDevice> deviceList, String sn) {
        //        List<FunDevice> devices = FunSupport.getInstance().getDeviceList();
        LUtils.d(TAG, " List<FunDevice>==" + deviceList.size());
        for (FunDevice device : deviceList) {
            if (device.getDevSn().equals(sn)) {
                return device;
            }
        }
        return null;
    }

    private void delectitem(String devId) {
        Call<ResponsHead> bean_wx_xMloginCall = HttpManager.getInstance().getHttpClient().
                remooveDevices(Contants.ACTIONID12, devId);
        bean_wx_xMloginCall.enqueue(new Callback<ResponsHead>() {
            @Override
            public void onResponse(Call<ResponsHead> call, Response<ResponsHead> response) {
                if (response.body() != null) {
                    ResponsHead body = response.body();
                    if (body.getErrorCode() == 0) {
                        LUtils.d(TAG, "删除成功");
                        removeDev();
                        mHandler.removeMessages(3001);
                        mHandler.sendEmptyMessageDelayed(3001, 100);
                        madapter.notifyDataSetChanged();
                        ToastUtils.showToast(getActivity(), body.getErrorMsg());
                    } else {
                        ToastUtils.showToast(getActivity(), body.getErrorMsg());
                    }
                } else {
                    LUtils.d(TAG, "删除失败 (response.body() = null");
                }
            }

            @Override
            public void onFailure(Call<ResponsHead> call, Throwable t) {
                LUtils.d(TAG, "   onFailure 删除失败");
            }
        });
    }

    private void removeDev() {
        List<FunDevice> deviceList = FunSupport.getInstance().getDeviceList();
        if (deviceList != null) {
            FunDevice funDeviceFromSN = getFunDeviceFromSN(deviceList, mCameraId);
            if (funDeviceFromSN != null) {
                LUtils.d(TAG, "mCameraId===" + mCameraId);
                LUtils.d(TAG, "funDeviceFromSN===" + funDeviceFromSN);
                FunSupport.getInstance().requestDeviceRemove(funDeviceFromSN);
            }
        }
    }


    class PreperHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3001:
                    LoadAllDevicesDate();
                    break;
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties_list, container, false);
        recyclerView = view.findViewById(R.id.list);
        button_adddevices = view.findViewById(R.id.button_adddevices);
        button_refreshdevices = view.findViewById(R.id.button_refreshdevices);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);

        mHandler = new PreperHandler();
        iamge_loaddate_anim = view.findViewById(R.id.proiamge_loaddate_anim);
        fragmentperp_nullmsg = view.findViewById(R.id.fragmentperp_nullmsg);
        adddevices = view.findViewById(R.id.adddevices);
        adddevices.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        madapter = new PropertiesRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(madapter);

        LUtils.d(TAG, "------------onCreateView--------");
        //        madapter.setOnreflashListher(this);
        madapter.setOnRecLongClickListener(this);
        button_adddevices.setOnClickListener(this);
        button_refreshdevices.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);

        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);

        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        LUtils.d(TAG, "------------onDestroy--------");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LUtils.d(TAG, "------------onCreate--------");
    }

    @Override
    public void onStop() {
        super.onStop();
        LUtils.d(TAG, "------------onStop--------");
    }

    @Override
    public void onStart() {
        super.onStart();
        LUtils.d(TAG, "------------onStart--------");
        mHandler.removeMessages(3001);
        mHandler.sendEmptyMessageDelayed(3001, 100);

    }

    @Override
    public void onResume() {
        super.onResume();
        //        List<FunDevice> deviceList = FunSupport.getInstance().getDeviceList();
        //        for (int i = 0; i < deviceList.size(); i++) {
        //            FunDevice device = deviceList.get(i);
        //            String serialNo = device.getSerialNo();
        //            LUtils.d(TAG, "------------onResume--- serialNo  -----" + serialNo);
        //        }
        //        LUtils.d(TAG, "------------onResume--------" + deviceList.size());
        //        if (madapter != null && mInfos != null) {
        //            madapter.setData(mInfos);
        //            madapter.notifyDataSetChanged();
        //        }

    }

    //列出用户所有绑定设备（手机app调用）
    private void LoadAllDevicesDate() {

        mAnimaition.start();
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
        if (MyApplications.getRegistXMId() == null) {
            return;
        }
        Call<Bean_AllDevices> tiger =
                HttpManager.getInstance().getHttpClient().queryListHome(Contants.ACTIONID13, MyApplications.getRegistXMId());
        tiger.enqueue(new Callback<Bean_AllDevices>() {
            @Override
            public void onResponse(Call<Bean_AllDevices> call, Response<Bean_AllDevices> response) {
                if (response.body() != null) {
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    Bean_AllDevices body = response.body();
                    mInfos = body.getInfos();
                    LUtils.d(TAG, "mInfos.tostring=" + mInfos.toString());
                    if (recyclerView != null && mInfos.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        madapter.setData(mInfos);
                        madapter.notifyDataSetChanged();
                        fragmentperp_nullmsg.setVisibility(View.GONE);
                        LUtils.d(TAG, "    madapter.setData(mInfos);");
                    } else {
                        mAnimaition.stop();
                        iamge_loaddate_anim.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        madapter.setData(mInfos);
                        fragmentperp_nullmsg.setVisibility(View.VISIBLE);
                        button_refreshdevices.setVisibility(View.GONE);
                        button_adddevices.setVisibility(View.VISIBLE);
                        LUtils.d(TAG, "null != recyclerView");
                    }
                } else {
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    madapter.setData(mInfos);
                    recyclerView.setVisibility(View.GONE);
                    fragmentperp_nullmsg.setVisibility(View.VISIBLE);
                    button_adddevices.setVisibility(View.GONE);
                    button_refreshdevices.setVisibility(View.VISIBLE);
                    LUtils.d(TAG, "response.body() == null");
                }
            }

            @Override
            public void onFailure(Call<Bean_AllDevices> call, Throwable t) {
                mAnimaition.stop();
                iamge_loaddate_anim.setVisibility(View.GONE);
                LUtils.d(TAG, "onFailure       madapter.setData(mInfos) ");
            }
        });
    }


    public static PropertiesFragment newInstance() {
        PropertiesFragment fragment = new PropertiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adddevices:
                //

                tobinds();


                break;
            case R.id.dialog_cancel:
                mMyDialog.dismiss();
                break;
            case R.id.dialog_ok:
                delectitem(mDevId);
                int deviceListEX = FunSupport.getInstance().getDeviceListEX();//获取第三方设备列表
                LUtils.d(TAG, "------deviceListEX------设备数目是---" + deviceListEX);
                mMyDialog.dismiss();
                break;
            case R.id.button_adddevices:
//                Intent intent2 = new Intent(getActivity(), BindXMCameraActivity.class);
//                startActivity(intent2);

                tobinds();
                break;
            case R.id.button_refreshdevices:
                mHandler.removeMessages(3001);
                mHandler.sendEmptyMessageDelayed(3001, 100);
                LUtils.d(TAG, "  button_refreshdevices   ");
                break;
        }
    }

    private void tobinds() {
        mGender = new String[]{"XM摄像头", "YT摄像头B"};
        mBuilder1 = new AlertDialog.Builder(getContext());
        mBuilder1.setTitle("请选择绑定的摄像头");

        mBuilder1.setItems(mGender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  ToastUtil.showMsg(AlertDialogActivity.this,gender[which]);
                if ("XM摄像头".equals(mGender[which])) {
                    LUtils.d(TAG, "摄像头A-----------");
                    Intent intent = new Intent(getActivity(), BindXMCameraActivity.class);
                    startActivity(intent);
                } else if ("YT摄像头B".equals(mGender[which])) {
                    LUtils.d(TAG, "摄像头B-----------");
                    Intent intent = new Intent(getActivity(), BindYTCameraActivity.class);
                    startActivity(intent);
                }
            }
        });
        mBuilder1.show();
    }

    //    @Override
    //    public void refresh() {
    //        mHandler.removeMessages(3001);
    //        mHandler.sendEmptyMessageDelayed(3001, 100);
    //        madapter.notifyDataSetChanged();
    //        LUtils.d(TAG, "  refresh   ");
    //    }


    @Override
    public void onRefresh() {
        //刷新完成
        mHandler.removeMessages(3001);
        //        mHandler.sendEmptyMessageDelayed(3001,100);
        mSwipeRefreshLayout.setRefreshing(false);
        LUtils.d(TAG, "onRefresh（）   ");
    }
}

