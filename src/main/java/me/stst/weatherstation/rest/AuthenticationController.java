package me.stst.weatherstation.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Controller
@Path("/auth")
public class AuthenticationController {

    @GET
    @Path("generate_token")
    public Response generateToken(){
        return Response.ok(RandomStringUtils.random(200,true,true)).build();
    }
}
