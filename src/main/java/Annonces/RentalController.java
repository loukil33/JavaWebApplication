package Annonces;

import Bikes.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Bikes.Bike;
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
import static Users.UserDatabase.users;
import static Annonces.AnnonceDB.annoncesList;
import static Bikes.bikesDB.bikes;

@Path("/rentals")
public class RentalController {
	private static List<Rental> rentalList = new ArrayList<>(); // In-memory list to store rentals
    private static int currentId = 1; // Auto-increment ID

    // **1. Add Rental**
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addRental(Rental rental, @QueryParam("userId") int userId) {
        // Find the user by userId
        Optional<User> userOptional = users.stream()
                                           .filter(user -> user.getId() == userId)
                                           .findFirst();

        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + userId)
                           .build();
        }
        
        
        // Use BikeService to fetch all bikes
        BikeService bikeService = new BikeService();
        Response bikeResponse = bikeService.getAllBikes();

        if (bikeResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error retrieving bikes")
                           .build();
        }

        // Retrieve the bike list from the response
        @SuppressWarnings("unchecked")
        List<Bike> bikes = (List<Bike>) bikeResponse.getEntity();

        // Find the bike by ID
        Optional<Bike> bikeOptional = bikes.stream()
                                           .filter(bike -> bike.getId() == rental.getBike().getId())
                                           .findFirst();

        if (bikeOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Bike not found for ID: " + rental.getBike().getId())
                           .build();
        }

        

        // Associate rental with the user
        User user = userOptional.get();
        if (user.getAnnonces() == null) {
            user.setAnnonces(new ArrayList<>());
        }

        
        rental.setId(currentId++); // Generate a unique rental ID
        annoncesList.add(rental);
        rentalList.add(rental);
        user.getAnnonces().add(rental); // Add the rental to the user's annonces list
        System.out.println("Rental added with ID: " + rental.getId());
        System.out.println("User's Annonces List: " + user.getAnnonces());


        return Response.status(Response.Status.CREATED)
                       .entity("Rental added successfully for user: " + user.getFirst_name())
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
        if (updatedRental.getStart_time() != null) {
            rental.setStart_time(updatedRental.getStart_time());
        }
        if (updatedRental.getEnd_time() != null) {
            rental.setEnd_time(updatedRental.getEnd_time());
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
 
        if (updatedRental.getBike() != null && updatedRental.getBike().getId() > 0) {
            Optional<Bike> bikeOptional = bikes.stream()
                    .filter(bike -> bike.getId() == updatedRental.getBike().getId())
                    .findFirst();

            if (bikeOptional.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Bike not found for ID: " + updatedRental.getBike().getId())
                        .build();
            }

            rental.setBike(bikeOptional.get());
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
        annoncesList.remove(existingRental.get());
        return Response.ok("Rental deleted successfully.").build();
    }

   

    // **6. Add User to Waiting List**
    @POST
    @Path("/{id}/waiting-list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addToWaitingList(@PathParam("id") int id, User user) {
        // Step 1: Find the rental by ID
        Optional<Annonce> annonceOptional = annoncesList.stream()
                .filter(annonce -> annonce.getId() == id)
                .findFirst();

        if (annonceOptional.isEmpty() || !(annonceOptional.get() instanceof Rental)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        Rental rental = (Rental) annonceOptional.get();
        Bike bike = rental.getBike();

        if (bike == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Bike not associated with this rental.")
                    .build();
        }

        // Step 2: Check if the user is the current winner
        if (user.equals(rental.getCurrentWinner())) {
            
        	/*return Response.status(Response.Status.CONFLICT)
                    .entity("You are already renting this bike as the current winner.")
                    .build();*/
        }

        // Step 3: Check if the user is already in the waiting list
        if (rental.getWaitingList() != null && rental.getWaitingList().stream().anyMatch(u -> u.getId() == user.getId())) {
            /*return Response.status(Response.Status.CONFLICT)
                    .entity("You are already in the waiting list for this bike.")
                    .build();*/
        }

        // Step 4: Handle bike availability
        if (bike.isAvailable()) {
            // Rent the bike directly
            bike.setAvailable(false);
            rental.setCurrentWinner(user);

            // Add the rental to the user's list
            Optional<User> userOptional = users.stream()
                    .filter(u -> u.getId() == user.getId())
                    .findFirst();

            if (userOptional.isPresent()) {
                User currentUser = userOptional.get();
                if (currentUser.getRentals() == null) {
                    currentUser.setRentals(new ArrayList<>());
                }

                // Add the rental to the user's list
                if (!currentUser.getRentals().contains(rental)) {
                    currentUser.getRentals().add(rental);
                }
            }

            return Response.ok("You have successfully rented the bike!").build();
        } else {
            // Step 5: Add the user to the waiting list
            if (rental.getWaitingList() == null) {
                rental.setWaitingList(new ArrayList<>());
            }

            rental.getWaitingList().add(user);
            return Response.ok("Bike is currently unavailable. You have been added to the waiting list.").build();
        }
    }




    // **7. Set Current Winner**
    @PUT
    @Path("/{id}/winner")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setCurrentWinner(@PathParam("id") int id) {
        Optional<Annonce> annonceOptional = annoncesList.stream()
                .filter(annonce -> annonce.getId() == id)
                .findFirst();

        if (annonceOptional.isEmpty() || !(annonceOptional.get() instanceof Rental)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + id)
                    .build();
        }

        Rental rental = (Rental) annonceOptional.get();
        Bike bike = rental.getBike();

        if (bike == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Bike not associated with this rental.")
                    .build();
        }

        List<User> waitingList = rental.getWaitingList();
        if (waitingList == null || waitingList.isEmpty()) {
            // No users in the waiting list, make the bike available
            bike.setAvailable(true);
            rental.setCurrentWinner(null);
            return Response.ok("No users in the waiting list. Bike is now available.").build();
        }

        // Assign the next user in the waiting list as the current winner
        User nextWinner = waitingList.remove(0);
        rental.setCurrentWinner(nextWinner);

        // Add the rental to the next user's list
        Optional<User> userOptional = users.stream()
                .filter(u -> u.getId() == nextWinner.getId())
                .findFirst();

        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();
            if (currentUser.getRentals() == null) {
                currentUser.setRentals(new ArrayList<>());
            }

            // Check if the rental is already in the user's list
            if (!currentUser.getRentals().contains(rental)) {
                currentUser.getRentals().add(rental);
            }
        }

        return Response.ok("Current winner updated successfully.").build();
    }
    
    public static List<Rental> getRentalList() {
        return rentalList;
    }

}
