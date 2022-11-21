package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.calculator.language.Language;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;
import java.util.ArrayList;
import java.util.List;

/**
 * Language calculator
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LanguageCalculator extends BaseLanguageCalculator {

  public static final String CALCULATOR_NAME = "languages";

  private Schema schema;

  public LanguageCalculator(Schema schema) {
    this.schema = schema;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (DataElement dataElement : schema.getPaths()) {
      if (dataElement.isActive()
        && !schema.getNoLanguageFields().contains(dataElement.getLabel())) {
        headers.add("lang:" + dataElement.getLabel());
      }
    }
    return headers;
  }

  @Override
  public List<MetricResult> measure(PathCache cache)
      throws InvalidJsonException {

    Language language = new Language(schema, cache);
    FieldCounter<String> languageMap = language.measure();
    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), languageMap).withNoCompression());
  }
}
