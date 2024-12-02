package Annonces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/rentals")
public class RentalController {
	private static List<Rental> rentalList = new ArrayList<>(); // In-memory list to store rentals
    private static int currentId = 1; // Auto-increment ID

    // **1. Add Rental**
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addRental(Rental rental) {
        rental.setId(currentId++); // Assign a unique ID
        rentalList.add(rental);
        return Response.status(Response.Status.CREATED)
                .entity("Rental created successfully with ID: " + rental.getId())
                .build();
    }

    // **2. Get All Rentals**
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rental> getAllRentals() {
        return rentalList;
    }

    // **3. Get Rental by ID**
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRentalById(@PathParam("id") int id) {
        Optional<Rental> foundRental = rentalList.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();

        if (foundRental.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        return Response.ok(foundRental.get()).build();
    }

    // **4. Update Rental**
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateRental(@PathParam("id") int id, Rental updatedRental) {
        Optional<Rental> existingRental = rentalList.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();

        if (existingRental.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        Rental rental = existingRental.get();

        if (updatedRental.getTitle() != null) {
            rental.setTitle(updatedRental.getTitle());
        }
        if (updatedRental.getDescription() != null) {
            rental.setDescription(updatedRental.getDescription());
        }
        if (updatedRental.getStartDate() != null) {
            rental.setStartDate(updatedRental.getStartDate());
        }
        if (updatedRental.getDuration() > 0) {
            rental.setDuration(updatedRental.getDuration());
        }
        if (updatedRental.getRentPrice() > 0) {
            rental.setRentPrice(updatedRental.getRentPrice());
        }
        if (updatedRental.getRentDuration() > 0) {
            rental.setRentDuration(updatedRental.getRentDuration());
        }
        if (updatedRental.getWaitingList() != null) {
            rental.setWaitingList(updatedRental.getWaitingList());
        }
        if (updatedRental.getCurrentWinner() != null) {
            rental.setCurrentWinner(updatedRental.getCurrentWinner());
        }

        return Response.ok("Rental updated successfully.").build();
    }

    // **5. Delete Rental**
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteRental(@PathParam("id") int id) {
        Optional<Rental> existingRental = rentalList.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();

        if (existingRental.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        rentalList.remove(existingRental.get());
        return Response.ok("Rental deleted successfully.").build();
    }

    // **6. Add User to Waiting List**
    @POST
    @Path("/{id}/waiting-list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addToWaitingList(@PathParam("id") int id, User user) {
        Optional<Rental> rentalOptional = rentalList.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();

        if (rentalOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        Rental rental = rentalOptional.get();
        List<User> waitingList = rental.getWaitingList();
        if (waitingList == null) {
            waitingList = new ArrayList<>();
            rental.setWaitingList(waitingList);
        }

        waitingList.add(user);
        return Response.ok("User added to waiting list.").build();
    }

    // **7. Set Current Winner**
    @PUT
    @Path("/{id}/winner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response setCurrentWinner(@PathParam("id") int id, User winner) {
        Optional<Rental> rentalOptional = rentalList.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();

        if (rentalOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        Rental rental = rentalOptional.get();
        rental.setCurrentWinner(winner);
        return Response.ok("Current winner set successfully.").build();
    }
}
