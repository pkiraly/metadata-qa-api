package de.gwdg.metadataqa.api.configuration;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
}
