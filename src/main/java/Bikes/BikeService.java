package Bikes;


import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/bikes")
public class BikeService {


    private static int currentId = 1; // Auto-increment ID counter
    private List<Bike> bikes = new ArrayList<>();
    
    private static final String IMAGE_UPLOAD_DIR = "/css/images";



    // Create (Add a new bike with image upload)
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBike(@FormDataParam("model") String model,
                            @FormDataParam("brand") String brand,
                            @FormDataParam("condition") String condition,
                            @FormDataParam("color") String color,
                            @FormDataParam("images") List<Part> imageParts) {

        // Validate inputs
        if (model == null || brand == null || condition == null || color == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("All fields are required").build();
        }

        // Handle image uploads
        List<String> imagePaths = new ArrayList<>();
        if (imageParts != null) {
            for (Part imagePart : imageParts) {
                String fileName = getFileName(imagePart);
                try {
                    File uploadDir = new File(IMAGE_UPLOAD_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs(); // Create the directory if it doesn't exist
                    }
                    File file = new File(uploadDir, fileName);
                    imagePart.write(file.getAbsolutePath());
                    imagePaths.add("css/images/" + fileName); // Save relative path
                } catch (IOException e) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to upload images").build();
                }
            }
        }

        // Create and add bike
        Bike bike = new Bike(currentId++, model, brand, condition, color, true, imagePaths);
        bikes.add(bike);

        return Response.status(Response.Status.CREATED).entity(bike).build();
    }

    // Read (Get all bikes)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBikes() {
        return Response.ok(bikes).build();
    }

    // Read (Get a bike by ID)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBikeById(@PathParam("id") int id) {
        Bike bike = findBikeById(id);
        if (bike != null) {
            return Response.ok(bike).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
        }
    }
    
    // Method to get the name (model) of a bike by ID
    public String getBikeNameById(int id) {
        for (Bike bike : bikes) {
            if (bike.getId() == id) {
                return bike.getModel();
            }
        }
        return "Bike not found"; // Return a message if no bike matches the ID
    }

    // Update (Update bike details)
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBike(@PathParam("id") int id, Bike updatedBike) {
        Bike existingBike = findBikeById(id);
        if (existingBike != null) {
            // Update bike details
            existingBike.setModel(updatedBike.getModel());
            existingBike.setBrand(updatedBike.getBrand());
            existingBike.setCondition(updatedBike.getCondition());
            existingBike.setColor(updatedBike.getColor());
            existingBike.setAvailable(updatedBike.isAvailable());
            existingBike.setImages(updatedBike.getImages());
            return Response.ok(existingBike).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
        }
    }

    // Delete (Delete a bike by ID)
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBike(@PathParam("id") int id) {
        boolean isDeleted = bikes.removeIf(bike -> bike.getId() == id);
        if (isDeleted) {
            return Response.ok("Bike deleted successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
        }
    }

    // Helper method to find a bike by ID
    private Bike findBikeById(int id) {
        return bikes.stream().filter(bike -> bike.getId() == id).findFirst().orElse(null);
    }

    // Helper method to get the file name from a Part
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return "unknown";
    }
}

