//
// Created by huzongyao on 2019/8/29.
//

#ifndef ANDROIDDLIBAPP_DLIBAPI_H
#define ANDROIDDLIBAPP_DLIBAPI_H

#include <jni.h>
#include <dlib/revision.h>
#include <FaceDetector.h>
#include "ndklog.h"

#ifdef __cplusplus
extern "C" {
#endif

#define JNI_FUNC(x) Java_com_hzy_dlib_libdlib_DLibDetector_##x

JNIEXPORT jstring JNICALL
JNI_FUNC(getVersionString)(JNIEnv *env, jclass type);

JNIEXPORT jlong JNICALL
JNI_FUNC(initDetector)(JNIEnv *env, jclass type);

JNIEXPORT jint JNICALL
JNI_FUNC(detectFromBitmap)(JNIEnv *env, jclass type, jlong instance, jobject bitmap);

JNIEXPORT jlongArray JNICALL
JNI_FUNC(getLastDetected)(JNIEnv *env, jclass type, jlong instance);

#ifdef __cplusplus
}
#endif


#endif //ANDROIDDLIBAPP_DLIBAPI_H
