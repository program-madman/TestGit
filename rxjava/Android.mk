
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    librxjava:rxjava-1.1.3.jar

include $(BUILD_MULTI_PREBUILT)

