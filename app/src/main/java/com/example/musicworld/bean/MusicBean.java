package com.example.musicworld.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.bean
 * 文件名：MusicBean
 * 创建者：Gyk
 * 创建时间：2019/4/30 9:14
 * 描述：  TODO
 */

public class MusicBean implements Parcelable {


    private int id;
    private String singer;
    private String song;
    private String path;
    private int duration;
    private long length;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.singer);
        dest.writeString(this.song);
        dest.writeString(this.path);
        dest.writeInt(this.duration);
        dest.writeLong(this.length);
        dest.writeLong(this.id);
    }

    public MusicBean() {
    }

    protected MusicBean(Parcel in) {
        this.singer = in.readString();
        this.song = in.readString();
        this.path = in.readString();
        this.duration = in.readInt();
        this.length = in.readLong();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<MusicBean> CREATOR = new Parcelable.Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel source) {
            return new MusicBean(source);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };
}
