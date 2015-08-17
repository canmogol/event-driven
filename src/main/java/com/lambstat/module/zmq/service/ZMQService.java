package com.lambstat.module.zmq.service;

import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import com.lambstat.stat.service.AbstractService;
import org.zeromq.ZMQ;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.util.HashSet;

public class ZMQService extends AbstractService {

    private ZMQListener zmqListener = new ZMQListener();

    public ZMQService() {
        super(new HashSet<Class<? extends Event>>());
    }

    @Override
    public void run() {
        Thread listenerThread = new Thread(zmqListener);
        listenerThread.start();
        super.run();
    }

    @Override
    public void handleEvent(ShutdownEvent event) {
        super.handleEvent(event);
        zmqListener.close();
    }

    private class ZMQListener implements Runnable {

        private boolean running = true;
        private String connectionString = "tcp://*:9555";
        private ZMQ.Context context;
        private ZMQ.Socket responder;

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
                } catch (Throwable t) {
                    if (!running) {
                        break;
                    } else {
                        t.printStackTrace();
                    }
                }

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                XMLDecoder xmlDecoder = new XMLDecoder(byteArrayInputStream);
                Object object = xmlDecoder.readObject();
                xmlDecoder.close();

                if (object instanceof Event) {
                    broadcast((Event) object);
                }

                // Send reply back to client
                responder.send("OK".getBytes(), 0);
            }
            if (running) {
                responder.close();
                context.term();
            }
        }

        public void close() {
            running = false;
            responder.close();
            context.term();
        }
    }


}
