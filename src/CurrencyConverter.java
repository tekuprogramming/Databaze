import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class for converting one currency to another.
 */
public class CurrencyConverter {
    private static final String apiKey = "3746f3c9ba0247ac8c1e13bed7589f7a";
    private static final String baseUrl = "https://openexchangerates.org/api/latest.json";

    /**
     * Converts a chosen city's currency to USD.
     * @param city City, the currency of which you want to convert.
     */
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

    /**
     * Gets the abbreviation of the city's currency.
     * @param city City, the currency of which you want an abbreviation of.
     * @return Returns null in the default case.
     */
    public String getCurrencyCodeForCity(String city) {
        switch (city) {
            case "London":
                return "GBP";
            case "Prague":
                return "CZK";
            case "Barcelona":
                return "EUR";
            case "New York":
                return "USD";
            case "Ottawa":
                return "CAD";
            default:
                return null;
        }
    }

    /**
     * Gets the conversion rate for the conversion using Open Exchange Rates API.
     * @param targetCurrency The currency you want to convert.
     * @return If the rates have targetCurrency, it returns targetCurrency as double, if not, it returns -1.
     * @throws IOException
     */
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
