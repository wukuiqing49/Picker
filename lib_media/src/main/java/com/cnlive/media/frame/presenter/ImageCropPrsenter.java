package com.cnlive.media.frame.presenter;

import android.text.TextUtils;

import com.cnlive.media.R;
import com.cnlive.media.frame.view.ImageCropView;
import com.cnlive.media.ui.activity.ImageCropActivity;
import com.wkq.base.frame.mosby.MvpBasePresenter;

import java.io.File;

/**
 * 作 者 : wkq
 * <p>
 * 时 间 : 2020/9/29 15:45
 * <p>
 * 名 字 : ImageCropPrsenter
 * <p>
 * 简 介 :
 */
public class ImageCropPrsenter extends MvpBasePresenter<ImageCropView> {

    public void initData(ImageCropActivity mActibity) {
        if (mActibity.mOptions == null) {
            getView().showMessage(mActibity.getResources().getString(R.string.error_imagepicker_lack_params));
            mActibity.setResult(mActibity.RESULT_CANCELED);
            mActibity.finish();
            return;
        }
        if (TextUtils.isEmpty(mActibity.mOriginPath) || mActibity.mOriginPath.length() == 0) {
            getView().showMessage(mActibity.getResources().getString(R.string.imagepicker_crop_decode_fail));
            mActibity.setResult(mActibity.RESULT_CANCELED);
            mActibity.finish();
            return;
        }

        File file = new File(mActibity.mOriginPath);
        if (!file.exists()) {
            getView().showMessage(mActibity.getResources().getString(R.string.imagepicker_crop_decode_fail));
            mActibity.finish();
            return;
        }

        mActibity.cacheFile = new File(mActibity.mOptions.cachePath);
        if (!mActibity.cacheFile.exists())
            mActibity.cacheFile.mkdirs();

        mActibity.mCropParams = mActibity.mOptions.getCropParams();
        mActibity.binding.cvCrop.load(mActibity.mOriginPath)
                .setAspect(mActibity.mCropParams.getAspectX(), mActibity.mCropParams.getAspectY())
                .setOutputSize(mActibity.mCropParams.getOutputX(), mActibity.mCropParams.getOutputY())
                .start(mActibity);
    }
}
