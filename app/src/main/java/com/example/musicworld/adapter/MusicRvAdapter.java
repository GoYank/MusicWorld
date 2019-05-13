package com.example.musicworld.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musicworld.MyApplication;
import com.example.musicworld.R;
import com.example.musicworld.activity.MusicAndServiceActivity;
import com.example.musicworld.activity.MusicPlayerActivity;
import com.example.musicworld.bean.MusicBean;
import com.example.musicworld.util.CallClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.adapter
 * 文件名：MusicRvAdapter
 * 创建者：Gyk
 * 创建时间：2019/4/30 9:40
 * 描述：  TODO
 */

public class MusicRvAdapter extends RecyclerView.Adapter<MusicRvAdapter.ViewHolder> {

    List<MusicBean> list = new ArrayList<>();
    Context context;
//    CallClick callClick;
//    boolean beforeStatus;
//    boolean afterStatus;

    public MusicRvAdapter(Context mContext, List<MusicBean> mList) {
        this.context = mContext;
        this.list = mList;
    }

//    public MusicRvAdapter(Context mContext, boolean mBeforeStatus, boolean mAfterStatus) {
//        this.context = mContext;
//        this.beforeStatus = mBeforeStatus;
//        this.afterStatus = mAfterStatus;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.tv_song.setText(list.get(position).getSong());
        viewHolder.tv_singer.setText(list.get(position).getSinger());
        viewHolder.tv_duration.setText(formaTime(list.get(position).getDuration()));
        viewHolder.tv_position.setText(position + 1 + "");
        //显示当前播放的歌曲
        if (position == MyApplication.seek) {
            viewHolder.tv_position.setVisibility(View.GONE);
            viewHolder.img_show.setVisibility(View.VISIBLE);
        }else {
            viewHolder.img_show.setVisibility(View.GONE);
            viewHolder.tv_position.setVisibility(View.VISIBLE);
        }

        MyApplication.list = list;
        viewHolder.rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAndServiceActivity.transMission(context, list.get(position).getSong(), list.get(position).getSinger()
                        , list.get(position).getPath(), list.get(position).getLength() + "", list.get(position).getDuration(), position);
//                viewHolder.tv_position.setVisibility(View.GONE);
//                viewHolder.img_show.setVisibility(View.VISIBLE);
            }
        });

    }

    public static String formaTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_all;
        TextView tv_song;
        TextView tv_singer;
        TextView tv_duration;
        TextView tv_position;
        ImageView img_show;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_all = itemView.findViewById(R.id.rl_all);
            tv_song = itemView.findViewById(R.id.tv_song);
            tv_singer = itemView.findViewById(R.id.tv_singer);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_position = itemView.findViewById(R.id.tv_position);
            img_show = itemView.findViewById(R.id.img_show);
        }
    }
}
