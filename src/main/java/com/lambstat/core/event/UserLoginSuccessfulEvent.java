package com.lambstat.core.event;

public class UserLoginSuccessfulEvent extends BaseEvent{

    private String username;

    public UserLoginSuccessfulEvent() {
    }

    public UserLoginSuccessfulEvent(String username) {
        this.username = username;
    }

    public UserLoginSuccessfulEvent(Event parent, String username) {
        super(parent);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
