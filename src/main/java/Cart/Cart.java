package Cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable{
	
	 	private int id;
	    private List<BikeForSale> items;
	    private double totalPrice;

	    public Cart(int id, int i) {
	        this.id = id;
	        this.items = new ArrayList<>();
	        this.totalPrice = 0.0;
	    }

	    public void addItemToCart(BikeForSale bike) {
	        items.add(bike);
	        totalPrice += bike.getPrice();
	    }

	    public void removeItemFromCart(int bikeId) {
	        items.removeIf(bike -> bike.getId() == bikeId);
	        totalPrice = items.stream().mapToDouble(BikeForSale::getPrice).sum();
	    }

	    public double getTotalPrice() {
	        return totalPrice;
	    }

	    public void checkoutCart() {
	        items.clear();
	        totalPrice = 0.0;
	    }

	    // Getters and setters
	    public int getId() {
	        return id;
	    }

	    public List<BikeForSale> getItems() {
	        return items;
	    }

}
