package Annonces;

import Bikes.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Bikes.Bike;

@Path("/sales")
public class SaleController {
	
	private List<Sale> sales = new ArrayList<>();

	@POST
	@Path("/addSale")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSale(@QueryParam("bikeId") int bikeId, Sale sale) {
	    // Validate input
	    if (bikeId <= 0) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("A valid bike ID must be provided as a query parameter.")
	                .build();
	    }
	    if (sale == null || sale.getSalePrice() <= 0) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("The sale price must be greater than zero.")
	                .build();
	    }
	    if (sale.getDuration() <= 0) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("The sale duration must be greater than zero.")
	                .build();
	    }
	    // Fetch the bike using BikeService
	    BikeService bikeService = new BikeService();
	    Bike bikeForSale = bikeService.getBikeByIdDirect(bikeId);
	    if (bikeForSale == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                .entity("Bike with the given ID does not exist.")
	                .build();
	    }
	   // Ensure the bike is not rented
	    Optional<Rental> associatedRental = RentalController.getRentalList().stream()
	            .filter(rental -> rental.getBike().getId() == bikeId)
	            .findFirst();

	    if (associatedRental.isPresent()) {
	        // Call deleteRental to remove the rental
	        Response deleteResponse = new RentalController().deleteRental(associatedRental.get().getId());
	        if (deleteResponse.getStatus() != Response.Status.OK.getStatusCode()) {
	            return Response.status(Response.Status.CONFLICT)
	                    .entity("Failed to delete the associated rental. Sale cannot proceed.")
	                    .build();
	        }
	    }
	    // Mark the bike as sold
	    bikeForSale.setAvailable(false);
	    // Associate the bike with the sale
	    sale.setBike(bikeForSale);
	    sale.setId(generateUniqueId()); // Generate a unique ID for the sale
	    // Persist the bike update
	    bikeService.updateBike(bikeForSale);
	    // Save the sale (assuming `sales` is a shared list of Sale objects)
	    synchronized (sales) {
	        sales.add(sale);
	    }

	    return Response.status(Response.Status.CREATED)
	            .entity(sale)
	            .build();
	}


	   // Helper method to generate a unique ID
	 private int generateUniqueId() {
	       return sales.size() + 1; // Or use a more robust ID generation logic
	  }
	   
	      
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBikesForSale() {
        // Create and populate the list of sales
        List<Sale> sales = new ArrayList<>();

        // Regular Bike (without sale price)
        Bike regularBike = new Bike(
            1, 1, "Mountain Bike", "BrandX", "New", "Red", true,
            Arrays.asList("css/images/ebe783d0b4cfae10f695a4c7c8dd076269e3429d.jpg")
        );

        // Adding a Sale for a regular bike
        sales.add(new Sale(
            1, "Mountain Bike Sale", "A regular mountain bike",
            LocalDate.now(), 0, 1, null, 150.0, regularBike
        ));

        // Sale Bike (with a sale price)
        Bike saleBike = new Bike(
            2, 1, "Road Bike", "BrandY", "Used", "Blue", true,
            Arrays.asList("css/images/045a512fa8b766a2ae47858d53d6c19587e91ab1.jpg")
        );
        sales.add(new Sale(
            2, "Road Bike Sale", "Used road bike",
            LocalDate.now(), 30, 1, null, 200.0, saleBike
        ));

        // Another Sale Bike
        Bike anotherSaleBike = new Bike(
            3, 2, "Mountain Bike Pro", "BrandZ", "New", "Green", true,
            Arrays.asList("css/images/045a512fa8b766a2ae47858d53d6c19587e91ab1.jpg")
        );
        sales.add(new Sale(
            3, "Mountain Bike Pro Sale", "High-end mountain bike",
            LocalDate.now(), 20, 2, null, 350.0, anotherSaleBike
        ));

        // Return the response with the list of sales
        return Response.ok(sales).build();
    }
    

}
