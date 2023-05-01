package de.gwdg.metadataqa.api.problemcatalog.skos;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.problemcatalog.ProblemCatalog;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;
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
public class AmbiguousPrefLabelTest {

  private String problemFileName = "problem-catalog/same-title-and-description.json";
  private AmbiguousPrefLabel detector;
  private JsonSelector cache;

  public AmbiguousPrefLabelTest() {
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
    detector = new AmbiguousPrefLabel(catalog);

    cache = new JsonSelector(FileUtils.readFirstLineFromResource(problemFileName));
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testAmbiguousPrefLabels() {
    FieldCounter<Double> results = new FieldCounter<>();

    detector.update(cache, results);
    assertEquals((Double) 21.0, results.get(detector.getHeader()));
  }
}
