package Cart;

import java.time.LocalDateTime;

public class PaymentRecord {
    private String bikeName;
    private double bikePrice;
    private LocalDateTime paymentDate;
    private String paymentId;

    // Constructor
    public PaymentRecord(String bikeName, double bikePrice, LocalDateTime paymentDate, String paymentId) {
        this.bikeName = bikeName;
        this.bikePrice = bikePrice;
        this.paymentDate = paymentDate;
        this.paymentId = paymentId;
    }

    // Getters
    public String getBikeName() {
        return bikeName;
    }

    public double getBikePrice() {
        return bikePrice;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    // To string for debugging
    @Override
    public String toString() {
        return "PaymentRecord{" +
                "bikeName='" + bikeName + '\'' +
                ", bikePrice=" + bikePrice +
                ", paymentDate=" + paymentDate +
                ", paymentId='" + paymentId + '\'' +
                '}';
    }
}
