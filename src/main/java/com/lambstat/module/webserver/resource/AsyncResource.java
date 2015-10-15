package com.lambstat.module.webserver.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/async")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AsyncResource extends BaseResource {

    @POST
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
            asyncResponse.resume("OK");
        });
        System.out.println("ok method call end");
    }

}