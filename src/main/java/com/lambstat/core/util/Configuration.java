package com.lambstat.core.util;

import com.lambstat.core.endpoint.EndpointListener;
import com.lambstat.core.service.Service;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Configuration {

    private List<Class<? extends Service>> services = new ArrayList<>();
    private Class<? extends EndpointListener> webServer;
    private int webServerPort;

    public List<Class<? extends Service>> getServices() {
        return services;
    }

    public void setServices(List<Class<? extends Service>> services) {
        this.services = services;
    }

    public Class<? extends EndpointListener> getWebServer() {
        return webServer;
    }

    public void setWebServer(Class<? extends EndpointListener> webServer) {
        this.webServer = webServer;
    }

    public int getWebServerPort() {
        return webServerPort;
    }

    public void setWebServerPort(int webServerPort) {
        this.webServerPort = webServerPort;
    }
}
