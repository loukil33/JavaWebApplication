package Cart;
/*
 * import com.stripe.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
 */
import java.util.HashMap;
import java.util.Map;
public class BankService {
	/* 
	 * public BankService() {
        Stripe.apiKey = "sk_test_YourStripeSecretKey";
    }

    public String processPayment(int userId, double amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100)); // Stripe uses cents
            params.put("currency", "eur");
            params.put("payment_method_types", List.of("card"));

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getStatus();
        } catch (StripeException e) {
            e.printStackTrace();
            return "Rejected";
        }
    } 
    */
}
