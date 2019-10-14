package com.hzy.dlib.simple.app.activity;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.hzy.dlib.libdlib.DLibDetector;
import com.hzy.dlib.simple.app.R;
import com.hzy.dlib.simple.app.consts.RouterHub;
import com.hzy.dlib.simple.app.utils.BitmapDrawUtils;
import com.hzy.dlib.simple.app.utils.DetectUtils;
import com.hzy.dlib.simple.app.utils.SpaceUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = RouterHub.DETECT_FILE_ACTIVITY)
public class DetectFileActivity extends AppCompatActivity {

    @BindView(R.id.demo_image)
    ImageView mDemoImage;
    private File mDetectFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_file);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        prepareAndDetectFile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void snakeBarShow(String msg) {
        SnackbarUtils.with(mDemoImage).setMessage(msg).show();
    }

    private void prepareAndDetectFile() {
        new Thread() {
            @Override
            public void run() {
                try {
                    File dir = SpaceUtils.getUsableFilePath();
                    mDetectFile = new File(dir, DetectUtils.DEMO_ASSET_NAME);
                    if (!mDetectFile.exists()) {
                        FileIOUtils.writeFileFromIS(mDetectFile,
                                getAssets().open(DetectUtils.DEMO_ASSET_NAME));
                    }
                    loadAndShowImage(null);
                    Rect[] faces = DLibDetector.INSTANCE.detectFromFile(mDetectFile.getPath());
                    loadAndShowImage(faces);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void loadAndShowImage(Rect[] faces) {
        Bitmap bitmap = ImageUtils.getBitmap(mDetectFile);
        Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), true);
        bitmap.recycle();
        if (faces != null) {
            BitmapDrawUtils.drawRectOnBitmap(newBitmap, faces);
        }
        mDemoImage.post(() -> {
            mDemoImage.setImageBitmap(newBitmap);
            if (faces != null) {
                snakeBarShow(faces.length + " Faces Detected!!");
            }
        });
    }
}
