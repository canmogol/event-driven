package com.lambstat.zmq;

import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.stat.event.Event;
import com.lambstat.stat.event.ShutdownEvent;
import org.zeromq.ZMQ;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;

public class ZMQClient {

    public static void main(String[] args) {
        ZMQClient zmqClient = new ZMQClient();
        zmqClient.sendEvent();
    }

    private void sendEvent() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REQ);

        // connect to zmq server
        socket.connect("tcp://localhost:9555");


        // ****************************
        // send a capture image event
        // ****************************
        Event event = new CaptureImageEvent();
        // serialize event object to xml
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(byteArrayOutputStream);
        xmlEncoder.writeObject(event);
        xmlEncoder.close();
        String xml = byteArrayOutputStream.toString();

        // send over
        System.out.println("Sending:\n" + xml);
        socket.send(xml.getBytes(), 0);

        // read response
        byte[] reply = socket.recv(0);
        System.out.println("Received:\n" + new String(reply));


        // sleep a few seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // ****************************
        // send shutdown event
        // ****************************
        event = new ShutdownEvent();
        // serialize event object to xml
        byteArrayOutputStream = new ByteArrayOutputStream();
        xmlEncoder = new XMLEncoder(byteArrayOutputStream);
        xmlEncoder.writeObject(event);
        xmlEncoder.close();
        xml = byteArrayOutputStream.toString();

        // send over
        System.out.println("Sending:\n" + xml);
        socket.send(xml.getBytes(), 0);

        // read response
        reply = socket.recv(0);
        System.out.println("Received:\n" + new String(reply));


        // close and exit
        socket.close();
        context.term();
    }

}
