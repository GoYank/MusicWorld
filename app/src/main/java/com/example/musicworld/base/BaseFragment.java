package com.example.musicworld.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.base
 * 文件名：BaseFragment
 * 创建者：Gyk
 * 创建时间：2019/4/29 10:51
 * 描述：  TODO
 */

public abstract class BaseFragment extends Fragment {
    public Context context;
    View view;
    Unbinder unbinder;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = initView();
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;

    }

    public abstract View initView();

    public abstract void initData();
}
