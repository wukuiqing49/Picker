package com.cnlive.media.frame.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.cnlive.media.PickerConfig;
import com.cnlive.media.media.entity.Media;
import com.cnlive.media.ui.activity.ImageCropActivity;
import com.cnlive.media.ui.activity.RecordActivity;
import com.cnlive.media.ui.fragment.RecordPreviewFragment;
import com.cnlive.media.utils.GlideCacheUtil;
import com.cnlive.media.utils.MediaUtils;
import com.cnlive.media.utils.album.AlbumProcessUtil;
import com.cnlive.media.utils.album.OnSaveCallback;
import com.cnlive.media.utils.observable.MediaAddObservable;
import com.cnlive.media.utils.observable.MeidaResultObservable;
import com.qiniu.pili.droid.shortvideo.PLDisplayMode;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;
import com.qiniu.pili.droid.shortvideo.PLShortVideoEditor;
import com.qiniu.pili.droid.shortvideo.PLVideoEditSetting;
import com.wkq.base.frame.mosby.delegate.MvpView;
import com.wkq.base.utils.AlertUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 作 者 : wkq
 * <p>
 * 时 间 : 2020/9/28 9:35
 * <p>
 * 名 字 : RecordPreviewView
 * <p>
 * 简 介 :
 */
public class RecordPreviewView implements MvpView {
    RecordPreviewFragment mFragment;
    private PLMediaFile mMediaFile;

    public RecordPreviewView(RecordPreviewFragment mFragment) {
        this.mFragment = mFragment;
    }

    public void initView(RecordActivity mActivity) {

        if (mFragment.type == 0) {
            mFragment.binding.ivPre.setVisibility(View.VISIBLE);
            mFragment.binding.flVideo.setVisibility(View.GONE);
            GlideCacheUtil.intoItemImageBitmap(mActivity, mActivity.preBitmap, mFragment.binding.ivPre);
        } else if (mFragment.type == 1) {
            mFragment.binding.ivPre.setVisibility(View.GONE);
            mFragment.binding.flVideo.setVisibility(View.VISIBLE);
        }

        ObjectAnimator animator_cancel = ObjectAnimator.ofFloat(mFragment.binding.tbOk, "translationX", 200, 0);
        ObjectAnimator animator_confirm = ObjectAnimator.ofFloat(mFragment.binding.tbCancel, "translationX", -200, 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator_cancel, animator_confirm);
        set.setInterpolator(new OvershootInterpolator());
        set.setDuration(200);
        set.start();

    }

    public void initPre() {
        mMediaFile = new PLMediaFile(mFragment.path);
        PLVideoEditSetting setting = new PLVideoEditSetting();
        setting.setSourceFilepath(mFragment.path);
        setting.setGifPreviewEnabled(false);
        if (mFragment.mShortVideoEditor == null)
            mFragment.mShortVideoEditor = new PLShortVideoEditor(mFragment.binding.gvVideo);
        mFragment.mShortVideoEditor.setVideoEditSetting(setting);
        mFragment.mShortVideoEditor.setPlaybackLoop(true);
        mFragment.mShortVideoEditor.setDisplayMode(PLDisplayMode.FULL);
        mFragment.mShortVideoEditor.startPlayback();
        mFragment.isPlaying = true;
    }

