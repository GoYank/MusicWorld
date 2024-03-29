package com.example.musicworld.activity;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicworld.MyApplication;
import com.example.musicworld.R;
import com.example.musicworld.base.BaseActivity;
import com.example.musicworld.service.MusicService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class MusicPlayerActivity extends BaseActivity {

    @BindView(R.id.tv_song)
    TextView tv_song;
    @BindView(R.id.tv_singer)
    TextView tv_singer;
    @BindView(R.id.tv_origin)
    TextView tv_origin;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.sb)
    SeekBar sb;
    @BindView(R.id.img_player)
    ImageView img_player;
    @BindView(R.id.img_pause)
    ImageView img_pause;
    @BindView(R.id.img_back)
    ImageView img_back;
    private String song;
    private String singer;
    private String path;
    private String length;
    private int position;
    private int duration;
    private MediaPlayer mediaPlayer;
    private boolean isStop;
    public static final String POSITION = "MusicPlayerActivity";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sb.setProgress(msg.what);
            tv_origin.setText(formatTime(msg.what));
        }
    };

    public static void transMission(Context context, String song, String singer, String path, String length, int duration, int position) {
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        intent.putExtra(POSITION, song);
        intent.putExtra(POSITION + 1, singer);
        intent.putExtra(POSITION + 2, path);
        intent.putExtra(POSITION + 3, length);
        intent.putExtra(POSITION + 4, duration);
        intent.putExtra(POSITION + 5, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_music_player);
    }

    @Override
    public void initData() {
        song = getIntent().getStringExtra(POSITION);
        singer = getIntent().getStringExtra(POSITION + 1);
        path = getIntent().getStringExtra(POSITION + 2);
        length = getIntent().getStringExtra(POSITION + 3);
        duration = getIntent().getIntExtra(POSITION + 4, 0);
        position = getIntent().getIntExtra(POSITION + 5, 0);
        MyApplication.seek = position;
        setData(song, singer, duration);
        play(path);
        updatePlayer();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }




    private void setData(String song, String singer, int duration) {
        tv_song.setText(song);
        tv_singer.setText(singer);
        tv_total.setText(formaTime(duration));
        sb.setMax(duration);
    }


    /**
     * 切换歌曲逻辑：拿到列表的list，假设当前歌曲位置为position,
     * 点击下一首直接播放list.get(position+1)
     * 点击上一首直接播放list.get(position-1)
     * 需要用到mediaplayer.reset()方法，重置上一首播放的歌曲
     */

    @OnClick({R.id.img_player, R.id.img_pause, R.id.img_back, R.id.img_before, R.id.img_after})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.img_player:
                img_player.setVisibility(View.GONE);
                img_pause.setVisibility(View.VISIBLE);
                isStop = false;
                Log.e("tag", "isStop==" + isStop);
                updatePlayer();
                break;
            case R.id.img_pause:
                img_pause.setVisibility(View.GONE);
                img_player.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                isStop = true;
                Log.e("tag", "isStop==" + isStop);
                updatePlayer();
                break;
            case R.id.img_back:
                finish();
                mediaPlayer.pause();
                break;
            case R.id.img_before:
                MyApplication.seek = MyApplication.seek - 1;
                setData(MyApplication.list.get(MyApplication.seek).getSong(), MyApplication.list.get(MyApplication.seek).getSinger()
                        , MyApplication.list.get(MyApplication.seek).getDuration());
                mediaPlayer.reset();
                play(MyApplication.list.get(MyApplication.seek).getPath());
                isStop = false;
                updatePlayer();
                break;
            case R.id.img_after:
                MyApplication.seek = MyApplication.seek + 1;
                setData(MyApplication.list.get(MyApplication.seek).getSong(), MyApplication.list.get(MyApplication.seek).getSinger()
                        , MyApplication.list.get(MyApplication.seek).getDuration());
                mediaPlayer.reset();
                play(MyApplication.list.get(MyApplication.seek).getPath());
                isStop = false;
                updatePlayer();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
    }

    @Override
    public void addActivity() {
        MyApplication.getInstance().addActivity(this);
    }

    //格式化时间
    private String formatTime(int length) {

        Date date = new Date(length);//调用Date方法获值

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定需要形式

        String TotalTime = simpleDateFormat.format(date);//转化为需要形式

        return TotalTime;

    }

    //格式化时间
    public static String formaTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }


    //定时更新 MyApplication.seekBar进度条
    public void updatePlayer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null && isStop == false) {
                    handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    //播放歌曲逻辑
    private void play(String path) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            img_pause.setVisibility(View.VISIBLE);
            img_player.setVisibility(View.GONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
