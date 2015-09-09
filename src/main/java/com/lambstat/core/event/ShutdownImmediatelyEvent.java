package com.lambstat.core.event;

public class ShutdownImmediatelyEvent extends BaseEvent {

    private ShutdownEvent shutdownEvent;

    public ShutdownImmediatelyEvent(ShutdownEvent shutdownEvent) {
        this.shutdownEvent = shutdownEvent;
    }

    public ShutdownEvent getShutdownEvent() {
        return shutdownEvent;
    }

}
