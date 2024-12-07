package Cart;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankService {
    static {
        Stripe.apiKey = "sk_test_51MfRIsHcaMLPP7A1X3INIItKLbEljzGYdpTujtvwb4mrggNEJtwS1SG2C6MyxYdz8T2uPVh219jsg7LBZRWSh2Ye00QEgBJZmW";
    }

    private List<PaymentRecord> paymentHistory;

    public BankService() {
        this.paymentHistory = new ArrayList<>();
    }

    public String processPayment(double amount, String bikeName, String bikeImage, int userId) {
        try {
            // Create payment intent parameters
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100)); // Stripe expects the amount in cents
            params.put("currency", "eur");
            params.put("payment_method_types", List.of("card"));

            // Create the PaymentIntent using Stripe API
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Check the status of the payment
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Save payment record with detailed information
                savePaymentRecord(bikeName, amount, paymentIntent.getId(), userId, bikeImage);
            }

            // Return the status of the payment
            return paymentIntent.getStatus();
        } catch (StripeException e) {
            e.printStackTrace();
            return "Rejected";
        }
    }


    public String createPaymentIntent(double amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100));
            params.put("currency", "usd");
            params.put("payment_method_types", List.of("card"));

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void savePaymentRecord(String bikeName, double bikePrice, String paymentId, int userId, String bikeImage) {
        // Validate inputs
        if (bikeName == null || bikeName.isEmpty()) {
            throw new IllegalArgumentException("Bike name cannot be null or empty.");
        }
        if (bikePrice <= 0) {
            throw new IllegalArgumentException("Bike price must be greater than zero.");
        }
        if (paymentId == null || paymentId.isEmpty()) {
            throw new IllegalArgumentException("Payment ID cannot be null or empty.");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid.");
        }
        if (bikeImage == null || bikeImage.isEmpty()) {
            throw new IllegalArgumentException("Bike image URL cannot be null or empty.");
        }

        // Create and add a new PaymentRecord
        PaymentRecord record = new PaymentRecord(
                bikeName,
                bikePrice,
                LocalDateTime.now(),
                paymentId,
                userId,
                bikeImage
        );

        paymentHistory.add(record);
    }

}
