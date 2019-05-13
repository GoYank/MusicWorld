package com.example.musicworld.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.transition.Visibility;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.musicworld.MyApplication;
import com.example.musicworld.R;
import com.example.musicworld.activity.MusicAndServiceActivity;
import com.example.musicworld.activity.MusicPlayerActivity;
import com.example.musicworld.fragment.MusicFragment;

import java.io.IOException;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.service
 * 文件名：MusicService
 * 创建者：Gyk
 * 创建时间：2019/5/6 14:33
 * 描述：  服务：绑定模式可以拿到service实例，当为启动模式时一旦startService（）开启服务，
 * 不论怎么调用startService(),都只有onStartCommand被调用，onCreate只有在第一次才会被调用
 */

public class MusicService extends Service {
    protected static final String TAG = "gyk";
    private static final String ID = "01";
    private static final String CHANNEL_NAME = "MyChannel";
    private static final int DEFAULT_ID = 1;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    public static final String POSITION = "MusicAndServiceActivity";
    private String song;
    private String singer;
    private int position;
    private String path;
    private boolean isStart;
    private boolean isClick;
    private boolean isNext;
    private boolean isStop;
    private NotificationManager notificationManager;
    private Intent intent1 = new Intent("com.example.musicworld.MUSICRECIVER");

