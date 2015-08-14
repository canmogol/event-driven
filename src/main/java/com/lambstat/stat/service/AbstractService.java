package com.lambstat.stat.service;


import com.lambstat.stat.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public abstract class AbstractService<E extends Event> implements Service<E> {

    private Logger L = Logger.getLogger(getClass().getSimpleName());

    private BlockingQueue<E> queue = new LinkedBlockingQueue<E>();
    private Service<Event> broadcastService;

    protected abstract void handleEvent(Event event);

    public void broadcast(Event event) {
        broadcastService.notify(event);
    }

    @Override
    public void notify(E event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBroadcastService(Service<Event> service) {
        this.broadcastService = service;
    }

    @Override
    public void run() {
        while (true) {
            try {
                E event = queue.take();
                try {
                    Method method = this.getClass().getDeclaredMethod("handleEvent", event.getClass());
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(this, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    handleEvent(event);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void log(String log) {
        L.info(Thread.currentThread().getId() + " " + log);
    }


}
