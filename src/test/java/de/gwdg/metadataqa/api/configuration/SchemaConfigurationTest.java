package de.gwdg.metadataqa.api.configuration;

import de.gwdg.metadataqa.api.configuration.schema.ApplicationScope;
import de.gwdg.metadataqa.api.configuration.schema.Field;
import de.gwdg.metadataqa.api.configuration.schema.Group;
import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.rule.logical.AndChecker;
import de.gwdg.metadataqa.api.rule.logical.NotChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxLengthChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class SchemaConfigurationTest {
  private static final String RULES_DIR = "src/test/resources/configuration/schema/rules/";

  @Test
  public void testReading_fromResource() {
    Yaml yaml = new Yaml(new Constructor(SchemaConfiguration.class, new LoaderOptions()));
    InputStream inputStream = this.getClass()
      .getClassLoader()
      .getResourceAsStream("configuration/schema/configuration.yaml");
    SchemaConfiguration config = (SchemaConfiguration) yaml.load(inputStream);
    testConfiguration(config);
  }

  @Test
  public void testReading_fromFile() throws FileNotFoundException {
    Yaml yaml = new Yaml(new Constructor(SchemaConfiguration.class, new LoaderOptions()));
    InputStream inputStream = new FileInputStream(new File("src/test/resources/configuration/schema/configuration.yaml"));
    SchemaConfiguration config = (SchemaConfiguration) yaml.load(inputStream);
    testConfiguration(config);
  }

  @Test
  public void testReading_fromConfigurationReader_yaml() throws FileNotFoundException {
    SchemaConfiguration config = ConfigurationReader.readSchemaYaml("src/test/resources/configuration/schema/configuration.yaml");
    testConfiguration(config);
  }

  @Test
  public void testReading_fromConfigurationReader_json() throws FileNotFoundException {
    SchemaConfiguration config = ConfigurationReader.readSchemaJson("src/test/resources/configuration/schema/configuration.json");
    testConfiguration(config);
  }

  @Test
  public void test_asSchema() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/configuration.yaml")
      .asSchema();

    assertEquals(Format.JSON, schema.getFormat());
    assertEquals(3, schema.getPaths().size());
  }

  @Test
  public void test_asSchema_withMeemoo() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/meemoo.yaml")
      .asSchema();

    assertEquals(Format.CSV, schema.getFormat());
    assertEquals(17, schema.getPaths().size());
    assertEquals("url", schema.getPaths().get(0).getLabel());
    assertEquals(1, schema.getPaths().get(0).getCategories().size());
    assertEquals("MANDATORY", schema.getPaths().get(0).getCategories().get(0));
    assertTrue(schema.getPaths().get(0).isExtractable());

    assertEquals(0, schema.getPaths().get(1).getCategories().size());
  }

  public void testConfiguration(SchemaConfiguration config) {
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
    Yaml yaml = new Yaml(new Constructor(Rulex.class, new LoaderOptions()));
    InputStream inputStream = this.getClass()
      .getClassLoader()
      .getResourceAsStream("configuration/schema/rules.yaml");
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
    SchemaConfiguration configuration = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/xmlsample.yaml");

    assertEquals(2, configuration.getNamespaces().size());
    assertEquals("http://www.lyncode.com/xoai", configuration.getNamespaces().get("xoai"));
    assertEquals("http://xmlns.com/foaf/0.1/", configuration.getNamespaces().get("foaf"));
  }

  @Test
  public void testReading_xml_as_schema() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/xmlsample.yaml")
      .asSchema();

    assertEquals(2, schema.getNamespaces().size());
    assertEquals("http://www.lyncode.com/xoai", schema.getNamespaces().get("xoai"));
    assertEquals("http://xmlns.com/foaf/0.1/", schema.getNamespaces().get("foaf"));
  }

  @Test
  public void yaml_minCount() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "minCount.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("about").getRules().get(0).getMinCount().intValue());
  }

  @Test
  public void yaml_maxCount() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "maxCount.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("about").getRules().get(0).getMaxCount().intValue());
  }

  @Test
  public void yaml_minLength() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "minLength.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("about").getRules().get(0).getMinLength().intValue());
  }

  @Test
  public void yaml_maxLength() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "maxLength.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("about").getRules().get(0).getMaxLength().intValue());
  }

  @Test
  public void yaml_minExclusive() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "minExclusive.yaml").asSchema();
    assertEquals(10.3, schema.getPathByLabel("price").getRules().get(0).getMinExclusive().doubleValue(), 0.0001);
  }

  @Test
  public void yaml_maxExclusive() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "maxExclusive.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("price").getRules().get(0).getMaxExclusive().intValue());
  }

  @Test
  public void yaml_minInclusive() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "minInclusive.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("price").getRules().get(0).getMinInclusive().intValue());
  }

  @Test
  public void yaml_maxInclusive() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "maxInclusive.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("price").getRules().get(0).getMaxInclusive().intValue());
  }

  @Test
  public void yaml_failureScore() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "failureScore.yaml").asSchema();
    assertEquals(-1, schema.getPathByLabel("about").getRules().get(0).getFailureScore().intValue());
  }

  @Test
  public void yaml_naScore() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "naScore.yaml").asSchema();
    assertEquals(-1, schema.getPathByLabel("about").getRules().get(0).getFailureScore().intValue());
    assertEquals(0, schema.getPathByLabel("about").getRules().get(0).getNaScore().intValue());
    assertEquals(1, schema.getPathByLabel("about").getRules().get(0).getSuccessScore().intValue());
  }

  @Test
  public void yaml_debug() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "debug.yaml").asSchema();
    assertEquals(true, schema.getPathByLabel("about").getRules().get(0).getDebug());
  }

  @Test
  public void yaml_successScore() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "successScore.yaml").asSchema();
    assertEquals(1, schema.getPathByLabel("about").getRules().get(0).getSuccessScore().intValue());
  }

  @Test
  public void yaml_pattern() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "pattern.yaml").asSchema();
    assertEquals("^.+$", schema.getPathByLabel("about").getRules().get(0).getPattern());
  }

  @Test
  public void yaml_hasValue() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "hasValue.yaml").asSchema();
    assertEquals("test", schema.getPathByLabel("about").getRules().get(0).getHasValue());
  }

  @Test
  public void yaml_equals() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "equals.yaml").asSchema();
    assertEquals("description", schema.getPathByLabel("about").getRules().get(0).getEquals());
  }

  @Test
  public void yaml_equals_wrong() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "equals_wrong.yaml").asSchema();
      assertEquals("description", schema.getPathByLabel("about").getRules().get(0).getEquals());
    });
    assertEquals("about refers to a nonexistent field in 'equals: title'", e.getMessage());
  }

  @Test
  public void yaml_lessThan() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "lessThan.yaml").asSchema();
    assertEquals("description", schema.getPathByLabel("about").getRules().get(0).getLessThan());
  }

  @Test
  public void yaml_lessThan_wrong() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "lessThan_wrong.yaml").asSchema();
      assertEquals("description", schema.getPathByLabel("about").getRules().get(0).getLessThan());
    });
    assertEquals("about refers to a nonexistent field in 'lessThan: title'", e.getMessage());
  }

  @Test
  public void yaml_lessThanOrEquals() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "lessThanOrEquals.yaml").asSchema();
    assertEquals("description", schema.getPathByLabel("about").getRules().get(0).getLessThanOrEquals());
  }

  @Test
  public void yaml_lessThanOrEquals_wrong() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "lessThanOrEquals_wrong.yaml").asSchema();
      assertEquals("description", schema.getPathByLabel("about").getRules().get(0).getLessThanOrEquals());
    });
    assertEquals("about refers to a nonexistent field in 'lessThanOrEquals: title'", e.getMessage());
  }

  @Test
  public void yaml_and() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "logical/and.yaml").asSchema();
    assertEquals("1.1", schema.getPathByLabel("about").getRules().get(0).getId());
    List<Rule> andRule = schema.getPathByLabel("about").getRules().get(0).getAnd();
    assertEquals(2, schema.getPathByLabel("about").getRules().get(0).getSuccessScore().intValue());
    assertEquals(2, andRule.size());
    assertEquals(1, andRule.get(0).getMinCount().intValue());
    assertEquals(1, andRule.get(1).getMaxCount().intValue());
  }

  @Test
  public void yaml_and_rule() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "logical/and.yaml").asSchema();
    assertEquals(1, schema.getRuleCheckers().size());
    assertEquals(AndChecker.class, schema.getRuleCheckers().get(0).getClass());
    AndChecker checker = (AndChecker) schema.getRuleCheckers().get(0);
    assertEquals(2, checker.getCheckers().size());
    assertEquals(MinCountChecker.class, checker.getCheckers().get(0).getClass());
    assertEquals(MaxCountChecker.class, checker.getCheckers().get(1).getClass());
  }

  @Test
  public void yaml_or() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "logical/or.yaml").asSchema();
    List<Rule> andRule = schema.getPathByLabel("about").getRules().get(0).getOr();
    assertEquals(2, schema.getPathByLabel("about").getRules().get(0).getSuccessScore().intValue());
    assertEquals(2, andRule.size());
    assertEquals(1, andRule.get(0).getMinCount().intValue());
    assertEquals(1, andRule.get(1).getMaxCount().intValue());
  }

  @Test
  public void yaml_not() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "logical/not.yaml").asSchema();
    List<Rule> andRule = schema.getPathByLabel("about").getRules().get(0).getNot();
    assertEquals(2, schema.getPathByLabel("about").getRules().get(0).getSuccessScore().intValue());
    assertEquals(2, andRule.size());
    assertEquals(2, andRule.get(0).getMinCount().intValue());
    assertEquals(10, andRule.get(1).getMaxLength().intValue());
  }

  @Test
  public void yaml_not_rule() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "logical/not.yaml").asSchema();
    assertEquals(1, schema.getRuleCheckers().size());
    assertEquals(NotChecker.class, schema.getRuleCheckers().get(0).getClass());
    NotChecker checker = (NotChecker) schema.getRuleCheckers().get(0);
    assertEquals(2, checker.getCheckers().size());
    assertEquals(MinCountChecker.class, checker.getCheckers().get(0).getClass());
    assertEquals(MaxLengthChecker.class, checker.getCheckers().get(1).getClass());
  }

  @Test
  public void yaml_contentType() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "contentType.yaml").asSchema();
    Rule contentTypeRule = schema.getPathByLabel("about").getRules().get(0);
    assertEquals("1.1", contentTypeRule.getId());
    assertEquals(2, contentTypeRule.getSuccessScore().intValue());
    assertNotNull(contentTypeRule.getContentType());
    assertFalse(contentTypeRule.getContentType().isEmpty());
    assertEquals(7, contentTypeRule.getContentType().size());
    assertEquals(List.of("image/jpeg", "image/png", "image/tiff", "image/tiff-fx", "image/gif", "image/svg+xml", "application/pdf"),
      contentTypeRule.getContentType());
  }

  @Test
  public void yaml_asLanguageTagged() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml("src/test/resources/configuration/schema/asLanguageTagged.yaml").asSchema();
    assertEquals(true, schema.getPathByLabel("description").isAsLanguageTagged());
  }

  @Test
  public void yaml_isMultilingual() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "isMultilingual.yaml").asSchema();
    assertEquals(true, schema.getPathByLabel("description").getRules().get(0).getMultilingual());
  }

  @Test
  public void yaml_hasLanguaggeTag() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "hasLanguageTag.yaml").asSchema();
    assertEquals(ApplicationScope.allOf, schema.getPathByLabel("description").getRules().get(0).getHasLanguageTag());
  }

  @Test
  public void yaml_hierarchy() throws FileNotFoundException {
    Schema schema = ConfigurationReader.readSchemaYaml(RULES_DIR + "hierarchy.yaml").asSchema();
    List<Rule> rules = schema.getPathByLabel("concept").getRules();
    assertEquals(1, rules.size());
    Rule rule = rules.get(0);
    assertNotNull(rule);
    Rule patternRule = rule.getAnd().get(3);
    assertEquals("^.+$", patternRule.getPattern());
    assertEquals("@rdf:about", patternRule.getValuePath());
    // assertEquals(ApplicationScope.allOf, schema.getPathByLabel("description").getRules().get(0).getHasLanguageTag());
  }
}
