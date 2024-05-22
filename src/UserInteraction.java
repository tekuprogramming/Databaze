import java.io.IOException;
import java.net.URLEncoder;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInteraction {
    private DataProcessor processor = new DataProcessor();
    private DataDownloader downloader = new DataDownloader();
    private TrafficDataProcessor trafficProcessor = new TrafficDataProcessor();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String[] cities = {"London", "Prague", "Tokyo", "New York", "Rio de Janeiro"};
        String[] countries = {"uk", "cz", "jp", "us", "br"};

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
        System.out.println("3. Traffic information");

        int dataTypeChoice = -1;
        while (dataTypeChoice < 1 || dataTypeChoice > 3) {
            try {
                System.out.println("Enter your choice (1-3): ");
                dataTypeChoice = scanner.nextInt();
                if (dataTypeChoice < 1 || dataTypeChoice > 3) {
                    System.out.println("Invalid choice. Please select a valid data type number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        scanner.close();

        try {
            if (dataTypeChoice == 1) {
                String currentWeatherUrl = buildWeatherUrl(cityName, countryName, processor.getApiKey(), false);
                String currentWeatherData = downloader.downloadData(currentWeatherUrl);
                processor.processAndSaveData(currentWeatherData);
            } else if (dataTypeChoice == 2) {
                String forecastWeatherUrl = buildWeatherUrl(cityName, countryName, processor.getApiKey(), true);
                String forecastWeatherData = downloader.downloadData(forecastWeatherUrl);
                processor.processAndSaveForecastData(forecastWeatherData);
            } else if (dataTypeChoice == 3) {
                trafficProcessor.getTrafficInfo(cityName);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while downloading data: " + e.getMessage());
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
}
