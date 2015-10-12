package com.lambstat.module.jetty.resource;

import com.lambstat.module.jetty.data.LoginResponse;
import com.lambstat.module.jetty.data.Param;

import javax.ws.rs.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("/api")
@Produces({"application/json"})
@Consumes({"application/json"})
public class JettyResource {

    /**
     * @param username user's name to login
     * @param password user's password
     * @return LoginResponse is a Response type
     */
    @GET
    @Path("/login")
    public LoginResponse login(@QueryParam("username") String username,
                               @QueryParam("password") String password) {
        // DO REQUEST TO:   http://localhost:8080/api/login/?username=john&password=123
        LoginResponse loginResponse = new LoginResponse();
        if ("john".equals(username) && "123".equals(password)) {
            loginResponse.setLogged(true);
            loginResponse.setName("john");
            loginResponse.getParams().add(new Param<String, Object>("city", "New York"));
            loginResponse.getParams().add(new Param<String, Object>("age", 21));
        }
        return loginResponse;
    }


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
                    e.printStackTrace();
                }
            }
        }
    }
}