import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MuseumFetcher {
    private static final String apiKey = "tTQefmw6eLhOCz1bX8cFmeVKAuoyONVHz28lz4Gn7EOq4dbOWVC5RR9847RuiKJe7FiNG5yXtepTrkd6ABpqY5bR88Efur6tfh4eqhbWGz0TgTVS2go3GZAZdY1QZnYx";
    private static final String baseUrl = "https://api.yelp.com/v3/businesses/search";

    public void searchMuseums(String cityName) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            String apiUrl = baseUrl + "?location=" + encodedCityName + "&categories=museums&sort_by=rating&limit=10";

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
            JsonArray businesses = jsonResponse.getAsJsonArray("businesses");

            System.out.println("Top museums in " + cityName + ":");
            for (JsonElement businessElement : businesses) {
                JsonObject business = businessElement.getAsJsonObject();
                String museumName = business.get("name").getAsString();
                double rating = business.get("rating").getAsDouble();
                String address = business.get("location").getAsJsonObject().get("address1").getAsString();
                System.out.println(museumName + " - Rating: " + rating + " - Address: " + address);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while fetching hotel data for " + cityName);
        }
    }
}
