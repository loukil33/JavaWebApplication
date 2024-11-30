package RentalRequests;
import Bikes.*;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rentalRequests")
public class RentalRequestService {
	private static List<RentalRequest> requests = new ArrayList<>();
    private static List<Bike> bikes = BikeService.getBikes(); // Récupérer les vélos

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRequest(RentalRequest request) {
        requests.add(request);
        return Response.ok("Rental request added successfully").build();
    }

    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRequestStatus(@PathParam("id") int requestId, String status) {
        for (RentalRequest request : requests) {
            if (request.getId() == requestId) {
                request.setStatus(status);
                return Response.ok("Request status updated successfully").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Request not found").build();
    }

    @POST
    @Path("/{id}/assignBike")
    public Response assignBikeToUser(@PathParam("id") int requestId) {
        for (RentalRequest request : requests) {
            if (request.getId() == requestId) {
                for (Bike bike : bikes) {
                    if (bike.getId() == request.getBikeId() && bike.isAvailable()) {
                        bike.setAvailable(false);
                        request.setStatus("Approved");
                        return Response.ok("Bike assigned successfully").build();
                    }
                }
                return Response.status(Response.Status.BAD_REQUEST).entity("Bike not available").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Request not found").build();
    }

    @POST
    @Path("/{bikeId}/returnBike")
    public Response returnBike(@PathParam("bikeId") int bikeId) {
        for (Bike bike : bikes) {
            if (bike.getId() == bikeId && !bike.isAvailable()) {
                bike.setAvailable(true);
                return Response.ok("Bike returned successfully").build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bike not found or already available").build();
    }
}