    /***
     *
     *绑定模式下：把Binder类返回给客户端即Activity
     * 启动模式下：此方法不被调用
     * */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        path = intent.getStringExtra("path");
        song = intent.getStringExtra("song");
        singer = intent.getStringExtra("singer");
        position = intent.getIntExtra("position", 0);
        MyApplication.song = song;
        MyApplication.singer = singer;
        MyApplication.position = position;
        MyApplication.path = path;
        sendFlagNoClearNotification(song, singer, position, true);
        Log.e("gyk", "MyAPP.position==" + position);
        Log.e("gyk", "se_song==" + song);
        isStart = intent.getBooleanExtra("isStart", false);
        isClick = intent.getBooleanExtra("isClick", false);
        isNext = intent.getBooleanExtra("isNext", false);
        Log.e(TAG, "path=" + path);
        if (!isClick) {
            if (path != null) {
                play(path);
                sendFlagNoClearNotification(song, singer, position, true);
                intent1.putExtra("song", song);
                intent1.putExtra("singer", singer);
                intent1.putExtra("isStop", false);
                sendBroadcast(intent1);
                upDatePlayer();
            }

        } else {
            if (isStart) {
                Log.e(TAG, "isStart=");
                startPlayer();
                sendFlagNoClearNotification(song, singer, position, true);
                Log.e(TAG,"My.isStop"+MyApplication.isStop);
                upDatePlayer();
            } else {
                Log.e(TAG, "isStop=");
                stopPlayer();
                sendFlagNoClearNotification(song, singer, position, false);
                Log.e(TAG,"My.isStop====="+MyApplication.isStop);
            }
        }
        if (isNext) {
            Log.e("gyk", "isNext--------------");
            intent.putExtra("isStop", false);
            sendBroadcast(intent1);
        }
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public void upDatePlayer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null && isStop == false) {
                    intent1.putExtra("curr", mediaPlayer.getCurrentPosition());
                    sendBroadcast(intent1);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    //发送通知(需要注意在android 8.0版本以后要添加渠道)
    private void sendFlagNoClearNotification(String song, String singer, int position, boolean isNotiStart) {

        Notification notification = null;
        boolean isStart = isNotiStart;
        Log.e("gyk", "isNotiStart====" + isNotiStart);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews normalView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.normal_notification);
        normalView.setTextViewText(R.id.tv_title, song);
        normalView.setTextViewText(R.id.tv_content, singer);
        if (isStart) {
            normalView.setViewVisibility(R.id.iv_no_player, View.GONE);
            normalView.setViewVisibility(R.id.iv_no_pause, View.VISIBLE);
        } else {
            normalView.setViewVisibility(R.id.iv_no_player, View.VISIBLE);
            normalView.setViewVisibility(R.id.iv_no_pause, View.GONE);
        }
        normalView.setOnClickPendingIntent(R.id.iv_no_pause, getIntent(true, false, 10, song, singer, position));
        normalView.setOnClickPendingIntent(R.id.iv_no_player, getIntent(true, true, 11, song, singer, position));
        normalView.setOnClickPendingIntent(R.id.iv_no_next, getNext(true, false, 12, position + 1, song, singer));
        RemoteViews bigView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_normation);
        bigView.setTextViewText(R.id.tv_tt, song);
        bigView.setTextViewText(R.id.tv_con, singer);
        if (isStart) {
            bigView.setViewVisibility(R.id.iv_big_player, View.GONE);
            bigView.setViewVisibility(R.id.iv_big_pause, View.VISIBLE);
        } else {
            bigView.setViewVisibility(R.id.iv_big_player, View.VISIBLE);
            bigView.setViewVisibility(R.id.iv_big_pause, View.GONE);
        }
        bigView.setOnClickPendingIntent(R.id.iv_big_pause, getIntent(true, false, 20, song, singer, position));
        bigView.setOnClickPendingIntent(R.id.iv_big_player, getIntent(true, true, 21, song, singer, position));
        bigView.setOnClickPendingIntent(R.id.iv_big_next, getNext(true, false, 22, position + 1, song, singer));
        bigView.setOnClickPendingIntent(R.id.iv_big_back, getNext(false, false, 23, position - 1, song, singer));
        Log.e("gyk", "songNo==" + song);
        Log.e("gyk", "singerNo==" + singer);
        Log.e("gyk", "position==" + position);
        Intent intent = new Intent(this, MusicAndServiceActivity.class);
        intent.putExtra(POSITION, song);
        intent.putExtra(POSITION + 1, singer);
        intent.putExtra(POSITION + 5, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(this)
                    .setChannelId(ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setCustomContentView(normalView)
                    .setCustomBigContentView(bigView);
            notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
        } else {
            //兼容8.0以下
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCustomContentView(normalView)
                    .setCustomBigContentView(bigView)
                    .setContentIntent(pendingIntent);
            notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }
        notificationManager.notify(DEFAULT_ID, notification);
    }

    //通知栏暂停操作
    private PendingIntent getIntent(boolean isClick, boolean isStart, int code, String song, String singer, int position) {
        Intent intent1 = new Intent(this, MusicService.class);
        intent1.putExtra("isClick", isClick);
        intent1.putExtra("isStart", isStart);
        intent1.putExtra("song", song);
        intent1.putExtra("singer", singer);
        intent1.putExtra("position", position);
        PendingIntent pendingIntent = PendingIntent.getService(this, code, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    //通知栏切换歌曲
    private PendingIntent getNext(boolean isNext, boolean isClick, int code, int position, String song, String singer) {
        Intent intent2 = new Intent(this, MusicService.class);
        intent2.putExtra("isClick", isClick);
        intent2.putExtra("isNext", isNext);
        intent2.putExtra("path", MyApplication.list.get(position).getPath());
        intent2.putExtra("position", position);
        intent2.putExtra("song", MyApplication.list.get(position).getSong());
        intent2.putExtra("singer", MyApplication.list.get(position).getSinger());
        PendingIntent pendingIntent = PendingIntent.getService(this, code, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    private void play(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayer() {
        mediaPlayer.start();
        isStop = false;
        intent1.putExtra("isStop", false);
        sendBroadcast(intent1);
    }

    public void stopPlayer() {
        mediaPlayer.pause();
        isStop = true;
        intent1.putExtra("isStop", true);
        sendBroadcast(intent1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

}


//  if (!isSame) {
//                    mediaPlayer.reset();
//                    Log.e(TAG, "path==" + path);
////                    play(MyApplication.path);
//                } else {
//                }
//                if (!isStart) {
//                    Log.e("gyk", "startMediaplayer");
//                    startPlayer();
//                } else {
//                    Log.e("gyk", "stopMediaplayer");
//                    stopPlayer();
//                }
//
//            }


//    /**
//     * 此方法只适用于绑定模式
//     * 创建一个Binder对象，返回给客户端即Activity使用，提供数据交换的接口
//     **/
//    public class MusicBinder extends Binder {
//        //声明一个方法，getservice(提供给客户端即Activity使用)
//        public MusicService getService() {
//            //返回当前对象MusicService，这样就可以在客户端即Activity中调用service的公共方法了
//            return MusicService.this;
//        }
//    }
