package com.estone.bank.estone_appsmartlock.adapters;//package com.estone.bank.estone_appsmartlock.adapters;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Message;
//import android.support.constraint.ConstraintLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.estone.bank.estone_appsmartlock.R;
//import com.estone.bank.estone_appsmartlock.utils.LUtils;
//import com.example.download.PictureDownload;
//import com.lib.funsdk.support.config.AlarmInfo;
//import com.lib.funsdk.support.utils.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Recy_PiclistitAdapter extends RecyclerView.Adapter<Recy_PiclistitAdapter.ViewHolder> implements PictureDownload.OnPictureDownloadListener {
//
//
//    private static final String TAG = "Recy_PiclistitAdapter";
//    private List<AlarmInfo> mListAlarmInfo;// = new ArrayList<AlarmInfo>();
//
//    private PictureDownload mPicDownload = null;
//
//
//    private List<ViewHolder> mViewHolders = new ArrayList<ViewHolder>();
//    private Context mContext = null;
//    private final int MESSAGE_PICTURE_DOWNLOADED = 0x100;
//
//    private OnItemClickListener mClickListener;// 声明自定义的接口
//
//    public Recy_PiclistitAdapter(CameraActivityPictureList cameraActivityPictureList) {
//        this.mContext = cameraActivityPictureList;
//        this.mListAlarmInfo = new ArrayList<>();
//        // 同时3个线程下载
//
//        mPicDownload = new PictureDownload(3);
//        mPicDownload.setOnPictureDownloadListener(this);
//
//    }
//    private android.os.Handler mHandler = new android.os.Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch(msg.what) {
//                case MESSAGE_PICTURE_DOWNLOADED:
//                {
//                    int position = msg.arg1;
//                    ViewHolder vh = findViewHolder(position);
//                    if ( null != vh ) {
//                        setImageViewPicture(vh.piclist_itemlist,
//                                position, null, (Bitmap)msg.obj);
//                    }
//                }
//                break;
//            }
//        }
//
//    };
//
//    private ViewHolder findViewHolder(int position) {
//        for ( int i = 0; i < mViewHolders.size(); i ++ ) {
//            if ( mViewHolders.get(i).position == position ) {
//                return mViewHolders.get(i);
//            }
//        }
//        return null;
//    }
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mClickListener = listener;
//    }
//
//    @Override
//    public void onPictureDownload(Integer position, Bitmap bmp) {
//        	if ( null != mHandler ) {
//			Message msg = new Message();
//			msg.what = MESSAGE_PICTURE_DOWNLOADED;
//			msg.arg1 = position;
//			msg.obj = bmp;
//			mHandler.sendMessage(msg);
//		}
//	}
//
//
//    public interface OnItemClickListener {
//        public void onItemClick(List<AlarmInfo> view, int postion);
//    }
//
//    @Override
//    public Recy_PiclistitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_cameralist_picitem, parent, false);
//        return new ViewHolder(view);
//
//    }
//    public void release() {
//        if ( null != mPicDownload ) {
//            mPicDownload.release();
//            mPicDownload = null;
//        }
//        mViewHolders.clear();
//    }
//    @Override
//    public void onBindViewHolder(Recy_PiclistitAdapter.ViewHolder holder, final int position) {
//        final AlarmInfo alarmInfo = mListAlarmInfo.get(position);
//        LUtils.d(TAG, "-----alarmInfo.getPic()--------" + alarmInfo.getPic());
//        if ( StringUtils.isStringNULL(alarmInfo.getPic()) ) {
//            // 图片地址是有效的
//            setImageViewPicture(holder.piclist_itemlist,
//                    position, alarmInfo.getPic(), null);
//            LUtils.d(TAG, "-----onBindViewHolder)--   // 图片地址是有效的------" );
//        }else {
//            LUtils.d(TAG, "-----onBindViewHolder)-  // 图片地址null的-------");
//        }
//
//        holder.todays.setText(alarmInfo.getTime());
//        holder.constlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mClickListener.onItemClick(mListAlarmInfo, position);
//            }
//        });
//
//    }
//
//    private void setImageViewPicture(ImageView iv, int position,
//                                     String picUrl, Bitmap bmp) {
//        if ( null == bmp && null != picUrl ) {
//            bmp = mPicDownload.get(position, picUrl);
//        }
//        LUtils.d(TAG, "bmp===" + bmp);
//        if ( null != bmp ) {
//            iv.setImageBitmap(bmp);
//            LUtils.d(TAG, "bmp=!!=null ="  );
//        } else {
//            // 显示默认图片
//            iv.setImageResource(R.drawable.icon_device);
//            LUtils.d(TAG, "bmp=!!=null ="  );
//            // 添加图片下载
//            mPicDownload.add(position, picUrl);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mListAlarmInfo.size();
//    }
//
//    public void updateAlarmInfos(List<AlarmInfo> devList) {
//        //        mListAlarmInfo.clear();
//        //        mListAlarmInfo.addAll(devList);
//        mListAlarmInfo = devList;
//        this.notifyDataSetChanged();
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView piclist_itemlist;
//        private TextView todays;
//        private ConstraintLayout constlayout;
//        int position = -1;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            piclist_itemlist = itemView.findViewById(R.id.piclist_itemlist);
//            constlayout = itemView.findViewById(R.id.constlayout);
//
//            todays = itemView.findViewById(R.id.todays);
//
//
//            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//            int screenWidth = dm.widthPixels;
//            int screenHeight = dm.heightPixels;
//            LUtils.d(TAG, "width===" + screenWidth);
//            LUtils.d(TAG, "height===" + screenHeight);
//
//            // 视频窗口全屏显示
//            GridLayoutManager.LayoutParams lpWnd = (GridLayoutManager.LayoutParams) constlayout.getLayoutParams();
//            lpWnd.width = screenWidth / 2 - 40;
//            lpWnd.height = screenWidth / 2 - 40;
//            constlayout.setLayoutParams(lpWnd);
//
//        }
//    }
//}
