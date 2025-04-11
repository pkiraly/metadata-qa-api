package de.gwdg.metadataqa.api.schema;

import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.schema.Rule;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SchemaUtilsTest {

  Schema schema;

  @Before
  public void setUp() throws Exception {
    String schemaFile = "src/test/resources/configuration/schema/ddb-lido.yaml";
    schema = ConfigurationReader.readSchemaYaml(schemaFile).asSchema();
  }

  @Test
  public void getRuleById() throws FileNotFoundException {
    Rule rule94 = SchemaUtils.getRuleById(schema, "Q-9.4");
    assertNotNull(rule94);
    assertEquals("Q-9.4", rule94.getId());
    assertEquals(
      "There must be at least one preferred designation for a thematic relation in the data record.",
      rule94.getDescription());
    assertEquals(null, rule94.getDependencies());
  }

  @Test
  public void getDependencies() throws FileNotFoundException {
    Rule rule94 = SchemaUtils.getRuleById(schema, "Q-9.4");
    List<String> dependencies = SchemaUtils.getDependencies(rule94);
    assertNotNull(dependencies);
    assertEquals(List.of("Q-9.4c"), dependencies);
  }

  @Test
  public void getAllDependencies() {
    Rule rule94 = SchemaUtils.getRuleById(schema, "Q-9.4");
    assertEquals(
      List.of("Q-9.4a", "Q-9.4b", "Q-9.4c"),
      SchemaUtils.getAllDependencies(schema, rule94)
        .stream()
        .sorted()
        .collect(Collectors.toList())
    );
  }
}