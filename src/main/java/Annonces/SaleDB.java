package Annonces;

import java.util.ArrayList;
import java.util.List;
import Bikes.Bike;
import Bikes.bikesDB;
import java.time.LocalDate;

public class SaleDB {
    public static List<Sale> salesList = new ArrayList<>(); // In-memory list to store sales

    static {
        // Add static sales to the salesList with bikes having ID 3, 4, and 5 from bikesDB
        salesList.add(new Sale(1, "Mountain Bike Sale", "High-performance mountain bike", LocalDate.of(2024, 5, 1), 7, 1, new ArrayList<>(), 300.0, bikesDB.bikes.get(2))); // Bike with ID 3
        salesList.add(new Sale(2, "Road Bike Sale", "Speedy road bike", LocalDate.of(2024, 6, 1), 7, 4, new ArrayList<>(), 250.0, bikesDB.bikes.get(3))); // Bike with ID 4
        salesList.add(new Sale(3, "Electric Bike Sale", "Eco-friendly electric bike", LocalDate.of(2024, 7, 1), 7, 5, new ArrayList<>(), 500.0, bikesDB.bikes.get(4))); // Bike with ID 5
    }
}
