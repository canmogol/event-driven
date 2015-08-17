package com.lambstat.stat.service;

import com.lambstat.module.camera.service.CameraService;
import com.lambstat.module.disc.service.DiscService;
import com.lambstat.stat.event.*;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatcher extends AbstractService {

    private Map<Class<? extends Event>, Set<Service>> eventServiceMap = new ConcurrentHashMap<>();
    private Set<Service> services = new ConcurrentSet<>();
    private HashSet<Class<? extends Service>> serviceClasses;

    public EventDispatcher() {
        super(new HashSet<Class<? extends Event>>() {{
            add(EventsRegisteredEvent.class);
            add(EventsUnregisteredEvent.class);
        }});
    }

    @Override
    public void run() {
        discoverServices();
        registerServices();
        for (Service service : services) {
            new Thread(service).start();
        }
        super.run();
    }

    private void discoverServices() {
        // discover service classes, xml, properties, arguments, classpath etc.
        serviceClasses = new HashSet<Class<? extends Service>>() {{
            add(CameraService.class);
            add(DiscService.class);
        }};
    }

    private Set<Service> registerServices() {
        for (Class<? extends Service> sClass : serviceClasses) {
            try {
                Service service = sClass.newInstance();
                service.setBroadcastService(this);
                Set<Class<? extends Event>> eventsToListen = service.getEventsToListen();
                for (Class<? extends Event> eventClass : eventsToListen) {
                    if (!eventServiceMap.containsKey(eventClass)) {
                        Set<Service> serviceSet = new ConcurrentSet<>();
                        serviceSet.add(service);
                        eventServiceMap.put(eventClass, serviceSet);
                    } else {
                        eventServiceMap.get(eventClass).add(service);
                    }
                }
                services.add(service);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return services;
    }

    @Override
    public void handleEvent(Event event) {
        if (eventServiceMap.containsKey(event.getClass()) && eventServiceMap.get(event.getClass()).size() > 0) {
            for (Service service : eventServiceMap.get(event.getClass())) {
                try {
                    service.notify(event);
                } catch (Throwable pikachu) {
                    pikachu.printStackTrace();
                }
            }
        } else {
            log("!!! THERE IS NO REGISTERED SERVICE FOR THIS EVENT: " + event);
        }
    }

    public void handleEvent(ShutdownImmediatelyEvent event) {
        // clear all events for services registered to ShutdownEvent
        if (eventServiceMap.containsKey(ShutdownEvent.class)) {
            for (Service service : eventServiceMap.get(ShutdownEvent.class)) {
                service.dropEvents();
            }
        }
        // notify ShutdownEvent
        handleEvent(event.getShutdownEvent());
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        // first shutdown other services
        handleEvent((Event) event);
        // then shutdown event dispatcher
        super.handleEvent(event);
    }

    public void handleEvent(EventsRegisteredEvent event) {
        // add this service for each of the event types
        for (Class<? extends Event> eventClass : event.getEvents()) {
            if (!eventServiceMap.containsKey(eventClass)) {
                Set<Service> serviceSet = new ConcurrentSet<>();
                serviceSet.add(event.getService());
                eventServiceMap.put(eventClass, serviceSet);
            } else {
                eventServiceMap.get(eventClass).add(event.getService());
            }
        }
    }

    public void handleEvent(EventsUnregisteredEvent event) {
        // remove this service for each of the event types
        for (Class<? extends Event> eventClass : event.getEvents()) {
            if (eventServiceMap.containsKey(eventClass)) {
                eventServiceMap.get(eventClass).remove(event.getService());
            }
        }
    }
}
