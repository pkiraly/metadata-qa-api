package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.schema.edm.EdmFullBeanSchema;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryTest {

  Schema schemaWithActiveField = new BaseSchema().addField(new JsonBranch("contentWarning").setCategories("CONTEXT"));
  Schema schemaWithInactiveField = new BaseSchema().addField(new JsonBranch("contentWarning").setCategories("CONTEXT").setActive(false));

  @Test
  public void extractCategories_withActive() {
    assertEquals(List.of("CONTEXT"), schemaWithActiveField.getCategories());
  }

  @Test
  public void extractCategories_withInactive() {
    assertEquals(List.of(), schemaWithInactiveField.getCategories());
  }

  @Test
  public void ExtractCategories_withTrue() {
    Schema schema = new EdmFullBeanSchema();
    assertEquals(List.of(
      "MANDATORY", "DESCRIPTIVENESS", "SEARCHABILITY", "CONTEXTUALIZATION", "IDENTIFICATION", "BROWSING", "VIEWING", "REUSABILITY", "MULTILINGUALITY"),
      Category.extractCategories(schema.getPaths(), true));
  }

  @Test
  public void ExtractCategories_withFalse() {
    Schema schema = new EdmFullBeanSchema();
    assertEquals(List.of(
      "MANDATORY", "DESCRIPTIVENESS", "SEARCHABILITY", "IDENTIFICATION", "MULTILINGUALITY", "CONTEXTUALIZATION",  "BROWSING", "REUSABILITY", "VIEWING"),
      Category.extractCategories(schema.getPaths(), false));
  }
}