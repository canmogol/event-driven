package com.lambstat.module.external.zmq.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class ZMQProtoBufferLogger extends BaseLogger {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void start() {
        log("starting context");
    }

    public void listening(String connectionString) {
        log("listening at " + connectionString);
    }

    public void waitingForClients() {
        log("waiting for next request from client");
    }

    public void nextRequestType(String requestType) {
        log("new request with type: " + requestType);
    }

    public void zmqError(String errorMessage) {
        error("ZMQ got exception while running, exception: " + errorMessage);
    }

    public void userRequest(String username) {
        log("another user login REQUEST: " + username);
    }

    public void userLoggedIn() {
        log("user logged in, will broadcast event");
    }

    public void nullRequest() {
        error("request is null");
    }

    public void loginResponse(boolean isLogged) {
        log("another user login RESPONSE: " + isLogged);
    }
}
