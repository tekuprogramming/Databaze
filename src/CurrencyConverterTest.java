import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CurrencyConverterTest {

    @Test
    public void testGetConversionRate() {
        String targetCurrency = "USD";

        CurrencyConverter converter = new CurrencyConverter();

        double expectedRate = 1.0;

        try {
            double rate = converter.getConversionRate(targetCurrency);
            assertEquals(expectedRate, rate, 0.001);
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }
}
