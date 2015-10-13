package com.lambstat.core.listener;

import com.lambstat.core.event.BaseEvent;

import java.io.Closeable;

public interface EndpointListener extends Closeable, Runnable {

    void broadcast(BaseEvent baseEvent);

    String getStatus();

}
