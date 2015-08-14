package com.lambstat.stat.service;

import com.lambstat.stat.event.Event;

import java.util.Set;

public interface Service<E extends Event> extends Runnable {

    void notify(E e);

    Set<Class<? extends Event>> eventsToListen();

    void setBroadcastService(Service<Event> service);

}
