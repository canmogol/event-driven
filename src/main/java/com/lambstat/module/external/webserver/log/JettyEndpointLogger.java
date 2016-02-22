package com.lambstat.module.external.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class JettyEndpointLogger extends BaseLogger {

    public void couldNotStartJoin(String errorMessage) {
        error("Could not start/join jetty, exception: " + errorMessage);
    }

    public void couldNotStop(String errorMessage) {
        error("Could not stop jetty, exception: " + errorMessage);
    }
}
