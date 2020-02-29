package com.estone.bank.estone_appsmartlock.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnChangeLisener;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.adapters.RecyVideoAdapter;
import com.estone.bank.estone_appsmartlock.adapters.Recycleview_FruitAdapter;
import com.estone.bank.estone_appsmartlock.base.BaseActivity;
import com.estone.bank.estone_appsmartlock.utils.DateUtils;
import com.estone.bank.estone_appsmartlock.utils.LUtils;
import com.lib.funsdk.support.FunLog;
import com.lib.funsdk.support.FunPath;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class CameraVideoActivity extends BaseActivity implements Recycleview_FruitAdapter.OnRcyItemClickListener, View.OnClickListener, OnChangeLisener, OnSureLisener {
    private String TAG = "CameraVideoActivity";

    private RecyclerView mlist_date;
    private ImageView picmylist_activity_back;
    private Recycleview_FruitAdapter mAdapter;
    private List<Integer> mIntegers;
    private TextView mselectdate, mlly_setdate;
    private LinearLayout rey_nullpiclist;
    private RecyclerView recyviewpiclist;
    ProgressDialog mDialog2;
    /**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;
    private List<String> mImagePathFromSD;
    private String mCurrentDate;
    private String mPicDic; //  文件路径
    private RecyVideoAdapter mAdapter1;
    private String mReplace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_pic);
        mDialog2 = ProgressDialog.show(this, "提醒", "正在登陆中");
        mDialog2.setCancelable(true);
        initPiocDate();
        initview();
        initRecyScooldate();

        initRecyviewmonth();
        initReclcleDate();

    }

    private void initReclcleDate() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayout.VERTICAL, false);
        recyviewpiclist.setLayoutManager(gridLayoutManager);
        mAdapter1 = new RecyVideoAdapter(this,mPicDic);
        recyviewpiclist.setAdapter(mAdapter1);

        List<String> list = currentDatePic(mCurrentDate); // 当前时间的 文件
        if (list.size() > 0) {
            rey_nullpiclist.setVisibility(View.GONE);
        } else {
            rey_nullpiclist.setVisibility(View.VISIBLE);
        }

        Vector<String> stack = new Vector<String>();
        for (int i = list.size()-1; i >=0; i--) {
            String s = list.get(i );
            LUtils.d(TAG, "s ==" + i + "===" + s);
            stack.add(s);
        }
        mDialog2.dismiss();
        mAdapter1.setDate(stack);
        mAdapter1.notifyDataSetChanged();
    }

    private List<String> currentDatePic(String Date) {
        List<String> piclist = new ArrayList<>();
        for (int i = 0; i < mImagePathFromSD.size(); i++) {
            String replace = mImagePathFromSD.get(i).replace(mPicDic, "");
            LUtils.d(TAG, "replace ==" + replace);
            //201901181338451547789925902.jpg
            String subyearMD = replace.substring(0, 8);
            LUtils.d(TAG, "视频日期 subyearMD ==" + subyearMD);
            LUtils.d(TAG, "选择日期  Date ==" + Date);
            if (subyearMD.equals(Date)) {
                piclist.add(mImagePathFromSD.get(i));
            }

        }

        return piclist;
    }

    private void initview() {
        mlist_date = findViewById(R.id.list_date);
        mselectdate = findViewById(R.id.selectdate);
        rey_nullpiclist = findViewById(R.id.rey_nullpiclist);
        recyviewpiclist = findViewById(R.id.recyviewpiclist);
        picmylist_activity_back = findViewById(R.id.picmylist_back);
        mlly_setdate = findViewById(R.id.lly_setdate);
        mlly_setdate.setText(DateUtils.longToString(System.currentTimeMillis(), "yyyy-MM") + "");
        picmylist_activity_back.setOnClickListener(this);
        mselectdate.setOnClickListener(this);
        mCurrentDate = DateUtils.longToString(System.currentTimeMillis(), "yyyyMMdd");
        LUtils.d(TAG, "mdate==" + mCurrentDate);
    }

    private void initRecyviewmonth() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mlist_date.setLayoutManager(linearLayoutManager);
        String s = DateUtils.longToString(System.currentTimeMillis(), "dd");
        mAdapter = new Recycleview_FruitAdapter(this, s);
        mlist_date.setAdapter(mAdapter);
        mAdapter.setDatas(mIntegers);
        mAdapter.setOnItemClickLitener(this);
        smoothMoveToPosition(mlist_date, Integer.parseInt(s));

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
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    public void initPiocDate() {
        mDialog2.show();
        String image_local_video = getIntent().getStringExtra("IMAGE_LOCAL_VIDEO");

        //        mPicDic = FunPath.PATH_VIDEO + File.separator;
        mPicDic = image_local_video;
        LUtils.d(TAG, "pic url ==" + mPicDic);

        mImagePathFromSD = getImagePathFromSD(mPicDic);
        LUtils.d(TAG, "pmImagePathFromSD  ==" + mImagePathFromSD.size());
        for (int i = 0; i < mImagePathFromSD.size(); i++) {
            LUtils.d(TAG, "mImagePathFromSD==" + mImagePathFromSD.get(i));
        }

    }

    private void initRecyScooldate() {
        Date yyyydate = DateUtils.longToDate(System.currentTimeMillis(), "yyyy年MM月");
        LUtils.d(TAG, "==yyyydate=" + yyyydate);
        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
        String month = Formatmonth.format(yyyydate);
        String year = Formatyear.format(yyyydate);
        LUtils.d(TAG, "当前时间  ==month=" + month);
        int day = DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month));
        LUtils.d(TAG, "当前时间==day=" + day);
        mIntegers = initlISTdate(day);
    }

    private List<Integer> initlISTdate(int day1) {
        List<Integer> numlist = new ArrayList<>();
        for (int i = 1; i <= day1; i++) {
            numlist.add(i);
        }
        return numlist;
    }


    /**
     * 从sd卡获取图片资源
     */
    private List<String> getImagePathFromSD(String filePath) {
        // 图片列表
        List<String> imagePathList = new ArrayList<>();

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("mp4") || FileEnd.equals("3gp")|| FileEnd.equals("avi")
            //                || FileEnd.equals("gif")
            //                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")
                ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    @Override
    public void onItemClicks(View view, TextView textView, int position) {
        String MMstart;
        mDialog2.show();
        int days = (position + 1);
        String trim = mlly_setdate.getText().toString().trim();
        if (days > 9) {
            MMstart = trim + String.valueOf(days);
            LUtils.d(TAG, "MMstart====" + MMstart);
        } else {
            MMstart = trim + "0" + String.valueOf(days);
        }
        LUtils.d(TAG, "MMstart====" + MMstart);
        if (MMstart.contains("-")) {
            mReplace = MMstart.replace("-", "");
        } else if (MMstart.contains("年")) {
            String replace1 = MMstart.replace("年", "");
            mReplace = replace1.replace("月", "");
        }
        LUtils.d(TAG, " 选择日期 mReplace====" + mReplace);
        List<String> list = currentDatePic(mReplace);
        if (list.size() > 0) {
            rey_nullpiclist.setVisibility(View.GONE);
        } else {
            rey_nullpiclist.setVisibility(View.VISIBLE);
        }
        Vector<String> stack = new Vector<String>();
        for (int i = list.size()-1; i >=0; i--) {
            String s = list.get(i );
            LUtils.d(TAG, "s ==" + i + "===" + s);
            stack.add(s);
        }
        mDialog2.dismiss();
        mAdapter1.setDate(stack);
        mAdapter1.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picmylist_back:
                finish();
                break;

            case R.id.selectdate:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
        }
    }

    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(CameraVideoActivity.this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        //dialog.setTitle("选择时间");
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
    public void onChanged(Date date) {

    }

    @Override
    public void onSure(Date date, int Viewtype) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        String format1 = format.format(date);
        mlly_setdate.setText(format1);

        SimpleDateFormat Formatmonth = new SimpleDateFormat("MM");
        SimpleDateFormat Formatyear = new SimpleDateFormat("yyyy");
        String month = Formatmonth.format(date);
        String year = Formatyear.format(date);
        int day = DateUtils.getDay(Integer.parseInt(year), Integer.parseInt(month));
        mAdapter.setDatas(initlISTdate(day));
        LUtils.i("ActivityGuideDevicePictureList", date.toString());

    }
}
