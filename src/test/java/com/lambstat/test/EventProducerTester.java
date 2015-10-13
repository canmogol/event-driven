package com.lambstat.test;

import com.lambstat.core.event.Event;
import com.lambstat.core.service.EventDispatcher;
import com.lambstat.module.camera.event.CameraCaptureImageEvent;

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
/*
            Thread.sleep(sleep);
            log("will send CaptureImageEvent");
            Event event = new CaptureImageEvent();
            eventDispatcher.notify(event);

            Thread.sleep(sleep);
            log("will send CaptureVideoEvent");
            event = new CaptureVideoEvent();
            eventDispatcher.notify(event);

*/
            Thread.sleep(sleep);
            log("will send CameraCaptureImageEvent");
            Event event = new CameraCaptureImageEvent();
            eventDispatcher.notify(event);



      /*
            Thread.sleep(sleep * 10);
            log("will send ShutdownEvent");
            event = new ShutdownEvent();
            // event = new ShutdownImmediatelyEvent(new ShutdownEvent());
            eventDispatcher.notify(event);
*/

        } catch (InterruptedException e) {
            log("Could tester got interrupted, exception: " + e.getMessage());
        }

        log("tester out");
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
