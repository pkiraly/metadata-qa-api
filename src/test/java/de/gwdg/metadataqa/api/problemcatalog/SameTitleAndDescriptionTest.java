package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.util.FileUtils;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class SameTitleAndDescriptionTest {

  @Test
  public void hello() throws IOException, URISyntaxException {
    String jsonString = FileUtils.readFirstLineFromResource("problem-catalog/same-title-and-description.json");
    JsonSelector cache = new JsonSelector(jsonString);

    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    ProblemDetector detector = new TitleAndDescriptionAreSame(catalog);
    FieldCounter<Double> results = new FieldCounter<>();

    detector.update(cache, results);
    assertEquals((Double)1.0, (Double)results.get("TitleAndDescriptionAreSame"));
  }

  @Test
  public void testGetHeaders() {
    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    TitleAndDescriptionAreSame detector = new TitleAndDescriptionAreSame(catalog);

    assertNotNull(detector.getHeader());
    assertEquals("TitleAndDescriptionAreSame", detector.getHeader());
  }

}
