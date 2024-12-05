package Annonces;
import Bikes.*;
import Currency.CurrencyConverter;
import java.time.LocalDate;
import java.util.List;

import Bikes.Bike;
import Users.User;

public class Sale extends Annonce{

    //private Bike bike; // Composition relationship
    private double salePrice; // Assumed to be in EUR by default

    // Default constructor for deserialization
    public Sale() {
        // No-op constructor needed for deserialization
    }

    // Parameterized constructor
    public Sale(int id, String title, String description, LocalDate startDate, int duration, int userid, List<Note> notes, double salePrice, Bike bike) {
        super(id, title, description, startDate, duration, userid, notes);
        this.salePrice = salePrice;
        this.setBike(bike); // Set the bike using the inherited setBike method
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    /*
    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }*/

    public double getPriceInCurrency(String currencyCode) {
        try {
            return CurrencyConverter.convertCurrency(currencyCode, this.salePrice); // Convert from EUR to target currency
        } catch (Exception e) {
            System.err.println("Error converting currency: " + e.getMessage());
            return -1; // Indicate failure
        }
    }

    @Override
    public String toString() {
        return "Sale [salePrice=" + salePrice + ", " + super.toString() + "]";
    }
}
 

 
 
 

