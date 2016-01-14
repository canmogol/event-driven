package com.lambstat.module.camera.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class CameraServiceLogger extends BaseLogger {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void willHandleImageCaptureEvent(String event) {
        log("CaptureImageEvent: " + event);
    }

    public void willHandleVideoCaptureEvent(String event) {
        log("CaptureVideoEvent: " + event);
    }

    public void willHandleCameraCaptureEvent(String event) {
        log("CameraCaptureImageEvent: " + event);
    }

    public void willSendWriteToDiskEvent(String event) {
        log("will send WriteToDiskEvent: " + event);
    }

    public void simulatingWork() {
        log("simulating work");
    }

    public void workDone() {
        log("work done");
    }

    public void couldNotSleep(String errorMessage) {
        error("Could not sleep, exception: " + errorMessage);
    }
}
