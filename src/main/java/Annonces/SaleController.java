package Annonces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Bikes.Bike;

@Path("/sales")
public class SaleController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBikesForSale() {
        // Create and populate the list of sales
        List<Sale> sales = new ArrayList<>();

        // Regular Bike (without sale price)
        Bike regularBike = new Bike(
            1, "Mountain Bike", "BrandX", "New", "Red", true,
            Arrays.asList("css/images/ebe783d0b4cfae10f695a4c7c8dd076269e3429d.jpg")
        );
        // Adding a Sale for a regular bike with no discount
        sales.add(new Sale(
            1, "Mountain Bike Sale", "A regular mountain bike", 
            LocalDate.now(), 0, 1, null, 150.0, regularBike
        ));

        // Sale Bike (with a sale price)
        Bike saleBike = new Bike(
            2, "Road Bike", "BrandY", "Used", "Blue", true,
            Arrays.asList("css/images/045a512fa8b766a2ae47858d53d6c19587e91ab1.jpg")
        );
        sales.add(new Sale(
            2, "Road Bike Sale", "Used road bike", 
            LocalDate.now(), 30, 1, null, 200.0, saleBike
        ));

        // Another Sale Bike
        Bike anotherSaleBike = new Bike(
            3, "Mountain Bike Pro", "BrandZ", "New", "Green", true,
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
