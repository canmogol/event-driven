package com.lambstat.module.external.webserver.service;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.event.Event;
import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.service.AbstractService;
import com.lambstat.core.service.Service;
import com.lambstat.module.external.webserver.event.WebServerStatusRequestEvent;
import com.lambstat.module.external.webserver.event.WebServerStatusResponseEvent;
import com.lambstat.module.external.webserver.listener.JettyEndpointListener;
import com.lambstat.module.external.webserver.log.WebServerServiceLogger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;


public class WebServerService extends AbstractService {

    private WebServerServiceLogger logger = new WebServerServiceLogger();
    private AbstractEndpointListener webServerListener;

    public WebServerService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(WebServerStatusRequestEvent.class);
        }});
    }

    @Override
    public void run() {
        try {
            Constructor constructor = getConfiguration().getWebServer().getConstructor(Service.class);
            webServerListener = (AbstractEndpointListener) constructor.newInstance(this);
            webServerListener = new JettyEndpointListener(this);
            Thread listenerThread = new Thread(webServerListener);
            listenerThread.start();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.couldNotCreateClass(getConfiguration().getWebServer().getName(), e.getMessage());
        }
        super.run();
    }

    public void handleEvent(WebServerStatusRequestEvent event) {
        logger.willHandleWebServerEvent(event.toString());
        WebServerStatusResponseEvent e = new WebServerStatusResponseEvent(webServerListener.getStatus());
        broadcast(e);
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        super.handleEvent(event);
        try {
            webServerListener.close();
        } catch (IOException e) {
            logger.couldNotCloseWebServer(webServerListener.getClass().getName(), e.getMessage());
        }
    }

    public void handleEvent(Event event) {
        webServerListener.handleEvent(event);
    }

}
