package com.lambstat.core.service;


import com.lambstat.core.event.Event;
import com.lambstat.core.event.EventsRegisteredEvent;
import com.lambstat.core.event.EventsUnregisteredEvent;
import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.log.AbstractServiceLogger;
import com.lambstat.core.util.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractService implements Service {

    private static Method handleMethod = null;
    private AbstractServiceLogger logger = new AbstractServiceLogger(getClass().getName());
    private Set<Class<? extends Event>> eventsToListen = new HashSet<Class<? extends Event>>() {{
        add(ShutdownEvent.class);
    }};
    private BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
    private Service broadcastService;
    private boolean running = true;
    private Configuration configuration;

    public AbstractService(Set<Class<? extends Event>> eventsToListen) {
        if (eventsToListen != null) {
            getEventsToListen().addAll(eventsToListen);
        }
    }

    public Method getHandleMethod() {
        if (handleMethod == null) {
            for (Method method : Service.class.getMethods()) {
                if (method.getAnnotation(HandleMethod.class) != null) {
                    handleMethod = method;
                    break;
                }
            }
        }
        return handleMethod;
    }

    public void handleEvent(Event baseEvent) {
        logger.genericEventFired(baseEvent.toString());
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
            logger.couldNotAddEventToQueue(baseEvent.toString(), e.getMessage());
        }
    }

    @Override
    public void setBroadcastService(Service service) {
        this.broadcastService = service;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
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
                    Method method = this.getClass().getMethod(getHandleMethod().getName(), baseEvent.getClass());
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(this, baseEvent);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.couldNotAccessMethod(e.getMessage());
                } catch (NoSuchMethodException e) {
                    handleEvent(baseEvent);
                }
            } catch (InterruptedException e) {
                // interrupted, doing down
                logger.serviceInterrupted(e.getMessage());
                break;
            }
        }
        logger.serviceIsShutdown();
    }

    public void handleEvent(ShutdownEvent event) {
        // first unregister all events for this service
        Set<Class<? extends Event>> allEvents = new HashSet<>();
        allEvents.addAll(getEventsToListen());
        unregisterEvents(allEvents);
        logger.shutdownServiceEvent(event.toString());
        running = false;// will break the loop at while or if condition
    }

}
