import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
