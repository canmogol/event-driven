package com.lambstat.core.endpoint;

import com.lambstat.core.event.Event;
import com.lambstat.core.log.AbstractEndpointLogger;
import com.lambstat.core.service.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class AbstractEndpointListener implements EndpointListener {

    private AbstractEndpointLogger logger = new AbstractEndpointLogger();

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
                logger.noObserverFoundForEvent(event.toString());
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
            logger.endPointAlreadyRegistered(event.toString());
        }
    }

    public Service getService() {
        return service;
    }

}
