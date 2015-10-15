package com.lambstat.module.webserver.resource;


import com.lambstat.model.LoginResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/async")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AsyncResource extends BaseResource {

    @GET
    @Path("/ok")
    public void ok(@Suspended final AsyncResponse asyncResponse) {
        System.out.println("ok method call begin");
        async(() -> {
            System.out.println("will sleep");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                error("Could not sleep, e: " + e.getMessage());
            }
            System.out.println("woke up");
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setLogged(true);
            loginResponse.setName("john");
            asyncResponse.resume(loginResponse);
        });
        System.out.println("ok method call end");
    }

}