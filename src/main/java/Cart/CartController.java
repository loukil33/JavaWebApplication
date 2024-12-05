package Cart;

import Bikes.*;
import Annonces.Sale;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;
import javax.ws.rs.*;

@Path("/cart")
public class CartController {
    private static final List<Cart> carts = new ArrayList<>(); // Track carts for different users
    private static final List<PaymentRecord> paymentHistory = new ArrayList<>(); // Store payment history
    private static final int currentUserId = 1; // Static user ID for testing; replace with actual user logic

    // Get cart for the current user (or a specific user by ID)
    private Cart getCartForUser(int userId) {
        return carts.stream().filter(cart -> cart.getUserId() == userId).findFirst().orElse(null);
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(@QueryParam("userId") int userId, Sale sale) {
        // Debug log to inspect the received Sale object
        System.out.println("Received Sale object: " + sale);
        
        // Validate the Sale object
        if (sale == null || sale.getSalePrice() <= 0 || sale.getBike() == null || sale.getBike().getImages() == null || sale.getBike().getImages().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid sale data provided. SalePrice and bike images must be valid.")
                           .build();
        }

        // Get the user's cart or create a new one
        Cart cart = getCartForUser(userId);
        if (cart == null) {
            cart = new Cart(userId); // Create a new cart if none exists
            carts.add(cart);
        }

        try {
            // Add item to cart
            cart.addItemToCart(sale);
            return Response.ok("Item added to cart.").build();
        } catch (IllegalArgumentException e) {
            // Handle errors from adding the item to the cart (e.g., already in cart)
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Error: " + e.getMessage())
                           .build();
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

        List<Sale> cartItems = cart.getItems();
        double totalPrice = cart.getTotalPrice();

        // Prepare a response with the needed attributes
        List<Map<String, Object>> responseItems = new ArrayList<>();
        for (Sale sale : cartItems) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("id", sale.getId());
            itemData.put("model", sale.getBike().getModel()); // Ensure you have the bike's model
            itemData.put("image", sale.getBike().getImages() != null && !sale.getBike().getImages().isEmpty() ? sale.getBike().getImages().get(0) : "default_image.jpg");
            itemData.put("salePrice", sale.getSalePrice());  // Ensure salePrice is included
            responseItems.add(itemData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("cartItems", responseItems);
        response.put("totalPrice", totalPrice);
        return Response.ok(response).build();
    }


    @DELETE
    @Path("/remove/{saleId}")
    public Response removeItem(@QueryParam("userId") int userId, @PathParam("saleId") int saleId) {
        Cart cart = getCartForUser(userId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();
        }

        Sale sale = cart.getItems().stream()
                        .filter(item -> item.getId() == saleId)
                        .findFirst()
                        .orElse(null);
        if (sale == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Item not found in the cart.")
                           .build();
        }

        try {
            cart.removeItemFromCart(saleId);
            return Response.ok(Map.of("cartItems", cart.getItems(), "totalPrice", cart.getTotalPrice()))
                           .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error removing item from cart.")
                           .build();
        }
    }
    
    


    @POST
    @Path("/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkout(@QueryParam("userId") int userId) {
        Cart cart = getCartForUser(userId);
        if (cart == null || cart.getTotalPrice() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Cart is empty or invalid.")
                           .build();
        }

        BankService bankService = new BankService();
        String clientSecret = bankService.createPaymentIntent(cart.getTotalPrice());

        if (clientSecret == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to create payment intent.")
                           .build();
        }

        try {
            for (Sale sale : cart.getItems()) {
                String image = sale.getBike().getImages().isEmpty() ? null : sale.getBike().getImages().get(0);

                paymentHistory.add(new PaymentRecord(
                    sale.getBike().getModel(),
                    sale.getSalePrice(),
                    LocalDateTime.now(),
                    clientSecret,
                    userId,
                    image
                ));
            }

            cart.checkoutCart();
            return Response.ok(Map.of("clientSecret", clientSecret)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error during checkout: " + e.getMessage())
                           .build();
        }
    }


    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentHistory(@QueryParam("userId") int userId) {
        List<PaymentRecord> userHistory = new ArrayList<>();

        for (PaymentRecord record : paymentHistory) {
            if (record.getUserId() == userId) {
                userHistory.add(record);
            }
        }

        if (userHistory.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No payment history found for this user.").build();
        }

        return Response.ok(userHistory).build();
    }
}