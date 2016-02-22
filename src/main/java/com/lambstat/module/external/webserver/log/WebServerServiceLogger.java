package com.lambstat.module.external.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class WebServerServiceLogger extends BaseLogger {

    public void couldNotCreateClass(String className, String errorMessage) {
        error("Could not create class: " + className + " exception: " + errorMessage);
    }

    public void willHandleWebServerEvent(String event) {
        log("WebServerStatusRequestEvent: " + event);
    }

    public void couldNotCloseWebServer(String webServerListener, String errorMessage) {
        error("Could not close web server listener: " + webServerListener + " exception: " + errorMessage);
    }
}
