package com.estone.bank.estone_appsmartlock.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnChangeLisener;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.DeviceCameraPicAdapter;
import com.estone.bank.estone_appsmartlock.adapters.Recycleview_FruitAdapter;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.utils.DateUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lib.SDKCONST;
import com.lib.funsdk.support.FunPath;
import com.lib.funsdk.support.FunSupport;
import com.lib.funsdk.support.OnFunDeviceFileListener;
import com.lib.funsdk.support.OnFunDeviceOptListener;
import com.lib.funsdk.support.config.OPCompressPic;
import com.lib.funsdk.support.models.FunDevice;
import com.lib.funsdk.support.models.FunFileData;
import com.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.sdk.struct.H264_DVR_FINDINFO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityGuideDeviceRecordList extends BaseActivity implements View.OnClickListener
        //        , OnFunDeviceRecordListener
        , OnFunDeviceOptListener
        , OnFunDeviceFileListener
        , OnChangeLisener
        , OnSureLisener
        , Recycleview_FruitAdapter.OnRcyItemClickListener, DeviceCameraPicAdapter.OnPictureOnclickLitener {
    private static String TAG = "ActivityGuideDeviceRecordList";

    private FunDevice mFunDevice;
    private Calendar calendar;
    private boolean byFile = true;
    private DeviceCameraPicAdapter mRecordByFileAdapter;
    private TextView mTextTitle;
    private ImageView mBtnBack;
    private RecyclerView mRecordList;
    private RecyclerView list_date;
    private LinearLayout rey_nullvideolist;
    private final int MESSAGE_REFRESH_PROGRESS = 0x100;
    private final int MESSAGE_SET_IMAGE = 0x102;
    private RecodeHandler mHandler;
    private TextView selectdate, lly_setdate;
    private List<Integer> mIntegers;
    private Recycleview_FruitAdapter mMAdapter;
    private int mYear;
    private int mMonth;
    private int mDays;

    /**
     * 目标项是否在最后一个可见项之后
     */
    //    private boolean mShouldScroll;

    /**
     * 记录目标项位置
     */
    //    private int mToPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_device_record_list);
        mHandler = new RecodeHandler();
        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
        FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
        if (devId == 0) {
            funDevice = FunSupport.getInstance().mCurrDevice;
        }
        if (null == funDevice) {
            finish();
            return;
        } else {
            mFunDevice = funDevice;
        }
        calendar = Calendar.getInstance();
        initview();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayout.VERTICAL, false);
        mRecordList.setLayoutManager(gridLayoutManager);

        // 1. 注册录像文件搜索结果监听 - 在搜索完成后以回调的方式返回
        //        FunSupport.getInstance().registerOnFunDeviceRecordListener(this);
        FunSupport.getInstance().registerOnFunDeviceOptListener(this);

    }

    private void initview() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mTextTitle = findViewById(R.id.textViewInTopLayout);
        rey_nullvideolist = findViewById(R.id.rey_nullvideolist);
        mBtnBack = findViewById(R.id.backBtnInTopLayout);
        mRecordList = findViewById(R.id.lv_records);
        list_date = findViewById(R.id.list_date);
        lly_setdate = findViewById(R.id.lly_setdate);
        selectdate = findViewById(R.id.selectdate);
        mBtnBack.setOnClickListener(this);
        mTextTitle.setText(sdf.format(calendar.getTime()));
        lly_setdate.setText(DateUtils.longToString(System.currentTimeMillis(), "yyyy年MM月") + "");
        selectdate.setOnClickListener(this);


        initRecyScooldate();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_date.setLayoutManager(linearLayoutManager);
        String s = DateUtils.longToString(System.currentTimeMillis(), "dd");
        mMAdapter = new Recycleview_FruitAdapter(this, s);

        list_date.setAdapter(mMAdapter);
        mMAdapter.setDatas(mIntegers);
        mMAdapter.setOnItemClickLitener(this);
        mRecordByFileAdapter = new DeviceCameraPicAdapter(this, mRecordList, mFunDevice);
        mRecordList.setAdapter(mRecordByFileAdapter);
        Date datemm = DateUtils.longToDate(System.currentTimeMillis(), "yyyy年MM月dd日");

        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat Formatday = new SimpleDateFormat("dd");
        mYear = Integer.parseInt(Formatyear.format(datemm));
        mMonth = Integer.parseInt(Formatmonth.format(datemm));
        mDays = Integer.parseInt(Formatday.format(datemm));
        onSearchFile(mYear, mMonth, mDays);

        mRecordByFileAdapter.setOnPIcClickListener(this);
        LUtils.d(TAG, "initview" + mYear + "===" + mMonth + "===" + mDays);
        smoothMoveToPosition(list_date, Integer.parseInt(s));
    }

    private void initRecyScooldate() {
        Date yyyydate = DateUtils.longToDate(System.currentTimeMillis(), "yyyy年MM月");
        LUtils.d(TAG, "==yyyydate=" + yyyydate);
        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
        String month = Formatmonth.format(yyyydate);
        String year = Formatyear.format(yyyydate);
        LUtils.d(TAG, "==month=" + month);
        int day = DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month));
        LUtils.d(TAG, "==day=" + day);
        mIntegers = initlISTdate(day);
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


    private List<Integer> initlISTdate(int day1) {
        List<Integer> numlist = new ArrayList<>();
        for (int i = 1; i <= day1; i++) {
            numlist.add(i);
        }
        return numlist;
    }

    @Override
    public void onChanged(Date date) {

    }

    @Override
    public void onSure(Date date, int Viewtype) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        String format1 = format.format(date);
        lly_setdate.setText(format1);

        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
        mYear = Integer.parseInt(Formatyear.format(date));
        mMonth = Integer.parseInt(Formatmonth.format(date));
        int days = DateUtils.getDay(mYear, mMonth);
        mMAdapter.setDatas(initlISTdate(days));

        LUtils.d(TAG, "year" + mYear);
        LUtils.d(TAG, "month" + mMonth);
        LUtils.d(TAG, "day" + days);
        LUtils.d(TAG, "onSure" + mYear + "===" + mMonth + "===" + days);
    }

    @Override
    public void onItemClicks(View view, TextView textView, int position) {
        int days = (position + 1);
        onSearchFile(mYear, mMonth, days);
        LUtils.d(TAG, "onItemClicks" + mYear + "===" + mMonth + "===" + days);

    }

    @Override
    public void onImageClick(FunFileData info, int position) {

    }

    class RecodeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_REFRESH_PROGRESS: {
                    resetProgressInterval();
                }
                break;

                case MESSAGE_SET_IMAGE: {
                    if (mRecordByFileAdapter != null) {
                        mRecordByFileAdapter.setBitmapTempPath((String) msg.obj);
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        // 5. 退出注销监听
        //        FunSupport.getInstance().removeOnFunDeviceRecordListener(this);
        FunSupport.getInstance().removeOnFunDeviceOptListener(this);

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();

    }

    private void onSearchFile(int year, int month, int days) {

        LUtils.d(TAG, "YEAR" + calendar.get(Calendar.YEAR));
        LUtils.d(TAG, "MONTH" + calendar.get(Calendar.MONTH));
        LUtils.d(TAG, "DATE" + calendar.get(Calendar.DATE));

        int time[] = {year, month, days};
        if (byFile) {
            H264_DVR_FINDINFO info = new H264_DVR_FINDINFO();
            info.st_1_nFileType = SDKCONST.FileType.SDK_RECORD_ALL;
            info.st_2_startTime.st_0_dwYear = time[0];
            info.st_2_startTime.st_1_dwMonth = time[1];
            info.st_2_startTime.st_2_dwDay = time[2];
            info.st_2_startTime.st_3_dwHour = 0;
            info.st_2_startTime.st_4_dwMinute = 0;
            info.st_2_startTime.st_5_dwSecond = 0;
            info.st_3_endTime.st_0_dwYear = time[0];
            info.st_3_endTime.st_1_dwMonth = time[1];
            info.st_3_endTime.st_2_dwDay = time[2];
            info.st_3_endTime.st_3_dwHour = 23;
            info.st_3_endTime.st_4_dwMinute = 59;
            info.st_3_endTime.st_5_dwSecond = 59;
            info.st_0_nChannelN0 = mFunDevice.CurrChannel;
            FunSupport.getInstance().requestDeviceFileList(mFunDevice, info);
        }

    }


    private void resetProgressInterval() {
        if (null != mHandler) {
            mHandler.removeMessages(MESSAGE_REFRESH_PROGRESS);
            mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_PROGRESS, 500);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                // 返回/退出
                finish();
                break;
            case R.id.selectdate:
                // 返回/退出
                showDatePickDialog(DateType.TYPE_YMD);
                break;

        }
    }


    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        //        dialog.setTitle("选择时间");
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

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {
        List<FunFileData> files = new ArrayList<>();
        if (null != funDevice && null != mFunDevice && funDevice.getId() == mFunDevice.getId()) {
            for (H264_DVR_FILE_DATA data : datas) {
                FunFileData funFileData = new FunFileData(data, new OPCompressPic());
                files.add(funFileData);
            }
            if (files.size() == 0) {
                //                ToastUtils.showToast(this, R.string.device_camera_video_list_empty);
                rey_nullvideolist.setVisibility(View.VISIBLE);
                if (mRecordByFileAdapter != null) {
                    mRecordByFileAdapter.setDatas(files);
                    mRecordByFileAdapter.notifyDataSetChanged();
                    mRecordByFileAdapter.release();
                }
            } else {
                rey_nullvideolist.setVisibility(View.GONE);

                mRecordByFileAdapter.setDatas(files);
                if (mRecordByFileAdapter != null) {
                    mRecordByFileAdapter.notifyDataSetChanged();
                    mRecordByFileAdapter.release();
                }

            }
        } else {
            mRecordByFileAdapter.notifyDataSetChanged();
            mRecordByFileAdapter.release();
            rey_nullvideolist.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {
    }


    @Override
    public void onDeviceLoginSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {
        onSearchFile(mYear, mMonth, mDays);
        LUtils.d(TAG, "onDeviceGetConfigSuccess" + mYear + "===" + mMonth + "===" + mDays);
    }

    @Override
    public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
        mRecordByFileAdapter.notifyDataSetChanged();
        mRecordByFileAdapter.release();
    }

    @Override
    public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {

    }

    @Override
    public void onDeviceSetConfigFailed(FunDevice funDevice, String configName, Integer errCode) {

    }

    @Override
    public void onDeviceChangeInfoSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {
        mRecordByFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeviceOptionSuccess(FunDevice funDevice, String option) {

    }

    @Override
    public void onDeviceOptionFailed(FunDevice funDevice, String option, Integer errCode) {
        mRecordByFileAdapter.notifyDataSetChanged();
        mRecordByFileAdapter.release();
    }

    @Override
    public void onDeviceFileListGetFailed(FunDevice funDevice) {
        // TODO Auto-generated method stub
        mRecordByFileAdapter.notifyDataSetChanged();
        mRecordByFileAdapter.release();
    }

    @Override
    public void onDeviceFileDownCompleted(FunDevice funDevice, String path, int nSeq) {
        if (path != null) {
            Toast.makeText(this, "Download Complete!!!" + "Path:" + path, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Download Complete!!!" + "Path:" + FunPath.PATH_VIDEO, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeviceFileDownProgress(int totalSize, int progress, int nSeq) {

    }

    @Override
    public void onDeviceFileDownStart(boolean isStartSuccess, int nSeq) {
        if (isStartSuccess) {
            Toast.makeText(this, "Download Start!!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Download Faile!!!", Toast.LENGTH_LONG).show();
        }
    }

}