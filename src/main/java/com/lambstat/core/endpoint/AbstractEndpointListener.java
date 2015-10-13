package com.lambstat.core.endpoint;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;

import java.util.*;
import java.util.logging.Logger;

public abstract class AbstractEndpointListener implements EndpointListener {

    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private Service service;

    public AbstractEndpointListener(Service service) {
        this.service = service;
    }

    @Override
    public void broadcast(Event baseEvent) {
        service.broadcast(baseEvent);
    }

    @Override
    public <T extends Event> void broadcast(BaseEvent event, Class<T> eventClass, EndpointObserver endpointObserver) {
        Map<Class<T>, List<EndpointObserver>> map = new HashMap<>();
        if (!map.containsKey(eventClass)) {
            List<EndpointObserver> endpointObservers = new ArrayList<EndpointObserver>() {{
                add(endpointObserver);
            }};
            map.put(eventClass, endpointObservers);
        }
        service.registerEvents(new HashSet<Class<? extends Event>>() {{
            add(eventClass);
        }});
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
