package com.estone.bank.estone_appsmartlock.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.apps.MyApplications;
import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
import com.estone.bank.estone_appsmartlock.https.beans.User_AllDevices2;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.DateUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.estone.bank.estone_appsmartlock.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserChangeRoom extends Fragment implements View.OnClickListener, OnSureLisener {
    private static final String TAG = "EditUserChangeRoom";


    private RelativeLayout inpendtimute, inpstarttimute, buttoncommit;
    private EditText addidnum, addphonenum, addusername;
    private TextView endtimute, starttimute;
    private Button buttoncommit2;
    private ImageView bookinforedit_anim;
    private AnimationDrawable mAnimaition;
    private User_AllDevices2.InfosBean mUser_allDevicesInfosBean;
    private int mMNumPosit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.edituserchangeroom, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mUser_allDevicesInfosBean = (User_AllDevices2.InfosBean) getArguments().getSerializable("user_allDevicesInfosBean");
        mMNumPosit = getArguments().getInt("intnumber", 1);
        inpstarttimute = (view).findViewById(R.id.inpstarttimute);
        buttoncommit = (view).findViewById(R.id.buttoncommit);
        endtimute = (view).findViewById(R.id.endtimute);
        starttimute = (view).findViewById(R.id.starttimute);
        inpendtimute = (view).findViewById(R.id.inpendtimute);
        buttoncommit2 = (view).findViewById(R.id.buttoncommit2);
        bookinforedit_anim = (view).findViewById(R.id.bookinforedit_anim);//---------
        addidnum = (view).findViewById(R.id.changeidnum);
        addphonenum = (view).findViewById(R.id.changephonenum);
        addusername = (view).findViewById(R.id.changeusername);

        inpendtimute.setOnClickListener(this);
        inpstarttimute.setOnClickListener(this);
        buttoncommit2.setOnClickListener(this);

        bookinforedit_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) bookinforedit_anim.getBackground();
        mAnimaition.setOneShot(false);

        addusername.setText(mUser_allDevicesInfosBean.getReserveName());
        starttimute.setText(DateUtils.longToString(mUser_allDevicesInfosBean.getCheckInStart(), "yyyy-MM-dd HH:mm:ss"));
        endtimute.setText(DateUtils.longToString(mUser_allDevicesInfosBean.getCheckInEnd(), "yyyy-MM-dd HH:mm:ss"));
        addidnum.setText(mUser_allDevicesInfosBean.getIdNumber());
        addphonenum.setText(mUser_allDevicesInfosBean.getMobile());


        if (mMNumPosit == 1) {

        } else if (mMNumPosit == 2) {
            addusername.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            addphonenum.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            addidnum.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            starttimute.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            addusername.setEnabled(false);
            addusername.setClickable(false);

            addphonenum.setEnabled(false);
            addphonenum.setClickable(false);

            inpstarttimute.setEnabled(false);
            inpstarttimute.setClickable(false);

            addidnum.setEnabled(false);
            addidnum.setClickable(false);

            starttimute.setEnabled(false);
            starttimute.setClickable(false);


        } else if (mMNumPosit == 3) {
            addusername.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            addphonenum.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            addidnum.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            starttimute.setTextColor(getActivity().getResources().getColor(R.color.c6c6c6));
            addusername.setEnabled(false);
            addusername.setClickable(false);

            addphonenum.setEnabled(false);
            addphonenum.setClickable(false);

            addidnum.setEnabled(false);
            addidnum.setClickable(false);
            inpstarttimute.setEnabled(false);
            inpstarttimute.setClickable(false);
            starttimute.setEnabled(false);
            starttimute.setClickable(false);
        }


    }

    public static EditUserChangeRoom newInstance(User_AllDevices2.InfosBean user_allDevicesInfosBean, int inum) {
        EditUserChangeRoom fragment = new EditUserChangeRoom();
        Bundle args = new Bundle();
        args.putSerializable("user_allDevicesInfosBean", user_allDevicesInfosBean);
        args.putInt("intnumber", inum);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inpendtimute:
                showDatePickDialog(DateType.TYPE_HM, 2);

                break;
            case R.id.inpstarttimute:
                showDatePickDialog(DateType.TYPE_HM, 1);
                break;
            //case R.id.buttoncommit:
            case R.id.buttoncommit2:

                if (mMNumPosit == 1) {
                    commit();
                } else if (mMNumPosit == 2) {
                    beHeadCheckOut(String.valueOf(30));
                } else if (mMNumPosit == 3) {
                    beHeadCheckOut(String.valueOf(31));
                }

                break;

        }
    }

    private void beHeadCheckOut(String contant) {

        if (endtimute.getText().toString() == null) {
            return;
        }
        long endtime = DateUtils.dateToStamp(endtimute.getText().toString());
        Call<ResponsHead> checkOut = HttpManager.getInstance().getHttpClient().checkOut(contant, mUser_allDevicesInfosBean.getDevId()
                , mUser_allDevicesInfosBean.getIdNumber(), endtime);
        checkOut.enqueue(new Callback<ResponsHead>() {
            @Override
            public void onResponse(Call<ResponsHead> call, Response<ResponsHead> response) {
                ResponsHead body = response.body();
                if (body != null) {
                    int errorCode = body.getErrorCode();
                    if (errorCode == 0) {
                        getActivity().finish();
                        Toast.makeText(getActivity(), body.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + body.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "" + body.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponsHead> call, Throwable t) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void commit() {
        Call<ResponsHead> removebookinformation =
                HttpManager.getInstance().getHttpClient().removebookinformation(Contants.ACTIONID05,
                        mUser_allDevicesInfosBean.getDevId(),
                        mUser_allDevicesInfosBean.getIdNumber(),
                        mUser_allDevicesInfosBean.getCheckInStart(),
                        mUser_allDevicesInfosBean.getCheckInEnd());
        removebookinformation.enqueue(new Callback<ResponsHead>() {
            @Override
            public void onResponse(Call<ResponsHead> call, Response<ResponsHead> response) {
                if (response.body() != null) {
                    if (response.body().getErrorCode() == 0) {
                        addBookInfortion();
                    } else {
                        ToastUtils.showToast(getActivity(), response.body().getErrorMsg());
                    }
                } else {
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsHead> call, Throwable t) {
                ToastUtils.showToast(getActivity(), "请求失败");
            }
        });


    }

    private void addBookInfortion() {
        mAnimaition.start();
        bookinforedit_anim.setVisibility(View.VISIBLE);
        long endtime = DateUtils.dateToStamp(endtimute.getText().toString());
        long starttime = DateUtils.dateToStamp(starttimute.getText().toString());
        String trimidnum = addidnum.getText().toString().trim();
        String trimphonenum = addphonenum.getText().toString().trim();
        String trimusernum = addusername.getText().toString().trim();

        Call<ResponsHead> responsHeadCall = HttpManager.getInstance().getHttpClient().addBookinformation(Contants.ACTIONID01,
                MyApplications.getRegistXMId(),
                mUser_allDevicesInfosBean.getDevId(),
                trimidnum,
                starttime,
                endtime,
                trimphonenum,
                trimusernum,
                "");
        responsHeadCall.enqueue(new Callback<ResponsHead>() {

            @Override
            public void onResponse(Call<ResponsHead> call, Response<ResponsHead> response) {
                if (response.body() != null) {
                    ResponsHead body = response.body();
                    if (body.getErrorCode() == 0) {
                        mAnimaition.stop();
                        bookinforedit_anim.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else {
                        mAnimaition.stop();
                        bookinforedit_anim.setVisibility(View.GONE);
                        LUtils.d(TAG, "---------body.getErrorCode()--+" + body.getErrorCode());
                        Toast.makeText(getActivity(), body.getErrorCode() + body.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LUtils.d(TAG, "---------body.getErrorCode()--+" + response.body());
                    mAnimaition.stop();
                    bookinforedit_anim.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "请求失败" + response.body().getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsHead> call, Throwable t) {
                mAnimaition.stop();
                bookinforedit_anim.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDatePickDialog(DateType type, int viewtype) {
        DatePickDialog dialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        //        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        dialog.setViewType(viewtype);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(this);
        dialog.show();
    }

    @Override
    public void onSure(Date date, int Viewtype) {
        LUtils.d(TAG, "Viewtype==" + Viewtype);
        if (Viewtype == 1) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String format1 = format.format(date);
            starttimute.setText(format1);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String format1 = format.format(date);
            endtimute.setText(format1);

        }
    }
}
