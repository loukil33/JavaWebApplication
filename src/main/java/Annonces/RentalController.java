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
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
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

        Bike bike = bikeOptional.get();
        if (bike.isAvailable()) {
            bike.setRented(true); // Mark the bike as rented
          
        } else {
            return Response.status(Response.Status.CONFLICT)
                           .entity("Bike is already rented.")
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
    
    // Send email to the first user in the waiting list
    public void notifyFirstInWaitingList(Rental rental) {
        User firstUser = rental.getFirstInWaitingList();
        if (firstUser == null) {
            System.out.println("No users in the waiting list.");
            return;
        }

        // Compose the email
        String subject = "You're next in line!";
        String messageBody = "Hello " + firstUser.getFirst_name() + ",\n\n" +
                             "Good news! You are now the first in line for the rental: " + rental.getTitle() + ".\n" +
                             "Please proceed to finalize your rental.\n\n" +
                             "Best regards,\nEiffel Corp Team";

        // Send the email
        sendEmail(firstUser.getEmail(), subject, messageBody);

        // Optionally, mark this user as notified
        rental.setCurrentWinner(firstUser);
    }

    private void sendEmail(String toEmail, String subject, String messageBody) {
        String fromEmail = "your-email@example.com";
        String password = "your-email-password";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
}
