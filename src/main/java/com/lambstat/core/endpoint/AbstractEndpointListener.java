package com.lambstat.core.endpoint;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;

import java.util.*;
import java.util.logging.Logger;

public abstract class AbstractEndpointListener implements EndpointListener {

    private Logger L = Logger.getLogger(getClass().getSimpleName());
    private Map<Class<? extends Event>, Set<EndpointObserver>> map = new HashMap<>();
    private Service service;

    public AbstractEndpointListener(Service service) {
        this.service = service;
    }

    public void handleEvent(Event event){
        if(map.containsKey(event.getClass())){
            for(EndpointObserver endpointObserver : map.get(event.getClass())){
                endpointObserver.handleEvent(event);
            }
        }
    }

    @Override
    public void broadcast(Event baseEvent) {
        service.broadcast(baseEvent);
    }

    @Override
    public <T extends Event> void broadcast(BaseEvent event, Class<T> eventClass, EndpointObserver endpointObserver) {
        addEndpoint(eventClass, endpointObserver);
        service.registerEvents(new HashSet<Class<? extends Event>>() {{
            add(eventClass);
        }});
        broadcast(event);
    }

    protected <T extends Event> void addEndpoint(Class<T> eventClass, EndpointObserver endpointObserver) {
        if (!map.containsKey(eventClass)) {
            map.put(eventClass, new HashSet<>());
        }
        map.get(eventClass).add(endpointObserver);
    }


    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }


}
