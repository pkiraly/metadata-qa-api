package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmSchema;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EmptyStringsTest {

  @Test
  public void testGetHeaders() {
    EdmSchema schema = new EdmOaiPmhJsonSchema();
    ProblemCatalog catalog = new ProblemCatalog(schema);
    EmptyStrings detector = new EmptyStrings(catalog);

    assertNotNull(detector.getHeader());
    assertEquals("EmptyStrings", detector.getHeader());
  }
}
