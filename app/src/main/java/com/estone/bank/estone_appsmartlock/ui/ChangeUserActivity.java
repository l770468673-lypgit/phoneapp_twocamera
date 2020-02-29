package com.estone.bank.estone_appsmartlock.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.fragments.EditUserChangeRoom;
import com.estone.bank.estone_appsmartlock.https.beans.User_AllDevices2;

public class ChangeUserActivity extends BaseActivity implements View.OnClickListener
           , RadioGroup.OnCheckedChangeListener
{
    private static final String TAG = "ChangeUserActivity";

    private ImageView addkeyl_activity_back;
    private User_AllDevices2.InfosBean mUser_allDevicesInfosBean = null;


    private FrameLayout medit_userfameLayout;

    /**
     * 暂时注销，100 台之后，在放开
     * private RadioGroup medit_radiogrop;
     * //    private RadioButton mradiobtn_editroom;
     * //    private RadioButton mradiobtn_beforeroom;
     * //    private RadioButton mradiobtn_delroom;
     *
     * @param savedInstanceState
     */
        private RadioGroup medit_radiogrop;
        private RadioButton mradiobtn_editroom;
        private RadioButton mradiobtn_beforeroom;
        private RadioButton mradiobtn_delroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeuseractivity);

        mUser_allDevicesInfosBean = (User_AllDevices2.InfosBean) getIntent().getSerializableExtra("User_AllDevices2InfosBean");

        initview();
    }

    private void initview() {
        medit_userfameLayout = findViewById(R.id.edit_userfameLayout);//------------
                medit_radiogrop = findViewById(R.id.edit_radiogrop);//==============
                mradiobtn_editroom = findViewById(R.id.radiobtn_editroom);//------------
                mradiobtn_delroom = findViewById(R.id.radiobtn_delroom);//--------
                mradiobtn_beforeroom = findViewById(R.id.radiobtn_beforeroom);//------------
        addkeyl_activity_back = findViewById(R.id.addkeyl_activity_back);//============
                medit_radiogrop.setOnCheckedChangeListener(this);
                medit_radiogrop.getChildAt(0).performClick();//模拟点击第一个RB
        addkeyl_activity_back.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.edit_userfameLayout, EditUserChangeRoom.newInstance(mUser_allDevicesInfosBean, 1)).commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addkeyl_activity_back:
                finish();
                break;
        }
    }


        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radiobtn_editroom) {
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_userfameLayout, EditUserChangeRoom.newInstance(mUser_allDevicesInfosBean, 1)).commit();
                mradiobtn_editroom.setBackgroundResource(R.drawable.radiobutton_select_select);
                mradiobtn_editroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radioselect));
                mradiobtn_beforeroom.setBackgroundResource(R.drawable.radiobutton_select_default);
                mradiobtn_beforeroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radiodefault));
                mradiobtn_delroom.setBackgroundResource(R.drawable.radiobutton_select_default);
                mradiobtn_delroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radiodefault));
            }
            if (checkedId == R.id.radiobtn_beforeroom) {
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_userfameLayout, EditUserChangeRoom.newInstance(mUser_allDevicesInfosBean, 2)).commit();
                mradiobtn_beforeroom.setBackgroundResource(R.drawable.radiobutton_select_select);
                mradiobtn_beforeroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radioselect));
                mradiobtn_editroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radiodefault));
                mradiobtn_editroom.setBackgroundResource(R.drawable.radiobutton_select_default);
                mradiobtn_delroom.setBackgroundResource(R.drawable.radiobutton_select_default);
                mradiobtn_delroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radiodefault));
            }
            if (checkedId == R.id.radiobtn_delroom) {
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_userfameLayout, EditUserChangeRoom.newInstance(mUser_allDevicesInfosBean, 3)).commit();
                mradiobtn_delroom.setBackgroundResource(R.drawable.radiobutton_select_select);
                mradiobtn_delroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radioselect));
                mradiobtn_editroom.setBackgroundResource(R.drawable.radiobutton_select_default);
                mradiobtn_editroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radiodefault));
                mradiobtn_beforeroom.setBackgroundResource(R.drawable.radiobutton_select_default);
                mradiobtn_beforeroom.setTextColor(
                        getResources().getColor(R.color.btn_proper_radiodefault));
            }
        }
}
