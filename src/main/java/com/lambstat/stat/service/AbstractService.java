package com.lambstat.stat.service;


import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.EventsRegisteredEvent;
import com.lambstat.stat.event.EventsUnregisteredEvent;
import com.lambstat.stat.event.ShutdownEvent;

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

    public void handleEvent(Event event) {
        log("Generic Event, will do nothing, event: " + event);
    }

    @Override
    public Set<Class<? extends Event>> getEventsToListen() {
        return eventsToListen;
    }

    public void registerEvents(Set<Class<? extends Event>> events) {
        // add events to list
        getEventsToListen().addAll(events);
        // notify that new events added for this service
        notify(new EventsRegisteredEvent(this, events));
    }

    @Override
    public void unregisterEvents(Set<Class<? extends Event>> events) {
        // remove events from list
        getEventsToListen().removeAll(events);
        // notify that new events removed for this service
        broadcast(new EventsUnregisteredEvent(this, events));
    }

    public void broadcast(Event event) {
        if (broadcastService != null) {
            broadcastService.notify(event);
        }
    }

    @Override
    public void dropEvents() {
        queue.clear();
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

}
