package com.lambstat.stat;

import com.lambstat.camera.event.CameraTestEvent;
import com.lambstat.camera.event.CaptureImageEvent;
import com.lambstat.camera.event.CaptureVideoEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.service.StatService;

import java.util.logging.Logger;


public class EventProducerTester implements Runnable {

    private StatService<Event> statService;
    private Logger L = Logger.getLogger(getClass().getSimpleName());

    public EventProducerTester(StatService<Event> statService) {
        this.statService = statService;
    }

    @Override
    public void run() {
        int sleep = 100;
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(sleep);
                L.info(Thread.currentThread().getId() + " will send CaptureImageEvent");
                Event event = new CaptureImageEvent();
                statService.notify(event);

                Thread.sleep(sleep);
                L.info(Thread.currentThread().getId() + " will send CaptureVideoEvent");
                event = new CaptureVideoEvent();
                statService.notify(event);

                Thread.sleep(sleep);
                L.info(Thread.currentThread().getId() + " will send CameraTestEvent");
                event = new CameraTestEvent();
                statService.notify(event);

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
