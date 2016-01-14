package com.lambstat.module.zmq.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class ZMQServiceLogger extends BaseLogger {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void couldNotCloseListener(String listener, String errorMessage) {
        error("Could not close (zmq) listener: " + listener + " exception: " + errorMessage);
    }

}
