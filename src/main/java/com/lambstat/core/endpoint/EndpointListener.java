package com.lambstat.core.endpoint;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

import java.io.Closeable;

public interface EndpointListener extends Closeable, Runnable {

    String getStatus();

    void handleEvent(Event event);

    void broadcast(Event baseEvent);

    <T extends Event> void broadcast(BaseEvent event, Class<T> eventClass, EndpointObserver endpointObserver);

}
