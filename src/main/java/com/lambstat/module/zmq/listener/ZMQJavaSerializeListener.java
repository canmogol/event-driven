package com.lambstat.module.zmq.listener;

import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.service.Service;
import com.lambstat.module.zmq.event.ZMQFailEvent;
import com.lambstat.module.zmq.event.ZMQSuccessEvent;
import com.lambstat.module.zmq.log.ZMQJavaSerializeLogger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.io.*;
import java.nio.channels.ClosedChannelException;

public class ZMQJavaSerializeListener extends AbstractEndpointListener {

    private ZMQJavaSerializeLogger logger = new ZMQJavaSerializeLogger();
    private boolean running = true;
    private String connectionString = "tcp://*:9555";
    private ZMQ.Context context;
    private ZMQ.Socket responder;

    public ZMQJavaSerializeListener(Service service) {
        super(service);
    }

    @Override
    public void run() {
        logger.start();
        context = ZMQ.context(1);
        //  Socket to talk to clients
        responder = context.socket(ZMQ.REP);
        responder.bind(connectionString);
        logger.listening(connectionString);

        while (running && !Thread.currentThread().isInterrupted()) {
            logger.waitingForClients();

            byte[] bytes = new byte[0];
            try {
                // Wait for next request from the client
                bytes = responder.recv(0);
            } catch (ZMQException e) {
                if (!running) {
                    break;
                } else {
                    logger.zmqError(e.getMessage());
                }
            }

            Object object = byteToObject(bytes);
            if (object != null && object instanceof BaseEvent) {
                logger.broadcastEvent();
                broadcast((BaseEvent) object);
                // Send reply back to client
                responder.send(objectToByte(new ZMQSuccessEvent()), 0);
            } else {
                logger.unknownObject(object);
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
            logger.couldNotWriteEvent(baseEvent.toString(), e.getMessage());
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
            logger.couldNotReadObject(bytes.length, e.getMessage());
        }
        return object;
    }

    public void close() throws IOException {
        running = false;
        responder.close();
        try {
            context.close();
        } catch (Exception e) {
            if (!(e instanceof zmq.ZError.IOException)) {
                throw e;
            }else if(e.getCause() != null && e.getCause() instanceof ClosedChannelException){
                logger.channelAlreadyClosed(e.getCause().getMessage());
            }else{
                logger.zmqIOError(e.getMessage());
            }
        }
    }

    @Override
    public String getStatus() {
        return String.valueOf(running);
    }
}
