package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EdmOaiPmhJsonSchemaTest  {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  Schema schema = new EdmOaiPmhJsonSchema();

  @Test
  public void testGetExtractableFields() {
    assertEquals(3, schema.getExtractableFields().size());
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
    fail("Exception did not thrown.");
  }
}