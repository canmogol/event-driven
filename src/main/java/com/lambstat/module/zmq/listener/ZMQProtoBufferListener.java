package com.lambstat.module.zmq.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lambstat.core.event.UserLoginSuccessfulEvent;
import com.lambstat.core.listener.AbstractEndpointListener;
import com.lambstat.core.service.Service;
import com.lambstat.core.util.ModelConverter;
import com.lambstat.model.LambstatModels;
import com.lambstat.model.LoginRequest;
import com.lambstat.model.LoginResponse;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

public class ZMQProtoBufferListener extends AbstractEndpointListener {

    private boolean running = true;
    private String connectionString = "tcp://*:9666";
    private ZMQ.Context context;
    private ZMQ.Socket responder;

    public ZMQProtoBufferListener(Service service) {
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

            LambstatModels.loginRequest loginRequest = null;
            try {
                // Wait for next request from the client
                byte[] bytes = responder.recv(0);
                loginRequest = LambstatModels.loginRequest.parseFrom(bytes);
            } catch (ZMQException | InvalidProtocolBufferException e) {
                if (!running) {
                    break;
                } else {
                    log("ZMQ got exception while running, exception: " + e.getMessage());
                }
            }

            ModelConverter modelConverter = new ModelConverter();
            LoginRequest request = modelConverter.convert(loginRequest);

            LoginResponse loginResponse = new LoginResponse();
            if (request != null) {
                log("another user login REQUEST: " + request);
                if ("john".equals(request.getUsername()) && "123".equals(request.getPassword())) {
                    loginResponse.setLogged(true);
                    loginResponse.setName("john");
                    log("user logged in, will broadcast event");
                    // broadcast this user's login event
                    broadcast(new UserLoginSuccessfulEvent(request.getUsername()));
                }
            } else {
                log("request is null");
            }
            log("another user login RESPONSE: " + loginResponse);
            // Send reply back to client
            responder.send(modelConverter.convert(loginResponse));

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

    @Override
    public String getStatus() {
        return String.valueOf(running);
    }
}
