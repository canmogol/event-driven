package com.lambstat.stat.event;

import com.lambstat.stat.service.Service;

import java.util.Set;

public class EventsRegisteredEvent implements Event {

    private final Service service;
    private final Set<Class<? extends Event>> events;

    public EventsRegisteredEvent(Service service, Set<Class<? extends Event>> events) {
        this.service = service;
        this.events = events;
    }

    public Service getService() {
        return service;
    }

    public Set<Class<? extends Event>> getEvents() {
        return events;
    }
}
