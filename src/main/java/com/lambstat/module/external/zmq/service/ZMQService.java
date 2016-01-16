package com.lambstat.module.external.zmq.service;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.event.Event;
import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.external.zmq.listener.ZMQJavaSerializeListener;
import com.lambstat.module.external.zmq.listener.ZMQProtoBufferListener;
import com.lambstat.module.external.zmq.log.ZMQServiceLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ZMQService extends AbstractService {

    private ZMQServiceLogger logger = new ZMQServiceLogger();
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
            } catch (Exception e) {
                logger.couldNotCloseListener(listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        super.handleEvent(event);
        for (AbstractEndpointListener listener : zmqListeners) {
            listener.handleEvent(event);
        }
    }

}
