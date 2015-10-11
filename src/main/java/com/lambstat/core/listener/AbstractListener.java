package com.lambstat.core.listener;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.service.Service;

import java.util.Date;
import java.util.logging.Logger;

public abstract class AbstractListener implements Listener {

    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private Service service;

    public AbstractListener(Service service) {
        this.service = service;
    }

    public void broadcast(BaseEvent baseEvent) {
        service.broadcast(baseEvent);
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
