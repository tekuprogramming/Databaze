import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;

public class DataProcessorTest {

    @Test
    public void testSaveCurrentTimeToFile() {
        String timeZone = "Europe/Prague";
        String fileName = "src//current_time.txt";

        DataProcessor processor = new DataProcessor();

        processor.saveCurrentTimeToFile(timeZone);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            assertNotNull("File should contain content", line);
            assertTrue("File content should contain city name", line.contains(timeZone));

            ZoneId zoneId = ZoneId.of(processor.getTimeZone(timeZone));
            LocalDateTime currentTime = LocalDateTime.now(zoneId);
            assertTrue("File content should contain current time", line.contains(currentTime.toLocalDate().toString()));
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }
}
