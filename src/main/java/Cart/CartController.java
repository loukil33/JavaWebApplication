package Cart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;

@Path("/cart")
public class CartController {
    private static List<Cart> carts = new ArrayList<>(); // Track carts for different users
    private static List<PaymentRecord> paymentHistory = new ArrayList<>(); // Store payment history
    private static int currentUserId = 1; // Static user ID for testing; replace with actual user logic

    // Get cart for the current user (or a specific user by ID)
    private Cart getCartForUser(int userId) {
        return carts.stream().filter(cart -> cart.getUserId() == userId).findFirst().orElse(null);
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

        try {
            cart.addItemToCart(bike);
            return "Item added to cart.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItems(@QueryParam("userId") int userId) {
        Cart cart = getCartForUser(userId);

        if (cart == null || cart.getItems().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("cartItems", new ArrayList<>());
            response.put("totalPrice", 0.0);
            response.put("message", "Cart is empty");
            return Response.ok(response).build();
        }

        List<BikeForSale> cartItems = cart.getItems();
        double totalPrice = cart.getTotalPrice();

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
            // Attempt to remove the bike by its ID
            cart.removeItemFromCart(bikeId);

            // Get updated cart items and total price
            List<BikeForSale> cartItems = cart.getItems();
            double totalPrice = cart.getTotalPrice();

            // Return updated cart and price details
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
        String clientSecret = bankService.createPaymentIntent(cart.getTotalPrice());

        if (clientSecret != null) {
            for (BikeForSale bike : cart.getItems()) {
                paymentHistory.add(new PaymentRecord(
                    bike.getModel(),
                    bike.getSalePrice(),
                    LocalDateTime.now(),
                    clientSecret,
                    userId,
                    bike.getImage() // Pass the bike image
                ));
            }

            cart.checkoutCart();
            return Response.ok(Map.of("clientSecret", clientSecret)).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Payment failed").build();
    }


    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentHistory(@QueryParam("userId") int userId) {
        List<PaymentRecord> userHistory = new ArrayList<>();

        for (PaymentRecord record : paymentHistory) {
            if (record.getUserId() == userId) { // Ensure history is filtered by user ID
                userHistory.add(record);
            }
        }

        if (userHistory.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No payment history found for this user.").build();
        }

        return Response.ok(userHistory).build();
    }

}
