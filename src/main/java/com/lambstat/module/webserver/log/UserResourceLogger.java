package com.lambstat.module.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class UserResourceLogger extends BaseLogger {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    public void anotherUserLoginRequest(String username) {
        log("another user login request: " + username);
    }

    public void userLoggedIn(String username) {
        log("user with username: " + username + " logged in, will broadcast event");
    }

    public void willReturnLoginResponse(String username, boolean isLogged) {
        log("will return another login response, is user with username: " + username + " logged: " + isLogged);
    }
}
