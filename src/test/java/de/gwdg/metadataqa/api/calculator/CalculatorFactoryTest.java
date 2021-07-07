package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CalculatorFactoryTest {

  @Test
  public void createWithExtractable() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url"))
      .addField(new JsonBranch("name").setExtractable());

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .enableCompletenessMeasurement();

    List<Calculator> calculators = CalculatorFactory.create(config, schema);
    assertEquals(2, calculators.size());
    assertEquals("fieldExtractor", calculators.get(0).getCalculatorName());
    assertEquals("completeness", calculators.get(1).getCalculatorName());
  }

  @Test
  public void createWithoutExtractable() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url"))
      .addField(new JsonBranch("name"));

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .enableCompletenessMeasurement();

    List<Calculator> calculators = CalculatorFactory.create(config, schema);
    assertEquals(1, calculators.size());
    assertEquals("completeness", calculators.get(0).getCalculatorName());
  }
}