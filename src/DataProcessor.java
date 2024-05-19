import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataProcessor {
    public void processAndSaveData(String jsonData) throws IOException {
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

        processCurrentData(jsonObject);

        processForecastData(jsonObject);
    }

    public void processCurrentData(JsonObject jsonObject) throws IOException {
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withLocale(Locale.UK)
                    .withZone(ZoneId.systemDefault());

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
            System.out.println("---------------------------------------");

            saveDataToFile(city, country, latitude, longitude, temperature, humidity, windSpeed, cloudiness, sunrise, sunset);
    }

    public void processForecastData(JsonObject jsonObject) {
        JsonArray forecasts = jsonObject.getAsJsonArray("list");

        for (int i = 0; i < forecasts.size(); i++) {
            JsonObject forecast = forecasts.get(i).getAsJsonObject();
            long forecastTimestamp = forecast.get("dt").getAsLong();
            double forecastTemperature = forecast.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
            double forecastHumidity = forecast.getAsJsonObject("main").get("humidity").getAsDouble();
            double forecastWindSpeed = forecast.getAsJsonObject("wind").get("speed").getAsDouble();
            int forecastCloudiness = forecast.getAsJsonObject("clouds").get("all").getAsInt();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withLocale(Locale.UK)
                    .withZone(ZoneId.systemDefault());

            String forecastDateTime = formatter.format(Instant.ofEpochSecond(forecastTimestamp));

            System.out.println("Forecast data:");
            System.out.println("Timestamp: " + forecastDateTime);
            System.out.printf("Temperature: %.2f °C%n", forecastTemperature);
            System.out.println("Humidity: " + forecastHumidity+ "%");
            System.out.println("Wind speed: " + forecastWindSpeed + " m/s");
            System.out.println("Cloudiness: " + forecastCloudiness + "%");
            System.out.println("---------------------------------------");
        }
    }

    public void saveDataToFile(String city, String country, double latitude, double longitude, double temperature,
                               double humidity, double windSpeed, int cloudiness, String sunrise, String sunset) throws IOException {
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
            String weatherCategory = getWeatherCategory(temperature);
            System.out.println("Weather category: " + weatherCategory);
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
