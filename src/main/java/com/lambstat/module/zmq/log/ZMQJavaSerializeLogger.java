package com.lambstat.module.zmq.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class ZMQJavaSerializeLogger extends BaseLogger {

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

    public void zmqError(String errorMessage) {
        error("zmq got exception while running, exception: " + errorMessage);
    }

    public void broadcastEvent() {
        log("broadcast event");
    }

    public void unknownObject(Object object) {
        error("unknown object, expecting event, object: " + object);
    }

    public void couldNotWriteEvent(String event, String errorMessage) {
        error("Could not write event to bytes, event: " + event + " exception: " + errorMessage);
    }

    public void couldNotReadObject(int byteLength, String errorMessage) {
        error("Could not read object from #bytes: " + byteLength + " exception: " + errorMessage);
    }

    public void zmqIOError(String errorMessage) {
        error("got ZMQ IO Error, exception: " + errorMessage);
    }

    public void channelAlreadyClosed(String message) {
        log("channel already closed, message: " + message);
    }
}
