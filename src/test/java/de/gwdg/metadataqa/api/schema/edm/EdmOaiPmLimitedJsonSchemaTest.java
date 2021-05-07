package de.gwdg.metadataqa.api.schema.edm;

import junit.framework.TestCase;

public class EdmOaiPmLimitedJsonSchemaTest extends TestCase {

  public void testGetPaths() {
    EdmSchema schema = new EdmOaiPmLimitedJsonSchema();
    assertEquals(35, schema.getPaths().size());
  }
}