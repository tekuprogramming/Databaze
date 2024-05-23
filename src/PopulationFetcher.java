import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PopulationFetcher {
    public int getPopulation(String cityName) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");

            String apiUrl = "http://api.geonames.org/searchJSON?q=" + encodedCityName + "&maxRows=1&username=tekuuma";

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
            JsonArray geoNamesArray = jsonResponse.getAsJsonArray("geonames");
            if (geoNamesArray.size() > 0) {
                JsonObject geoName = geoNamesArray.get(0).getAsJsonObject();
                int population = geoName.get("population").getAsInt();
                return population;
            } else {
                System.out.println("No data available for population of " + cityName);
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error occurred while fetching population data: " + e.getMessage());
            return -1;
        }
    }

    public void printPopulation(String cityName) {
        int population = getPopulation(cityName);
        if (population != 1) {
            System.out.println("Population of " + cityName + ": " + population);
        } else {
            System.out.println("Error occurred while fetching population data for " + cityName);
        }
    }
}
