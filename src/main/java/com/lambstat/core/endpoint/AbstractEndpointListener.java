package com.lambstat.core.endpoint;

import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;

import java.util.*;
import java.util.logging.Logger;

public abstract class AbstractEndpointListener implements EndpointListener {

    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private Map<String, EndpointObserver<Event>> map = new HashMap<>();
    private Service service;

    public AbstractEndpointListener(Service service) {
        this.service = service;
    }

    public void handleEvent(Event event) {
        if (map.containsKey(event.getRoot().getUuid())) {
            String uuid = event.getRoot().getUuid();
            if (map.containsKey(uuid)) {
                map.get(uuid).handleEvent(event);
            } else {
                log("there is no observer for this event, uuid: " + event.getUuid());
            }
        }
    }

    @Override
    public void broadcast(Event baseEvent) {
        service.broadcast(baseEvent);
    }

    @Override
    public void broadcast(Event event, Class<? extends Event> eventClass, EndpointObserver<Event> endpointObserver) {
        if (!map.containsKey(event.getUuid())) {
            map.put(event.getUuid(), endpointObserver);
            service.registerEvents(new HashSet<Class<? extends Event>>() {{
                add(eventClass);
            }});
            broadcast(event);
        } else {
            log("end point already registered to event: " + event.getUuid());
        }
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

    public Service getService() {
        return service;
    }
}
