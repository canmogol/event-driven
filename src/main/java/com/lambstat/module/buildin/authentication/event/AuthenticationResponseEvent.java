package com.lambstat.module.buildin.authentication.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class AuthenticationResponseEvent extends BaseEvent {

    private String username;
    private boolean authenticated;

    public AuthenticationResponseEvent(String username, boolean authenticated) {
        this.username = username;
        this.authenticated = authenticated;
    }

    public AuthenticationResponseEvent(Event parent, String username, boolean authenticated) {
        super(parent);
        this.username = username;
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
