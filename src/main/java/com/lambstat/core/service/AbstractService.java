package com.lambstat.core.service;


import com.lambstat.core.event.Event;
import com.lambstat.core.event.EventsRegisteredEvent;
import com.lambstat.core.event.EventsUnregisteredEvent;
import com.lambstat.core.event.ShutdownEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
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

    public AbstractService(Set<Class<? extends Event>> eventsToListen) {
        if (eventsToListen != null) {
            getEventsToListen().addAll(eventsToListen);
        }
    }

    public void handleEvent(Event baseEvent) {
        log("Generic Event, will do nothing, baseEvent: " + baseEvent);
    }

    @Override
    public Set<Class<? extends Event>> getEventsToListen() {
        return eventsToListen;
    }

    public void registerEvents(Set<Class<? extends Event>> events) {
        // add events to list
        getEventsToListen().addAll(events);
        // new events added for this service
        broadcast(new EventsRegisteredEvent(this, events));
    }

    @Override
    public void unregisterEvents(Set<Class<? extends Event>> events) {
        // remove events from list
        getEventsToListen().removeAll(events);
        // notify that new events removed for this service
        broadcast(new EventsUnregisteredEvent(this, events));
    }

    public void broadcast(Event baseEvent) {
        if (broadcastService != null) {
            broadcastService.notify(baseEvent);
        }
    }

    @Override
    public void dropEvents() {
        queue.clear();
    }

    @Override
    public void notify(Event baseEvent) {
        try {
            queue.put(baseEvent);
        } catch (InterruptedException e) {
            error("Could not add event to queue, event: " + baseEvent + " exception: " + e.getMessage());
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
                Event baseEvent = queue.take();
                if (!running) {
                    // current baseEvent will be discarded
                    break;
                }
                try {
                    Method method = this.getClass().getMethod("handleEvent", baseEvent.getClass());
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(this, baseEvent);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    error("Could not access/invoke method, exception: " + e.getMessage());
                } catch (NoSuchMethodException e) {
                    handleEvent(baseEvent);
                }
            } catch (InterruptedException e) {
                // interrupted, doing down
                error("service interrupted, exception: " + e.getMessage());
                break;
            }
        }
        log("shutdown");
    }

    public void handleEvent(ShutdownEvent event) {
        // first unregister all events for this service
        Set<Class<? extends Event>> allEvents = new HashSet<>();
        allEvents.addAll(getEventsToListen());
        unregisterEvents(allEvents);
        log("Shutdown Event, will shutdown this service, event: " + event);
        running = false;// will break the loop at while or if condition
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

    public void error(String log) {
        L.severe("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
