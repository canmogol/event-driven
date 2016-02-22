package com.lambstat.module.external.webserver.log;

import com.lambstat.core.log.BaseLogger;

import java.util.logging.Logger;

public class AsyncResourceLogger extends BaseLogger {

    public void okMethodCalled() {
        log("ok method call begin");
    }

    public void willSleep() {
        log("will sleep");
    }

    public void couldNotSleep(String errorMessage) {
        error("Could not sleep, e: " + errorMessage);
    }

    public void wokeUp() {
        log("woke up");
    }

    public void loginSuccessful(String username) {
        log("logged in successfully, username: " + username);
    }

    public void couldNotLoggedIn(String username) {
        log("could not logged in, username: " + username);
    }

    public void willReturnResponse(String username, boolean isLogged) {
        log("will return response, is user: " + username + " logged: " + isLogged);
    }

    public void okMethodCallEnd() {
        log("ok method call end");
    }
}
