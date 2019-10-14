//
// Created by huzongyao on 2019/8/29.
//

#include "dlibapi.h"
#include <android/bitmap.h>
#include <cstdio>
#include <FaceDetector.h>

JNIEXPORT jstring JNICALL
JNI_FUNC(getVersionString)(JNIEnv *env, jclass type) {
    return env->NewStringUTF(DLIB_VERSION);
}

JNIEXPORT jlong JNICALL
JNI_FUNC(initDetector)(JNIEnv *env, jclass type) {
    return (jlong) new FaceDetector();
}

JNIEXPORT jintArray JNICALL
JNI_FUNC(detectFromBitmap)(JNIEnv *env, jclass type, jlong instance, jobject bitmap) {
    jintArray array = env->NewIntArray(0);
    auto *detector = (FaceDetector *) instance;
    if (detector == nullptr) {
        detector = new FaceDetector();
    }
    if (nullptr == bitmap) {
        LOGE("Bitmap is NULL!");
        return array;
    }
    AndroidBitmapInfo info;
    int result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_getInfo Failed [%d]", result);
        return array;
    }
    LOGI("Bitmap Info:[%d x %d]", info.width, info.height);
    void *rgba;
    result = AndroidBitmap_lockPixels(env, bitmap, &rgba);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_lockPixels Failed [%d]", result);
        return array;
    }
    std::vector<rectangle> rectangles = detector->detectFromRGBA(rgba, info.width, info.height);
    array = FaceDetector::rectangle2JintArray(env, rectangles);
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_unlockPixels Failed [%d]", result);
        return array;
    }
    return array;
}

JNIEXPORT jintArray JNICALL
JNI_FUNC(detectFromFile)(JNIEnv *env, jclass type, jlong instance, jstring filePath_) {
    const char *filePath = env->GetStringUTFChars(filePath_, nullptr);
    jintArray array = env->NewIntArray(0);
    auto *detector = (FaceDetector *) instance;
    if (detector == nullptr) {
        detector = new FaceDetector();
    }
    if (nullptr == filePath) {
        LOGE("filePath is NULL!");
        env->ReleaseStringUTFChars(filePath_, filePath);
        return array;
    }
    std::vector<rectangle> rectangles = detector->detectFromFile(filePath);
    array = FaceDetector::rectangle2JintArray(env, rectangles);
    env->ReleaseStringUTFChars(filePath_, filePath);
    return array;
}