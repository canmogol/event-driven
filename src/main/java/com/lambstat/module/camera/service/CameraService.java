package com.lambstat.module.camera.service;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.camera.event.CameraTestEvent;
import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.module.camera.event.CaptureVideoEvent;
import com.lambstat.module.disc.event.WriteToDiscEvent;

import java.util.HashSet;

public class CameraService extends AbstractService {

    public CameraService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(CaptureImageEvent.class);
            add(CaptureVideoEvent.class);
            add(CameraTestEvent.class);
        }});
    }

    public void handleEvent(CaptureImageEvent event) {
        log("CaptureImageEvent: " + event);
        simulate();
    }

    public void handleEvent(CaptureVideoEvent event) {
        log("CaptureVideoEvent: " + event);
        simulate();
    }

    public void handleEvent(CameraTestEvent event) {
        log("CameraTestEvent: " + event);
        simulate();
        BaseEvent e = new WriteToDiscEvent(
                event,
                new byte[]{1, 2, 3, 4, 5},
                "image0123.jpg"
        );
        log("will send WriteToDiscEvent: " + e);
        broadcast(e);
    }

    private void simulate() {
        try {
            log("simulating work");
            Thread.sleep(3000);
            log("work done");
        } catch (InterruptedException e) {
            error("Could not sleep, exception: " + e.getMessage());
        }
    }

}
