package com.example.musicworld.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.musicworld.R;
import com.example.musicworld.adapter.MusicRvAdapter;
import com.example.musicworld.base.BaseFragment;
import com.example.musicworld.util.ScanMusicUtil;

import butterknife.BindView;

/**
 * 项目名：MusicWorld
 * 包名：com.example.musicworld.fragment
 * 文件名：MusicFragment
 * 创建者：Gyk
 * 创建时间：2019/4/29 13:50
 * 描述：  TODO
 */

public class MusicFragment extends BaseFragment {
    @BindView(R.id.recy_music)
    RecyclerView recy_music;
    MusicRvAdapter musicRvAdapter;

    @Override
    public View initView() {
        return View.inflate(context, R.layout.fragment_music, null);
    }

    @Override
    public void initData() {
        musicRvAdapter = new MusicRvAdapter(getActivity(), ScanMusicUtil.getMusicInfo(getActivity()));
        recy_music.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_music.setAdapter(musicRvAdapter);
        musicRvAdapter.notifyDataSetChanged();
    }
}
