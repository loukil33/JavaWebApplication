package Bikes;


import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Base64;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static Bikes.bikesDB.bikes;

@Path("/bikes")
public class BikeService {


    private static int currentId = 4; // Auto-increment ID counter

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBike(Bike bike) {
        // Validate the bike object (ensure all required fields are present)
        if (bike.getModel() == null || bike.getBrand() == null || bike.getCondition() == null || bike.getColor() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("All fields are required").build();
        }

        // Handle the image data (if provided)
        if (bike.getImages() != null && !bike.getImages().isEmpty()) {
            String imageBase64 = bike.getImages().get(0);  // Assuming one image for simplicity

            // Check if the image data has the prefix and remove it
            if (imageBase64.startsWith("data:image")) {
                imageBase64 = imageBase64.split(",")[1];  // Remove the prefix (data:image/jpeg;base64,)
            }

            try {
                byte[] imageBytes = Base64.getDecoder().decode(imageBase64);  // Decode the Base64 string

                // Save the image to a file (adjust file path as needed)
                String fileName = "uploaded_image.jpg";  // You can generate dynamic names if needed
                String filePath = "/css/images/" + fileName;
                File outputFile = new File(filePath);
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(imageBytes);  // Write the image bytes to the file
                }

                // Update the bike object with the correct image path
                bike.setImages(List.of("css/images/" + fileName));  // Set relative image path
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Base64 image data").build();
            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to upload image").build();
            }
        } else {
            // If no image is provided, use a default image
            bike.setImages(List.of("/css/images/imagevelo.jpg"));
        }

        // Add the bike to your data store (list, database, etc.)
        bike.setId(currentId++);  // Increment ID
        bike.setAvailable(true);
;        bikes.add(bike);  // Assuming 'bikes' is a list

        return Response.status(Response.Status.CREATED).entity(bike).build(); // Respond with the created bike object
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

