package com.lambstat.module.external.webserver.resource;


import com.lambstat.model.LoginRequest;
import com.lambstat.model.LoginResponse;
import com.lambstat.module.external.webserver.log.AsyncResourceLogger;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/async")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AsyncResource extends BaseResource {

    private AsyncResourceLogger logger = new AsyncResourceLogger();

    @POST
    @Path("/ok")
    public void ok(LoginRequest loginRequest, @Suspended final AsyncResponse asyncResponse) {
        logger.okMethodCalled();
        async(() -> {
            logger.willSleep();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.couldNotSleep(e.getMessage());
            }
            logger.wokeUp();
            LoginResponse loginResponse = new LoginResponse();
            if ("john".equals(loginRequest.getUsername()) && "123".equals(loginRequest.getPassword())) {
                loginResponse.setLogged(true);
                loginResponse.setName("john");
                logger.loginSuccessful(loginRequest.getUsername());
            } else {
                loginResponse.setLogged(false);
                logger.couldNotLoggedIn(loginRequest.getUsername());
            }
            logger.willReturnResponse(loginRequest.getUsername(), loginResponse.isLogged());
            asyncResponse.resume(loginResponse);
        });
        logger.okMethodCallEnd();
    }

}