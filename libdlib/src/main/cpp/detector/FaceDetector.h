//
// Created by huzongyao on 2019/9/2.
//

#ifndef ANDROIDDLIBAPP_FACEDETECTOR_H
#define ANDROIDDLIBAPP_FACEDETECTOR_H

#include <dlib/image_processing/frontal_face_detector.h>

using namespace dlib;

class FaceDetector {
public:
    FaceDetector();

    int startDetect(unsigned char *rgba, unsigned int width, unsigned int height);

    std::vector<rectangle> getLastDetected();

private:
    frontal_face_detector face_detector;
    std::vector<rectangle> last_detected;
};


#endif //ANDROIDDLIBAPP_FACEDETECTOR_H
