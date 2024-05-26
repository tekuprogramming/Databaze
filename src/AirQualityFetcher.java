import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AirQualityFetcher {
    private static final String apiKey = "d3f85466-e368-4e56-9142-4ce2b22d3c11";

    public void getAirQuality(String cityName) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            String apiUrl = "https://api.airvisual.com/v2/city?city=" + encodedCityName + "&key=" + apiKey;

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
            JsonObject data = jsonResponse.getAsJsonObject("data");
            JsonObject current = data.getAsJsonObject("current");
            JsonObject pollution = current.getAsJsonObject("pollution");

            int aqi = pollution.get("aqius").getAsInt();
            String mainPollutant = pollution.get("mainus").getAsString();

            System.out.println("Air quality in " + cityName + ":");
            System.out.println("AQÍ: " + aqi);
            System.out.println("Main pollutant: " + mainPollutant);
        } catch (Exception e) {
            System.out.println("Error occurred while fetching air quality data: " + e.getMessage());
        }
        }
    }
