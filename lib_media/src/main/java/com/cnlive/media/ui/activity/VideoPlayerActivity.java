//package com.cnlive.media.ui.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.cnlive.libs.base.frame.activity.MvpBindingActivity;
//import com.cnlive.media.R;
//import com.cnlive.media.frame.presenter.VideoPlayerPresenter;
//import com.cnlive.media.frame.view.VideoPlayerView;
//import com.cnlive.media.databinding.ActivityVideoPlayBinding;
//
///**
// * 作 者 : wkq
// * <p>
// * 时 间 : 2020/10/15 11:12
// * <p>
// * 名 字 : VideoPlayerActivity
// * <p>
// * 简 介 : 视频播放页面逻辑
// */
//public class VideoPlayerActivity extends MvpBindingActivity<VideoPlayerView, VideoPlayerPresenter, Object, ActivityVideoPlayBinding> {
//
//    public String path;
//    //是否准备好
//    public boolean isCanPlay = true;
//    //是否正在播放
//    public boolean isPlayIng = false;
//
//    public static void newInstance(Context context, String uri) {
//        Intent intent = new Intent();
//        intent.putExtra("path", uri);
//        intent.setClass(context, VideoPlayerActivity.class);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_video_play;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        path = getIntent().getStringExtra("path");
//        if (getMvpView() != null) getMvpView().requestAudioFocus();
//        if (getMvpView() != null) getMvpView().initView();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (getMvpView() != null) getMvpView().onResume();
//    }
//
//    @Override
//    public void onPause() {
//        if (getMvpView() != null) getMvpView().abandonAudioFocus();
//        super.onPause();
//        if (getMvpView() != null) getMvpView().onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (getMvpView() != null) getMvpView().abandonAudioFocus();
//        super.onDestroy();
//        if (binding.video != null) binding.video.release();
//    }
//
//}
