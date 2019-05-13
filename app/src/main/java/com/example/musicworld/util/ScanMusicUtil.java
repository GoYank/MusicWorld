package com.example.musicworld.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicworld.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.util
 * 文件名：ScanMusicUtil
 * 创建者：Gyk
 * 创建时间：2019/4/30 9:22
 * 描述：  TODO
 */

public class ScanMusicUtil {

    public static List<MusicBean> getMusicInfo(Context context) {
        List<MusicBean> list = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicBean musicBean = new MusicBean();
                Log.e("tag", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                musicBean.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                musicBean.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                musicBean.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                musicBean.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                musicBean.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                musicBean.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
//                if (musicBean.getLength() > 1000 * 800) {
//                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
//                    if (musicBean.getSong().contains("-")) {
//                        String[] str = musicBean.getSong().split("-");
//                        musicBean.setSinger(str[0]);
//                        musicBean.setSong(str[1]);
//                    }
//
//                }
                list.add(musicBean);
            }
            cursor.close();
            Log.e("tag", "song===" + list.size());
        }
        return list;
    }
}