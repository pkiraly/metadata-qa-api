package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EdmOaiPmhJsonSchemaTest  {

  Schema schema = new EdmOaiPmhJsonSchema();

  @Test
  public void testGetExtractableFields() {
    assertEquals(3, schema.getExtractableFields().size());
  }

  @Test
  public void getIndexFields() {
    assertEquals(3, schema.getIndexFields().size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void getRootChildrenPaths() {
    assertEquals(3, schema.getRootChildrenPaths().size());
  }
}