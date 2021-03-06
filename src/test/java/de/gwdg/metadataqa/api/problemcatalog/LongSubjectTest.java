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
public class LongSubjectTest {

  public LongSubjectTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void test() throws IOException, URISyntaxException {
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("problem-catalog/long-subject.json"));

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
