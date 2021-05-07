package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.schema.Schema;
import junit.framework.TestCase;

public class EdmOaiPmhJsonSchemaTest extends TestCase {

  public void testGetExtractableFields() {
    Schema schema = new EdmOaiPmhJsonSchema();
    assertEquals(3, schema.getExtractableFields().size());
  }
}