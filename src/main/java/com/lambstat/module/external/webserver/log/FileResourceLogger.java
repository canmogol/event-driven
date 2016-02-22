package com.lambstat.module.external.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class FileResourceLogger extends BaseLogger {

    public void couldNotCloseReader(String errorMessage) {
        error("Could not close buffered reader, exception: " + errorMessage);
    }
}
