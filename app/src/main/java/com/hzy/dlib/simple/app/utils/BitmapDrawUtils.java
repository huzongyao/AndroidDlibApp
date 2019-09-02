package com.hzy.dlib.simple.app.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapDrawUtils {

    public static Rect[] getFromLongArray(long[] array) {
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

    public static void drawRectOnBitmap(Bitmap bitmap, Rect[] rects) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        for (Rect rect : rects) {
            canvas.drawRect(rect, paint);
        }
    }
}
