//package com.cnlive.media.frame.view;
//
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.media.AudioManager;
//import android.net.Uri;
//import android.os.Build;
//import android.text.TextUtils;
//import android.view.View;
//
//import com.bumptech.glide.Glide;
//import com.cnlive.libs.base.util.AlertUtil;
//import com.cnlive.media.ui.activity.VideoPlayerActivity;
//import com.hannesdorfmann.mosby3.mvp.MvpView;
//
///**
// * 作 者 : wkq
// * <p>
// * 时 间 : 2020/10/15 11:13
// * <p>
// * 名 字 : VideoPlayerView
// * <p>
// * 简 介 :
// */
//public class VideoPlayerView implements MvpView {
//    VideoPlayerActivity mActivity;
//
//    public VideoPlayerView(VideoPlayerActivity mActivity) {
//        this.mActivity = mActivity;
//    }
//
//    public void initView() {
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        mActivity.binding.video.setTimeout(60, 60);
//        mActivity.binding.video.setKeepScreenOn(true);
//        mActivity.binding.video.setLooping(true);
//        mActivity.binding.video.setDataSource(mActivity, Uri.parse(mActivity.path));
//
//        mActivity.binding.video.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(IMediaPlayer iMediaPlayer) {
//                mActivity.isCanPlay = true;
//                setPlayState();
//            }
//        });
//
//        mActivity.binding.video.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
//                mActivity.isPlayIng = false;
//                mActivity.binding.video.seekTo(0);
//                onPause();
//            }
//        });
//
//        mActivity.binding.video.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                showMessage("视频播放异常");
//                mActivity.isPlayIng = false;
//                return false;
//            }
//        });
//
//        mActivity.binding.ivPlay.setOnClickListener(v -> {
//            setPlayState();
//        });
//        mActivity.binding.video.setOnClickListener(v -> {
//            setPlayState();
//        });
//
//
//        Glide.with(mActivity).load(Uri.parse(mActivity.path)).into(mActivity.binding.ivThumbnail);
//
//    }
//
//
//    public void showMessage(String message) {
//        if (mActivity == null || TextUtils.isEmpty(message)) {
//            return;
//        }
//        AlertUtil.showDeftToast(mActivity, message);
//    }
//
//    public void requestAudioFocus() {
//        if (mActivity == null)
//            return;
//        AudioManager audioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
//        if (audioManager != null) {
//            int ret = audioManager.requestAudioFocus(mOnAudioFocusChangeListener,
//                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//            if (ret != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            }
//        }
//    }
//
//    /**
//     * 释放音频焦点
//     */
//    public void abandonAudioFocus() {
//        if (mActivity == null)
//            return;
//        AudioManager audioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
//        if (audioManager != null) {
//            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
//        }
//    }
//
//    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
//                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
//                //失去焦点之后的操作
//                onPause();
//            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN ||
//                    focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT ||
//                    focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE ||
//                    focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK) {
//                //获得焦点之后的操作
//            }
//        }
//    };
//
//    /**
//     * 设置播放状态
//     */
//    public void setPlayState() {
//        if (mActivity == null) return;
//        if (mActivity.isCanPlay) {
//            if (mActivity.isPlayIng) {
//                mActivity.binding.video.pause();
//                mActivity.binding.rlThum.setVisibility(View.VISIBLE);
//                mActivity.binding.ivThumbnail.setVisibility(View.GONE);
//                mActivity.isPlayIng = false;
//            } else {
//                mActivity.binding.video.start();
//                mActivity.binding.rlThum.setVisibility(View.GONE);
//                mActivity.isPlayIng = true;
//            }
//        }
//    }
//
//
//    public void onResume() {
//        mActivity.isPlayIng = false;
//        mActivity.binding.video.pause();
//        mActivity.binding.rlThum.setVisibility(View.VISIBLE);
//    }
//
//    public void onPause() {
//        mActivity.isPlayIng = false;
//        mActivity.binding.video.pause();
//        mActivity.binding.rlThum.setVisibility(View.VISIBLE);
//    }
//}
