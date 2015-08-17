package com.lambstat.zmq;

import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import org.zeromq.ZMQ;

import java.io.*;

public class ZMQClient {

    private ZMQ.Context context;
    private ZMQ.Socket socket;

    public static void main(String[] args) {
        ZMQClient zmqClient = new ZMQClient();
        zmqClient.sendEvents();
    }

    private void sendEvents() {
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.REQ);

        // connect to zmq server
        socket.connect("tcp://localhost:9555");


        // ****************************
        // send a capture image event
        // ****************************
        Event event = new CaptureImageEvent();
        System.out.println("Sending: " + event);
        Object object = sendEvent(event);
        System.out.println("Received: " + object);


        // sleep a few seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ****************************
        // send a shutdown event
        // ****************************
        event = new ShutdownEvent();
        System.out.println("Sending: " + event);
        object = sendEvent(event);
        System.out.println("Received: " + object);


        // close and exit
        socket.close();
        context.term();
    }

    private Object sendEvent(Event event) {
        Object object = null;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
        ) {
            out.writeObject(event);
            byte[] yourBytes = bos.toByteArray();
            // send over
            socket.send(yourBytes, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read response
        byte[] reply = socket.recv(0);
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(reply);
                ObjectInput in = new ObjectInputStream(bis);
        ) {
            object = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

}
