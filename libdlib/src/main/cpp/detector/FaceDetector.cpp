//
// Created by huzongyao on 2019/9/2.
//

#include "FaceDetector.h"
#include "ndklog.h"

FaceDetector::FaceDetector() {
    LOGD("Start Get Face Detector!");
    face_detector = get_frontal_face_detector();
    LOGD("End Get Face Detector!");
}

int FaceDetector::startDetect(unsigned char *rgba, unsigned int width, unsigned int height) {
    array2d<unsigned char> img;
    image_view<array2d<unsigned char>> view(img);
    view.set_size((long) height, (long) width);
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            rgb_pixel p;
            p.red = rgba[4 * (width * i + j)];
            p.green = rgba[4 * (width * i + j) + 1];
            p.blue = rgba[4 * (width * i + j) + 2];
            auto gray = (unsigned char) ((p.red * 30 + p.green * 59 + p.blue * 11 + 50) / 100);
            assign_pixel(view[i][j], gray);
        }
    }
    this->last_detected.clear();
    LOGD("Start Detect Face!");
    std::vector<rectangle> rectList = this->face_detector(img);
    LOGD("End Detect: [%d]", rectList.size());
    this->last_detected = rectList;
    return rectList.size();
}

std::vector<rectangle> FaceDetector::getLastDetected() {
    return this->last_detected;
}

