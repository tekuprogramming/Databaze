import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserInteractionTest {

    @Test
    public void testBuildWeatherUrl_CurrentWeather() {
        UserInteraction interaction = new UserInteraction();
        String cityName = "Prague";
        String countryName = "CZ";
        String apiKey = "3eaab97ed540e25e7b261d686d5dfc42";
        boolean isForecast = false;

        String expectedUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "," + countryName + "&appid=" + apiKey;
        String actualUrl = interaction.buildWeatherUrl(cityName, countryName, apiKey, isForecast);

        assertEquals("URL should match the expected current weather URL", expectedUrl, actualUrl);
    }

    @Test
    public void testBuildWeatherUrl_Forecast() {
        UserInteraction interaction = new UserInteraction();
        String cityName = "Prague";
        String countryName = "CZ";
        String apiKey = "3eaab97ed540e25e7b261d686d5dfc42";
        boolean isForecast = true;

        String expectedUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "," + countryName + "&appid=" + apiKey;
        String actualUrl = interaction.buildWeatherUrl(cityName, countryName, apiKey, isForecast);

        assertEquals("URL should match the expected forecast URL", expectedUrl, actualUrl);
    }

    @Test
    public void testBuildWeatherUrl_EncodedCharacters() {
        UserInteraction interaction = new UserInteraction();
        String cityName = "New York";
        String countryName = "CZ";
        String apiKey = "3eaab97ed540e25e7b261d686d5dfc42";
        boolean isForecast = false;

        String expectedUrl = "https://api.openweathermap.org/data/2.5/weather?q=New+York" + "," + countryName + "&appid=" + apiKey;
        String actualUrl = interaction.buildWeatherUrl(cityName, countryName, apiKey, isForecast);

        assertEquals("URL should match the expected encoded URL", expectedUrl, actualUrl);
    }

    @Test
    public void testStart() {
        String userInput = "2\n3\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        UserInteraction interaction = new UserInteraction();
        interaction.start();

        String output = outputStream.toString();
        assertTrue(output.contains("Select a city to get weather data:"));
        assertTrue(output.contains("Select data type:"));
        assertTrue(output.contains("Current time in Prague"));

        System.setIn(System.in);
        System.setOut(System.out);
    }
}
