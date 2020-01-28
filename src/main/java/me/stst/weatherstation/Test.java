package me.stst.weatherstation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class Test {

    @GET
    @Path("ping")
    public String ping(){
        return "pong";
    }
}
