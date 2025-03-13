package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class HasChildrenCheckerTest {

  String BASE_DIR = "src/test/resources/configuration/schema/rules/hasChildren/";

  @Test
  public void prefix() {
    assertEquals("hasValue", HasChildrenChecker.PREFIX);
  }

  @Test
  public void success() throws IOException, CsvValidationException {
    Schema schema = ConfigurationReader.readSchemaYaml(BASE_DIR + "schema.yml").asSchema();

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade calculator = new CalculatorFacade(config).setSchema(schema);
    String content = new String(Files.readAllBytes(Paths.get(BASE_DIR + "date-two-children.xml")));

    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(schema.getFormat(), content, schema.getNamespaces());

    DataElement de = schema.getPathByLabel("date");
    HasChildrenChecker checker = new HasChildrenChecker(de, de.getRules().get(0).getHasChildren());

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.STATUS);

    assertEquals(1, fieldCounter.size());
    assertEquals("date:hasValue", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader()).getStatus());
  }

  @Test
  public void failure() throws IOException, CsvValidationException {
    Schema schema = ConfigurationReader.readSchemaYaml(BASE_DIR + "schema.yml").asSchema();

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade calculator = new CalculatorFacade(config).setSchema(schema);
    String content = new String(Files.readAllBytes(Paths.get(BASE_DIR + "date-one-child.xml")));

    Selector<? extends XmlFieldInstance> cache = SelectorFactory.getInstance(schema.getFormat(), content, schema.getNamespaces());

    DataElement de = schema.getPathByLabel("date");
    HasChildrenChecker checker = new HasChildrenChecker(de, de.getRules().get(0).getHasChildren());

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.STATUS);

    assertEquals(1, fieldCounter.size());
    assertEquals("date:hasValue", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader()).getStatus());
  }
}