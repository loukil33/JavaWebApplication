package Bikes;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/bikes")
public class BikeService {
	private static List<Bike> bikes = new ArrayList<>();

    // Add a new bike to the system
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBike(Bike bike) {
        bikes.add(bike);
        return Response.ok("Bike added successfully").build();
    }

    // Update details of an existing bike
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBike(Bike bike) {
        for (int i = 0; i < bikes.size(); i++) {
            if (bikes.get(i).getId() == bike.getId()) {
                bikes.set(i, bike);
                return Response.ok("Bike updated successfully").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
    }

    // Delete a bike from the system by its ID
    @DELETE
    @Path("/{id}")
    public Response deleteBike(@PathParam("id") int bikeId) {
        bikes.removeIf(bike -> bike.getId() == bikeId);
        return Response.ok("Bike removed successfully").build();
    }

    // Check if a bike is available for rental
    @GET
    @Path("/{id}/availability")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAvailability(@PathParam("id") int bikeId) {
        for (Bike bike : bikes) {
            if (bike.getId() == bikeId) {
                return Response.ok(bike.isAvailable()).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
    }

    // Get the list of all bikes (static method for use in other services)
    public static List<Bike> getBikes() {
        return bikes;
    }

    // Get the list of all bikes (REST endpoint for external clients)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBikes() {
        return Response.ok(bikes).build();
    }
}