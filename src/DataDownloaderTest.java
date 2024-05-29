import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DataDownloaderTest {

    @Test
    public void testDownloadData() {
        String testUrl = "https://openexchangerates.org/api/latest.json?app_id=3746f3c9ba0247ac8c1e13bed7589f7a";

        DataDownloader downloader = new DataDownloader();

        try {
            String result = downloader.downloadData(testUrl);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }
}
