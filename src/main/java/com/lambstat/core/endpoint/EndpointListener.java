package com.lambstat.core.endpoint;

import com.lambstat.core.event.Event;

import java.io.Closeable;

public interface EndpointListener extends Closeable, Runnable {

    String getStatus();

    void handleEvent(Event event);

    void broadcast(Event baseEvent);

    public void broadcast(Event baseEvent, Class<? extends Event> eventClass, EndpointObserver<Event> endpointObserver);

}
