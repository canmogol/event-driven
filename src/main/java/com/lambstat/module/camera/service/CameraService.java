package com.lambstat.module.camera.service;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.camera.event.CameraCaptureImageEvent;
import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.module.camera.event.CaptureVideoEvent;
import com.lambstat.module.camera.log.CameraServiceLogger;
import com.lambstat.module.disk.event.WriteToDiskEvent;

import java.util.HashSet;

public class CameraService extends AbstractService {

    private CameraServiceLogger logger = new CameraServiceLogger();

    public CameraService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(CaptureImageEvent.class);
            add(CaptureVideoEvent.class);
            add(CameraCaptureImageEvent.class);
        }});
    }

    public void handleEvent(CaptureImageEvent event) {
        logger.willHandleImageCaptureEvent(event.toString());
        simulate();
    }

    public void handleEvent(CaptureVideoEvent event) {
        logger.willHandleVideoCaptureEvent(event.toString());
        simulate();
    }

    public void handleEvent(CameraCaptureImageEvent event) {
        logger.willHandleCameraCaptureEvent(event.toString());
        simulate();
        BaseEvent e = new WriteToDiskEvent(
                event,
                new byte[]{1, 2, 3, 4, 5},
                "image0123.jpg"
        );
        logger.willSendWriteToDiskEvent(e.toString());
        broadcast(e);
    }

    private void simulate() {
        try {
            logger.simulatingWork();
            Thread.sleep(3000);
            logger.workDone();
        } catch (InterruptedException e) {
            logger.couldNotSleep(e.getMessage());
        }
    }

}
