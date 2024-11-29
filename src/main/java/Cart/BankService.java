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

    public String processPayment(double amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100));
            params.put("currency", "usd");
            params.put("payment_method_types", List.of("card"));

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if ("succeeded".equals(paymentIntent.getStatus())) {
                savePaymentRecord("Sample Bike", amount, paymentIntent.getId()); // Use placeholder data
            }

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

    private void savePaymentRecord(String bikeName, double bikePrice, String paymentId) {
        PaymentRecord record = new PaymentRecord(
                bikeName,
                bikePrice,
                LocalDateTime.now(),
                paymentId
        );
        paymentHistory.add(record);
    }

    public List<PaymentRecord> getPaymentHistory() {
        // Add an example record for debugging
        if (paymentHistory.isEmpty()) {
            paymentHistory.add(new PaymentRecord(
                    "Sample Bike",
                    20.00,
                    LocalDateTime.now(),
                    "pi_dummy_id_12345"
            ));
        }
        return paymentHistory;
    }
}
