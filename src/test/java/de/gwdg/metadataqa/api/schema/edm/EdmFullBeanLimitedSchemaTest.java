package de.gwdg.metadataqa.api.schema.edm;

import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EdmFullBeanLimitedSchemaTest {

  Schema schema = new EdmFullBeanLimitedSchema();

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
    Exception e = assertThrows(UnsupportedOperationException.class, () -> {
      assertEquals(3, schema.getRootChildrenPaths().size());
    });
    assertEquals("Not supported yet.", e.getMessage());
  }
}
