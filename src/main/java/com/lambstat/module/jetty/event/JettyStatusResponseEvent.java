package com.lambstat.module.jetty.event;

import com.lambstat.core.event.BaseEvent;

public class JettyStatusResponseEvent extends BaseEvent {

    private String status;

    public JettyStatusResponseEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
