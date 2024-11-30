package Cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    private int userId;  // Store the userId to link the cart to a user
    private List<BikeForSale> items;
    private double totalPrice;

    public Cart(int userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public void addItemToCart(BikeForSale bike) {
        if (bike.getSalePrice() <= 0) {
            throw new IllegalArgumentException("Invalid bike price.");
        }
        // Ensure that the same bike cannot be added twice
        boolean bikeExists = items.stream().anyMatch(existingBike -> existingBike.getId() == bike.getId());
        if (bikeExists) {
            throw new IllegalArgumentException("Bike already in cart.");
        }
        items.add(bike);
        totalPrice += bike.getSalePrice();
    }

    public void removeItemFromCart(int bikeId) {
        BikeForSale bikeToRemove = items.stream()
            .filter(bike -> bike.getId() == bikeId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Bike not found in cart"));

        items.remove(bikeToRemove);
        totalPrice = items.stream().mapToDouble(BikeForSale::getSalePrice).sum(); // Update totalPrice
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<BikeForSale> getItems() {
        return items;
    }

    public int getUserId() {
        return userId;
    }
}
