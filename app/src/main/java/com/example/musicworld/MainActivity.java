package com.example.musicworld;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.musicworld.base.BaseActivity;
import com.example.musicworld.fragment.HomeFragment;
import com.example.musicworld.fragment.MusicFragment;
import com.example.musicworld.fragment.MyFragment;
import com.example.musicworld.util.BottomNavigationViewHelper;
import com.example.musicworld.util.ScanMusicUtil;

import java.lang.reflect.Field;

import butterknife.BindView;

public class MainActivity extends BaseActivity  {

    @BindView(R.id.flAll)
    FrameLayout flAll;
    @BindView(R.id.bntv)
    BottomNavigationView bntv;
    private HomeFragment homeFragment;
    private MusicFragment musicFragment;
    private MyFragment myFragment;


    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initData() {
        selectedFragment(0);
//        BottomNavigationViewHelper.disableShiftMode(bntv);
        bntv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        selectedFragment(0);
                        break;
                    case R.id.music:
                        selectedFragment(1);
                        break;
                    case R.id.my:
                        selectedFragment(2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }



    @Override
    public void addActivity() {
        MyApplication.getInstance().addActivity(this);
    }

    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.flAll, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (musicFragment == null) {
                    musicFragment = new MusicFragment();
                    transaction.add(R.id.flAll, musicFragment);
                } else {
                    transaction.show(musicFragment);
                }
                break;
            case 2:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.flAll, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (musicFragment != null) {
            transaction.hide(musicFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

}
