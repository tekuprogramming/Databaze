import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
public class DataProcessor {
    public void processAndSaveData(String data) throws IOException {
        System.out.println("Gathered data: " + data);

        Gson gson = new Gson();
        JsonObject jsonData = gson.fromJson(data, JsonObject.class);

        if (jsonData != null) {
            JsonObject coordData = jsonData.getAsJsonObject("coord");
            JsonObject mainData = jsonData.getAsJsonObject("main");
            JsonObject windData = jsonData.getAsJsonObject("wind");
            JsonObject cloudsData = jsonData.getAsJsonObject("clouds");
            JsonObject sysData = jsonData.getAsJsonObject("sys");

            if (coordData != null && mainData != null && windData != null && cloudsData != null && sysData != null) {
                Double lon = coordData.get("lon").getAsDouble();
                Double lat = coordData.get("lat").getAsDouble();

                Double temperature = mainData.get("temp").getAsDouble();
                Double humidity = mainData.get("humidity").getAsDouble();
                Double windSpeed = windData.get("speed").getAsDouble();
                Integer clouds = cloudsData.get("all").getAsInt();
                String country = sysData.get("country").getAsString();
                Long sunrise = sysData.get("sunrise").getAsLong();
                Long sunset = sysData.get("sunset").getAsLong();

                System.out.println("City: " + jsonData.get("name").getAsString());
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

    public void saveDataToFile(String data) throws IOException {
        FileWriter writer = new FileWriter("processed_data.txt");
        writer.write(data);
        writer.close();
    }
}
