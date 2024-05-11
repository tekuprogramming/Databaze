import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=3eaab97ed540e25e7b261d686d5dfc42";
            DataDownloader downloader = new DataDownloader();
            String data = downloader.downloadData(apiUrl);

            System.out.println("Data was successfully downloaded.");
        } catch (IOException e) {
            System.out.println("An error occurred while downloading data: " + e.getMessage());
        }
    }
}