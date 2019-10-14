package com.hzy.dlib.libdlib;

import android.graphics.Bitmap;

public class DLibApi {

    public static native String getVersionString();

    public static native long initDetector();

    public static native int[] detectFromBitmap(long instance, Bitmap bitmap);

    public static native int[] detectFromFile(long instance, String filePath);

    static {
        System.loadLibrary("dlib-api");
    }
}
