package servlet;

//package com.sun.jersey.samples.helloworld.resources;

//import javax.jws.*;
import com.google.gson.JsonObject;

import javax.management.Notification;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import servlet.util.*;

import java.io.IOException;

@Path("/balance")
@Produces(MediaType.APPLICATION_JSON)
public class BalanceResource {
    @GET
//    @Path("/ping")
    public void getBalanceBySessionId(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        JsonObject body =  ServletUtils.readRequestBodyAsJSON(request);
        String ssid = body.get("ssid").getAsString();

        response.getWriter().write(String.format("ssid: %s", ssid));
        response.getWriter().close();
    }

//    public Response getUserrDetails(@Context HttpServletRequest request, @Context HttpServletResponse response) {
//        String username = request.getParameter("txt_username");
//        String password = request.getParameter("txt_password");
//        System.out.println(username);
//        System.out.println(password);
//
//        User user = new User(username, password);
//
//        return Response.ok().status(200).entity(user).build();
//    }
//
//    @GET
//    @Path("/get/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getNotification(@PathParam("id") int id) {
//        return Response.ok()
//                .entity(new Notification(id, "john", "test notification"))
//                .build();
//    }
//
//    @POST
//    @Path("/post/")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response postNotification(Notification notification) {
//        return Response.status(201).entity(notification).build();
//    }

}


