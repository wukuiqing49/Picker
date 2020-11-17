package com.cnlive.media.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.cnlive.media.R;
import com.cnlive.media.frame.presenter.MediaFPresenter;
import com.cnlive.media.frame.view.MediaFView;
import com.cnlive.media.media.entity.Media;
import com.cnlive.media.databinding.FragmentMediaBinding;
import com.wkq.base.frame.fragment.MvpBindingFragment;

/**
 * 作 者 : wkq
 * <p>
 * 时 间 : 2020/9/17 16:14
 * <p>
 * 名 字 : MediaFragment
 * <p>
 * 简 介 :
 */
public class MediaFragment extends MvpBindingFragment<MediaFView, MediaFPresenter,  FragmentMediaBinding> {

    public Media mMedia;

    public static MediaFragment newInstance(Media mMedia) {

        MediaFragment fragment = new MediaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("media", mMedia);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_media;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMedia = getArguments().getParcelable("media");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       if (getMvpView()!=null)getMvpView().initView();
    }
}
