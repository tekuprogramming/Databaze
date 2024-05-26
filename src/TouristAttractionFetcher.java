import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TouristAttractionFetcher {
    private static final String apiKey = "fsq3EcKnfEJJe4FpQ8vfNVAjUuAORfaOfOmabnmrR7iU6pE=";
    private static final String baseUrl = "https://api.foursquare.com/v3/places/search";
    public void displayTouristAttractions(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            String urlStr = baseUrl + "?near=" + encodedCity + "&limit=10";
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", apiKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonResponse = jsonParser.parse(reader).getAsJsonObject();
            reader.close();

            JsonArray results = jsonResponse.getAsJsonArray("results");

            System.out.println("Tourist attractions in " + city + ":");
            for (int i = 0; i < results.size(); i++) {
                JsonObject venue = results.get(i).getAsJsonObject();
                String name = venue.get("name").getAsString();
                String address = venue.get("location").getAsJsonObject().get("formatted_address").getAsString();
                System.out.println((i + 1) + ". " + name + " - " + address);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while fetching tourist attractions: " + e.getMessage());
        }
    }
}
