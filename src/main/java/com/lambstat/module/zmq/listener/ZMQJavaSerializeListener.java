package com.lambstat.module.zmq.listener;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;
import com.lambstat.module.zmq.event.ZMQFailEvent;
import com.lambstat.module.zmq.event.ZMQSuccessEvent;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.io.*;

public class ZMQJavaSerializeListener extends AbstractEndpointListener  {

    private boolean running = true;
    private String connectionString = "tcp://*:9555";
    private ZMQ.Context context;
    private ZMQ.Socket responder;

    public ZMQJavaSerializeListener(Service service) {
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
            } catch (ZMQException e) {
                if (!running) {
                    break;
                } else {
                    log("Could zmq got exception while running, exception: " + e.getMessage());
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
            log("Could not write event to bytes, event: " + baseEvent + " exception: " + e.getMessage());
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
            log("Could not read object from #bytes: " + bytes.length + " exception: " + e.getMessage());
        }
        return object;
    }

    public void close() throws IOException{
        running = false;
        responder.close();
        context.term();
    }

    @Override
    public String getStatus() {
        return String.valueOf(running);
    }

    @Override
    public void handleEvent(Event event) {
    }

}
