package com.estone.bank.estone_appsmartlock.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.DateUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserActivity extends BaseActivity implements View.OnClickListener, OnSureLisener {
    private static final String TAG = "AddUserActivity";
    private RelativeLayout inpendtimute, inpstarttimute, buttoncommit;
    private EditText addidnum, addphonenum, addusername;
    private TextView endtimute, starttimute;
    private Button buttoncommit2;
    private ImageView addkeyl_activity_back;
    private String mDevId = null;
    private String mMSendinfosBeangetDevId = null;

    private ImageView adduserload_anim;
    private AnimationDrawable mAnimaition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        mMSendinfosBeangetDevId = getIntent().getStringExtra("mSendinfosBeangetDevId");
        if (mMSendinfosBeangetDevId != null) {
            mDevId = mMSendinfosBeangetDevId;
            LUtils.d(TAG, "mMSendinfosBeangetDevId==" + mMSendinfosBeangetDevId);
        }
        initview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        dissKeyBoard();
    }

    private void initview() {
        inpendtimute = findViewById(R.id.inpendtimute);
        addkeyl_activity_back = findViewById(R.id.addkeyl_activity_back);
        inpstarttimute = findViewById(R.id.inpstarttimute);
        buttoncommit = findViewById(R.id.buttoncommit);
        endtimute = findViewById(R.id.endtimute);
        starttimute = findViewById(R.id.starttimute);
        buttoncommit2 = findViewById(R.id.buttoncommit2);
        adduserload_anim = findViewById(R.id.adduserload_anim);

        addidnum = findViewById(R.id.addidnum);
        addphonenum = findViewById(R.id.addphonenum);
        addusername = findViewById(R.id.addusername);

//        addidnum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                EditText ev = (EditText) v;
//                String hint = null;
//
//                if (hasFocus) {
//                    addidnum.setCursorVisible(true);
//                    addidnum.setFocusable(true);
//                    addidnum.setFocusableInTouchMode(true);
//                    addidnum.requestFocus();
//                    showKeyBoard(AddUserActivity.this, addidnum);
//                    hint = ev.getHint().toString();
//                    ev.setTag(hint);
//                    ev.setHint("");
//
//                } else {
//                    hint = ev.getTag().toString();
//                    ev.setHint(hint);
//                }
//
//
//            }
//        });
//        addphonenum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                EditText ev = (EditText) v;
//                String hint = null;
//                if (hasFocus) {
//                    addphonenum.setCursorVisible(true);
//                    addphonenum.setFocusable(true);
//                    addphonenum.setFocusableInTouchMode(true);
//                    addphonenum.requestFocus();
//                    showKeyBoard(AddUserActivity.this, addphonenum);
//                    hint = ev.getHint().toString();
//                    ev.setTag(hint);
//                    ev.setHint("");
//
//                } else {
//                    hint = ev.getTag().toString();
//                    ev.setHint(hint);
//                }
//
//
//            }
//        });
//        addusername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                EditText ev = (EditText) v;
//                String hint ;
//                if (hasFocus) {
//                    addusername.setCursorVisible(true);
//                    addusername.setFocusable(true);
//                    addusername.setFocusableInTouchMode(true);
//                    addusername.requestFocus();
//                    //                            addusername.setTextIsSelectable(true);
//                    showKeyBoard(AddUserActivity.this, addusername);
//                    hint = ev.getHint().toString();
//                    ev.setTag(hint);
//                    ev.setHint("");
//
//                } else {
//                    hint = ev.getTag().toString();
//                    ev.setHint(hint);
//                }
//            }
//
//        });

//        addidnum.addTextChangedListener(this);
//        addphonenum.addTextChangedListener(this);
//        addusername.addTextChangedListener(this);

        inpendtimute.setOnClickListener(this);
        inpstarttimute.setOnClickListener(this);
        buttoncommit.setOnClickListener(this);
        buttoncommit2.setOnClickListener(this);
        addkeyl_activity_back.setOnClickListener(this);
        adduserload_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) adduserload_anim.getBackground();
        mAnimaition.setOneShot(false);
    }


    public void checkEditText() {
        if ((addidnum.getText().toString().trim().length() > 0) &&
                (addphonenum.getText().toString().trim().length() > 10) &&
                (addusername.getText().toString().trim().length() > 17)) {
//            dissKeyBoard();
        }
        LUtils.d(TAG, "addidnum.getText().toString().trim().length()==" + addidnum.getText().toString().trim().length());
        LUtils.d(TAG, "addphonenum.getText().toString().trim().length()==" + addphonenum.getText().toString().trim().length());
        LUtils.d(TAG, "addusername.getText().toString().trim().length()==" + addusername.getText().toString().trim().length());
    }

//    private void showKeyBoard(Context context, EditText addphonenum) {
//        if (context == null) return;
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(addphonenum, InputMethodManager.SHOW_FORCED);
//    }

//    private void dissKeyBoard() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//    }

//    protected void hideInput() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        View v = getWindow().peekDecorView();
//        if (null != v) {
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inpendtimute:
                showDatePickDialog(DateType.TYPE_HM, 2);
//                dissKeyBoard();
                break;
            case R.id.inpstarttimute:
                showDatePickDialog(DateType.TYPE_HM, 1);
//                dissKeyBoard();
                break;
            case R.id.buttoncommit2:
//                dissKeyBoard();
                try {
                    commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.addkeyl_activity_back:
//                dissKeyBoard();
                finish();
                break;
        }

    }

    private void commit() {

        String tendtime = endtimute.getText().toString().trim();
        String tstarttimute = starttimute.getText().toString().trim();
        String trimidnum = addidnum.getText().toString().trim();
        String trimphonenum = addphonenum.getText().toString().trim();
        String trimusernum = addusername.getText().toString().trim();
        if (trimusernum.length() > 0 && tstarttimute.length() > 2 && tendtime.length() > 2
                && trimidnum.length() == 18 && trimphonenum.length() == 11) {
            long endtime = DateUtils.dateToStamp(tendtime);
            long starttime = DateUtils.dateToStamp(tstarttimute);

            mAnimaition.start();
            adduserload_anim.setVisibility(View.VISIBLE);
            Call<ResponsHead> responsHeadCall = HttpManager.getInstance().getHttpClient().addBookinformation(Contants.ACTIONID01,
                    MyApplications.getRegistXMId(),
                    mDevId,
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
                            Toast.makeText(AddUserActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            mAnimaition.stop();
                            adduserload_anim.setVisibility(View.GONE);
                            finish();
                        } else {
                            mAnimaition.stop();
                            adduserload_anim.setVisibility(View.GONE);
                            Toast.makeText(AddUserActivity.this, body.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        mAnimaition.stop();
                        adduserload_anim.setVisibility(View.GONE);
                        Toast.makeText(AddUserActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsHead> call, Throwable t) {
                    mAnimaition.stop();
                    adduserload_anim.setVisibility(View.GONE);
                    Toast.makeText(AddUserActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddUserActivity.this, "请检查参数长度是否正确", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickDialog(DateType type, int viewtype) {
        DatePickDialog dialog = new DatePickDialog(this);
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


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
//
    //    @Override
    //    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    //
    //    }
    //
    //    @Override
    //    public void onTextChanged(CharSequence s, int start, int before, int count) {
    ////        checkEditText();
    //        LUtils.d(TAG, "onTextChanged");
    //    }
    //
    //    @Override
    //    public void afterTextChanged(Editable s) {
    ////        checkEditText();
    ////        LUtils.d(TAG, "afterTextChanged");
    //    }
}
