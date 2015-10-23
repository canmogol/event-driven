package com.lambstat.core.endpoint;

import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;

import java.io.Closeable;

public interface EndpointListener extends Closeable, Runnable {

    String getStatus();

    void handleEvent(Event event);

    void broadcast(Event baseEvent);

    void broadcast(Event baseEvent, Class<? extends Event> eventClass, EndpointObserver<Event> endpointObserver);

    Service getService();

}
