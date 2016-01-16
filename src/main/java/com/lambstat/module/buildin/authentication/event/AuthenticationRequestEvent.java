package com.lambstat.module.buildin.authentication.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class AuthenticationRequestEvent extends BaseEvent {

    private String username;
    private String password;

    public AuthenticationRequestEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthenticationRequestEvent(Event parent, String username, String password) {
        super(parent);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
