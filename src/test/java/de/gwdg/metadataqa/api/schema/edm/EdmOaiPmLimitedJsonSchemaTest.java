package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.schema.Format;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

  @Test
  public void getRootChildrenPaths() {
    UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, () ->
      schema.getRootChildrenPaths().size());
    assertEquals("Not supported yet.", e.getMessage());
  }

  @Test
  public void getPathByLabel() {
    UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, () -> schema.getPathByLabel("label"));
    assertEquals("Not supported yet.", e.getMessage());
  }

  @Test
  public void getFormat() {
    assertEquals(Format.JSON, schema.getFormat());
  }
}