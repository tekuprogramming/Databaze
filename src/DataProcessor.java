import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        System.out.println("Gathered data: " + data);

        Map<String,Object> jsonData = parseJson(data);

        if (jsonData != null) {
            Map<String, Object> coordData = getNestedMap(jsonData, "coord");
            Map<String, Object> mainData = getNestedMap(jsonData, "main");
            Map<String, Object> windData = getNestedMap(jsonData, "wind");
            Map<String, Object> cloudsData = getNestedMap(jsonData, "clouds");
            Map<String, Object> sysData = getNestedMap(jsonData, "sys");

            if (coordData != null && mainData != null && windData != null && cloudsData != null && sysData != null) {
                Double lon = parseDouble(coordData.get("lon").toString());
                Double lat = parseDouble(coordData.get("lat").toString());

                Double temperature = parseDouble(mainData.get("temp").toString());
                Double humidity = parseDouble(mainData.get("humidity").toString());
                Double windSpeed = parseDouble(windData.get("speed").toString());
                Integer clouds = parseInt(cloudsData.get("all").toString());
                String country = parseString((sysData.get("country")));
                Long sunrise = parseLong(sysData.get("sunrise").toString());
                Long sunset = parseLong(sysData.get("sunset").toString());

                System.out.println("City: " + parseString(jsonData.get("name")));
                System.out.println("Country: " + country);
                System.out.println("Latitude: " + lat);
                System.out.println("Longitude: " + lon);
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Wind speed: " + windSpeed + " m/s");
                System.out.println("Cloudiness: " + clouds + "%");
                System.out.println("Sunrise: " + sunrise);
                System.out.println("Sunset: " + sunrise);

                saveDataToFile(data);
            } else {
                System.out.println("Error: Data wasn't extracted correctly.");
            }
        } else {
            System.out.println("Error: JSON data was not processed correctly.");
        }
    }

    public Map<String, Object> parseJson(String json) {
        Map<String, Object> jsonData = new HashMap<>();

        json = json.replaceAll("[{}\"]", "").trim();

        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                jsonData.put(key, value);
            }
            }

        return jsonData;
    }

    public Map<String, Object> getNestedMap(Map<String, Object> jsonData, String key) {
        Map<String, Object> nestedMap = new HashMap<>();
        String nestedJson = (String) jsonData.get(key);

        if (nestedJson != null) {
            nestedJson = nestedJson.replaceAll("[{}\"]", "").trim();
            String[] pairs = nestedJson.split(",");

            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String nestedKey = keyValue[0].trim();
                    String nestedValue = keyValue[1].trim();
                    nestedMap.put(nestedKey, nestedValue);
                }
            }
        }

        return nestedMap;
    }

    public Double parseDouble(Object value) {
        if (value != null) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                System.out.println("Error while parsing to Double: " + value);
            }
        }
        return null;
    }

    public Integer parseInt(Object value) {
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                System.out.println("Error while parsing to Integer: " + value);
            }
        }
        return null;
    }

    public Long parseLong(Object value) {
        if (value != null) {
            try {
                return Long.parseLong(value.toString());
            } catch (NumberFormatException e) {
                System.out.println("Error while parsing to Long: " + value);
            }
        }
        return null;
    }

    public String parseString(Object value) {
        return value != null ? value.toString() : null;
    }

    public void saveDataToFile(String data) throws IOException {
        FileWriter writer = new FileWriter("processed_data.txt");
        writer.write(data);
        writer.close();
    }
}
