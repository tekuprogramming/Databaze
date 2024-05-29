import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for events.
 */
public class EventFetcher {

    private static final String apiKey = "zeV40MUyFKm0wL7JEcowYME1ybsDutZr";

    /**
     * Fetches events using Ticket Master API.
     * @param cityName Name of the city you want events for.
     */
    public void searchEvents(String cityName) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            String apiUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + apiKey + "&city=" + encodedCityName;

            URL url = new URL(apiUrl);
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
            JsonArray events = jsonResponse.getAsJsonObject("_embedded").getAsJsonArray("events");

            Set<String> uniqueEvents = new HashSet<>();
            System.out.println("Events in " + cityName + ":");
            for (JsonElement eventElement : events) {
                JsonObject event = eventElement.getAsJsonObject();
                String eventName = event.get("name").getAsString().toLowerCase();
                if (uniqueEvents.add(eventName)) {
                    System.out.println(event.get("name").getAsString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while fetching event data: " + e.getMessage());
        }
    }
    }
