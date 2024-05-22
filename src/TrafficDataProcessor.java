import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TrafficDataProcessor {
    private final String apiKey = "idrmN9zom1mCeCFQUnNjYhHRLTq3mHHugbq1-tWZvyA";

    public void getTrafficInfo(String city) {
        try {
            String trafficUrl = "https://traffic.ls.hereapi.com/traffic/6.2/flow.json" +
                    "?apiKey=" + apiKey +
                    "&bbox=" + getBoundingBox(city);

            URL url = new URL(trafficUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner scanner = new Scanner(System.in);
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.hasNextLine());
            }
            scanner.close();

            System.out.println("Traffic information for " + city + ":");
            System.out.println(response.toString());

        } catch (IOException e) {
            System.out.println("Error getting traffic information: " + e.getMessage());
        }
    }

    public String getBoundingBox(String city) {
        switch (city) {
            case "London":
                return "51.2867602,-0.5103751,51.6918741,0.3340155";
            case "Prague":
                return "50.0848,14.4112,50.1081,14.4858";
            case "Tokyo":
                return "35.652832,139.839478,35.6895,139.6917";
            case "New York":
                return "40.4774,-74.2591,40.9176,-73.7004";
            case "Rio de Janeiro":
                return "-22.9068,-43.1729,-22.9133,-43.1964";
            default:
                return "50.0848,14.4112,50.1081,14.4858";
        }
    }
}
