package com.lambstat.module.jetty.resource;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.listener.AbstractListener;
import org.eclipse.jetty.servlet.ServletContextHandler;

public abstract class BaseResource {
    public void broadcast(BaseEvent event) {
        AbstractListener abstractListener = (AbstractListener) ServletContextHandler.getCurrentContext().getAttribute(AbstractListener.class.getName());
        abstractListener.broadcast(event);
    }
}
