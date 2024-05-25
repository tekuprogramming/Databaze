import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ShopFetcher {
    private static final String apiKey = "tTQefmw6eLhOCz1bX8cFmeVKAuoyONVHz28lz4Gn7EOq4dbOWVC5RR9847RuiKJe7FiNG5yXtepTrkd6ABpqY5bR88Efur6tfh4eqhbWGz0TgTVS2go3GZAZdY1QZnYx";
    private static final String baseUrl = "https://api.yelp.com/v3/businesses/search";

    public void searchShops(String cityName) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            String apiUrl = baseUrl + "?location=" + encodedCityName + "&term=shops&limit=5";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray shops = jsonResponse.getAsJsonArray("businesses");

            System.out.println("Shopping areas in " + cityName + ":");
            for (JsonElement shopElement : shops) {
                JsonObject shop = shopElement.getAsJsonObject();
                String shopName = shop.get("name").getAsString();
                System.out.println(shopName);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while fetching restaurant data for " + cityName);
        }
    }
}
