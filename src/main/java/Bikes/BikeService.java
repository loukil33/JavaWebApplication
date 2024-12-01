package Bikes;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.*;


@Path("/bikes")
public class BikeService {
	private static List<Bike> bikes = new ArrayList<>();
    // Static block to add a bike to the list
    static {
        Bike bike = new Bike();
        bike.setId(1);
        bike.setModel("Roadster");
        bike.setBrand("Giant");
        bike.setCondition("New");
        bike.setColor("Red");
        bike.setAvailable(true);
        bike.setImages(List.of("roadster1.jpg", "roadster2.jpg"));
        bike.setRentalPrice(15.5);
        bike.setNotes(List.of("Smooth ride", "Good for long distances"));
        bike.setForSale(true);
        bike.setSalePrice(200.0);
        bike.setAlreadyRented(false);

        bikes.add(bike); // Add the bike to the list
    }
    // Add a new bike to the system
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addBike(@FormDataParam("model") String model,
                            @FormDataParam("brand") String brand,
                            @FormDataParam("condition") String condition,
                            @FormDataParam("color") String color,
                            @FormDataParam("rentalPrice") double rentalPrice,
                            @FormDataParam("isForSale") boolean isForSale,
                            @FormDataParam("images") List<FormDataBodyPart> images) {
        try {
            // Save images to /webapp/css/images
            String uploadPath = "/path/to/webapp/css/images";
            for (FormDataBodyPart image : images) {
                InputStream fileStream = image.getValueAs(InputStream.class);
                String fileName = image.getContentDisposition().getFileName();
                Files.copy(fileStream, Paths.get(uploadPath, fileName), StandardCopyOption.REPLACE_EXISTING);
            }

            // Save bike details to the database
            Bike newBike = new Bike(model, brand, condition, color, rentalPrice, false, imagesFileNames);
            bikeService.addBike(newBike);

            return Response.ok("Bike added successfully!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add bike").build();
        }
    }


    // Update details of an existing bike
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBike(Bike bike) {
        for (int i = 0; i < bikes.size(); i++) {
            if (bikes.get(i).getId() == bike.getId()) {
                bikes.set(i, bike);
                return Response.ok("Bike updated successfully").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
    }

    // Delete a bike from the system by its ID
    @DELETE
    @Path("/{id}")
    public Response deleteBike(@PathParam("id") int bikeId) {
        bikes.removeIf(bike -> bike.getId() == bikeId);
        return Response.ok("Bike removed successfully").build();
    }

    // Check if a bike is available for rental
    @GET
    @Path("/{id}/availability")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAvailability(@PathParam("id") int bikeId) {
        for (Bike bike : bikes) {
            if (bike.getId() == bikeId) {
                return Response.ok(bike.isAvailable()).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Bike not found").build();
    }
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    // Get the list of all bikes (static method for use in other services)
    public static List<Bike> getBikes() {
        return bikes;
    }

    // Get the list of all bikes (REST endpoint for external clients)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBikes() {
        return Response.ok(bikes).build();
    }
}