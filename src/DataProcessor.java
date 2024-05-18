import com.google.gson.Gson;
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

        saveDataToFile(city, country, latitude, longitude, temperature, humidity, windSpeed, cloudiness, sunrise, sunset);

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
            writer.write("Sunset: " + longitude + "\n");
            System.out.println("Data saved to processed_data.txt");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }
}
