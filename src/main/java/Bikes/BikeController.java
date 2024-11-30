package Bikes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/bikes")
public class BikeController {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bike> getAllBikes() {
        // Initialize sample bikes
        List<Bike> bikes = new ArrayList<>();
        bikes.add(new Bike(1, "Mountain Bike", "BrandX", "New", "Red", true,
                Arrays.asList("css/images/ebe783d0b4cfae10f695a4c7c8dd076269e3429d.jpg"), 15.0, true, 200.0));
        bikes.add(new Bike(2, "Road Bike", "BrandY", "Used", "Blue", true,
                Arrays.asList("css/images/045a512fa8b766a2ae47858d53d6c19587e91ab1.jpg"), 10.0, true, 150.0));
        bikes.add(new Bike(3, "Amine Bike", "BrandZ", "Used", "Blue", true,
                Arrays.asList("css/images/045a512fa8b766a2ae47858d53d6c19587e91ab1.jpg"), 10.0, true, 300.0));
        return bikes;
    }
}
