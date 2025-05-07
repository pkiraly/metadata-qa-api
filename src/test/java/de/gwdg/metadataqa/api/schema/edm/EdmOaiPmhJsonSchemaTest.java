package de.gwdg.metadataqa.api.schema.edm;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

  @Test
  public void getRootChildrenPaths() {
    Exception e = assertThrows(UnsupportedOperationException.class, () -> {
      assertEquals(3, schema.getRootChildrenPaths().size());
    });
    assertEquals("Not supported yet.", e.getMessage());
  }
}