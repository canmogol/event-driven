package com.lambstat.module.webserver.listener;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;


public class WildflyEmbeddedServerListener extends AbstractEndpointListener {

    public WildflyEmbeddedServerListener(Service service) {
        super(service);
    }

    @Override
    public void handleEvent(Event event) {
        // not implemented yet
    }

    @Override
    public void run() {
       // not implemented yet
    }

    @Override
    public void close() {
        // not implemented yet
    }

    public String getStatus() {
        return null;
    }

}
