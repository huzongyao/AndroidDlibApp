package com.hzy.dlib.simple.app;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
        LogUtils.getConfig().setBorderSwitch(false);
        Utils.init(this);
    }
}
