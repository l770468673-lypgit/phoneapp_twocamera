package com.estone.bank.estone_appsmartlock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estone.bank.estone_appsmartlock.R;
import com.estone.bank.estone_appsmartlock.ui.binds.BindXMCameraActivity;
import com.lib.funsdk.support.models.FunDevice;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterFunDevice extends BaseAdapter {

    private BindXMCameraActivity mActivity;
    private List<FunDevice> mListDevs;
    private int litviewchange = -1;


    public void changeListItemColor(int posit) {
        if (posit != litviewchange) {
            litviewchange = posit;
            notifyDataSetChanged();
        }
    }

    public ListAdapterFunDevice(BindXMCameraActivity bindCameraActivity) {
        this.mActivity = bindCameraActivity;
        this.mListDevs = new ArrayList<>();
    }


    public void setDateDevs(List<FunDevice> listDevs) {

        if (listDevs.size() > 0) {
            this.mListDevs = listDevs;
            this.notifyDataSetChanged();

        }

    }

    @Override
    public int getCount() {
        return mListDevs.size();
    }

    @Override
    public Object getItem(int position) {
        return mListDevs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHodler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.simpledeviceslist_item, parent, false);
            viewHodler = new ViewHolder(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder) convertView.getTag();
        }

        if (mListDevs.size() != 0) {
            FunDevice integer = mListDevs.get(position);
            viewHodler.devicessetnum.setText(integer.getSerialNo() + "");
            viewHodler.deviceip.setText(integer.getDevIP() + "");
            //viewHodler.textzhiliansuo.setText(integer.getDevType() + "");
            if (litviewchange == position) {
                convertView.setBackgroundResource(R.drawable.listview_item_select);
            } else {
                convertView.setBackgroundResource(R.drawable.deviceslistbackground);

            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView devicessetnum;
        TextView deviceip;
        //        TextView textzhiliansuo;

        public ViewHolder(View convertView) {
            devicessetnum = (TextView) convertView.findViewById(R.id.devicessetnum);
            deviceip = (TextView) convertView.findViewById(R.id.deviceip);
            //            textzhiliansuo = (TextView) convertView.findViewById(R.id.textzhiliansuo);
        }
    }
}
