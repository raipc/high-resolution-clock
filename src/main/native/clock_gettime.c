#define _POSIX_C_SOURCE 199309L
#include <jni.h>
#include <time.h>
#include "clock_gettime.h"

JNIEXPORT jlong JNICALL Java_com_axibase_clock_HighResolutionClock_clock_1gettime(JNIEnv *env, jobject thisObj) {
    struct timespec tp;
    clock_gettime(CLOCK_REALTIME, &tp);
    return (tp.tv_sec << 32) | (tp.tv_nsec);
}
