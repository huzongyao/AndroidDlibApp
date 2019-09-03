package com.hzy.dlib.simple.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.hzy.dlib.libdlib.DLibDetector;
import com.hzy.dlib.simple.app.R;
import com.hzy.dlib.simple.app.consts.RequestCode;
import com.hzy.dlib.simple.app.consts.RouterHub;
import com.hzy.dlib.simple.app.utils.BitmapDrawUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = RouterHub.DETECT_ACTIVITY)
public class DetectActivity extends AppCompatActivity {

    @BindView(R.id.demo_image)
    ImageView mDemoImage;
    private Bitmap mDemoBitmap;

    public static final int MAX_BITMAP_SIZE = 2000;

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
            Bitmap bitmap = ImageUtils.getBitmap(is, MAX_BITMAP_SIZE, MAX_BITMAP_SIZE);
            mDemoBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDemoImage.setImageBitmap(mDemoBitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RequestCode.CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        if (uri != null) {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        } else {
                            Bundle bundleExtras = data.getExtras();
                            if (bundleExtras != null) {
                                bitmap = bundleExtras.getParcelable("data");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        mDemoBitmap.recycle();
                        bitmap = ImageUtils.compressBySampleSize(bitmap,
                                MAX_BITMAP_SIZE, MAX_BITMAP_SIZE, true);
                        mDemoBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        bitmap.recycle();
                        mDemoImage.setImageBitmap(mDemoBitmap);
                        detectFromBitmap();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.button_choose_image)
    public void onChooseImageClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent = Intent.createChooser(intent, getString(R.string.choose_a_image));
        startActivityForResult(intent, RequestCode.CHOOSE_IMAGE);
    }
}
