package Annonces;
import Currency.CurrencyConverter;
import java.time.LocalDate;
import java.util.List;

public class Sale extends Annonce {

    private double salePrice; // Assumed to be in EUR by default

    public Sale(int id, String title, String description, LocalDate startDate, int duration, List<Note> notes, double salePrice) {
        super(id, title, description, startDate, duration, notes);
        this.salePrice = salePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getPriceInCurrency(String currencyCode) {
        try {
            return CurrencyConverter.convertCurrency(currencyCode, this.salePrice); // Convert from EUR to target currency
        } catch (Exception e) {
            System.err.println("Error converting currency: " + e.getMessage());
            return -1; // Indicate failure
        }
    }

    @Override
    public String toString() {
        return "Sale [salePrice (EUR)=" + salePrice + "]";
    }
}
