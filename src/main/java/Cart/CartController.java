package Cart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
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
	 private static List<Cart> carts = new ArrayList<>(); // Track carts for different users
	    private static int currentUserId = 1; // For testing, static user ID. You can modify this dynamically based on actual login

	    // Get cart for the current user (or a specific user by ID)
	    private Cart getCartForUser(int userId) {
	        return carts.stream().filter(cart -> cart.getUserId()== userId).findFirst().orElse(null);
	    }

	    @POST
	    @Path("/add")
	    @Consumes(MediaType.APPLICATION_JSON)
	    public String addItem(@QueryParam("userId") int userId, BikeForSale bike) {
	        Cart cart = getCartForUser(userId);

	        if (cart == null) {
	            cart = new Cart(userId); // Create a new cart if not found
	            carts.add(cart);
	        }

	        // Add the item to the cart
	        try {
	            cart.addItemToCart(bike);
	            return "Item added to cart.";
	        } catch (IllegalArgumentException e) {
	            return e.getMessage(); // Return error message
	        }
	    }

	    @GET
	    @Path("/items")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCartItems(@QueryParam("userId") int userId) {
	        // Get the user's cart (this is where you might retrieve the cart for a specific user)
	        Cart cart = getCartForUser(userId);

	        if (cart == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();
	        }

	        List<BikeForSale> cartItems = cart.getItems(); // Retrieve items from the cart
	        double totalPrice = cart.getTotalPrice(); // Get the total price from the Cart class
	        
	        if (cartItems.isEmpty()) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Cart is empty").build();
	        }
	        
	        // Create a response with both cart items and the total price
	        Map<String, Object> response = new HashMap<>();
	        response.put("cartItems", cartItems);
	        response.put("totalPrice", totalPrice);
	        
	        return Response.ok(response).build();
	    }

	    @DELETE
	    @Path("/remove/{bikeId}")
	    public Response removeItem(@QueryParam("userId") int userId, @PathParam("bikeId") int bikeId) {
	        Cart cart = getCartForUser(userId);
	        if (cart == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();
	        }

	        try {
	            cart.removeItemFromCart(bikeId); // This will also update the totalPrice in the Cart class
	            double totalPrice = cart.getTotalPrice(); // Get the updated total price
	            List<BikeForSale> cartItems = cart.getItems(); // Get the updated items list
	            
	            // Return both cart items and the updated total price
	            Map<String, Object> response = new HashMap<>();
	            response.put("cartItems", cartItems);
	            response.put("totalPrice", totalPrice);
	            
	            return Response.ok(response).build();
	        } catch (IllegalArgumentException e) {
	            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
	        }
	    }

   
	    @POST
	    @Path("/checkout")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response checkout(@QueryParam("userId") int userId) {
	        Cart cart = getCartForUser(userId);
	        if (cart == null || cart.getTotalPrice() <= 0) {
	            return Response.status(Response.Status.BAD_REQUEST).entity("Cart is empty or invalid.").build();
	        }

	        BankService bankService = new BankService();
	        String clientSecret = bankService.createPaymentIntent(cart.getTotalPrice()); // Pass the total price

	        if (clientSecret != null) {
	            // Clear the cart after generating the payment intent
	            cart.checkoutCart(); // This clears all items and resets the total price
	            return Response.ok(Map.of("clientSecret", clientSecret)).build();
	        }

	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Payment failed").build();
	    }


    /*
    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentHistory() {
        BankService bankService = new BankService();
        return Response.ok(bankService.getPaymentHistory()).build();
    }
    */


}
