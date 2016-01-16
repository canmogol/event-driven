package com.lambstat.module.external.zmq.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lambstat.core.endpoint.AbstractEndpointListener;
import com.lambstat.core.endpoint.EndpointObserver;
import com.lambstat.core.event.Event;
import com.lambstat.core.service.Service;
import com.lambstat.core.util.ModelConverter;
import com.lambstat.model.LambstatModels;
import com.lambstat.model.LoginResponse;
import com.lambstat.module.buildin.authentication.event.AuthenticationRequestEvent;
import com.lambstat.module.buildin.authorization.event.AuthorizationResultEvent;
import com.lambstat.module.external.zmq.log.ZMQProtoBufferLogger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

public class ZMQProtoBufferListener extends AbstractEndpointListener {

    private ZMQProtoBufferLogger logger = new ZMQProtoBufferLogger();
    private boolean running = true;
    private String connectionString = "tcp://*:9666";
    private ZMQ.Context context;
    private ZMQ.Socket responder;

    public ZMQProtoBufferListener(Service service) {
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

            LambstatModels.loginRequest loginRequest = null;
            try {
                // Wait for next request from the client
                byte[] bytes = responder.recv(0);
                String requestType = new String(bytes);
                logger.nextRequestType(requestType);
                while (responder.hasReceiveMore()) {
                    bytes = responder.recv(0);
                }
                loginRequest = LambstatModels.loginRequest.parseFrom(bytes);
            } catch (ZMQException | InvalidProtocolBufferException e) {
                if (!running) {
                    break;
                } else {
                    logger.zmqError(e.getMessage());
                }
            }

            if (loginRequest != null && loginRequest.getUsername() != null && loginRequest.getPassword() != null) {
                broadcast(new AuthenticationRequestEvent(loginRequest.getUsername(), loginRequest.getPassword()), AuthorizationResultEvent.class, new EndpointObserver<Event>() {
                    @Override
                    public void handleEvent(Event e) {
                        AuthorizationResultEvent event = (AuthorizationResultEvent) e;
                        logger.loginResponse(event.isAuthorized());

                        LoginResponse loginResponse = new LoginResponse();
                        logger.userRequest(event.getUsername());
                        if (event.isAuthorized()) {
                            loginResponse.setName(event.getUsername());
                            loginResponse.setLogged(event.isAuthorized());
                            logger.userLoggedIn();
                            logger.loginResponse(loginResponse.isLogged());
                        }
                        // Send reply back to client
                        ModelConverter modelConverter = new ModelConverter();
                        responder.send(modelConverter.convert(loginResponse));
                    }
                });
            } else {
                logger.nullRequest();
            }
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
