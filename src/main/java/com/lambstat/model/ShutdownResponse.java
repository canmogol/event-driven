package com.lambstat.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso(AbstractResponse.class)
@XmlRootElement
public class ShutdownResponse extends AbstractResponse {

    private SystemStatus systemStatus = SystemStatus.E1_UNKNOWN;

    public ShutdownResponse() {
    }

    public ShutdownResponse(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    public SystemStatus getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }
}
