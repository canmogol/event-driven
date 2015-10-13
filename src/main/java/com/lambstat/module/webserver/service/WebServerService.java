package com.lambstat.module.webserver.service;

import com.lambstat.core.event.Event;
import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.service.AbstractService;
import com.lambstat.core.service.Service;
import com.lambstat.module.webserver.event.WebServerStatusRequestEvent;
import com.lambstat.module.webserver.event.WebServerStatusResponseEvent;
import com.lambstat.module.webserver.listener.JettyEndpointListener;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;


public class WebServerService extends AbstractService {

    private AbstractEndpointListener webServerListener;
    private Thread listenerThread;

    public WebServerService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(WebServerStatusRequestEvent.class);
        }});
    }

    @Override
    public void run() {
        String currentWebServerImplementation = /*GET THIS FROM PROPERTIES/XML/CLASSPATH ETC.*/"com.lambstat.module.webserver.listener.JettyEndpointListener";
        try {
            Constructor constructor = Class.forName(currentWebServerImplementation).getConstructor(Service.class);
            webServerListener = (AbstractEndpointListener) constructor.newInstance(this);
            webServerListener = new JettyEndpointListener(this);
            listenerThread = new Thread(webServerListener);
            listenerThread.start();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log("Could not create class: " + currentWebServerImplementation + " exception: " + e.getMessage());
        }
        super.run();
    }

    public void handleEvent(WebServerStatusRequestEvent event) {
        log("WebServerStatusRequestEvent: " + event);
        WebServerStatusResponseEvent e = new WebServerStatusResponseEvent(webServerListener.getStatus());
        broadcast(e);
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        super.handleEvent(event);
        try {
            webServerListener.close();
        } catch (IOException e) {
            log("Could not close web server listener: " + webServerListener + " exception: " + e.getMessage());
        }
    }

    public void handleEvent(Event event) {
        webServerListener.handleEvent(event);
    }

}
