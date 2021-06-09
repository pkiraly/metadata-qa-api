package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.schema.edm.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfCalculatorTest {

  @Test
  public void testGetHeaders() {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    List<String> expected = Arrays.asList(
      "Proxy/dc:title:sum", "Proxy/dc:title:avg",
      "Proxy/dcterms:alternative:sum", "Proxy/dcterms:alternative:avg",
      "Proxy/dc:description:sum", "Proxy/dc:description:avg"
    );
    assertEquals(6, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());

    calculator = new TfIdfCalculator(new EdmFullBeanSchema());
    assertEquals(6, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());
  }

  @Test
  public void getCalculatorName() throws Exception {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    assertEquals("uniqueness", calculator.getCalculatorName());
  }
}
