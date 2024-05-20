import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataProcessor {
    private Gson gson = new Gson();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault());

    public void processAndSaveData(String jsonData) throws IOException {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = gson.fromJson(jsonData, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error parsing JSON data: " + e.getMessage());
            return;
        }

        processCurrentData(jsonObject);

        JsonObject forecastData = getForecastData(jsonObject);
        if (forecastData != null) {
            processForecastData(forecastData);
        }
    }

    public JsonObject getForecastData(JsonObject jsonObject) {
        JsonObject forecastData = new JsonObject();
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=London,uk&APPID=3eaab97ed540e25e7b261d686d5dfc42";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                forecastData = gson.fromJson(response.toString(), JsonObject.class);

                if (!forecastData.has("list")) {
                    System.out.println("Forecast data is missing 'list field.'");
                    return null;
                }
            } else {
                throw new IOException("Error response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error getting forecast data: " + e.getMessage());
        }
        return forecastData;
    }

    public void processCurrentData(JsonObject jsonObject) throws IOException {
        try {
            String city = jsonObject.get("name").getAsString();
            String country = jsonObject.getAsJsonObject("sys").get("country").getAsString();
            double latitude = jsonObject.getAsJsonObject("coord").get("lat").getAsDouble();
            double longitude = jsonObject.getAsJsonObject("coord").get("lon").getAsDouble();
            double temperature = jsonObject.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
            double humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsDouble();
            double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
            int cloudiness = jsonObject.getAsJsonObject("clouds").get("all").getAsInt();
            long sunriseUnix = jsonObject.getAsJsonObject("sys").get("sunrise").getAsLong();
            long sunsetUnix = jsonObject.getAsJsonObject("sys").get("sunset").getAsLong();

            String sunrise = formatter.format(Instant.ofEpochSecond(sunriseUnix));
            String sunset = formatter.format(Instant.ofEpochSecond(sunsetUnix));

            System.out.println("Current weather data:");
            System.out.println("City: " + city);
            System.out.println("Country: " + country);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            System.out.printf("Temperature: %.2f °C%n", temperature);
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind speed: " + windSpeed + " m/s");
            System.out.println("Cloudiness: " + cloudiness + "%");
            System.out.println("Sunrise: " + sunrise);
            System.out.println("Sunset: " + sunset);
            String weatherCategory = getWeatherCategory(temperature);
            System.out.println("Weather category: " + weatherCategory);
            System.out.println("---------------------------------------");

            saveDataToFile(city, country, latitude, longitude, temperature, humidity, windSpeed, cloudiness, sunrise, sunset, weatherCategory);
        } catch (Exception e) {
            System.out.println("Error processing current weather data: " + e.getMessage());
        }
    }

    public void processForecastData(JsonObject forecastData) {
        try {
            JsonArray forecasts = forecastData.getAsJsonArray("list");
            if (forecasts == null) {
                System.out.println("Forecasts data is missing 'list' field.");
                return;
            }

            for (int i = 0; i < forecasts.size(); i++) {
                JsonObject forecast = forecasts.get(i).getAsJsonObject();
                long forecastTimestamp = forecast.get("dt").getAsLong();
                double forecastTemperature = forecast.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
                double forecastHumidity = forecast.getAsJsonObject("main").get("humidity").getAsDouble();
                double forecastWindSpeed = forecast.getAsJsonObject("wind").get("speed").getAsDouble();
                int forecastCloudiness = forecast.getAsJsonObject("clouds").get("all").getAsInt();

                String forecastDateTime = formatter.format(Instant.ofEpochSecond(forecastTimestamp));

                String weatherCategory = getWeatherCategory(forecastTemperature);

                System.out.println("Forecast data:");
                System.out.println("Timestamp: " + forecastDateTime);
                System.out.printf("Temperature: %.2f °C%n", forecastTemperature);
                System.out.println("Humidity: " + forecastHumidity + "%");
                System.out.println("Wind speed: " + forecastWindSpeed + " m/s");
                System.out.println("Cloudiness: " + forecastCloudiness + "%");
                System.out.println("Weather category: " + weatherCategory);
                System.out.println("---------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Error processing forecast data: " + e.getMessage());
        }
    }

    public void saveDataToFile(String city, String country, double latitude, double longitude, double temperature,
                               double humidity, double windSpeed, int cloudiness, String sunrise, String sunset, String weatherCategory) throws IOException {
        try (FileWriter writer = new FileWriter("src//processed_data.txt")) {
            writer.write("City: " + city + "\n");
            writer.write("Country: " + country + "\n");
            writer.write("Latitude: " + latitude + "\n");
            writer.write("Longitude: " + longitude + "\n");
            writer.write("Temperature: " + temperature + "°C\n");
            writer.write("Humidity: " + humidity + "%\n");
            writer.write("Wind speed: " + windSpeed + " m/s\n");
            writer.write("Cloudiness: " + cloudiness + "%\n");
            writer.write("Sunrise: " + sunrise + "\n");
            writer.write("Sunset: " + sunset + "\n");
            writer.write("Weather category: " + weatherCategory + "\n");
            System.out.println("Data saved to processed_data.txt");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    public String getWeatherCategory(double temperature) {
        if (temperature < 0) {
            return "Freezing";
        } else if (temperature >= 0 && temperature < 10) {
            return "Cold";
        } else if (temperature >= 10 && temperature < 20) {
            return "Cool";
        } else if (temperature >= 20 && temperature < 30) {
            return "Warm";
        } else {
            return "Hot";
        }
    }
}
