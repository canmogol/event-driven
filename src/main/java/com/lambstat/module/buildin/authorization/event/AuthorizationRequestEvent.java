package com.lambstat.module.buildin.authorization.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class AuthorizationRequestEvent extends BaseEvent {

    private String username;
    private String operationName;

    public AuthorizationRequestEvent(String username, String operationName) {
        this.username = username;
        this.operationName = operationName;
    }

    public AuthorizationRequestEvent(Event parent, String username, String operationName) {
        super(parent);
        this.username = username;
        this.operationName = operationName;
    }

    public String getUsername() {
        return username;
    }

    public String getOperationName() {
        return operationName;
    }
}
