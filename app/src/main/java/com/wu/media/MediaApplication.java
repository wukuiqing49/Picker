package com.wu.media;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.cnlive.media.utils.QiniuVideoUtil;


public class MediaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        ARouter.init(this);
        MultiDex.install(this);
        //七牛需要后台开通权限
        QiniuVideoUtil.initVideo(this);
    }
}
