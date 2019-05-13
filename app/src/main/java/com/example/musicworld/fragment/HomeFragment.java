package com.example.musicworld.fragment;

import android.view.View;

import com.example.musicworld.R;
import com.example.musicworld.base.BaseFragment;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.fragment
 * 文件名：HomeFragment
 * 创建者：Gyk
 * 创建时间：2019/4/29 13:50
 * 描述：  TODO
 */

public class HomeFragment extends BaseFragment {
    @Override
    public View initView() {
        return View.inflate(context, R.layout.fragment_home,null);
    }

    @Override
    public void initData() {

    }
}
