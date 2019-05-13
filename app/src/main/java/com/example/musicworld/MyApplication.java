package com.example.musicworld;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.musicworld.bean.MusicBean;
import com.example.musicworld.service.MusicService;

import java.util.List;
import java.util.Stack;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld
 * 文件名：MyApplication
 * 创建者：Gyk
 * 创建时间：2019/4/29 8:42
 * 描述：  TODO
 */

public class MyApplication extends MultiDexApplication {

    public static MyApplication instance;
    public static Stack<Activity> activityStack;
    public static List<MusicBean> list;
    public static int seek = -1;
    public static MusicService musicService;
    public static boolean isStop = true;
    public static String song;
    public static String singer;
    public static int position;
    public static String path;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        Log.e("gyk","onTerminate");
        super.onTerminate();

    }

    @Override
    public void onTrimMemory(int level) {
        Log.e("gyk","onTrimMemory");
        super.onTrimMemory(level);
    }

    /**
     * 双重检查--单例模式
     **/

    public static MyApplication getInstance() {
        if (null == instance) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取在前台运行的activity
     */
    public Activity currentActivity() {
        if (activityStack == null) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束指定的activity
     **/
    public void finishActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (activityStack != null) {
            if (activityStack.contains(activity)) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 清空activity栈
     **/
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activityStack.get(i) != null) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void AppExit(Context context, boolean isClear) {
        try {
            if (isClear) {
            }
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
