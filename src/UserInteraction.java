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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInteraction {
    private DataProcessor processor = new DataProcessor();
    private DataDownloader downloader = new DataDownloader();
    private CurrencyConverter converter = new CurrencyConverter();
    private PopulationFetcher fetcher = new PopulationFetcher();
    private YelpApiFetcher apiFetcher = new YelpApiFetcher();
    private EventFetcher eventFetcher = new EventFetcher();
    private static final String foursquareApiKey = "fsq3EcKnfEJJe4FpQ8vfNVAjUuAORfaOfOmabnmrR7iU6pE=";
    private static final String foursquareBaseUrl = "https://api.foursquare.com/v3/places/search";

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String[] cities = {"London", "Prague", "Barcelona", "New York", "Ottawa"};
        String[] countries = {"uk", "cz", "es", "us", "ca"};

        System.out.println("Select a city to get weather data: ");
        for (int i = 0; i < cities.length; i++) {
            System.out.println((i + 1) + ". " + cities[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > cities.length) {
            try {
                System.out.println("Enter your choice (1-" + cities.length + "): ");
                choice = scanner.nextInt();
                if (choice < 1 || choice > cities.length) {
                    System.out.println("Invalid choice. Please select a valid city number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        String cityName = cities[choice - 1];
        String countryName = countries[choice - 1];

        System.out.println("Select data type:");
        System.out.println("1. Current weather");
        System.out.println("2. Weather forecast");
        System.out.println("3. Current time");
        System.out.println("4. Currency converter");
        System.out.println("5. Tourist attractions");
        System.out.println("6. Population");
        System.out.println("7. Top restaurants");
        System.out.println("8. Events");

        int dataTypeChoice = -1;
        while (dataTypeChoice < 1 || dataTypeChoice > 8) {
            try {
                System.out.println("Enter your choice (1-8): ");
                dataTypeChoice = scanner.nextInt();
                if (dataTypeChoice < 1 || dataTypeChoice > 8) {
                    System.out.println("Invalid choice. Please select a valid data type number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        scanner.close();

        try {
            switch (dataTypeChoice) {
                case 1:
                    String currentWeatherUrl = buildWeatherUrl(cityName, countryName, processor.getApiKey(), false);
                    String currentWeatherData = downloader.downloadData(currentWeatherUrl);
                    processor.processAndSaveData(currentWeatherData);
                    break;
                case 2:
                    String forecastWeatherUrl = buildWeatherUrl(cityName, countryName, processor.getApiKey(), true);
                    String forecastWeatherData = downloader.downloadData(forecastWeatherUrl);
                    processor.processAndSaveForecastData(forecastWeatherData);
                    break;
                case 3:
                    displayCurrentTime(cityName);
                    processor.saveCurrentTimeToFile(cityName);
                    break;
                case 4:
                    converter.convertForCity(cityName);
                    break;
                case 5:
                    displayTouristAttractions(cityName);
                    break;
                case 6:
                    fetcher.printPopulation(cityName);
                    break;
                case 7:
                    apiFetcher.printTopRestaurants(cityName);
                    break;
                case 8:
                    eventFetcher.searchEvents(cityName);
                    break;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while downloading data: " + e.getMessage());
        }
    }

    public void displayCurrentTime(String city) {
        ZoneId zoneId = ZoneId.of(getTimeZone(city));
        LocalDateTime currentTime = LocalDateTime.now(zoneId);
        System.out.println("Current time in " + city + ": " + currentTime);
    }

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

    public String buildWeatherUrl(String cityName, String countryName, String apiKey, boolean isForecast) {
        try {
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
            String encodedCountryName = URLEncoder.encode(countryName, "UTF-8");
            String endpoint = isForecast ? "forecast" : "weather";
            return "https://api.openweathermap.org/data/2.5/" + endpoint + "?q=" + encodedCityName + "," + encodedCountryName + "&appid=" + apiKey;
        } catch (Exception e) {
            System.out.println("Error building weather URL: " + e.getMessage());
            return null;
        }
    }

    public void displayTouristAttractions(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            String urlStr = foursquareBaseUrl + "?near=" + encodedCity + "&limit=10";
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", foursquareApiKey);

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
