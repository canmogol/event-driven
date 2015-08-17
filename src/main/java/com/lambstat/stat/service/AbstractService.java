package com.lambstat.stat.service;


import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public abstract class AbstractService implements Service {

    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private Set<Class<? extends Event>> eventsToListen = new HashSet<Class<? extends Event>>() {{
        add(ShutdownEvent.class);
    }};
    private BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
    private Service broadcastService;
    private boolean running = true;

    public void handleEvent(Event event) {
        log("Generic Event, will do nothing, event: " + event);
    }

    @Override
    public Set<Class<? extends Event>> eventsToListen() {
        return eventsToListen;
    }

    public void registerEvents(Set<Class<? extends Event>> events) {
        eventsToListen().addAll(events);
    }

    @Override
    public void unregisterEvents(Set<Class<? extends Event>> events) {
        eventsToListen().removeAll(events);
    }

    public void broadcast(Event event) {
        broadcastService.notify(event);
    }

    @Override
    public void notify(Event event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBroadcastService(Service service) {
        this.broadcastService = service;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Event event = queue.take();
                if (!running) {
                    // current event will be discarded
                    break;
                }
                try {
                    Method method = this.getClass().getMethod("handleEvent", event.getClass());
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
                // interrupted, doing down
                e.printStackTrace();
                break;
            }
        }
        log("shutdown");
    }

    public void handleEvent(ShutdownEvent event) {
        log("Shutdown Event, will shutdown this service, event: " + event);
        running = false;// will break the loop at while or if condition
    }

    public void log(String log) {
        L.info(Thread.currentThread().getId() + " [" + getClass().getSimpleName() + "] " + log);
    }

}
