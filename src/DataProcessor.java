import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        Map<String, Object> jsonData = parseJson(data);

        if (jsonData != null) {
            String cityName = (String) jsonData.get("name");
            Map<String, Object> weatherData = (Map<String, Object>) jsonData.get("weather");

            if (weatherData != null) {
                Double temperature = (Double) weatherData.get("temp");
                Double humidity = (Double) weatherData.get("humidity");

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

    public Map<String, Object> parseJson(String json) {
        Map<String, Object> jsonData = new HashMap<>();

        json = json.substring(1, json.length() - 1);

        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].trim().replaceAll("\"", "");
            String value = keyValue[1].trim().replaceAll("\"", "");

            jsonData.put(key, value);
        }

        return jsonData;
    }

    public void saveDataToFile(String data) throws IOException {
        FileWriter writer = new FileWriter("processed_data.txt");
        writer.write(data);
        writer.close();
    }
}
