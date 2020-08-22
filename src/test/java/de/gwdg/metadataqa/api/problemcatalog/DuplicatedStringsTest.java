package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.EdmSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class DuplicatedStringsTest {

  private String problemFileName = "problem-catalog/duplicated-string.json";
  private DuplicatedStrings detector;
  private JsonPathCache cache;

  public DuplicatedStringsTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws URISyntaxException, IOException {
    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    detector = new DuplicatedStrings(catalog);

    cache = new JsonPathCache(FileUtils.readFirstLine(problemFileName));
  }

  @After
  public void tearDown() {
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
