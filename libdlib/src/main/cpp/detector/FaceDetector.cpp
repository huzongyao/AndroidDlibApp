//
// Created by huzongyao on 2019/9/2.
//

#include <dlib/image_loader/load_image.h>
#include "FaceDetector.h"
#include "ndklog.h"

FaceDetector::FaceDetector() {
    LOGD("Start Get Face Detector!");
    face_detector = get_frontal_face_detector();
    LOGD("End Get Face Detector!");
}

std::vector<rectangle>
FaceDetector::detectFromRGBA(void *rgba, unsigned int width, unsigned int height) {
    array2d<unsigned char> img;
    image_view<array2d<unsigned char>> view(img);
    view.set_size((long) height, (long) width);
    auto *data = (unsigned char *) rgba;
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            rgb_pixel p;
            p.red = data[4 * (width * i + j)];
            p.green = data[4 * (width * i + j) + 1];
            p.blue = data[4 * (width * i + j) + 2];
            // convert rgb to gray
            auto gray = (unsigned char) ((p.red * 30 + p.green * 59 + p.blue * 11 + 50) / 100);
            assign_pixel(view[i][j], gray);
        }
    }
    LOGD("Start Detect Face!");
    std::vector<rectangle> rectList = this->face_detector(img);
    LOGD("End Detect: [%d]", rectList.size());
    return rectList;
}

std::vector<rectangle>
FaceDetector::detectFromFile(const char *filePath) {
    array2d<unsigned char> img;
    load_image(img, filePath);
    LOGD("Start Detect Face!");
    std::vector<rectangle> rectList = this->face_detector(img);
    LOGD("End Detect: [%d]", rectList.size());
    return rectList;
}

jintArray
FaceDetector::rectangle2JintArray(JNIEnv *env, std::vector<rectangle> &rectangles) {
    if (rectangles.empty()) {
        return env->NewIntArray(0);
    }
    unsigned int bufferSize = rectangles.size() * 4;
    jintArray array = env->NewIntArray(bufferSize);
    auto *buffer = (jint *) malloc(sizeof(jint) * bufferSize);
    int index = 0;
    for (auto it: rectangles) {
        buffer[index * 4] = it.left();
        buffer[index * 4 + 1] = it.top();
        buffer[index * 4 + 2] = it.right();
        buffer[index * 4 + 3] = it.bottom();
        index++;
    }
    LOGD("Get Detected Rects: [%d]", bufferSize);
    env->SetIntArrayRegion(array, 0, bufferSize, buffer);
    free(buffer);
    return array;
}
