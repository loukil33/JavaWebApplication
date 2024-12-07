package Bikes;

import java.util.ArrayList;
import java.util.List;

public class bikesDB {
	
    public static List<Bike> bikes = new ArrayList<>();
    
    public static final String IMAGE_UPLOAD_DIR = "http://localhost:8081/UserWebService/css/images/";
    public static final String DEFAULT_IMAGE = "http://localhost:8081/UserWebService/css/images/imagevelo.jpg"; // Default image path

    static {
        bikes.add(new Bike(1,1, "Mountain Bike", "Trek", "New", "Red", true, List.of(IMAGE_UPLOAD_DIR + "1.jpg"),false));
        bikes.add(new Bike(2,1, "Road Bike", "Giant", "Used", "Blue", true, List.of(IMAGE_UPLOAD_DIR + "2.jpg"),false));
        bikes.add(new Bike(3, 4, "Gravel Bike", "Trek", "Used", "Brown", true, List.of(IMAGE_UPLOAD_DIR + "7.jpg"),false));
        bikes.add(new Bike(4, 4, "Commuter Bike", "Brompton", "New", "Red", true, List.of(IMAGE_UPLOAD_DIR + "8.jpg"),false));
        bikes.add(new Bike(5, 5, "Fat Tire Bike", "Mongoose", "New", "Orange", true, List.of(IMAGE_UPLOAD_DIR + "11.jpg"),false));
        bikes.add(new Bike(6, 5, "Touring Bike", "Surly", "New", "Purple", true, List.of(IMAGE_UPLOAD_DIR + "12.jpg"),false));
        
        bikes.add(new Bike(7,1, "Endurance Bike", "Pinarello", "New", "Red", true, List.of(IMAGE_UPLOAD_DIR + "9.jpg"),true));
        bikes.add(new Bike(8,1, "Electric Bike", "Riese & Müller", "Used", "Green", true, List.of(IMAGE_UPLOAD_DIR + "10.jpg"),true));
        bikes.add(new Bike(9,4, "Cruiser Bike", "Electra", "Used", "Pink", true, List.of(IMAGE_UPLOAD_DIR + "15.jpg"), true));
        bikes.add(new Bike(10,4, "Racing Bike", "Cervélo", "New", "Blue", true, List.of(IMAGE_UPLOAD_DIR + "16.jpg"), true));
        bikes.add(new Bike(11, 5, "Fat Tire Bike", "Gravel Bike", "New", "Yellow", true, List.of(IMAGE_UPLOAD_DIR + "13.jpg"),true));
        bikes.add(new Bike(12, 5, "Commuter Bike", "Electra", "New", "Purple", true, List.of(IMAGE_UPLOAD_DIR + "14.jpg"),true));
        
    }



}
