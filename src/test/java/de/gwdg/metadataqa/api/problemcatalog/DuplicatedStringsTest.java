package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class DuplicatedStringsTest {

  private String problemFileName = "problem-catalog/duplicated-string.json";
  private DuplicatedStrings detector;
  private JsonSelector cache;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    detector = new DuplicatedStrings(catalog);

    cache = new JsonSelector(FileUtils.readFirstLineFromResource(problemFileName));
  }

  // <edm:provider>Europeana Food and DrinkEuropeana Food and Drink</edm:provider>
  // <dc:date>1890 ; 1890</dc:date>
  @Test
  public void test() throws IOException, URISyntaxException {
    try {
      FieldCounter<Double> results = new FieldCounter<>();
      detector.update(cache, results);
      assertEquals((Double) 8.0, results.get(detector.getHeader()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
