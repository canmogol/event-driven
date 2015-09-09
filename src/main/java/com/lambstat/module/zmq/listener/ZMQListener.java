package com.lambstat.module.zmq.listener;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.module.zmq.event.ZMQFailEvent;
import com.lambstat.module.zmq.event.ZMQSuccessEvent;
import com.lambstat.core.listener.AbstractListener;
import com.lambstat.core.service.Service;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.io.*;

public class ZMQListener extends AbstractListener implements Runnable{

    private boolean running = true;
    private String connectionString = "tcp://*:9555";
    private ZMQ.Context context;
    private ZMQ.Socket responder;

    public ZMQListener(Service service) {
        super(service);
    }

    @Override
    public void run() {
        log("starting context");
        context = ZMQ.context(1);
        //  Socket to talk to clients
        responder = context.socket(ZMQ.REP);
        responder.bind(connectionString);
        log("listening at " + connectionString);

        while (running && !Thread.currentThread().isInterrupted()) {
            log("waiting for next request from client");

            byte[] bytes = new byte[0];
            try {
                // Wait for next request from the client
                bytes = responder.recv(0);
            } catch (ZMQException ex) {
                if (!running) {
                    break;
                } else {
                    ex.printStackTrace();
                }
            }

            Object object = byteToObject(bytes);
            if (object != null && object instanceof BaseEvent) {
                log("broadcast event");
                broadcast((BaseEvent) object);
                // Send reply back to client
                responder.send(objectToByte(new ZMQSuccessEvent()), 0);
            } else {
                log("unknown object, expecting event, object: " + object);
                // Send reply back to client
                responder.send(objectToByte(new ZMQFailEvent()), 0);
            }

        }
        if (running) {
            responder.close();
            context.term();
        }
    }

    private byte[] objectToByte(BaseEvent baseEvent) {
        byte[] bytes = new byte[0];
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
        ) {
            out.writeObject(baseEvent);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private Object byteToObject(byte[] bytes) {
        Object object = null;
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInput in = new ObjectInputStream(bis);
        ) {
            object = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void close() {
        running = false;
        responder.close();
        context.term();
    }
}
