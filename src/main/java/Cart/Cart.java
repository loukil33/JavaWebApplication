package Cart;

import Bikes.Bike;
import Annonces.Sale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    private int userId;  // Store the userId to link the cart to a user
    private List<Sale> items;  // Use Sale instead of Bike to access sale price
    private double totalPrice;

    public Cart(int userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public void addItemToCart(Sale sale) {
        if (sale.getSalePrice() <= 0) {
            throw new IllegalArgumentException("Invalid sale price.");
        }

        // Ensure that the same sale item cannot be added twice
        boolean saleExists = items.stream().anyMatch(existingSale -> existingSale.getId() == sale.getId());
        if (saleExists) {
            throw new IllegalArgumentException("Sale item already in cart.");
        }

        items.add(sale); // Add the Sale object to the cart
        totalPrice += sale.getSalePrice(); // Add the sale price to total price
    }


    public void removeItemFromCart(int saleId) {
        Sale saleToRemove = items.stream()
            .filter(sale -> sale.getId() == saleId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Sale item not found in cart"));

        items.remove(saleToRemove);
        totalPrice = items.stream().mapToDouble(Sale::getSalePrice).sum(); // Update totalPrice
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Sale> getItems() {
        return items;
    }

    public int getUserId() {
        return userId;
    }

    public void checkoutCart() {
        items.clear();
        totalPrice = 0.0;
    }

    @Override
    public String toString() {
        return "Cart [userId=" + userId + ", items=" + items + ", totalPrice=" + totalPrice + "]";
    }
}
