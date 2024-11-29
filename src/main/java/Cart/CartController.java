package Cart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/cart")
public class CartController {
	 // Static cart instance (shared globally)
    private static Cart cart = new Cart(1, 0); // Static cart with a default ID

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addItem(BikeForSale bike) {
        cart.addItemToCart(bike);
        return "Item added to cart";
    }

    @DELETE
    @Path("/remove/{bikeId}")
    public String removeItem(@PathParam("bikeId") int bikeId) {
        cart.removeItemFromCart(bikeId);
        return "Item removed from cart";
    }

    /*
    @POST
    @Path("/checkout")
    public String checkout() {
        BankService bankService = new BankService();
        String paymentStatus = bankService.processPayment(0, cart.getTotalPrice()); // Passing 0 as the dummy user ID
        if ("succeeded".equals(paymentStatus)) {
            cart.checkoutCart();
            return "Payment successful and cart cleared";
        }
        return "Payment failed";
    }*/
}
