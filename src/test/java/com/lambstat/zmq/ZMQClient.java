package com.lambstat.zmq;

import org.zeromq.ZMQ;

public class ZMQClient {

    public static void main(String[] args) {
        ZMQClient zmqClient = new ZMQClient();
        zmqClient.sendEvent();
    }

    private void sendEvent() {
        ZMQ.Context context = ZMQ.context(1);

        // Socket to talk to server
        System.out.println("Connecting to hello world server");

        ZMQ.Socket socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://localhost:9555");

        socket.send("".getBytes(), 0);

        byte[] reply = socket.recv(0);
        System.out.println("Received " + new String(reply));

        socket.close();
        context.term();
    }

}
