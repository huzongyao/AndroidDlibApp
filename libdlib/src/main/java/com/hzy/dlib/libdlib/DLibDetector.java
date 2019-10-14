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

    public Rect[] detectFromBitmap(Bitmap bitmap) {
        if (mInstance <= 0) {
            init();
        }
        return intArray2Rects(DLibApi.detectFromBitmap(mInstance, bitmap));
    }

    public Rect[] detectFromFile(String filePath) {
        if (mInstance <= 0) {
            init();
        }
        return intArray2Rects(DLibApi.detectFromFile(mInstance, filePath));
    }

    public static Rect[] intArray2Rects(int[] array) {
        int rectLength = array.length / 4;
        Rect[] rectArray = new Rect[rectLength];
        for (int i = 0; i < rectLength; i++) {
            Rect rect = new Rect(array[i * 4], array[i * 4 + 1],
                    array[i * 4 + 2], array[i * 4 + 3]);
            rectArray[i] = rect;
        }
        return rectArray;
    }

    static {
        System.loadLibrary("dlib-api");
    }
}
