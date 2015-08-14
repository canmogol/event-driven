package com.lambstat.stat.service;

import com.lambstat.camera.service.CameraService;
import com.lambstat.camera.service.CompleteService;
import com.lambstat.stat.event.Event;
import io.netty.util.internal.ConcurrentSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StatService<E extends Event> extends AbstractService<E> {

    private Set<Class<? extends Event>> eventsToListen = new HashSet<Class<? extends Event>>();

    private Map<Class<? extends Event>, Set<Service>> eventServiceMap = new ConcurrentHashMap<>();
    private Set<Service> services = new ConcurrentSet<>();
    private Map<Service, Thread> serviceThreadMap = new ConcurrentHashMap<>();
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
            Thread serviceThread = new Thread(service);
            serviceThreadMap.put(service, serviceThread);
            serviceThread.start();
        }
        super.run();
    }

    private void discoverServices() {
        // discover service classes, xml, properties, arguments, classpath etc.
        serviceClasses = new HashSet<Class<? extends Service>>() {{
            add(CameraService.class);
            add(CompleteService.class);
        }};
    }

    @SuppressWarnings("unchecked")
    private Set<Service> registerServices() {
        for (Class<? extends Service> sClass : serviceClasses) {
            try {
                Service service = sClass.newInstance();
                service.setBroadcastService(this);
                // service is raw type, eventsToListen downs to "Set" so unchecked annotation added
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
    @SuppressWarnings("unchecked")
    protected void handleEvent(Event event) {
        if (eventServiceMap.containsKey(event.getClass())) {
            for (Service service : eventServiceMap.get(event.getClass())) {
                try {
                    // service is raw type, unchecked annotation added for notify method call
                    service.notify(event);
                } catch (Throwable pikachu) {
                    pikachu.printStackTrace();
                }
            }
        }
    }

}
