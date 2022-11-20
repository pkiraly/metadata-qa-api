package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;

public class ReaderTestBase {

  protected CalculatorFacade getCalculator(Format format) {
    Schema schema = new BaseSchema()
      .setFormat(format)
      .addField(new JsonBranch("url").setExtractable())
      .addField(new JsonBranch("name").setExtractable());

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    CalculatorFacade facade = new CalculatorFacade(config).setSchema(schema);

    return facade;
  }

}
