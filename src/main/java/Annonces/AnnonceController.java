package Annonces;

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
import static Annonces.AnnonceDB.annoncesList;
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
}
