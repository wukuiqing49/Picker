package com.cnlive.media.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cnlive.media.PickerConfig;
import com.cnlive.media.R;
import com.cnlive.media.frame.presenter.ImageCropPrsenter;
import com.cnlive.media.frame.view.ImageCropView;
import com.cnlive.media.model.ImagePickerCropParams;
import com.cnlive.media.model.ImagePickerOptions;
import com.cnlive.media.databinding.ActivityMediaCropBinding;
import com.wkq.base.frame.activity.MvpBindingActivity;
import com.wkq.base.utils.StatusBarUtil2;

import java.io.File;


/**
 * 裁剪界面
 */
public class ImageCropActivity extends MvpBindingActivity<ImageCropView, ImageCropPrsenter, ActivityMediaCropBinding> implements View.OnClickListener {
    /**
     * 跳转到该界面的公共方法
     *
     * @param activity   发起跳转的Activity
     * @param originPath 待裁剪图片路径
     * @param options    参数
     */
    public static void start(Activity activity, String originPath, ImagePickerOptions options) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(PickerConfig.INTENT_KEY_ORIGIN_PATH, originPath);
        intent.putExtra(PickerConfig.INTENT_KEY_OPTIONS, options);
        activity.startActivityForResult(intent, PickerConfig.REQUEST_CODE_CROP);
    }

    public ImagePickerOptions mOptions;
    public String mOriginPath;
    private Handler mHandler;
    private ProgressDialog mDialog;
    public ImagePickerCropParams mCropParams;
    public File cacheFile;


    @Override
    protected int getLayoutId() {
        mOriginPath = getIntent().getStringExtra(PickerConfig.INTENT_KEY_ORIGIN_PATH);
        mOptions = getIntent().getParcelableExtra(PickerConfig.INTENT_KEY_OPTIONS);
        mHandler = new Handler(getMainLooper());
        return R.layout.activity_media_crop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil2.setTransparentForWindow(this);
        StatusBarUtil2.addTranslucentView(this, 0);
        if (getMvpView()!=null)getMvpView().initView();
        if (getPresenter()!=null)getPresenter().initData(this);
    }




    @Override
    protected void onDestroy() {
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_crop_cancel) {
            finish();
        } else if (id == R.id.btn_crop_confirm) {
            if (getMvpView()!=null)getMvpView().returnCropedImage();
        }
    }
}
