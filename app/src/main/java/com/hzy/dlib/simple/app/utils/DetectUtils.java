package com.hzy.dlib.simple.app.utils;

import com.hzy.dlib.libdlib.DLibDetector;

public class DetectUtils {
    public static void initDLibAsync() {
        new Thread() {
            @Override
            public void run() {
                DLibDetector.INSTANCE.init();
            }
        }.start();
    }
}
