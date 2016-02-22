package com.lambstat.module.external.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class CameraResourceLogger extends BaseLogger {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    public void futureExecutionError(String errorMessage) {
        error("execution exception while getting future, exception: " + errorMessage);
    }
}
