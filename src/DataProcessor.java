import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Class for processing data.
 */
public class DataProcessor {
    private static final String apiKey = "3eaab97ed540e25e7b261d686d5dfc42";
    private Gson gson = new Gson();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault());

    /**
     * Processes and saves data.
     * @param jsonData JSON data you want to work with.
     * @throws IOException
     */
    public void processAndSaveData(String jsonData) throws IOException {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = gson.fromJson(jsonData, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error parsing JSON data: " + e.getMessage());
            return;
        }

        processCurrentData(jsonObject);
    }

    /**
     * Processes and saves forecast data.
     * @param jsonData JSON data you want to work with.
     */
    public void processAndSaveForecastData(String jsonData) {
        JsonObject forecastData;
        try {
            forecastData = gson.fromJson(jsonData, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error parsing forecast JSON data: " + e.getMessage());
            return;
        }

        processForecastData(forecastData);
    }

    /**
     * Processes and saves current weather data.
     * @param jsonObject JSON data you want to work with.
     * @throws IOException
     */
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
        } catch (IOException e) {
            System.out.println("Error processing current weather data: " + e.getMessage());
        }
    }

    /**
     * Processes forecast data.
     * @param forecastData Forecast data you want to work with.
     */
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

            saveForecastDataToFile(forecasts);
        } catch (Exception e) {
            System.out.println("Error processing forecast data: " + e.getMessage());
        }
    }

    /**
     * Saves current weather data to .txt file.
     * @param city City.
     * @param country Country.
     * @param latitude Latitude.
     * @param longitude Longitude.
     * @param temperature Temperature (Celsius).
     * @param humidity Humidity (%).
     * @param windSpeed Wind speed (m/s).
     * @param cloudiness Cloudiness (%).
     * @param sunrise Sunrise time (yy:mm:dd hh:mm:ss).
     * @param sunset Sunset time (yy:mm:dd hh:mm:ss).
     * @param weatherCategory Weather category (freezing, cold, cool, warm, or hot).
     * @throws IOException
     */
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
            writer.write("---------------------------------------\n");
            System.out.println("Data saved to processed_data.txt");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    /**
     * Saves forecast data to .txt file.
     * @param forecasts Array of JSON forecasts.
     */
    public void saveForecastDataToFile(JsonArray forecasts) {
        try (FileWriter writer = new FileWriter("src//processed_forecastdata.txt")) {
            for (int i = 0; i < forecasts.size(); i++) {
                JsonObject forecast = forecasts.get(i).getAsJsonObject();
                long forecastTimestamp = forecast.get("dt").getAsLong();
                double forecastTemperature = forecast.getAsJsonObject("main").get("temp").getAsDouble() - 273.15;
                double forecastHumidity = forecast.getAsJsonObject("main").get("humidity").getAsDouble();
                double forecastWindSpeed = forecast.getAsJsonObject("wind").get("speed").getAsDouble();
                int forecastCloudiness = forecast.getAsJsonObject("clouds").get("all").getAsInt();

                String forecastDateTime = formatter.format(Instant.ofEpochSecond(forecastTimestamp));

                String weatherCategory = getWeatherCategory(forecastTemperature);

                writer.write("Forecast data:\n");
                writer.write("Timestamp: " + forecastDateTime + "\n");
                writer.write(String.format("Temperature: %.2f °C%n", forecastTemperature));
                writer.write("Humidity: " + forecastHumidity + "%\n");
                writer.write("Wind speed: " + forecastWindSpeed + " m/s\n");
                writer.write("Cloudiness: " + forecastCloudiness + "%\n");
                writer.write("Weather category: " + weatherCategory + "\n");
                writer.write("---------------------------------------\n");
            }
            System.out.println("Forecast data saved to processed_forecastdata.txt");
        } catch (IOException e) {
            System.out.println("Error saving forecast data to file: " + e.getMessage());
        }
    }

    /**
     * Saves current time to file.
     * @param cityName City, the current time of which you want to save.
     */
    public void saveCurrentTimeToFile(String cityName) {
        String fileName = "src//current_time.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            ZoneId zoneId = ZoneId.of(getTimeZone(cityName));
            LocalDateTime currentTime = LocalDateTime.now(zoneId);
            writer.println("Current time in " + cityName + ": " + currentTime);
            System.out.println("Current time in " + cityName + " has been saved to current_time.txt");
        } catch (IOException e) {
            System.out.println("Error saving current time to file: " + e.getMessage());
        }
    }

    /**
     * Determines the weather category.
     * @param temperature Temperature.
     * @return Returns the weather category.
     */
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

    public String getApiKey() {
        return apiKey;
    }

    /**
     * Determines the time zone of the city.
     * @param city City you want the time zone of.
     * @return Returns the time zone, in default case returns GMT.
     */
    public String getTimeZone(String city) {
        switch (city) {
            case "London":
                return "Europe/London";
            case "Prague":
                return "Europe/Prague";
            case "Barcelona":
                return "Europe/Madrid";
            case "New York":
                return "America/New_York";
            case "Ottawa":
                return "America/Toronto";
            default:
                return "GMT";
        }
    }
}
