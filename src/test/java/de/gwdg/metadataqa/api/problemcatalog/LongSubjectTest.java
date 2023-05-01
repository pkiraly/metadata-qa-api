package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LongSubjectTest {

  @Test
  public void test() throws IOException, URISyntaxException {
    JsonSelector cache = new JsonSelector(FileUtils.readFirstLineFromResource("problem-catalog/long-subject.json"));

    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    ProblemDetector detector = null;
    try {
      detector = new LongSubject(catalog);
      FieldCounter<Double> results = new FieldCounter<>();

      detector.update(cache, results);
      assertEquals((Double) 1.0, results.get("LongSubject"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetHeaders() {
    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    LongSubject detector = new LongSubject(catalog);

    assertNotNull(detector.getHeader());
    assertEquals("LongSubject", detector.getHeader());
  }

}
