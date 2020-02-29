package com.estone.bank.estone_appsmartlock.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zhouxinliang on 18-7-2.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    public ViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);

    }

    public void setList(ArrayList<Fragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }


}
