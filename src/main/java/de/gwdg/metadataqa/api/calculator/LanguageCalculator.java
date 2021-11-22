package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.calculator.language.Language;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LanguageCalculator extends BaseLanguageCalculator {

  public static final String CALCULATOR_NAME = "languages";

  private static final Logger LOGGER = Logger.getLogger(LanguageCalculator.class.getCanonicalName());

  // private FieldCounter<String> languageMap;
  // private Map<String, SortedMap<String, Integer>> rawLanguageMap;

  private Schema schema;

  public LanguageCalculator() {
    // this.recordID = null;
  }

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
    for (JsonBranch jsonBranch : schema.getPaths()) {
      if (jsonBranch.isActive()
        && !schema.getNoLanguageFields().contains(jsonBranch.getLabel())) {
        headers.add("lang:" + jsonBranch.getLabel());
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

  /*
  public Map<String, String> getLanguageMap() {
    return languageMap.getMap();
  }

  @Override
  public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
    Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
    labelledResultMap.put(getCalculatorName(), rawLanguageMap);
    return labelledResultMap;
  }
  */
}
