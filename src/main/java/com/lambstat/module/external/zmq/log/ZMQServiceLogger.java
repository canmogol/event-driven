package com.lambstat.module.external.zmq.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class ZMQServiceLogger extends BaseLogger {

    public void couldNotCloseListener(String listener, String errorMessage) {
        error("Could not close (zmq) listener: " + listener + " exception: " + errorMessage);
    }

}
