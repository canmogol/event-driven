package com.lambstat.module.zmq.service;

import com.lambstat.module.zmq.listener.ZMQListener;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import com.lambstat.stat.service.AbstractService;

import java.util.HashSet;

public class ZMQService extends AbstractService {

    private ZMQListener zmqListener;
    private Thread listenerThread;

    public ZMQService() {
        super(new HashSet<Class<? extends Event>>());
    }

    @Override
    public void run() {
        zmqListener = new ZMQListener(this);
        listenerThread = new Thread(zmqListener);
        listenerThread.start();
        super.run();
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        super.handleEvent(event);
        zmqListener.close();
    }

}
