package Bikes;

import java.util.ArrayList;
import java.util.List;

public class bikesDB {
	
    public static List<Bike> bikes = new ArrayList<>();
    
    public static final String IMAGE_UPLOAD_DIR = "http://localhost:8081/UserWebService/css/images/";
    public static final String DEFAULT_IMAGE = "http://localhost:8081/UserWebService/css/images/imagevelo.jpg"; // Default image path

    static {
        bikes.add(new Bike(1,1, "Mountain Bike", "Trek", "New", "Red", true, null));
        bikes.add(new Bike(2,1, "Road Bike", "Giant", "Used", "Blue", true, null));
        bikes.add(new Bike(3,1, "Hybrid Bike", "Specialized", "New", "Black", true, null));
    }



}
