package de.gwdg.metadataqa.api.io;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;

public class IOTestBase {

  CalculatorFacade calculator;

  protected CalculatorFacade getCalculator(Format format) {
    if (calculator == null) {
      MeasurementConfiguration config = new MeasurementConfiguration()
        .enableFieldExtractor()
        .disableCompletenessMeasurement();

      calculator = new CalculatorFacade(config).setSchema(getSchema(format));
    }

    return calculator;
  }

  protected Schema getSchema(Format format) {
    return new BaseSchema()
      .setFormat(format)
      .addField(new DataElement("url").setExtractable())
      .addField(new DataElement("name").setExtractable());
  }
}
