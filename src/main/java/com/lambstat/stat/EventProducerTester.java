package com.lambstat.stat;

import com.lambstat.module.camera.event.CameraTestEvent;
import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.module.camera.event.CaptureVideoEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import com.lambstat.stat.service.EventDispatcher;

import java.util.Date;
import java.util.logging.Logger;


public class EventProducerTester implements Runnable {

    private EventDispatcher eventDispatcher;
    private Logger L = Logger.getLogger(getClass().getSimpleName());

    public EventProducerTester(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void run() {

        try {
            int sleep = 1000;
            Thread.sleep(sleep);
            log("will send CaptureImageEvent");
            Event event = new CaptureImageEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep);
            log("will send CaptureVideoEvent");
            event = new CaptureVideoEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep);
            log("will send CameraTestEvent");
            event = new CameraTestEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep * 10);
            log("will send ShutdownEvent");
            event = new ShutdownEvent();
            // event = new ShutdownImmediatelyEvent(new ShutdownEvent());
            eventDispatcher.notify(event);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log("tester out");
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
