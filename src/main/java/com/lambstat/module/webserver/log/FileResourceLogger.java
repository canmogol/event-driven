package com.lambstat.module.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class FileResourceLogger extends BaseLogger {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void couldNotCloseReader(String errorMessage) {
        error("Could not close buffered reader, exception: " + errorMessage);
    }
}
