package Annonces;
import Bikes.*;
import java.time.LocalDate;
import java.util.List;

import Bikes.Bike;
import Users.User;

public class Sale extends Annonce{

    private double salePrice; // Assumed to be in EUR by default

    
    public Sale() {
        
    }

    public Sale(int id, String title, String description, LocalDate startDate, int duration, int userid, List<Note> notes, double salePrice, Bike bike) {
        super(id, title, description, startDate, duration, userid, bike, notes);
        this.salePrice = salePrice;
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

  

    @Override
    public String toString() {
        return "Sale [salePrice=" + salePrice + ", " + super.toString() + "]";
    }
}
 

 
 
 

