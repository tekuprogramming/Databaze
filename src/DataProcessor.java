import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        System.out.println("Gathered data: " + data);

        Map<String, Object> jsonData = parseJson(data);

        if (jsonData != null) {
            Map<String, Object> coordData = getMapData(jsonData, "coord");
            Map<String, Object> mainData = getMapData(jsonData, "main");
            Map<String, Object> windData = getMapData(jsonData, "wind");
            Map<String, Object> cloudsData = getMapData(jsonData, "clouds");
            Map<String, Object> sysData = getMapData(jsonData, "sys");

            if (coordData != null && mainData != null && windData != null && cloudsData != null && sysData != null) {
                Double lon = getDoubleValue(coordData, "lon");
                Double lat = getDoubleValue(coordData, "lat");

                Object weatherObject = jsonData.get("weather");
                if (weatherObject instanceof Map) {
                    Map<String, Object> weatherData = (Map<String, Object>) weatherObject;
                    String weatherMain = getStringValue(weatherData, "main");
                    String weatherDescription = getStringValue(weatherData, "description");

                    Double temperature = getDoubleValue(mainData, "temp");
                    Double humidity = getDoubleValue(mainData, "humidity");
                    Double windSpeed = getDoubleValue(windData, "speed");
                    Integer clouds = getIntegerValue(cloudsData, "all");
                    String country = getStringValue(sysData, "country");
                    Long sunrise = getLongValue(sysData, "sunrise");
                    Long sunset = getLongValue(sysData, "sunset");

                    System.out.println("City: " + jsonData.get("name"));
                    System.out.println("Country: " + country);
                    System.out.println("Latitude: " + lat);
                    System.out.println("Longitude: " + lon);
                    System.out.println("Temperature: " + temperature + " °C");
                    System.out.println("Humidity: " + humidity + "%");
                    System.out.println("Wind speed: " + windSpeed + " m/s");
                    System.out.println("Cloudiness: " + clouds + "%");
                    System.out.println("Weather: " + weatherMain + " - " + weatherDescription);
                    System.out.println("Sunrise: " + sunrise);
                    System.out.println("Sunset: " + sunrise);

                    saveDataToFile(data);
                } else {
                    System.out.println("Error: Weather data wasn't extracted correctly.");
                }
            } else {
                System.err.println("Error: Data wasn't extracted correctly.");
            }
        } else {
            System.err.println("Error: JSON data was not processed correctly.");
        }
    }

    public Map<String, Object> parseJson(String json) {
        Map<String, Object> jsonData = new HashMap<>();

        json = json.replaceAll("[{}\"]", "");

        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            jsonData.put(key, value);
        }

        return jsonData;
    }

    public Map<String, Object> getMapData(Map<String, Object> jsonData, String key) {
        Object object = jsonData.get(key);
        if (object instanceof Map) {
            return (Map<String, Object>) object;
        } else {
            System.out.println("Error: Key '" + key + "' isn't the expected data type.");
            return null;
        }
    }

    public Double getDoubleValue(Map<String, Object> data, String key) {
        Object object = data.get(key);
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        } else {
            System.out.println("Error: Key '" + key + "' isn't a number.");
            return null;
        }
    }

    public Integer getIntegerValue(Map<String, Object> data, String key) {
        Object object = data.get(key);
        if (object instanceof Number) {
            return ((Number) object).intValue();
        } else {
            System.out.println("Error: Key '" + key + "' isn't a number.");
            return null;
        }
    }

    public Long getLongValue(Map<String, Object> data, String key) {
        Object object = data.get(key);
        if (object instanceof Number) {
            return ((Number) object).longValue();
        } else {
            System.out.println("Error: Key '" + key + "' isn't a number.");
            return null;
        }
    }

    public String getStringValue(Map<String, Object> data, String key) {
        Object object = data.get(key);
        if (object instanceof String) {
            return (String) object;
        } else {
            System.out.println("Error: Key '" + key + "' isn't a String.");
            return null;
        }
    }

        public void saveDataToFile (String data) throws IOException {
            FileWriter writer = new FileWriter("processed_data.txt");
            writer.write(data);
            writer.close();
        }

}
