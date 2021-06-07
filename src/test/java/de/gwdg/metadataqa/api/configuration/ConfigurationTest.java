package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ConfigurationTest {

  @Test
  public void testReading_fromResource() {
    Yaml yaml = new Yaml(new Constructor(Configuration.class));
    InputStream inputStream = this.getClass()
      .getClassLoader()
      .getResourceAsStream("configuration/configuration.yaml");
    Configuration config = (Configuration) yaml.load(inputStream);
    testConfiguration(config);
  }

  @Test
  public void testReading_fromFile() throws FileNotFoundException {
    Yaml yaml = new Yaml(new Constructor(Configuration.class));
    InputStream inputStream = new FileInputStream(new File("src/test/resources/configuration/configuration.yaml"));
    Configuration config = (Configuration) yaml.load(inputStream);
    testConfiguration(config);
  }

  @Test
  public void testReading_fromConfigurationReader_yaml() throws FileNotFoundException {
    Configuration config = ConfigurationReader.readYaml("src/test/resources/configuration/configuration.yaml");
    testConfiguration(config);
  }

  @Test
  public void testReading_fromConfigurationReader_json() throws FileNotFoundException {
    Configuration config = ConfigurationReader.readJson("src/test/resources/configuration/configuration.json");
    testConfiguration(config);
  }

  @Test
  public void test_asSchema() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readYaml(
        "src/test/resources/configuration/configuration.yaml"
      )
      .asSchema();

    assertEquals(Format.JSON, schema.getFormat());
    assertEquals(3, schema.getPaths().size());
  }

  @Test
  public void test_asSchema_withMeemoo() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readYaml(
        "src/test/resources/configuration/meemoo.yaml"
      )
      .asSchema();

    assertEquals(Format.CSV, schema.getFormat());
    assertEquals(17, schema.getPaths().size());
    assertEquals("url", schema.getPaths().get(0).getLabel());
    assertEquals(1, schema.getPaths().get(0).getCategories().size());
    assertEquals("MANDATORY", schema.getPaths().get(0).getCategories().get(0));
    assertTrue(schema.getPaths().get(0).isExtractable());

    assertEquals(0, schema.getPaths().get(1).getCategories().size());
  }

  public void testConfiguration(Configuration config) {
    assertNotNull(config);
    assertNotNull(config.getFormat());
    assertNotNull(config.getFields());
    assertNotNull(config.getGroups());

    assertEquals("json", config.getFormat());

    assertEquals(3, config.getFields().size());
    Field first = config.getFields().get(0);
    assertEquals("edm:ProvidedCHO/@about", first.getName());
    assertEquals("$.['providedCHOs'][0]['about']", first.getPath());
    assertEquals(1, first.getCategories().size());
    assertEquals("MANDATORY", first.getCategories().get(0));

    assertEquals(1, config.getGroups().size());
    Group group = config.getGroups().get(0);
    assertEquals(2, group.getFields().size());
    assertEquals("Proxy/dc:title", group.getFields().get(0));
    assertEquals("Proxy/dc:description", group.getFields().get(1));
    assertEquals(1, group.getCategories().size());
    assertEquals("MANDATORY", group.getCategories().get(0));
  }

  @Test
  public void testReading_rules() {
    Yaml yaml = new Yaml(new Constructor(Rulex.class));
    InputStream inputStream = this.getClass()
      .getClassLoader()
      .getResourceAsStream("configuration/rules.yaml");
    Rulex rule = (Rulex) yaml.load(inputStream);
    assertNotNull(rule.getAnd());
    assertEquals(2, rule.getAnd().size());
    assertNotNull(rule.getAnd().get(0).getEquals());
    assertEquals("for example", rule.getAnd().get(0).getEquals());
    assertNotNull(rule.getAnd().get(1).getIn());
    assertEquals(2, rule.getAnd().get(1).getIn().size());
    assertEquals("aa", rule.getAnd().get(1).getIn().get(0));
    assertEquals("bb", rule.getAnd().get(1).getIn().get(1));
  }

  @Test
  public void testReading_xml_as_config() throws FileNotFoundException {
    Configuration configuration = ConfigurationReader
      .readYaml("src/test/resources/configuration/xmlsample.yaml");

    assertEquals(2, configuration.getNamespaces().size());
    assertEquals("http://www.lyncode.com/xoai", configuration.getNamespaces().get("xoai"));
    assertEquals("http://xmlns.com/foaf/0.1/", configuration.getNamespaces().get("foaf"));
  }

  @Test
  public void testReading_xml_as_schema() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readYaml("src/test/resources/configuration/xmlsample.yaml")
      .asSchema();

    assertEquals(2, schema.getNamespaces().size());
    assertEquals("http://www.lyncode.com/xoai", schema.getNamespaces().get("xoai"));
    assertEquals("http://xmlns.com/foaf/0.1/", schema.getNamespaces().get("foaf"));
  }

}
