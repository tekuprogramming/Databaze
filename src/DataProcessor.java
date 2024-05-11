import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        Map<String,String> jsonData = parseJson(data);

        if (jsonData != null) {
            String cityName = jsonData.get("name");
            String weatherData = jsonData.get("weather");

            if (weatherData != null) {
                Map<String, String> weatherMap = parseJson(weatherData);
                String temperature = weatherMap.get("temperature");
                String humidity = weatherMap.get("humidity");

                System.out.println("City: " + cityName);
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Humidity: " + humidity + " %");

                saveDataToFile(data);
            } else {
                System.out.println("Error: 'weather' was not found in JSON object.");
            }
        } else {
            System.out.println("Error: JSON data was not processed correctly.");
        }
    }

    public Map<String, String> parseJson(String json) {
        Map<String, String> jsonData = new HashMap<>();

        json = json.substring(1, json.length() - 1);

        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim().replaceAll("\"", "");

                jsonData.put(key, value);
            } else {
                System.out.println("Error: Incorrect format of the pair: " + pair);
            }
        }

        return jsonData;
    }

    public void saveDataToFile(String data) throws IOException {
        FileWriter writer = new FileWriter("processed_data.txt");
        writer.write(data);
        writer.close();
    }
}
