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
import static Annonces.AnnonceDB.annoncesList;
import static Users.UserDatabase.users;
@Path("/annonces")
public class AnnonceController {
    private static int currentId = 1; // Auto-increment ID

    // **1. Create Annonce**
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addAnnonce(Annonce annonce) {
        annonce.setId(currentId++); // Auto-increment ID
        annoncesList.add(annonce);
        return Response.status(Response.Status.CREATED)
                .entity("Annonce created successfully with ID: " + annonce.getId())
                .build();
    }
    @GET
    @Path("/{id}/notes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotesForAnnonce(@PathParam("id") int id) {
        // Find the annonce by ID
        Optional<Annonce> annonceOptional = annoncesList.stream()
                .filter(annonce -> annonce.getId() == id)
                .findFirst();

        if (annonceOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Annonce not found for ID: " + id)
                    .build();
        }

        Annonce annonce = annonceOptional.get();

        // Check if the annonce has notes
        List<Note> notes = annonce.getNotes();
        if (notes == null || notes.isEmpty()) {
        	System.out.println("notes vide");
            /*return Response.status(Response.Status.NOT_FOUND)
                    .entity("No notes found for this annonce.")
                    .build();*/
        }

        // Add the username dynamically by fetching the User by userId
        for (Note note : notes) {
            Optional<User> userOptional = users.stream()
                    .filter(user -> user.getId() == note.getId_user())
                    .findFirst();

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                note.setUsername(user.getFirst_name()); // Dynamically set the username from the User object
            } else {
                note.setUsername("Unknown User"); // In case no user is found, set a default name
            }
        }

        return Response.ok(notes).build();
    }


    // **2. Get All Annonces**
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Annonce> getAllAnnonces() {
        return annoncesList;
    }

    // **3. Get Annonce by ID**
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnonceById(@PathParam("id") int id) {
        Optional<Annonce> foundAnnonce = annoncesList.stream()
                .filter(annonce -> annonce.getId() == id)
                .findFirst();

        if (foundAnnonce.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Annonce not found for ID: " + id)
                    .build();
        }

        return Response.ok(foundAnnonce.get()).build();
    }

    // **4. Update Annonce**
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateAnnonce(@PathParam("id") int id, Annonce updatedAnnonce) {
        Optional<Annonce> existingAnnonce = annoncesList.stream()
                .filter(annonce -> annonce.getId() == id)
                .findFirst();

        if (existingAnnonce.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Annonce not found for ID: " + id)
                    .build();
        }

        Annonce annonce = existingAnnonce.get();
        if (updatedAnnonce.getTitle() != null) {
            annonce.setTitle(updatedAnnonce.getTitle());
        }
        if (updatedAnnonce.getDescription() != null) {
            annonce.setDescription(updatedAnnonce.getDescription());
        }
        if (updatedAnnonce.getStartDate() != null) {
            annonce.setStartDate(updatedAnnonce.getStartDate());
        }
        if (updatedAnnonce.getDuration() > 0) {
            annonce.setDuration(updatedAnnonce.getDuration());
        }

        return Response.ok("Annonce updated successfully.").build();
    }

    // **5. Delete Annonce**
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAnnonce(@PathParam("id") int id) {
        Optional<Annonce> existingAnnonce = annoncesList.stream()
                .filter(annonce -> annonce.getId() == id)
                .findFirst();

        if (existingAnnonce.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Annonce not found for ID: " + id)
                    .build();
        }

        annoncesList.remove(existingAnnonce.get());
        return Response.ok("Annonce deleted successfully.").build();
    }
    @POST
    @Path("/{annonceId}/notes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addNoteToAnnonce(@PathParam("annonceId") int annonceId, Note note) {
        Optional<Annonce> annonceOptional = annoncesList.stream()
                .filter(annonce -> annonce.getId() == annonceId)
                .findFirst();

        if (annonceOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Annonce not found for ID: " + annonceId)
                    .build();
        }

        Annonce annonce = annonceOptional.get();
        annonce.getNotes().add(note);

        return Response.ok("Note added successfully.").build();
    }

    
}
