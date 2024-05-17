import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        System.out.println("Gathered data: " + data);

        Map<String,Object> jsonData = parseJson(data);

        if (jsonData != null) {
            Map<String, Object> coordData = (Map<String, Object>) jsonData.get("coord");
            Map<String, Object> mainData = (Map<String, Object>) jsonData.get("main");
            Map<String, Object> windData = (Map<String, Object>) jsonData.get("wind");
            Map<String, Object> cloudsData = (Map<String, Object>) jsonData.get("clouds");
            Map<String, Object> sysData = (Map<String, Object>) jsonData.get("sys");

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

                jsonData.put(key, value);
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
