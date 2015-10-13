package com.lambstat.core.endpoint;

import com.lambstat.core.event.Event;

public interface EndpointObserver {

    void handleEvent(Event event);

}
