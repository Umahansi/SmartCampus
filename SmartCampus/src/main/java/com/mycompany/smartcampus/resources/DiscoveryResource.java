/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.resources;

/**
 *
 * @author LENOVO
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {

        Map<String, Object> response = new HashMap<>();

        // Versioning info
        response.put("apiVersion", "1.0");
        response.put("title", "Smart Campus API");
        response.put("description", "RESTful API for managing smart campus rooms and sensors");

        // Administrative contact details
        Map<String, String> contact = new HashMap<>();
        contact.put("name", "Umasha Perera");
        contact.put("email", "W2120275@westminster.ac.uk");
        response.put("contact", contact);

        // Map of primary resource collections
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);

        return Response.ok(response).build();
    }
}