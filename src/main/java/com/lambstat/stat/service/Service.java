package com.lambstat.stat.service;

import com.lambstat.stat.event.Event;

import java.util.Set;

public interface Service extends Runnable {

    void dropEvents();

    void notify(Event e);

    void broadcast(Event e);

    Set<Class<? extends Event>> getEventsToListen();

    void registerEvents(Set<Class<? extends Event>> events);

    void unregisterEvents(Set<Class<? extends Event>> events);

    void setBroadcastService(Service service);

}
