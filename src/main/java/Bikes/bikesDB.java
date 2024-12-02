package Bikes;

import java.util.ArrayList;
import java.util.List;

public class bikesDB {
	
    public static List<Bike> bikes = new ArrayList<>();
    
    public static final String IMAGE_UPLOAD_DIR = "/css/images";
    public static final String DEFAULT_IMAGE = "././webapp/css/images/imagevelo.jpg"; // Default image path

    static {
        bikes.add(new Bike(1, "Mountain Bike", "Trek", "New", "Red", true, List.of(DEFAULT_IMAGE)));
        bikes.add(new Bike(2, "Road Bike", "Giant", "Used", "Blue", false, List.of(DEFAULT_IMAGE)));
        bikes.add(new Bike(3, "Hybrid Bike", "Specialized", "New", "Black", true, List.of(DEFAULT_IMAGE)));
    }



}
