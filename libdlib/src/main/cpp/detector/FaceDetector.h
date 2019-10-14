//
// Created by huzongyao on 2019/9/2.
//

#ifndef ANDROIDDLIBAPP_FACEDETECTOR_H
#define ANDROIDDLIBAPP_FACEDETECTOR_H

#include <dlib/image_processing/frontal_face_detector.h>
#include <jni.h>

using namespace dlib;

class FaceDetector {
public:
    FaceDetector();

    std::vector<rectangle>
    detectFromRGBA(void *rgba, unsigned int width, unsigned int height);

    std::vector<rectangle>
    detectFromFile(const char *filePath);

    static jintArray
    rectangle2JintArray(JNIEnv *env, std::vector<rectangle> &rectangles);

private:
    frontal_face_detector face_detector;
};


#endif //ANDROIDDLIBAPP_FACEDETECTOR_H
