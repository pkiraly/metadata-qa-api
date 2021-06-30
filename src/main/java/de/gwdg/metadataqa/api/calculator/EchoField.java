package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.json.JsonBranch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EchoField implements Calculator, Serializable {

  private static final Logger LOGGER = Logger.getLogger(EchoField.class.getCanonicalName());

  public static final String CALCULATOR_NAME = "echoField";

  protected Schema schema;

  public EchoField(Schema schema) {
    this.schema = schema;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public List<MetricResult> measure(PathCache cache)
      throws InvalidJsonException {
    // FieldCounter<T> resultMap;
    FieldCounter<String> resultMap = new FieldCounter<>();

    if (schema != null) {
      String path;
      for (String fieldName : schema.getEchoFields().keySet()) {
          path = schema.getEchoFields().get(fieldName);
          List<XmlFieldInstance> values = cache.get(path);
          String value = null;
          if (values == null || values.isEmpty() || values.get(0) == null || values.get(0).getValue() == null) {
            // logger.warning("Null value in field: " + fieldName + " (" + path + ")");
            value = null;
          } else {
            value = values.get(0).getValue();
          }
          resultMap.put(fieldName, value);
      }
    }
    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), resultMap).withNoCompression());
  }

  @Override
  public List<String> getHeader() {
	List<String> headers = new ArrayList<>();
	for (String fieldName : schema.getEchoFields().keySet()) {
      headers.add("echo:" + fieldName);
    }
    return headers;
  }
}
