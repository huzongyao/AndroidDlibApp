package com.hzy.dlib.libdlib;

import android.graphics.Bitmap;
import android.graphics.Rect;

public enum DLibDetector {

    INSTANCE;

    private volatile long mInstance;

    DLibDetector() {
    }

    public synchronized void init() {
        if (mInstance <= 0) {
            mInstance = DLibApi.initDetector();
        }
    }

    public int detectFromBitmap(Bitmap bitmap) {
        if (mInstance <= 0) {
            init();
        }
        return DLibApi.detectFromBitmap(mInstance, bitmap);
    }

    public long[] getLastDetected() {
        return DLibApi.getLastDetected(mInstance);
    }

    public Rect[] getLastDetectedRects() {
        long[] array = getLastDetected();
        int rectLength = array.length / 4;
        Rect[] rectArray = new Rect[rectLength];
        for (int i = 0; i < rectLength; i++) {
            Rect rect = new Rect((int) array[i * 4],
                    (int) array[i * 4 + 1],
                    (int) array[i * 4 + 2],
                    (int) array[i * 4 + 3]);
            rectArray[i] = rect;
        }
        return rectArray;
    }

    static {
        System.loadLibrary("dlib-api");
    }
}
