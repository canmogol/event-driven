package com.lambstat.module.webserver.resource;

import com.lambstat.core.event.UserLoginSuccessfulEvent;
import com.lambstat.model.LoginRequest;
import com.lambstat.model.LoginResponse;
import com.lambstat.model.LogoutResponse;

import javax.ws.rs.*;

@Path("/user")
@Produces({"application/json"})
@Consumes({"application/json"})
public class UserResource extends BaseResource {

    /**
     * @return LoginResponse is a Response type
     */
    @POST
    @Path("/login")
    public LoginResponse login(LoginRequest loginRequest) {
        log("another user login REQUEST: " + loginRequest);
        // create a response
        LoginResponse loginResponse = new LoginResponse();
        // check if credentials are correct
        if ("john".equals(loginRequest.getUsername()) && "123".equals(loginRequest.getPassword())) {
            loginResponse.setLogged(true);
            loginResponse.setName("john");
            log("user logged in, will broadcast event");
            // broadcast this user's login event
            broadcast(new UserLoginSuccessfulEvent(loginRequest.getUsername()));
        }
        log("another user login RESPONSE: " + loginResponse);
        //return response
        return loginResponse;
    }

    /**
     * @return LoginResponse is a Response type
     */
    @GET
    @Path("/logout")
    public LogoutResponse logout() {
        return new LogoutResponse(true);
    }

}