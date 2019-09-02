package com.hzy.dlib.libdlib;

import android.graphics.Bitmap;

public enum DLibDetector {

    INSTANCE;

    private volatile long mInstance;

    DLibDetector() {
    }

    public synchronized void init() {
        if (mInstance <= 0) {
            mInstance = initDetector();
        }
    }

    public int detectFromBitmap(Bitmap bitmap) {
        return detectFromBitmap(mInstance, bitmap);
    }

    public long[] getLastDetected() {
        return getLastDetected(mInstance);
    }

    public static native String getVersionString();

    private static native long initDetector();

    private static native int detectFromBitmap(long instance, Bitmap bitmap);

    private static native long[] getLastDetected(long instance);

    static {
        System.loadLibrary("dlib-api");
    }
}
