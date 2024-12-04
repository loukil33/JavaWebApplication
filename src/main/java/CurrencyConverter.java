import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";
    private static final String BASE_CURRENCY = "USD"; // Default base currency

    // Fetch conversion rate from the API
    public static double getConversionRate(String targetCurrency) throws IOException {
        String apiUrl = API_URL + BASE_CURRENCY;
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        // Check if the response is successful
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to fetch exchange rate. HTTP Response Code: " + responseCode);
        }

        // Parse the JSON response
        StringBuilder inline = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
        }

        JSONObject json = new JSONObject(inline.toString());
        return json.getJSONObject("rates").getDouble(targetCurrency);
    }

    // Convert price
    public static double convertPrice(double price, String targetCurrency) throws IOException {
        double conversionRate = getConversionRate(targetCurrency);
        return price * conversionRate;
    }
}
