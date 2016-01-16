package com.lambstat.module.buildin.authorization.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class AuthorizationResultEvent extends BaseEvent {

    private String username;
    private String operationName;
    private boolean authorized = false;

    public AuthorizationResultEvent(String username, String operationName, boolean authorized) {
        this.username = username;
        this.operationName = operationName;
        this.authorized = authorized;
    }

    public AuthorizationResultEvent(Event parent, String username, String operationName, boolean authorized) {
        super(parent);
        this.username = username;
        this.operationName = operationName;
        this.authorized = authorized;
    }

    public String getUsername() {
        return username;
    }

    public String getOperationName() {
        return operationName;
    }

    public boolean isAuthorized() {
        return authorized;
    }

}