    private void initGlSurfaceView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFragment.binding.flVideo.getLayoutParams();
        FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams) mFragment.binding.gvVideo.getLayoutParams();
        int outputWidth = mMediaFile.getVideoWidth();
        int outputHeight = mMediaFile.getVideoHeight();
        int rotation = mMediaFile.getVideoRotation();
        if ((rotation == 90 || rotation == 270)) {
            int temp = outputWidth;
            outputWidth = outputHeight;
            outputHeight = temp;
        }

        surfaceLayout.width = mFragment.mScreenSize.x;
        surfaceLayout.height = Math.round((float) outputHeight * mFragment.mScreenSize.x / outputWidth);
        ViewGroup.MarginLayoutParams marginParams = null;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) surfaceLayout;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(surfaceLayout);
        }
        mFragment.binding.flVideo.setLayoutParams(layoutParams);
        mFragment.binding.gvVideo.setLayoutParams(marginParams);
    }

    public void showMessage(String message) {
        if (mFragment == null || TextUtils.isEmpty(message)) return;
        AlertUtil.showDeftToast(mFragment.getActivity(), message);
    }

    public void showSuccessMessage(String message) {
        if (mFragment == null || TextUtils.isEmpty(message)) return;
        AlertUtil.showSuccessToast(mFragment.getActivity(), message);
    }

    public void savePic(RecordActivity mActivity) {

        AlbumProcessUtil.saveBitmap(mActivity, mActivity.preBitmap, new OnSaveCallback() {
            @Override
            public void onSuccess(String path) {
                File file = new File(path);
                if (file != null && file.exists() && file.canRead() && file.canWrite()) {
                    Media media = new Media(path, file.getName(), System.currentTimeMillis(), 1, file.length(), (int) System.currentTimeMillis(), file.getParent(), Uri.fromFile(file).toString());
                    media.setSelect(false);
                    media.setReturnUri(mActivity.mOptions.isReturnUri());
                    if (mActivity.preBitmap != null && mActivity.preBitmap.getHeight() > 0 && mActivity.preBitmap.getWidth() > 0) {
                        media.setImgHeight(mActivity.preBitmap.getHeight());
                        media.setImgWidth(mActivity.preBitmap.getWidth());
                        media.setLongImg(MediaUtils.isLongImg(mActivity.preBitmap.getWidth(), mActivity.preBitmap.getHeight()));
                    }
                    if (mActivity.mOptions.needCrop) {
                        ImageCropActivity.start(mActivity, path, mActivity.mOptions);
                    } else if (mActivity.mOptions.isSinglePick() || mActivity.mOptions.getMaxImageSize() == 1) {
                        MeidaResultObservable.getInstance().finishMedia(true, media);
                        mActivity.finish();
                    } else if (mActivity.isJumpCamera) {
                        if (mActivity.selectMedia == null)
                            mActivity.selectMedia = new ArrayList<Media>();
                        mActivity.selectMedia.add(media);
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, mActivity.selectMedia);
                        mActivity.setResult(mActivity.resultCode, intent);
                        mActivity.finish();
                    } else {
                        MediaAddObservable.getInstance().addMedia(media);
                        mActivity.finish();
                    }

                } else {
                    mFragment.requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }

            @Override
            public void onFail(String message) {
                showMessage("图片保存异常");
            }
        });
    }

    public void saveVideo(RecordActivity mActivity) {
        if (mMediaFile == null || mActivity == null) return;
        File file = new File(mMediaFile.getFilepath());
        Media media = new Media(mMediaFile.getFilepath(), file.getName(), System.currentTimeMillis(), 3, file.length(), (int) System.currentTimeMillis(), file.getParent(), (int) mMediaFile.getDurationMs(), Uri.fromFile(file).toString());
        media.setImgWidth(mMediaFile.getVideoWidth());
        media.setImgHeight(mMediaFile.getVideoHeight());
        media.setSelect(false);
        media.setReturnUri(mActivity.mOptions.isReturnUri());
        if (mActivity.isJumpCamera) {
            MeidaResultObservable.getInstance().finishMedia(true, media);
        } else if (mActivity.mOptions.isSinglePick() || mActivity.mOptions.getMaxImageSize() == 1) {
            MeidaResultObservable.getInstance().finishMedia(true, media);
            mActivity.finish();
        } else if (mActivity.mOptions.needCrop) {
            RecordActivity.newInstance(mActivity, mActivity.mOptions);
            mActivity.finish();
        } else {
            MediaAddObservable.getInstance().addMedia(media);
            mActivity.finish();
        }

        mMediaFile.release();

    }
}
