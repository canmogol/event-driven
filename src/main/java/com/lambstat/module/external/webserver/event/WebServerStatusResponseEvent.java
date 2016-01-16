package com.lambstat.module.external.webserver.event;

import com.lambstat.core.event.BaseEvent;

public class WebServerStatusResponseEvent extends BaseEvent {

    private String status;

    public WebServerStatusResponseEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
