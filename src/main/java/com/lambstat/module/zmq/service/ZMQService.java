package com.lambstat.module.zmq.service;

import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.zmq.listener.ZMQListener;

import java.util.HashSet;

public class ZMQService extends AbstractService {

    private ZMQListener zmqListener;
    private Thread listenerThread;

    public ZMQService() {
        super(new HashSet<>());
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
