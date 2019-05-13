package com.example.musicworld;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.musicworld.activity.MusicAndServiceActivity;
import com.example.musicworld.adapter.CustomFragmentAdapter;
import com.example.musicworld.base.BaseActivity;
import com.example.musicworld.fragment.HomeFragment;
import com.example.musicworld.fragment.MusicFragment;
import com.example.musicworld.fragment.MyFragment;
import com.example.musicworld.service.MusicService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MusicMainActivity extends BaseActivity {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.img_player)
    ImageView img_player;
    @BindView(R.id.img_pause)
    ImageView img_pause;
    @BindView(R.id.tv_song)
    TextView tv_song;
    @BindView(R.id.tv_singer)
    TextView tv_singer;
    private boolean isNext;
    private String path;
    private String song;
    private String singer;
    private int position;
    private MyRecevier myRecevier = new MyRecevier();
    private List<Fragment> list = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_main_tab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("gyk", "isStop======" + MyApplication.isStop);
        if (MyApplication.isStop) {
            img_player.setVisibility(View.VISIBLE);
            img_pause.setVisibility(View.GONE);
        } else {
            img_player.setVisibility(View.GONE);
            img_pause.setVisibility(View.VISIBLE);
        }
        if (MyApplication.song == null && MyApplication.singer == null) {
            SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
            song = sp.getString("song", "error");
            singer = sp.getString("singer", "error");
            path = sp.getString("path", "error");
            position = sp.getInt("position", 0);
            Log.e("gyk", "position==" + position);
            MyApplication.seek = position;
            tv_song.setText(song);
            tv_singer.setText(singer);
        } else {
            tv_song.setText(MyApplication.song);
            tv_singer.setText(MyApplication.singer);
            MyApplication.seek = MyApplication.position;
        }
        Log.e("gyk", "pathMusic======" + path);
    }

    @Override
    public void initData() {
        tabLayout.setSelectedTabIndicatorHeight(0);
        list.add(new HomeFragment());
        list.add(new MusicFragment());
        list.add(new MyFragment());
        titleList.add("首页");
        titleList.add("音乐");
        titleList.add("我的");
        vp.setAdapter(new CustomFragmentAdapter(getSupportFragmentManager(), this, list, titleList));
        tabLayout.setupWithViewPager(vp);
        img_player.setVisibility(View.VISIBLE);
        img_pause.setVisibility(View.GONE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.musicworld.MUSICRECIVER");
        registerReceiver(myRecevier, intentFilter);
        Log.e("gyk", "onCreate");
        isNext = false;
    }

    @OnClick({R.id.img_player, R.id.img_pause, R.id.rl_bottom, R.id.img_next})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.img_player:
                img_player.setVisibility(View.GONE);
                img_pause.setVisibility(View.VISIBLE);
                if (MyApplication.song == null && MyApplication.singer == null) {
                    transService(true, false, MyApplication.song, MyApplication.singer, MyApplication.position, path);
                } else {
                    transService(true, true, MyApplication.song, MyApplication.singer, MyApplication.position);
                }

                break;
            case R.id.img_pause:
                img_pause.setVisibility(View.GONE);
                img_player.setVisibility(View.VISIBLE);
                transService(false, true, MyApplication.song, MyApplication.singer, MyApplication.position);
                break;
            case R.id.rl_bottom:
                Log.e("gyk", "pathMusic======" + path);
                if (MyApplication.song == null && MyApplication.singer == null) {
                    MusicAndServiceActivity.transMission(this, song,
                            singer, position, path);
                } else {
                    if (isNext) {
                        MusicAndServiceActivity.transMission(this, MyApplication.list.get(MyApplication.seek).getSong(),
                                MyApplication.list.get(MyApplication.seek).getSinger(),
                                MyApplication.seek, MyApplication.list.get(MyApplication.seek).getPath());
                    } else {
                        MusicAndServiceActivity.transMission(this, MyApplication.song,
                                MyApplication.singer, MyApplication.position);
                    }

                }

                break;
            case R.id.img_next:
                isNext = true;
                MyApplication.seek = MyApplication.seek + 1;
                tv_song.setText(MyApplication.list.get(MyApplication.seek).getSong());
                tv_singer.setText(MyApplication.list.get(MyApplication.seek).getSinger());
                transService(true,false, MyApplication.list.get(MyApplication.seek).getPath(), MyApplication.list.get(MyApplication.seek).getSong(), MyApplication.list.get(MyApplication.seek).getSinger(), MyApplication.seek);
                break;
        }
    }

    @Override
    public void addActivity() {
        MyApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onPause() {
        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("song", MyApplication.song);
        editor.putString("singer", MyApplication.singer);
        editor.putString("path", MyApplication.path);
        editor.putInt("position", MyApplication.position);
        editor.commit();
        Log.e("gyk", "onPause");
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(myRecevier);
        Log.e("gyk", "onDestroy==" + MyApplication.song);
        super.onDestroy();
    }

    //启动模式:开启服务
    private void transService(boolean isStart, boolean isClcik, String song, String singer, int position) {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("isStart", isStart);
        intent.putExtra("isClick", isClcik);
        intent.putExtra("song", song);
        intent.putExtra("singer", singer);
        intent.putExtra("position", position);
        startService(intent);
    }

    //启动模式:开启服务，上一首与下一首传值
    private void transService(boolean isNext,boolean isClick, String path, String song, String singer, int position) {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("isNext", isNext);
        intent.putExtra("isClick", isClick);
        intent.putExtra("path", path);
        intent.putExtra("song", song);
        intent.putExtra("singer", singer);
        intent.putExtra("position", position);
        startService(intent);
    }

    //启动模式:开启服务
    private void transService(boolean isStart, boolean isClcik, String song, String singer, int position, String path) {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("isStart", isStart);
        intent.putExtra("isClick", isClcik);
        intent.putExtra("song", song);
        intent.putExtra("singer", singer);
        intent.putExtra("position", position);
        intent.putExtra("path", path);
        startService(intent);
    }

    class MyRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MyApplication.isStop = intent.getBooleanExtra("isStop", false);
            boolean isStopp = intent.getBooleanExtra("isStop", false);
            String song = intent.getStringExtra("song");
            String singer = intent.getStringExtra("singer");
            tv_song.setText(song);
            tv_singer.setText(singer);
            Log.e("gyk","isStop------------"+isStopp);
            if (isStopp) {
                img_player.setVisibility(View.VISIBLE);
                img_pause.setVisibility(View.GONE);
            } else {
                img_player.setVisibility(View.GONE);
                img_pause.setVisibility(View.VISIBLE);
            }
        }
    }

}
