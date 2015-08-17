package com.lambstat.stat.service;

import com.lambstat.module.camera.service.CameraService;
import com.lambstat.module.disc.service.DiscService;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import io.netty.util.internal.ConcurrentSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatcher extends AbstractService {

    private Set<Class<? extends Event>> eventsToListen = new HashSet<>();

    private Map<Class<? extends Event>, Set<Service>> eventServiceMap = new ConcurrentHashMap<>();
    private Set<Service> services = new ConcurrentSet<>();
    private HashSet<Class<? extends Service>> serviceClasses;


    @Override
    public Set<Class<? extends Event>> eventsToListen() {
        return eventsToListen;
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
                Set<Class<? extends Event>> eventsToListen = service.eventsToListen();
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
        if (eventServiceMap.containsKey(event.getClass())) {
            for (Service service : eventServiceMap.get(event.getClass())) {
                try {
                    service.notify(event);
                } catch (Throwable pikachu) {
                    pikachu.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        // first shutdown other services
        handleEvent((Event) event);
        // then shutdown event dispatcher
        super.handleEvent(event);
    }
}
