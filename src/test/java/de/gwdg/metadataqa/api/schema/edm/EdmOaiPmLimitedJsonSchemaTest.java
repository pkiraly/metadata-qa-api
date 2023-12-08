package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.schema.Format;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class EdmOaiPmLimitedJsonSchemaTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

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
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("Not supported yet.");
    assertEquals(3, schema.getRootChildrenPaths().size());
  }

  @Test
  public void getPathByLabel() {
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("Not supported yet.");
    assertEquals(3, schema.getPathByLabel("label"));
  }

  @Test
  public void getFormat() {
    assertEquals(Format.JSON, schema.getFormat());
  }
}