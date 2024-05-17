import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        System.out.println("Gathered data: " + data);

        Map<String,Object> jsonData = parseJson(data);

        if (jsonData != null) {
            Map<String, Object> coordData = extractMap(jsonData, "coord");
            Map<String, Object> mainData = extractMap(jsonData, "main");
            Map<String, Object> windData = extractMap(jsonData, "wind");
            Map<String, Object> cloudsData = extractMap(jsonData, "clouds");
            Map<String, Object> sysData = extractMap(jsonData, "sys");

            if (coordData != null && mainData != null && windData != null && cloudsData != null && sysData != null) {
                Double lon = Double.parseDouble(coordData.get("lon").toString());
                Double lat = Double.parseDouble(coordData.get("lat").toString());

                Double temperature = Double.parseDouble(mainData.get("temp").toString());
                Double humidity = Double.parseDouble(mainData.get("humidity").toString());
                Double windSpeed = Double.parseDouble(windData.get("speed").toString());
                Integer clouds = Integer.parseInt(cloudsData.get("all").toString());
                String country = sysData.get("country").toString();
                Long sunrise = Long.parseLong(sysData.get("sunrise").toString());
                Long sunset = Long.parseLong(sysData.get("sunset").toString());

                System.out.println("City: " + jsonData.get("name"));
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

                if (value.startsWith("{")) {
                    int nestedJsonStartIndex = json.indexOf(key + ":") + key.length() + 1;
                    int nestedJsonEndIndex = findClosingBraceIndex(json, nestedJsonStartIndex);
                    value = json.substring(nestedJsonStartIndex, nestedJsonEndIndex + 1);

                    Map<String, Object> nestedJsonData = parseJson(value);
                    jsonData.put(key, nestedJsonData);
                } else {
                    jsonData.put(key, value);
                }
            }
            }

        return jsonData;
    }

    public int findClosingBraceIndex(String json, int startIndex) {
        int braceCount = 0;
        for (int i = startIndex; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '{') {
                braceCount++;
            } else if (ch == '}') {
                braceCount--;
                if (braceCount == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Map<String, Object> extractMap(Map<String, Object> jsonData, String key) {
        Object value = jsonData.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }

    public void saveDataToFile(String data) throws IOException {
        FileWriter writer = new FileWriter("processed_data.txt");
        writer.write(data);
        writer.close();
    }
}
