import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AirQualityFetcher {

    DataProcessor processor = new DataProcessor();

    public void getAirQuality(String cityName) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            String geoApiUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + encodedCityName + "&limit=1&appid=" + processor.getApiKey();

            URL geoUrl = new URL(geoApiUrl);
            HttpURLConnection geoConnection = (HttpURLConnection) geoUrl.openConnection();
            geoConnection.setRequestMethod("GET");

            BufferedReader geoReader = new BufferedReader(new InputStreamReader(geoConnection.getInputStream()));
            StringBuilder geoResponse = new StringBuilder();
            String geoLine;
            while ((geoLine = geoReader.readLine()) != null) {
                geoResponse.append(geoLine);
            }
            geoReader.close();

            JsonArray geoArray = JsonParser.parseString(geoResponse.toString()).getAsJsonArray();
            if (geoArray.size() == 0) {
                System.out.println("City not found,");
                return;
            }

            JsonObject geoObject = geoArray.get(0).getAsJsonObject();
            double lat = geoObject.get("lat").getAsDouble();
            double lon = geoObject.get("lon").getAsDouble();

            String apiUrl = "http://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat + "&lon=" + lon + "&appid=" + processor.getApiKey();

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
            JsonArray list = jsonResponse.getAsJsonArray("list");
            if (list != null && list.size() > 0) {
                JsonObject main = list.get(0).getAsJsonObject().getAsJsonObject("main");
                JsonObject components = list.get(0).getAsJsonObject().getAsJsonObject("components");

                if (main != null && components != null) {
                    Integer aqi = main.has("aqi") ? main.get("aqi").getAsInt() : null;
                    Double pm10 = components.has("pm10") ? components.get("pm10").getAsDouble() : null;

                    System.out.println("Air quality in " + cityName + ":");
                    if (aqi != null) {
                        System.out.println("AQI: " + aqi);
                    } else {
                        System.out.println("AQI data not available.");
                    }
                    if (pm10 != null) {
                        System.out.println("PM10: " + pm10);
                    } else {
                        System.out.println("PM10 data not available.");
                    }
                } else {
                    System.out.println("Air quality data not available for " + cityName);
                }
            } else {
                System.out.println("No air quality data available for " + cityName);
            }
        } catch (Exception e) {
            System.out.println("Error occurred while fetching air quality data: " + e.getMessage());
        }
        }
    }
