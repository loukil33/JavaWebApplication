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
	    if (bike.getModel() == null || bike.getBrand() == null || bike.getCondition() == null || bike.getColor() == null) {
	        return Response.status(Response.Status.BAD_REQUEST).entity("All fields are required").build();
	    }

	    if (bike.getImages() != null && !bike.getImages().isEmpty()) {
	        String imageBase64 = bike.getImages().get(0);

	        if (imageBase64.startsWith("data:image")) {
	            imageBase64 = imageBase64.split(",")[1]; // Remove the prefix
	        }

	        try {
	            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);


	            String fileName = "bike_" + currentId + ".jpg";
	            String filePath = "E:/github/JavaWebApplication/src/main/webapp/css/images/" + fileName;


	            File outputFile = new File(filePath);
	            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
	                fos.write(imageBytes);
	                System.out.println("Image saved at: " + outputFile.getAbsolutePath());
	            }

	            bike.setImages(List.of("http://localhost:8081/UserWebService/css/images/" + fileName));
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Base64 image data").build();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity("Failed to write image file: " + e.getMessage())
	                           .build();
	        }
	    } else {
	        bike.setImages(null);
	    }

	    bike.setId(currentId++);
	    bike.setAvailable(true);
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

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBike(@PathParam("id") int id, Bike updatedBike) {
        // Validate the updated bike fields
        if (updatedBike.getModel() == null || updatedBike.getBrand() == null || updatedBike.getCondition() == null || updatedBike.getColor() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Required fields are missing.").build();
        }

        // Find the bike by ID
        Bike existingBike = findBikeById(id);
        if (existingBike != null) {
            // Update bike details
            existingBike.setModel(updatedBike.getModel());
            existingBike.setBrand(updatedBike.getBrand());
            existingBike.setCondition(updatedBike.getCondition());
            existingBike.setColor(updatedBike.getColor());
            existingBike.setAvailable(updatedBike.isAvailable());

            // Handle image update (check if new images are provided)
            if (updatedBike.getImages() != null && !updatedBike.getImages().isEmpty()) {
                existingBike.setImages(updatedBike.getImages()); // Update images if provided
            }

            // Persist the updated bike (if necessary)
            // Example: update the bike list or save to database
            // bikes.set(bikes.indexOf(existingBike), existingBike); // If using a list

            return Response.ok(existingBike).build(); // Return updated bike details
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build(); // Bike not found
        }
    }


    // Delete (Delete a bike by ID)
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBike(@PathParam("id") int id) {
        // Find the bike by ID
        Bike bikeToDelete = findBikeById(id);
        if (bikeToDelete != null) {
            // Remove the image file associated with the bike
            if (bikeToDelete.getImages() != null && !bikeToDelete.getImages().isEmpty()) {
                String imagePath = bikeToDelete.getImages().get(0).replace("http://localhost:8081/UserWebService/css/images/", "");
                File imageFile = new File("E:/github/JavaWebApplication/src/main/webapp/css/images/" + imagePath);

                if (imageFile.exists()) {
                    boolean imageDeleted = imageFile.delete();  // Delete the image file
                    if (!imageDeleted) {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete the image file").build();
                    }
                }
            }

            // Remove the bike from the list (or database)
            boolean isDeleted = bikes.removeIf(bike -> bike.getId() == id);
            if (isDeleted) {
                return Response.ok("Bike and its image deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
            }
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

