
package com.mycompany.smartcampus.resources;

/**
 *
 * @author LENOVO
 */

import com.mycompany.smartcampus.data.DataStore;
import com.mycompany.smartcampus.exception.RoomNotEmptyException;
import com.mycompany.smartcampus.models.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.getRooms().values());
        return Response.ok(roomList).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);
        if (room != null) {
            return Response.ok(room).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Bad Request");
            error.put("message", "Room ID is required");
            return Response.status(400).entity(error).build();
        }

        if (DataStore.getRooms().containsKey(room.getId())) {  
            Map<String, String> error = new HashMap<>();
            error.put("error", "Conflict");
            error.put("message", "A room with ID '" + room.getId() + "' already exists");
            return Response.status(409).entity(error).build();
        }

        DataStore.addRoom(room);
        return Response.status(201).entity(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room with associated sensors");
        }
        DataStore.removeRoom(roomId);
        return Response.ok("Room deleted successfully").build();
    }
}
