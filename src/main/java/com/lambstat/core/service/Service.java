package com.lambstat.core.service;

import com.lambstat.core.util.Configuration;
import com.lambstat.core.event.Event;

import java.util.Set;

public interface Service extends Runnable {

    @HandleMethod
    void handleEvent(Event event);

    void dropEvents();

    void notify(Event e);

    void broadcast(Event e);

    Set<Class<? extends Event>> getEventsToListen();

    void registerEvents(Set<Class<? extends Event>> events);

    void unregisterEvents(Set<Class<? extends Event>> events);

    void setBroadcastService(Service service);

    void setConfiguration(Configuration configuration);

    Configuration getConfiguration();

}
