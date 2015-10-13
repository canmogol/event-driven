package com.lambstat.module.webserver.resource;

import com.lambstat.core.event.ShutdownEvent;
import com.lambstat.model.ShutdownRequest;
import com.lambstat.model.ShutdownResponse;
import com.lambstat.model.SystemStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/system")
@Produces({"application/json"})
@Consumes({"application/json"})
public class SystemResource extends BaseResource {

    /**
     * @return ShutdownResponse is a Response type
     */
    @POST
    @Path("/shutdown")
    public ShutdownResponse shutdown(ShutdownRequest shutdownRequest) {
        broadcast(new ShutdownEvent());
        return new ShutdownResponse(SystemStatus.E4_SHUTTING_DOWN);
    }

}