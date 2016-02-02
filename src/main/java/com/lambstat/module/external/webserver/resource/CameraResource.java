package com.lambstat.module.external.webserver.resource;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.model.ShutdownRequest;
import com.lambstat.module.external.camera.event.CameraCaptureImageEvent;
import com.lambstat.module.external.disk.event.FileAvailableEvent;
import com.lambstat.module.external.webserver.log.CameraResourceLogger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Path("/camera")
@Produces({"application/json"})
@Consumes({"application/json"})
public class CameraResource extends BaseResource {

    private CameraResourceLogger logger = new CameraResourceLogger();

    @POST
    @Path("/cameraTest")
    public String cameraTest(ShutdownRequest shutdownRequest) {
        // create an event to broadcast
        BaseEvent event = new CameraCaptureImageEvent();
        // broadcast event and register to response as "FileAvailableEvent"
        Future<FileAvailableEvent> eventFuture = broadcast(event, FileAvailableEvent.class);
        try {
            // get method will block until the FileAvailableEvent available
            FileAvailableEvent eventResponse = eventFuture.get();
            // return generated file name
            return eventResponse.getFileName();
        } catch (InterruptedException | ExecutionException e) {
            logger.futureExecutionError(e.getMessage());
        }
        // return failed
        return "FAILED";
    }

}