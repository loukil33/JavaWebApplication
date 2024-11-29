package Cart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    
    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItems() {
        return Response.ok(cart.getItems()).build();
    }
    
    @POST
    @Path("/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkout() {
        BankService bankService = new BankService();
        String clientSecret = bankService.createPaymentIntent(20.00); // Default price for testing
        if (clientSecret != null) {
            return Response.ok(Map.of("clientSecret", clientSecret)).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Payment failed").build();
    }

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentHistory() {
        BankService bankService = new BankService();
        return Response.ok(bankService.getPaymentHistory()).build();
    }




   
}
