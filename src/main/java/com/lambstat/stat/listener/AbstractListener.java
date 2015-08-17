package com.lambstat.stat.listener;

import com.lambstat.stat.event.Event;
import com.lambstat.stat.service.Service;

import java.util.Date;
import java.util.logging.Logger;

public class AbstractListener implements Listener {

    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private Service service;

    public AbstractListener(Service service) {
        this.service = service;
    }

    public void broadcast(Event event) {
        service.broadcast(event);
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
