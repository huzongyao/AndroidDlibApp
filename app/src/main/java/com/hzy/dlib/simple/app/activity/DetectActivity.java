package com.hzy.dlib.simple.app.activity;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.hzy.dlib.libdlib.DLibDetector;
import com.hzy.dlib.simple.app.R;
import com.hzy.dlib.simple.app.consts.RouterHub;
import com.hzy.dlib.simple.app.utils.BitmapDrawUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = RouterHub.DETECT_ACTIVITY)
public class DetectActivity extends AppCompatActivity {

    @BindView(R.id.demo_image)
    ImageView mDemoImage;
    private Bitmap mDemoBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        ButterKnife.bind(this);
        loadBitmapFromImage();
        detectFromBitmap();
    }

    private void detectFromBitmap() {
        snakeBarShow("Detecting... Please Wait!!");
        new Thread() {
            @Override
            public void run() {
                DLibDetector.INSTANCE.init();
                DLibDetector.INSTANCE.detectFromBitmap(mDemoBitmap);
                long[] rectList = DLibDetector.INSTANCE.getLastDetected();
                Rect[] rects = BitmapDrawUtils.getFromLongArray(rectList);
                BitmapDrawUtils.drawRectOnBitmap(mDemoBitmap, rects);
                mDemoImage.post(() -> {
                    mDemoImage.setImageBitmap(mDemoBitmap);
                    snakeBarShow(rects.length + " Faces Detected!!");
                });
            }
        }.start();
    }

    private void snakeBarShow(String msg) {
        SnackbarUtils.with(mDemoImage).setMessage(msg).show();
    }

    private void loadBitmapFromImage() {
        try {
            InputStream is = getAssets().open("demo.jpg");
            mDemoBitmap = ImageUtils.getBitmap(is, 2000, 2000)
                    .copy(Bitmap.Config.ARGB_8888, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDemoImage.setImageBitmap(mDemoBitmap);
    }
}
