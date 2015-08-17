package com.lambstat.zmq;

import com.lambstat.module.camera.event.CaptureImageEvent;
import com.lambstat.stat.event.Event;
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

        // send a capture image event
        Event event = new CaptureImageEvent();

        // serialize event object to xml
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(byteArrayOutputStream);
        xmlEncoder.writeObject(event);
        xmlEncoder.close();
        String xml = byteArrayOutputStream.toString();

        System.out.println("Sending:\n" + xml);
        // send over
        socket.send(xml.getBytes(), 0);

        // read response
        byte[] reply = socket.recv(0);
        System.out.println("Received:\n" + new String(reply));

        // close and exit
        socket.close();
        context.term();
    }

}
