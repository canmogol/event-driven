package com.lambstat.module.webserver.resource;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.listener.AbstractEndpointListener;
import com.lambstat.core.listener.EndpointListener;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.Date;
import java.util.logging.Logger;

public abstract class BaseResource {

    private Logger L = Logger.getLogger(getClass().getSimpleName());

    /**
     * Broadcast event via web server, web server will deliver this event to its own service which is in the event loop
     *
     * @param event
     */
    public void broadcast(BaseEvent event) {
        EndpointListener endpointListener = (AbstractEndpointListener) ServletContextHandler.getCurrentContext().getAttribute(EndpointListener.class.getName());
        endpointListener.broadcast(event);
    }

    public void log(String log) {
        L.info("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

    public void error(String log){
        L.severe("[" + new Date() + "] [" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
