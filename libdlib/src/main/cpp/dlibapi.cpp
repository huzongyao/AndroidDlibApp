//
// Created by huzongyao on 2019/8/29.
//

#include "dlibapi.h"
#include <android/bitmap.h>
#include <cstdio>
#include <FaceDetector.h>

JNIEXPORT jstring JNICALL
JNI_FUNC(getVersionString)(JNIEnv *env, jclass type) {
    char versionString[1024];
    sprintf(versionString, "%d.%d.%d", DLIB_MAJOR_VERSION, DLIB_MINOR_VERSION, DLIB_PATCH_VERSION);
    LOGD("Get Version String:[%s]", versionString);
    return env->NewStringUTF(versionString);
}

JNIEXPORT jlong JNICALL
JNI_FUNC(initDetector)(JNIEnv *env, jclass type) {
    return (jlong) new FaceDetector();;
}

JNIEXPORT jint JNICALL
JNI_FUNC(detectFromBitmap)(JNIEnv *env, jclass type, jlong instance, jobject bitmap) {
    auto *detector = (FaceDetector *) instance;
    if (detector == nullptr) {
        detector = new FaceDetector();
    }
    if (nullptr == bitmap) {
        LOGE("Bitmap is NULL!");
        return -1;
    }
    AndroidBitmapInfo info;
    int result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_getInfo Failed [%d]", result);
        return result;
    }
    LOGI("Bitmap Info:[%d x %d]", info.width, info.height);
    unsigned char *rgba;
    result = AndroidBitmap_lockPixels(env, bitmap, (void **) &rgba);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_lockPixels Failed [%d]", result);
        return result;
    }
    detector->startDetect(rgba, info.width, info.height);
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_unlockPixels Failed [%d]", result);
        return result;
    }
    return 0;
}

JNIEXPORT jlongArray JNICALL
JNI_FUNC(getLastDetected)(JNIEnv *env, jclass type, jlong instance) {
    auto *detector = (FaceDetector *) instance;
    if (detector == nullptr) {
        return env->NewLongArray(0);
    }
    std::vector<rectangle> last_detected = detector->getLastDetected();
    if (last_detected.empty()) {
        return env->NewLongArray(0);
    }
    unsigned int bufferSize = last_detected.size() * 4;
    jlongArray array = env->NewLongArray(bufferSize);
    auto *buffer = (jlong *) malloc(sizeof(jlong) * bufferSize);
    auto it = last_detected.begin();
    int index = 0;
    for (; it != last_detected.end(); ++it) {
        long l = (*it).left();
        long r = (*it).right();
        long t = (*it).top();
        long b = (*it).bottom();
        buffer[index * 4] = l;
        buffer[index * 4 + 1] = t;
        buffer[index * 4 + 2] = r;
        buffer[index * 4 + 3] = b;
        index++;
    }
    LOGD("Get Detected Rects: [%d]", bufferSize);
    env->SetLongArrayRegion(array, 0, bufferSize, buffer);
    free(buffer);
    return array;
}