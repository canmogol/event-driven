package com.lambstat.stat;

import com.lambstat.module.camera.event.CameraTestEvent;
import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.module.camera.event.CaptureVideoEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import com.lambstat.stat.service.EventDispatcher;

import java.util.logging.Logger;


public class EventProducerTester implements Runnable {

    private EventDispatcher eventDispatcher;
    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private int sleep = 2000;

    public EventProducerTester(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(sleep);
            L.info(Thread.currentThread().getId() + " will send CaptureImageEvent");
            Event event = new CaptureImageEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep);
            L.info(Thread.currentThread().getId() + " will send CaptureVideoEvent");
            event = new CaptureVideoEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep);
            L.info(Thread.currentThread().getId() + " will send CameraTestEvent");
            event = new CameraTestEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep);
            L.info(Thread.currentThread().getId() + " will send ShutdownEvent");
            event = new ShutdownEvent();
            eventDispatcher.notify(event);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        L.info(Thread.currentThread().getId() + " tester out");
    }
}
