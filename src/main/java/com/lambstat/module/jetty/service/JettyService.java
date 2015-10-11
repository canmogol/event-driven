package com.lambstat.module.jetty.service;

import com.lambstat.core.event.Event;
import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.jetty.event.JettyStatusRequestEvent;
import com.lambstat.module.jetty.event.JettyStatusResponseEvent;
import com.lambstat.module.jetty.listener.JettyListener;

import java.util.HashSet;


public class JettyService extends AbstractService {

    private JettyListener jettyListener;
    private Thread listenerThread;

    public JettyService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(JettyStatusRequestEvent.class);
        }});
    }

    @Override
    public void run() {
        jettyListener = new JettyListener(this);
        listenerThread = new Thread(jettyListener);
        listenerThread.start();
        super.run();
    }

    public void handleEvent(JettyStatusRequestEvent event) {
        log("JettyStatusRequestEvent: " + event);
        JettyStatusResponseEvent e = new JettyStatusResponseEvent(jettyListener.getStatus());
        broadcast(e);
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        super.handleEvent(event);
        jettyListener.close();
    }

}
