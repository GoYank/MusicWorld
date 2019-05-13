package com.example.musicworld.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.musicworld.R;

import butterknife.ButterKnife;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.base
 * 文件名：BaseActivity
 * 创建者：Gyk
 * 创建时间：2019/4/29 9:25
 * 描述：  TODO
 */

public abstract class BaseActivity extends AppCompatActivity {
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
        ButterKnife.bind(this);
        initData();
        addActivity();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void addActivity();

    public void toastMsg(String text) {
        if (text != null && !text.equals("") && text.length() > 0) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
