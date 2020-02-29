package com.estone.bank.estone_appsmartlock.ui;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.https.beans.ChangeRoomName;
import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
import com.estone.bank.estone_appsmartlock.managers.HttpManager;
import com.estone.bank.estone_appsmartlock.utils.Contants;
import com.estone.bank.estone_appsmartlock.utils.LUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingRoomId extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final static String TAG = "SettingRoomId";

    private ImageView setting_room_back;
    private TextView settingok;
    private EditText settingroomname;
    private ImageView resetroomname, setting_loaddate_anim;
    private String mEdit_houseid;
    private String mEdit_lock;
    private AnimationDrawable mAnimaition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_room_id);
        String housename = getIntent().getStringExtra("housename");
        mEdit_houseid = getIntent().getStringExtra("edit_houseid");
        mEdit_lock = getIntent().getStringExtra("edit_lock");
        LUtils.d(TAG, "mEdit_houseid===" + mEdit_houseid);
        LUtils.d(TAG, "mEdit_lock===" + mEdit_lock);
        LUtils.d(TAG, "housename===" + housename);
        initView(housename);
    }

    private void initView(String housename) {
        settingok = findViewById(R.id.settingok);
        setting_room_back = findViewById(R.id.setting_room_back);
        resetroomname = findViewById(R.id.resetroomname);
        settingroomname = findViewById(R.id.settingroomname);

        setting_loaddate_anim = findViewById(R.id.setting_loaddate_anim);
        settingroomname.setText(housename);
        settingok.setTextColor(getResources().getColor(R.color.a3ac7b7));

        setting_room_back.setOnClickListener(this);
        settingok.setOnClickListener(this);
        resetroomname.setOnClickListener(this);

        settingroomname.addTextChangedListener(this);

        setting_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) setting_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);

    }

    public void changeRoomNames() {
        mAnimaition.start();//启动
        setting_loaddate_anim.setVisibility(View.VISIBLE);
        Call<ChangeRoomName> responsHeadCall = HttpManager.getInstance().getHttpClient().
                changeRoomName(Contants.ACTIONID91, mEdit_lock, mEdit_houseid, settingroomname.getText().toString().trim());
        responsHeadCall.enqueue(new Callback<ChangeRoomName>() {
            @Override
            public void onResponse(Call<ChangeRoomName> call, Response<ChangeRoomName> response) {
                ChangeRoomName body = response.body();
                if (body != null) {
                    mAnimaition.stop();//启动
                    setting_loaddate_anim.setVisibility(View.GONE);
                    int errorCode = body.getErrorCode();
                    if (errorCode == 0) {
                        finish();
                        Toast.makeText(SettingRoomId.this, body.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mAnimaition.stop();//启动
                    setting_loaddate_anim.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ChangeRoomName> call, Throwable t) {
                mAnimaition.stop();//启动
                setting_loaddate_anim.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_room_back:
                finish();
                break;
            case R.id.settingok:
                changeRoomNames();
                //                Toast.makeText(SettingRoomId.this, "OK", Toast.LENGTH_SHORT).show();
                break;
            case R.id.resetroomname:
                settingroomname.setText("");
                settingok.setTextColor(getResources().getColor(R.color.b3efe8));
                settingok.setClickable(false);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // 输入前的监听
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 输入的内容变化的监听
        if (s.toString().length() < 1) {
            settingok.setClickable(false);
            settingok.setTextColor(getResources().getColor(R.color.b3efe8));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // 输入后的监听
        String s1 = s.toString();
        if (s1.length() > 0) {
            settingok.setTextColor(getResources().getColor(R.color.a3ac7b7));
            settingok.setClickable(true);
        } else {
            settingok.setClickable(false);
            settingok.setTextColor(getResources().getColor(R.color.b3efe8));
        }
    }
}