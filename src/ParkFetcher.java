import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParkFetcher {

    public List<String> getPopularNaturalReserves(String cityName) {
        List<String> popularReserves = new ArrayList<>();
        try {
            String apiUrl = "https://nominatim.openstreetmap.org/search?city=" + cityName + "&format=json&limit=10";

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

            JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String reserveName = jsonObject.get("display_name").getAsString();
                popularReserves.add(reserveName);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while fetching restaurant data for " + cityName);
        }
        return popularReserves;
    }

    public void printPopularNaturalReserves(String cityName) {
        List<String> popularReserves = getPopularNaturalReserves(cityName);
        System.out.println("Popular natural reserves in " + cityName + ":");
        for (String reserve : popularReserves) {
            System.out.println(reserve);
        }
    }
}
