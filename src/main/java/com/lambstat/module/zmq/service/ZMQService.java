package com.lambstat.module.zmq.service;

import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.listener.AbstractEndpointListener;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.zmq.listener.ZMQJavaSerializeListener;
import com.lambstat.module.zmq.listener.ZMQProtoBufferListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ZMQService extends AbstractService {

    private List<AbstractEndpointListener> zmqListeners = new ArrayList<>();

    public ZMQService() {
        super(new HashSet<>());
    }

    @Override
    public void run() {
        ZMQJavaSerializeListener zmqJavaSerializeListener = new ZMQJavaSerializeListener(this);
        zmqListeners.add(zmqJavaSerializeListener);

        ZMQProtoBufferListener zmqProtoBufferListener = new ZMQProtoBufferListener(this);
        zmqListeners.add(zmqJavaSerializeListener);

        new Thread(zmqJavaSerializeListener).start();
        new Thread(zmqProtoBufferListener).start();

        super.run();
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        super.handleEvent(event);
        for (AbstractEndpointListener listener : zmqListeners) {
            try {
                listener.close();
            } catch (IOException e) {
                error("Could not close (zmq) listener: " + listener + " exception: " + e.getMessage());
            }
        }
    }

}
