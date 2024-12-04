package Cart;

import java.time.LocalDateTime;

public class PaymentRecord {
    private String bikeName;
    private double bikePrice;
    private LocalDateTime paymentDate;
    private String paymentId;
    private int userId;
    private String bikeImage; // Add this field

    // Constructor
    public PaymentRecord(String bikeName, double bikePrice, LocalDateTime paymentDate, String paymentId, int userId, String bikeImage) {
        this.bikeName = bikeName;
        this.bikePrice = bikePrice;
        this.paymentDate = paymentDate;
        this.paymentId = paymentId;
        this.userId = userId;
        this.bikeImage = bikeImage; // Initialize the image field
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

    public int getUserId() {
        return userId;
    }

    public String getBikeImage() {
        return bikeImage; // Add getter for the image
    }

    // To string for debugging
    @Override
    public String toString() {
        return "PaymentRecord{" +
                "bikeName='" + bikeName + '\'' +
                ", bikePrice=" + bikePrice +
                ", paymentDate=" + paymentDate +
                ", paymentId='" + paymentId + '\'' +
                ", userId=" + userId +
                ", bikeImage='" + bikeImage + '\'' +
                '}';
    }
}
