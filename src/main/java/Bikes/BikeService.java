package Bikes;

import java.util.ArrayList;
import java.util.List;

public class BikeService {
    private List<Bike> bikes = new ArrayList<>();

    public BikeService() {
        // Default bikes
        bikes.add(new Bike(1, "Mountain Bike", "Trek", "New", "Red", true, List.of("image1.jpg")));
        bikes.add(new Bike(2, "Road Bike", "Giant", "Used", "Blue", false, List.of("image2.jpg")));
    }

    public void addBike(Bike bike) {
        bikes.add(bike);
    }

    public List<Bike> getAvailableBikes() {
        List<Bike> availableBikes = new ArrayList<>();
        for (Bike bike : bikes) {
            if (bike.isAvailable()) {
                availableBikes.add(bike);
            }
        }
        return availableBikes;
    }

    public List<Bike> getAllBikes() {
        return bikes;
    }
}
