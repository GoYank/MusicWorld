package com.example.musicworld.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.adapter
 * 文件名：CustomFragmentAdapter
 * 创建者：Gyk
 * 创建时间：2019/5/7 14:56
 * 描述：  TODO
 */

public class CustomFragmentAdapter extends FragmentPagerAdapter {

    Context context;
    List<Fragment> list = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    public CustomFragmentAdapter(FragmentManager fm, Context mContext, List<Fragment> mList, List<String> mTitleList) {
        super(fm);
        this.context = mContext;
        this.list = mList;
        this.titleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
