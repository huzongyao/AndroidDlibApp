package com.hzy.dlib.simple.app.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hzy.dlib.libdlib.DLibDetector;
import com.hzy.dlib.simple.app.R;
import com.hzy.dlib.simple.app.consts.RouterHub;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.version_text)
    TextView mVersionText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initDetectSDKAsync();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showVersionInfo();
    }

    private void showVersionInfo() {
        mVersionText.setText(String.format("DLib Version: %s", DLibDetector.getVersionString()));
    }

    private void initDetectSDKAsync() {
        new Thread() {
            @Override
            public void run() {
                DLibDetector.INSTANCE.init();
            }
        }.start();
    }

    @OnClick(R.id.detect_from_image)
    public void onDetectImageClicked() {
        ARouter.getInstance().build(RouterHub.DETECT_ACTIVITY).navigation();
    }
}
