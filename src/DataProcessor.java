import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        try {
            Pattern pattern = Pattern.compile("\"name\":\"(.*?)\".*?\"country\":\"(.*?)\".*?\"temp\":(.*?),\"humidity\":(.*?),\"speed\":(.*?),\"all\":(.*?),\"sunrise\":(.*?),\"sunset\":(.*?)\"lat\":(.*?),\"lon\":(.*?)\\}");

            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                String cityName = matcher.group(1);
                Double temperature = Double.parseDouble(matcher.group(3));
                Integer humidity = Integer.parseInt(matcher.group(4));
                Double windSpeed = Double.parseDouble(matcher.group(5));
                Integer clouds = Integer.parseInt(matcher.group(6));
                String country = matcher.group(2);
                Long sunrise = Long.parseLong(matcher.group(7));
                Long sunset = Long.parseLong(matcher.group(8));
                Double latitude = Double.parseDouble(matcher.group(9));
                Double longitude = Double.parseDouble(matcher.group(10));

                System.out.println("City: " + cityName);
                System.out.println("Country: " + country);
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Wind speed: " + windSpeed + " m/s");
                System.out.println("Cloudiness: " + clouds + "%");
                System.out.println("Sunrise: " + sunrise);
                System.out.println("Sunset: " + sunset);

                saveDataToFile(data);
            } else {
                    System.err.println("Couldn't find the needed data.");
                }
            } catch (Exception e){
                System.err.println("Error in processing data: " + e.getMessage());
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

    public void saveDataToFile(String data) throws IOException {
        FileWriter writer = new FileWriter("processed_data.txt");
        writer.write(data);
        writer.close();
    }
}
