package com.lambstat.stat.event;

public class ShutdownImmediatelyEvent implements Event{

    private ShutdownEvent shutdownEvent;

    public ShutdownImmediatelyEvent(ShutdownEvent shutdownEvent) {
        this.shutdownEvent = shutdownEvent;
    }

    public ShutdownEvent getShutdownEvent() {
        return shutdownEvent;
    }

}
