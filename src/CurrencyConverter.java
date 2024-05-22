import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {
    private static final String apiKey = "3746f3c9ba0247ac8c1e13bed7589f7a";
    private static final String baseUrl = "https://openexchangerates.org/api/latest.json";

    public void convertForCity(String city) {
        try {
            String currencyCode = getCurrencyCodeForCity(city);
            if (currencyCode != null) {
                double conversionRate = getConversionRate(currencyCode);
                if (conversionRate != -1) {
                    System.out.println("Currency conversion for " + city + ":");
                    System.out.println("1 USD = " + conversionRate + " " + currencyCode);
                } else {
                    System.out.println("Conversion rate not available for " + city);
                }
            } else {
                System.out.println("Currency code not available for " + city);
            }
        } catch (IOException e) {
            System.out.println("Error occurred during currency conversion: " + e.getMessage());
        }
    }

    public String getCurrencyCodeForCity(String city) {
        switch (city) {
            case "London":
                return "GBP";
            case "Prague":
                return "CZK";
            case "Tokyo":
                return "JPY";
            case "New York":
                return "USD";
            case "Rio de Janeiro":
                return "BRL";
            default:
                return null;
        }
    }

    public double getConversionRate(String targetCurrency) throws IOException {
        URL url = new URL(baseUrl + "?app_id=" + apiKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonObject rates = jsonResponse.getAsJsonObject("rates");
        if (rates.has(targetCurrency)) {
            return rates.get(targetCurrency).getAsDouble();
        } else {
            return -1;
        }
    }
}
