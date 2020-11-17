package com.wu.media;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.cnlive.media.ImagePicker;
import com.cnlive.media.PickerConfig;
import com.cnlive.media.media.entity.Media;
import com.cnlive.media.utils.AndroidQUtil;
import com.cnlive.media.utils.GlideCacheUtil;
import com.wu.media.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mainBinding;
    private ArrayList<Media> select;
    private String fileDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setOnClick(this);


        boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
        File fileQ = getExternalFilesDir("");
        File file = Environment.getExternalStorageDirectory();
        String filePath = null;
        if (isAndroidQ) {
            if (fileQ != null) filePath = fileQ + "/strike/file/";
        } else {
            if (file != null) filePath = file + "/strike/file/";
        }
        fileDir = filePath == null ? "" : filePath;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_camera:
                //打开相机
                new ImagePicker
                        .Builder()
                        .setJumpCameraMode(PickerConfig.CAMERA_MODE_ALL)
                        .builder()
                        .startCamera(this, select, PickerConfig.PICKER_IMAGE, PickerConfig.DEFAULT_RESULT_CODE);
                break;
            case R.id.bt_preview:
                //打开预览
                new ImagePicker.Builder()
                        .builder()
                        .startPreview(this, 0, select, PickerConfig.PICKER_IMAGE, PickerConfig.DEFAULT_RESULT_CODE);
                break;
            case R.id.tv_media:
                //打开相册
                new ImagePicker.Builder()
                        .maxNum(1)
                        .setSelectGif(true)
                        .maxImageSize(25 * 1024 * 1024)
                        .maxVideoSize(100*1024*1024)
                        .isReturnUri(AndroidQUtil.isAndroidQ())
                        .selectMode(PickerConfig.PICKER_IMAGE_VIDEO)
                        .defaultSelectList(new ArrayList<Media>())
                        .needCamera(false)
                        .builder()
//                        .doCrop(1, 1, 900, 900)
                        .start(this, PickerConfig.PICKER_IMAGE, PickerConfig.DEFAULT_RESULT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PickerConfig.DEFAULT_RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            GlideCacheUtil.intoItemImage(this, select.get(0), mainBinding.ivPre, null);
        }

    }
}