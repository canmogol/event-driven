package com.lambstat.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso(AbstractResponse.class)
@XmlRootElement
public class LogoutResponse extends AbstractResponse {

    private boolean loggedOut = false;

    public LogoutResponse() {
    }

    public LogoutResponse(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        this.loggedOut = loggedOut;
    }
}
