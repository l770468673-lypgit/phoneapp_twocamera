package com.estone.bank.estone_appsmartlock.ui;//package com.estone.bank.estone_appsmartlock.ui;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.basic.G;
//import com.codbking.widget.DatePickDialog;
//import com.codbking.widget.OnChangeLisener;
//import com.codbking.widget.OnSureLisener;
//import com.codbking.widget.bean.DateType;
//import com.estone.bank.estone_appsmartlock.R;
//import com.estone.bank.estone_appsmartlock.adapters.Recy_PiclistitAdapter;
//import com.estone.bank.estone_appsmartlock.adapters.Recycleview_FruitAdapter;
//import com.estone.bank.estone_appsmartlock.utils.DateUtils;
//import com.estone.bank.estone_appsmartlock.utils.LUtils;
//import com.lib.Mps.XPMS_SEARCH_ALARMINFO_REQ;
//import com.lib.funsdk.support.FunAlarmNotification;
//import com.lib.funsdk.support.FunLog;
//import com.lib.funsdk.support.FunSupport;
//import com.lib.funsdk.support.OnFunDeviceAlarmListener;
//import com.lib.funsdk.support.config.AlarmInfo;
//import com.lib.funsdk.support.models.FunDevice;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import static com.estone.bank.estone_appsmartlock.utils.DiaUtils.hideWaitDialog;
//
//public class CameraActivityPictureList extends AppCompatActivity implements View.OnClickListener, OnChangeLisener, OnSureLisener,
//        Recycleview_FruitAdapter.OnRcyItemClickListener, OnFunDeviceAlarmListener, Recy_PiclistitAdapter.OnItemClickListener {
//
//    private static String TAG = "CameraActivityPictureList";
//    private ImageView mpiclist_activity_back;
//    private TextView mselectdate, mlly_setdate;
//    private Recycleview_FruitAdapter mAdapter;
//    private RecyclerView mlist_date;
//    private RecyclerView recyviewpiclist;
//    private LinearLayout rey_nullpiclist;
//    private String mFileType = null;
//
//    private List<Integer> mIntegers;
//    private FunDevice mFunDevice = null;
//
//    private Recy_PiclistitAdapter mPiclistitAdapteradapter;
//
//
//    private String mCurrDate = null;
//    /**
//     * 目标项是否在最后一个可见项之后
//     */
//    private boolean mShouldScroll;
//    /**
//     * 记录目标项位置
//     */
//    private int mToPosition;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera_picture_list);
//
//        initview();
//    }
//
//    private void initview() {
//
//        mpiclist_activity_back = findViewById(R.id.piclist_activity_back);
//        recyviewpiclist = findViewById(R.id.recyviewpiclist);
//        rey_nullpiclist = findViewById(R.id.rey_nullpiclist);
//
//        mselectdate = findViewById(R.id.selectdate);
//        mlly_setdate = findViewById(R.id.lly_setdate);
//        mlist_date = findViewById(R.id.list_date);
//        mIntegers = initlISTdate(30);
//
//        mselectdate.setOnClickListener(this);
//        mpiclist_activity_back.setOnClickListener(this);
//        mlly_setdate.setText(DateUtils.longToString(System.currentTimeMillis(), "yyyy-MM") + "");
//
//        initRecyviewmonth();
//        initRecyviepiclist();
//
//    }
//
//
//    /**
//     * 滑动到指定位置
//     */
//    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
//        // 第一个可见位置
//        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
//        // 最后一个可见位置
//        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
//        if (position < firstItem) {
//            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
//            mRecyclerView.smoothScrollToPosition(position);
//        } else if (position <= lastItem) {
//            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
//            int movePosition = position - firstItem;
//            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
//                int top = mRecyclerView.getChildAt(movePosition).getTop();
//                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
//                mRecyclerView.smoothScrollBy(0, top);
//            }
//        } else {
//            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
//            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
//            mRecyclerView.smoothScrollToPosition(position);
//            mToPosition = position;
//            mShouldScroll = true;
//        }
//    }
//
//    private void initRecyviepiclist() {
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayout.VERTICAL, false);
//        recyviewpiclist.setLayoutManager(gridLayoutManager);
//        //        recyviewpiclist.addItemDecoration(new GridLayoutDecoration());
//        mPiclistitAdapteradapter = new Recy_PiclistitAdapter(this);
//        recyviewpiclist.setAdapter(mPiclistitAdapteradapter);
//        int devId = getIntent().getIntExtra("FUN_DEVICE_ID", 0);
//        FunDevice funDevice = FunSupport.getInstance().findDeviceById(devId);
//        if (null == funDevice) {
//            finish();
//            return;
//        }
//        mFunDevice = funDevice;
//        LUtils.d(TAG, "mFunDevice==" + mFunDevice);
//        refreshAlarmNotificationEnable();
//        // 注册设备报警消息监听
//        FunSupport.getInstance().registerOnFunDeviceAlarmListener(this);
//        // 搜索当天的报警消息
//        trySearchAlarm(null);
//        mPiclistitAdapteradapter.setOnItemClickListener(this);
//    }
//
//    private void refreshAlarmNotificationEnable() {
//        FunAlarmNotification.getInstance().setDeviceAlarmNotification(
//                mFunDevice.getDevSn(), true);
//        boolean enable = FunAlarmNotification.getInstance().getDeviceAlarmNotification(mFunDevice.getDevSn());
//        //        mSwitchAlarmNotification.setSelected(enable);
//        // 保存设备报警通知状态(本地操作,不分设备类型)
//        LUtils.d(TAG, "enable ==" + enable);
//    }
//
//    private void initRecyviewmonth() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mlist_date.setLayoutManager(linearLayoutManager);
//        String s = DateUtils.longToString(System.currentTimeMillis(), "dd");
//        mAdapter = new Recycleview_FruitAdapter(this, s);
//        mlist_date.setAdapter(mAdapter);
//        mAdapter.setDatas(mIntegers);
//        mAdapter.setOnItemClickLitener(this);
//
//
//        smoothMoveToPosition(mlist_date,Integer.parseInt(s));
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.piclist_activity_back:
//                finish();
//                break;
//            case R.id.selectdate:
//                showDatePickDialog(DateType.TYPE_YMD);
//                break;
//        }
//    }
//
//    private void showDatePickDialog(DateType type) {
//        DatePickDialog dialog = new DatePickDialog(CameraActivityPictureList.this);
//        //设置上下年分限制
//        dialog.setYearLimt(5);
//        //设置标题
//        //dialog.setTitle("选择时间");
//        //设置类型
//        dialog.setType(type);
//        //设置消息体的显示格式，日期格式
//        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
//        //设置选择回调
//        dialog.setOnChangeLisener(this);
//        //设置点击确定按钮回调
//        dialog.setOnSureLisener(this);
//        dialog.show();
//    }
//
//
//    @Override
//    public void onChanged(Date date) {
//
//    }
//
//    @Override
//    public void onSure(Date date, int Viewtype) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
//        String format1 = format.format(date);
//        mlly_setdate.setText(format1);
//
//        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
//        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
//        String month = Formatmonth.format(date);
//        String year = Formatyear.format(date);
//        int day = DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month));
//        mAdapter.setDatas(initlISTdate(day));
//
//        FunLog.i("ActivityGuideDevicePictureList", date.toString());
//    }
//
//
//    private List<Integer> initlISTdate(int day1) {
//        List<Integer> numlist = new ArrayList<>();
//        for (int i = 1; i <= day1; i++) {
//            numlist.add(i);
//        }
//        return numlist;
//    }
//
//    @Override
//    protected void onDestroy() {
//        // 注销监听
//        FunSupport.getInstance().removeOnFunDeviceAlarmListener(this);
//        // 释放资源
//        if (null != mAdapter) {
//            mPiclistitAdapteradapter.release();
//        }
//        super.onDestroy();
//    }
//
//
//    @Override
//    public void onItemClicks(View view, TextView textView, int position) {
//        //        mlly_setdate.setBackgroundResource(R.drawable.circle_fill);
//        int days = (position + 1);
////        Toast.makeText(this, "" + days, Toast.LENGTH_SHORT).show();
//        String trim = mlly_setdate.getText().toString().trim() + "-";
//        String MMstart = trim + String.valueOf(days);
//        Date selectdate = DateUtils.stringToDate(MMstart, "yyyy-MM-dd");
//        LUtils.d(TAG, "selectdate====" + selectdate);
//        trySearchAlarm(selectdate);
//    }
//
//    private void initSearchInfo(XPMS_SEARCH_ALARMINFO_REQ info, Date date, int channel) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        info.st_02_StarTime.st_0_year = c.get(Calendar.YEAR);
//        info.st_02_StarTime.st_1_month = c.get(Calendar.MONTH) + 1;
//        info.st_02_StarTime.st_2_day = c.get(Calendar.DATE);
//        info.st_02_StarTime.st_4_hour = 0;
//        info.st_02_StarTime.st_5_minute = 0;
//        info.st_02_StarTime.st_6_second = 0;
//        info.st_03_EndTime.st_0_year = c.get(Calendar.YEAR);
//        info.st_03_EndTime.st_1_month = c.get(Calendar.MONTH) + 1;
//        info.st_03_EndTime.st_2_day = c.get(Calendar.DATE);
//        info.st_03_EndTime.st_4_hour = 23;
//        info.st_03_EndTime.st_5_minute = 59;
//        info.st_03_EndTime.st_6_second = 59;
//        // for test 05-10
//        //		info.st_02_StarTime.st_1_month = 5;
//        //		info.st_02_StarTime.st_2_day = 9;
//        //		info.st_03_EndTime.st_1_month = 5;
//        //		info.st_03_EndTime.st_2_day = 9;
//        info.st_04_Channel = channel;
//    }
//
//    private void trySearchAlarm(Date date) {
//        XPMS_SEARCH_ALARMINFO_REQ info = new XPMS_SEARCH_ALARMINFO_REQ();
//        G.SetValue(info.st_00_Uuid, mFunDevice.getDevSn());
//        if (null == date) {
//            date = new Date();
//        }
//        initSearchInfo(info, date, -1);
//        info.st_06_Number = 128;
//        mCurrDate = String.format("%04d-%02d-%02d",
//                info.st_02_StarTime.st_0_year,
//                info.st_02_StarTime.st_1_month,
//                info.st_02_StarTime.st_2_day);
//        //        mlly_setdate.setText(mCurrDate);
//
//        boolean b = FunSupport.getInstance().requestDeviceSearchAlarmInfo(mFunDevice, info);
//        Log.d(TAG, "trySearchAlarm : b==" + b);
//    }
//
//    @Override
//    public void onDeviceAlarmReceived(FunDevice funDevice) {
////        mFunDevice = funDevice;
//    }
//
//    @Override
//    public void onDeviceAlarmSearchSuccess(FunDevice funDevice, List<AlarmInfo> infos) {
//
//        LUtils.d(TAG, "onDeviceAlarmSearchSuccess()");
//        LUtils.d(TAG, "mPiclistitAdapteradapter()" + mPiclistitAdapteradapter);
//        LUtils.d(TAG, "infos.size()" + infos.size());
//        //        hideWaitDialog();
//        //		showToast(Integer.toString(infos.size()));
//        if (null != mPiclistitAdapteradapter && infos.size() != 0) {
//            // 临时处理,现在取到的报警消息有错误的情况,先在这里做二次过滤,BUG修复后可以注释掉
//            List<AlarmInfo> tmpInfos = new ArrayList<AlarmInfo>();
//            for (AlarmInfo alarmInfo : infos) {
//                LUtils.d(TAG, "alarmInfo.getDate() = [" + alarmInfo.getDate() + "] - [" + mCurrDate + "]");
//                if (alarmInfo.getDate().equals(mCurrDate)) {
//                    tmpInfos.add(alarmInfo);
//                    LUtils.d(TAG, "alarmInfo = [" +alarmInfo.toString());
//                }
//            }
//
//            if (tmpInfos.size() > 0) {
//                rey_nullpiclist.setVisibility(View.GONE);
//            }else {
//                rey_nullpiclist.setVisibility(View.VISIBLE);
//            }
//            mPiclistitAdapteradapter.updateAlarmInfos(tmpInfos);
//            mPiclistitAdapteradapter.notifyDataSetChanged();
//            LUtils.d(TAG, "updateAlarmInfos = " + tmpInfos.size());
//            LUtils.d(TAG, "mCurrDate = " + mCurrDate);
//
//        } else {
//            rey_nullpiclist.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onDeviceAlarmSearchFailed(FunDevice funDevice, int errCode) {
//        rey_nullpiclist.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onDeviceLanAlarmReceived(FunDevice funDevice, AlarmInfo alarmInfo) {
//
//    }
//
//
//    @Override
//    public void onItemClick(List<AlarmInfo> view, int postion) {
//        AlarmInfo alarmInfo = view.get(postion);
//        String pic = alarmInfo.getPic();
//
//        LUtils.d(TAG,"22222222222"+pic);
//    }
//}
