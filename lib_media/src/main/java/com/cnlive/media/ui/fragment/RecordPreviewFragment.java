package com.cnlive.media.ui.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import com.cnlive.media.R;
import com.cnlive.media.frame.presenter.RecordPreviewPresenter;
import com.cnlive.media.frame.view.RecordPreviewView;
import com.cnlive.media.ui.activity.RecordActivity;
import com.cnlive.media.utils.Utils;
import com.cnlive.media.databinding.FragmentRecordPreviewBinding;
import com.qiniu.pili.droid.shortvideo.PLShortVideoEditor;
import com.wkq.base.frame.fragment.MvpBindingFragment;
import com.wkq.base.utils.DoublePressed;

/**
 * 作 者 : wkq
 * <p>
 * 时 间 : 2020/9/28 9:34
 * <p>
 * 名 字 : RecordPreviewFragment
 * <p>
 * 简 介 :
 */
public class RecordPreviewFragment extends MvpBindingFragment<RecordPreviewView, RecordPreviewPresenter,FragmentRecordPreviewBinding> implements View.OnClickListener {

    //类型 0 是图片  1 是视频
    public int type;
    //路径
    public String path;
    public PLShortVideoEditor mShortVideoEditor;
    public Point mScreenSize;
    //播放状态
    public boolean isPlaying;
    // 处理 返回事件的监听
    public OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            callback.setEnabled(false);
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }
    };
    private RecordActivity mActivity;


    public static RecordPreviewFragment newInstance(int type, String path) {

        Bundle args = new Bundle();

        RecordPreviewFragment fragment = new RecordPreviewFragment();
        args.putInt("type", type);
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_record_preview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (int) getArguments().get("type");
        path = (String) getArguments().get("path");
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (RecordActivity) getActivity();
        mScreenSize = Utils.getScreenSize(mActivity);
        binding.setOnClick(this);
        if (getMvpView() != null) getMvpView().initView(mActivity);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (type == 1&& getMvpView()!=null){
            getMvpView().initPre();
        }
        isPlaying = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mShortVideoEditor != null) mShortVideoEditor.stopPlayback();
        isPlaying = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callback != null) callback.remove();
        if (mShortVideoEditor != null) mShortVideoEditor.stopPlayback();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.gv_video) {
            if (isPlaying) {
                if (mShortVideoEditor != null) mShortVideoEditor.pausePlayback();
                binding.playControlIv.setVisibility(View.VISIBLE);
                isPlaying = false;
            }
        } else if (v.getId() == R.id.play_control_iv) {
            if (!isPlaying) {
                if (mShortVideoEditor != null) mShortVideoEditor.resumePlayback();
                isPlaying = true;
                binding.playControlIv.setVisibility(View.GONE);
            }

        } else if (v.getId() == R.id.tb_cancel) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        } else if (v.getId() == R.id.tb_ok) {
            if (getMvpView() == null || DoublePressed.onDoublePressed()) return;
            if (type == 0) {
                getMvpView().savePic(mActivity);
            } else if (type == 1) {
                getMvpView().saveVideo(mActivity);
            }


        }

    }
}
