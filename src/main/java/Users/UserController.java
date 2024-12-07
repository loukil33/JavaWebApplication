package Users;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Annonces.Annonce;
import Annonces.Rental;
import Bikes.Bike;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static Bikes.bikesDB.bikes;
import static Users.UserDatabase.users;
import static Annonces.AnnonceDB.annoncesList;

@Path("/users")
public class UserController {
	
	private static int idCounter = 4; // Initialize counter for id, starts from 1
    // Add a new user
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerUser(User user) {
        // Check if a user with the same email or CIN already exists
    	Optional<User> existingUser = users.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()) || u.getCin() == user.getCin())
                .findFirst();

        if (existingUser.isPresent()) {
            return Response.status(Response.Status.CONFLICT) // HTTP 409 Conflict
                    .entity("User with the same email or CIN already exists.")
                    .build();
        }

        // Assign a new id to the user
        user.setId(idCounter); // Set the user's id to the current counter value
        idCounter++; // Increment the counter for the next user

        // Add the user to the list
        users.add(user);

        return Response.status(Response.Status.CREATED) // HTTP 201 Created
                .entity("User registered successfully.")
                .build();
    
    }
    @GET
    @Path("/{id}/annonces")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAnnonces(@PathParam("id") int id) {
        // Find the user by ID
        Optional<User> user = users.stream()
                                   .filter(u -> u.getId() == id)
                                   .findFirst();

        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + id)
                           .build();
        }

        // Check if the user has no annonces and return an empty list
        List<Annonce> annonces = user.get().getAnnonces();
        if (annonces == null || annonces.isEmpty()) {
            return Response.ok(new ArrayList<>()) // Return an empty list
                           .build();
        }

        // Return the list of annonces
        return Response.ok(annonces)
                       .build();
    }
    @GET
    @Path("/{id}/rentals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRentals(@PathParam("id") int id) {
        // Find the user by ID
        Optional<User> user = users.stream()
                                   .filter(u -> u.getId() == id)
                                   .findFirst();

        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + id)
                           .build();
        }

        // Check if the user has no annonces and return an empty list
        List<Rental> rentals = user.get().getRentals();
        if (rentals == null || rentals.isEmpty()) {
            return Response.ok(new ArrayList<>()) // Return an empty list
                           .build();
        }

        // Return the list of annonces
        return Response.ok(rentals)
                       .build();
    }

    
    
    @GET
    @Path("/{id}/bikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBikes(@PathParam("id") int id) {
        // Find the user by ID
        Optional<User> user = users.stream()
                                   .filter(u -> u.getId() == id)
                                   .findFirst();

        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + id)
                           .build();
        }
        
        List<Bike> bikes = user.get().getBikes();
        if (bikes == null || bikes.isEmpty()) {
            return Response.ok(new ArrayList<>()) // Return an empty list
                           .build();
        }

        // Return the list of annonces
        return Response.ok(bikes)
                       .build();
    }

    // Get all users (for testing purposes)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return users;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        // Search for the user by ID
        Optional<User> user = users.stream()
                                   .filter(u -> u.getId() == id)
                                   .findFirst();

        if (user.isEmpty()) {
            // If the user is not found, return HTTP 404 with a message
            return Response.status(Response.Status.NOT_FOUND) // HTTP 404 Not Found
                           .entity("User not found for ID: " + id)
                           .build();
        }

        // If the user is found, return HTTP 200 with the user object
        return Response.ok(user.get()) // HTTP 200 OK
                       .build();
    }
    
 // Method to remove rental from the user's rental list and handle waiting list
    @DELETE
    @Path("/{userId}/return/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response returnRental(@PathParam("id") int rentalId, @PathParam("userId") int userId) {
        // Find the rental by ID
    	// Step 1: Find the user by userId
        Optional<User> userOptional = users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst();

        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found for ID: " + userId)
                    .build();
        }

        User user = userOptional.get();

        // Step 2: Check if the user has a rentals list
        if (user.getRentals() == null || user.getRentals().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No rentals found for this user.")
                    .build();
        }

        // Step 3: Find and remove the rental from the user's list
        Optional<Rental> rentalOptional = user.getRentals().stream()
                .filter(rental -> rental.getId() == rentalId)
                .findFirst();

        if (rentalOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + rentalId)
                    .build();
        }

        Rental rental = rentalOptional.get();

        // Check if the user is the current winner
        if (!rental.getCurrentWinner().equals(user)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You are not the current winner of this rental.")
                    .build();
        }


        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();
            currentUser.getRentals().remove(rental); // Remove rental from user's list
        }

        // Handle the waiting list and set the next user as the current winner
        List<User> waitingList = rental.getWaitingList();
        if (waitingList != null && !waitingList.isEmpty()) {
            User nextWinner = waitingList.remove(0); // Get the first user from the waiting list
            rental.setCurrentWinner(nextWinner); // Set the new current winner

            // Add the rental to the new winner's rental list
            Optional<User> newUserOptional = users.stream()
                    .filter(u -> u.getId() == nextWinner.getId())
                    .findFirst();

            if (newUserOptional.isPresent()) {
                User nextWinnerUser = newUserOptional.get();
                nextWinnerUser.getRentals().add(rental);
            }

            // Update the rental's waiting list
            rental.setWaitingList(waitingList);
            return Response.ok("Rental returned successfully. The next user is now the current winner.").build();
        } else {
            // No users in the waiting list, make the bike available again
            rental.getBike().setAvailable(true);
            rental.setCurrentWinner(null);
            return Response.ok("Rental returned successfully. The bike is now available for rent.").build();
        }
    }

    @DELETE
    @Path("/{userId}/rentals/{rentalId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeRentalFromUser(@PathParam("userId") int userId, @PathParam("rentalId") int rentalId) {
        // Step 1: Find the user by userId
        Optional<User> userOptional = users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst();
        

        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found for ID: " + userId)
                    .build();
        }

        User user = userOptional.get();

        // Step 2: Check if the user has a rentals list
        if (user.getRentals() == null || user.getRentals().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No rentals found for this user.")
                    .build();
        }

        // Step 3: Find and remove the rental from the user's list
        Optional<Rental> rentalOptional = user.getRentals().stream()
                .filter(rental -> rental.getId() == rentalId)
                .findFirst();

        if (rentalOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Rental not found for ID: " + rentalId)
                    .build();
        }

        Rental rentalToRemove = rentalOptional.get();
        
        Optional<Bike> bikeOptional = bikes.stream()
                .filter(bike -> bike.getId() == rentalToRemove.getBike().getId())
                .findFirst();
        Bike bike_sell = bikeOptional.get();
        bike_sell.setRented(true);
        
        
     // Check if the user is the current winner
        /*if (!rentalToRemove.getCurrentWinner().equals(user)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You are not the current winner of this rental.")
                    .build();
        }*/
        
        user.getRentals().remove(rentalToRemove);

     // Step 4: Update the bike availability if applicable
        if (rentalToRemove instanceof Rental) {
            Rental rental = (Rental) rentalToRemove;
            Bike bike = rental.getBike();
            if (bike != null) {
                bike.setAvailable(true); // Mark the bike as available
                bike.setRented(true);
                bike_sell.setRented(true);
            }
        }
     // Handle the waiting list and set the next user as the current winner
        List<User> waitingList = rentalToRemove.getWaitingList();
        if (waitingList != null && !waitingList.isEmpty()) {
            User nextWinner = waitingList.remove(0); // Get the first user from the waiting list
            rentalToRemove.setCurrentWinner(nextWinner); // Set the new current winner

            // Add the rental to the new winner's rental list
            Optional<User> newUserOptional = users.stream()
                    .filter(u -> u.getId() == nextWinner.getId())
                    .findFirst();

            if (newUserOptional.isPresent()) {
                User nextWinnerUser = newUserOptional.get();
                if (nextWinnerUser.getRentals() == null) {
                    nextWinnerUser.setRentals(new ArrayList<>());
                }
                nextWinnerUser.getRentals().add(rentalToRemove);
            }

            
         // Send email notification to the next winner
            sendEmailNotification(nextWinner.getEmail(), rentalToRemove);
            // Update the rental's waiting list
            rentalToRemove.setWaitingList(waitingList);
            Rental rental = (Rental) rentalToRemove;
            Bike bike = rental.getBike();
            bike.setRented(true);
            bike_sell.setRented(true);
            return Response.ok("Rental returned successfully. The next user is now the current winner.").build();
        } else {
            // No users in the waiting list, make the bike available again
        	rentalToRemove.getBike().setAvailable(true);
        	Rental rental = (Rental) rentalToRemove;
            Bike bike = rental.getBike();
        	bike.setRented(true);
        	bike_sell.setRented(true);
        	rentalToRemove.setCurrentWinner(null);
            return Response.ok("Rental returned successfully. The bike is now available for rent.").build();
        }
        
        //return Response.ok("Rental removed successfully from the user's list.").build();

        
    }
    private void sendEmailNotification(String recipientEmail, Rental rental) {
        String subject = "You Have Been Assigned a Rental!";
        String body = "Congratulations! You are now the current renter for the bike: " + rental.getBike().getModel() +
                ". Please pick it up at your convenience.\n\nRental Details:\n" +
                "Title: " + rental.getTitle() + "\n" +
                "Description: " + rental.getDescription() + "\n" +
                "Start Time: " + rental.getStart_time() + "\n" +
                "End Time: " + rental.getEnd_time();

        try {
            EmailUtility.sendEmail(recipientEmail, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + recipientEmail);
            e.printStackTrace();
        }
    }

    @DELETE
    @Path("/{userId}/annonces/{annonceId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUserAnnonce(@PathParam("userId") int userId, @PathParam("annonceId") int annonceId) {
        // Find the user by userId
        Optional<User> userOptional = users.stream()
                                           .filter(user -> user.getId() == userId)
                                           .findFirst();

        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + userId)
                           .build();
        }

        User user = userOptional.get();

        if (user.getAnnonces() == null || user.getAnnonces().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No annonces found for user ID: " + userId)
                           .build();
        }

        // Find and remove the annonce
        Optional<Annonce> annonceOptional = user.getAnnonces().stream()
                                                .filter(annonce -> annonce.getId() == annonceId)
                                                .findFirst();

        if (annonceOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Annonce not found for ID: " + annonceId)
                           .build();
        }
        

        user.getAnnonces().remove(annonceOptional.get());

        return Response.ok("Annonce deleted successfully for user ID: " + userId).build();
    }

    @DELETE
    @Path("/{userId}/bikes/{bikeId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUserBike(@PathParam("userId") int userId, @PathParam("bikeId") int bikeId) {
        // Find the user by userId
        Optional<User> userOptional = users.stream()
                                           .filter(user -> user.getId() == userId)
                                           .findFirst();

        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + userId)
                           .build();
        }

        User user = userOptional.get();

        // Check if the user has a bike list and if it's empty
        if (user.getBikes() == null || user.getBikes().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No bikes found for user ID: " + userId)
                           .build();
        }

        // Find and remove the bike
        Optional<Bike> bikeOptional = user.getBikes().stream()
                                          .filter(bike -> bike.getId() == bikeId)
                                          .findFirst();

        if (bikeOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Bike not found for ID: " + bikeId)
                           .build();
        }

        // Remove the bike from the user's list
        user.getBikes().remove(bikeOptional.get());

        // Optionally, remove the bike from the global bikes list
        bikes.removeIf(bike -> bike.getId() == bikeId);

        return Response.ok("Bike deleted successfully for user ID: " + userId).build();
    }


    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateUser(@PathParam("id") int id, User updatedUser) {
        // Search for the user by ID
        Optional<User> existingUser = users.stream()
                                           .filter(u -> u.getId() == id)
                                           .findFirst();

        if (existingUser.isEmpty()) {
            // User not found, return 404 Not Found
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + id)
                           .build();
        }

        // Update only the fields that have changed
        User user = existingUser.get();
        if (updatedUser.getFirst_name() != null) {
            user.setFirst_name(updatedUser.getFirst_name());
        }
        if (updatedUser.getLast_name() != null) {
            user.setLast_name(updatedUser.getLast_name());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getAddress() != null) {
            user.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getBirth_date() != null) {
            user.setBirth_date(updatedUser.getBirth_date());
        }
        if (updatedUser.getImage_profile() != null) {
            user.setImage_profile(updatedUser.getImage_profile());
        }
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }
        if (updatedUser.getPhoneNumber() != 0) { // Assuming phoneNumber is an int
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getBikes() != null) {
            List<Bike> validBikes = updatedUser.getBikes().stream()
                                               .filter(bike -> bike.getModel() != null && bike.getBrand() != null)
                                               .collect(Collectors.toList());
            user.setBikes(validBikes);
        }

        return Response.ok("User updated successfully.") // HTTP 200 OK
                       .build();
    }




    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("id") int id) {
        // Search for the user by ID
        Optional<User> existingUser = users.stream()
                                           .filter(u -> u.getId() == id)
                                           .findFirst();

        if (existingUser.isEmpty()) {
            // User not found, return 404 Not Found
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found for ID: " + id)
                           .build();
        }

        // Remove the user from the list
        users.remove(existingUser.get());

        return Response.ok("User deleted successfully.") // HTTP 200 OK
                       .build();
    }
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(User loginRequest) {
        Optional<User> user = users.stream()
                                   .filter(u -> u.getEmail().equals(loginRequest.getEmail()) && 
                                                u.getPassword().equals(loginRequest.getPassword()))
                                   .findFirst();

        if (user.isPresent()) {
            return Response.ok(user.get()).build(); // Return user details on successful login
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("{\"message\": \"Invalid email or password.\"}")
                           .build();
        }
    }



}
