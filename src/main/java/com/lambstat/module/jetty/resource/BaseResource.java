package com.lambstat.module.jetty.resource;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.event.BaseEvent;
import org.eclipse.jetty.servlet.ServletContextHandler;

public abstract class BaseResource {
    public void broadcast(BaseEvent event) {
        AbstractEndpointListener abstractListener = (AbstractEndpointListener) ServletContextHandler.getCurrentContext().getAttribute(AbstractEndpointListener.class.getName());
        abstractListener.broadcast(event);
    }
}
