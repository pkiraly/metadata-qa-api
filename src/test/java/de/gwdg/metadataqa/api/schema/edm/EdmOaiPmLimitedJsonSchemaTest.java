package de.gwdg.metadataqa.api.schema.edm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EdmOaiPmLimitedJsonSchemaTest {

  EdmSchema schema = new EdmOaiPmLimitedJsonSchema();

  @Test
  public void getPaths() {
    assertEquals(35, schema.getPaths().size());
  }

  @Test
  public void getIndexFields() {
    assertEquals(3, schema.getIndexFields().size());
  }

}