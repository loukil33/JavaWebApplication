package Users;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Path("/users")
public class UserController {
	
	private static int idCounter = 1; // Initialize counter for id, starts from 1
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

        // Return the list of annonces
        return Response.ok(user.get().getAnnonces())
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
