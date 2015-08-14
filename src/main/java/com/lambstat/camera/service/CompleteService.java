package com.lambstat.camera.service;

import com.lambstat.camera.event.CameraTestCompleteEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.service.AbstractService;

import java.util.HashSet;
import java.util.Set;

public class CompleteService<E extends Event> extends AbstractService<E> {

    private Set<Class<? extends Event>> eventsToListen = new HashSet<Class<? extends Event>>() {{
        add(CameraTestCompleteEvent.class);
    }};

    public Set<Class<? extends Event>> eventsToListen() {
        return eventsToListen;
    }

    protected void handleEvent(Event event) {
        log("Generic Event: " + event);
    }

    private void handleEvent(CameraTestCompleteEvent event) {
        log("CameraTestCompleteEvent: " + event);
    }

}
