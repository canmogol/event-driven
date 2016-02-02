package com.lambstat.core.util;

import com.lambstat.core.model.*;

public class ModelConverter {

    public LoginRequest convert(LambstatModels.loginRequest pb) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(pb.getPassword());
        loginRequest.setUsername(pb.getUsername());
        Status status = convert(pb.getStatus());
        loginRequest.setStatus(status);
        loginRequest.setType(pb.getType());
        return loginRequest;
    }

    private Status convert(LambstatModels.status pb) {
        Status status = new Status();
        status.setMessage(pb.getMessage());
        status.setStatus(pb.getStatus());
        return status;
    }

    public ShutdownRequest convert(LambstatModels.shutdownRequest pb) {
        ShutdownRequest shutdownRequest = new ShutdownRequest();
        shutdownRequest.setImmediately(pb.getImmediately());
        shutdownRequest.setStatus(convert(pb.getStatus()));
        shutdownRequest.setType(pb.getType());
        return shutdownRequest;
    }

    public byte[] convert(LoginResponse loginResponse) {
        LambstatModels.loginResponse pb = LambstatModels.loginResponse.newBuilder()
                .setName(loginResponse.getName())
                .setLogged(loginResponse.isLogged())
                .setStatus(convert(loginResponse.getStatus()))
                .setType(loginResponse.getType())
                .build();
        return pb.toByteArray();
    }

    private LambstatModels.status convert(Status status) {
        LambstatModels.status pb = LambstatModels.status.newBuilder()
                .setMessage(status.getMessage())
                .setStatus(status.getStatus())
                .build();
        return pb;
    }
}
