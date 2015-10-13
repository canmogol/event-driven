package com.lambstat.core.endpoint;

import com.lambstat.core.event.Event;

public interface EndpointObserver<T extends Event> {

    void handleEvent(T event);

}
