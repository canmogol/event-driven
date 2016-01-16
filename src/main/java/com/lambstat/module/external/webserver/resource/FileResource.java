package com.lambstat.module.external.webserver.resource;

import com.lambstat.module.external.webserver.log.FileResourceLogger;

import javax.ws.rs.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("/file")
@Produces({"application/json"})
@Consumes({"application/json"})
public class FileResource extends BaseResource {

    private FileResourceLogger logger = new FileResourceLogger();

    @GET
    @Path("/file/{name}")
    @Produces({"application/xml"})
    public String file(@PathParam("name") String name) {
        BufferedReader br = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + name);
            String line;
            StringBuilder sb = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String wadlContent = sb.toString();
            return wadlContent;
        } catch (Exception e) {
            return "Exception occurred, " + e.getClass().getSimpleName() + ": " + e.getMessage();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.couldNotCloseReader(e.getMessage());
                }
            }
        }
    }

}