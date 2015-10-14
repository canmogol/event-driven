package com.lambstat.core.service;

import com.lambstat.core.event.*;
import com.lambstat.core.util.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatcher extends AbstractService {

    private Map<Class<? extends Event>, Set<Service>> eventServiceMap = new ConcurrentHashMap<>();
    private Set<Service> services = new HashSet<>();
    private HashSet<Class<? extends Service>> serviceClasses;
    private Configuration configuration;

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
        // discover service classes
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("configuration.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            configuration = (Configuration) jaxbUnmarshaller.unmarshal(inputStream);
            serviceClasses = new HashSet<>();
            serviceClasses.addAll(configuration.getServices());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private Set<Service> registerServices() {
        for (Class<? extends Service> sClass : serviceClasses) {
            try {
                Service service = sClass.newInstance();
                service.setBroadcastService(this);
                service.setConfiguration(configuration);
                Set<Class<? extends Event>> eventsToListen = service.getEventsToListen();
                for (Class<? extends Event> eventClass : eventsToListen) {
                    if (!eventServiceMap.containsKey(eventClass)) {
                        Set<Service> serviceSet = new HashSet<>();
                        serviceSet.add(service);
                        eventServiceMap.put(eventClass, serviceSet);
                    } else {
                        eventServiceMap.get(eventClass).add(service);
                    }
                }
                services.add(service);
            } catch (InstantiationException | IllegalAccessException e) {
                error("Could not create service: " + sClass + " exception: " + e.getMessage());
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
                    error("Could not notify service: " + service + " exception: " + pikachu.getMessage());
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
        handleEvent((BaseEvent) event);
        // then shutdown event dispatcher
        super.handleEvent(event);
    }

    public void handleEvent(EventsRegisteredEvent event) {
        // add this service for each of the event types
        for (Class<? extends Event> eventClass : event.getEvents()) {
            if (!eventServiceMap.containsKey(eventClass)) {
                Set<Service> serviceSet = new HashSet<>();
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
