package com.hzy.dlib.simple.app.utils;

import com.hzy.dlib.libdlib.DLibDetector;

public class DetectUtils {
    public static final String DEMO_ASSET_NAME = "demo.jpg";

    public static void initDLibAsync() {
        new Thread() {
            @Override
            public void run() {
                DLibDetector.INSTANCE.init();
            }
        }.start();
    }
}
