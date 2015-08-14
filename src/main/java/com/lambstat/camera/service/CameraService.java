package com.lambstat.camera.service;

import com.lambstat.camera.event.CameraTestEvent;
import com.lambstat.camera.event.CaptureImageEvent;
import com.lambstat.camera.event.CaptureVideoEvent;
import com.lambstat.camera.event.CameraTestCompleteEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.service.AbstractService;

import java.util.HashSet;
import java.util.Set;

public class CameraService<E extends Event> extends AbstractService<E> {

    private Set<Class<? extends Event>> eventsToListen = new HashSet<Class<? extends Event>>() {{
        add(CaptureImageEvent.class);
        add(CaptureVideoEvent.class);
        add(CameraTestEvent.class);
    }};

    public Set<Class<? extends Event>> eventsToListen() {
        return eventsToListen;
    }

    protected void handleEvent(Event event) {
        log("Generic Event: " + event);
        simulate();
    }

    private void handleEvent(CaptureImageEvent event) {
        log("CaptureImageEvent: " + event);
        simulate();
    }

    private void handleEvent(CaptureVideoEvent event) {
        log("CaptureVideoEvent: " + event);
        simulate();
    }

    private void handleEvent(CameraTestEvent event) {
        log("CameraTestEvent: " + event);
        simulate();
        Event e = new CameraTestCompleteEvent();
        log("will send CameraTestCompleteEvent: " + e);
        broadcast(e);
    }

    private void simulate() {
        try {
            log("simulating work");
            Thread.sleep(5000);
            log("work done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
