package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.FileUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractor implements Calculator, Serializable {

  private static final Logger LOGGER = Logger.getLogger(FieldExtractor.class.getCanonicalName());

  public static final String CALCULATOR_NAME = "fieldExtractor";
  public static final String FIELD_NAME = "recordId";

  private String idPath;
  protected String nullValue = "";
  protected Schema schema;

  public FieldExtractor() {
  }

  public FieldExtractor(Schema schema) {
    this.schema = schema;
    if (schema.getExtractableFields().get(FIELD_NAME) != null)
      setIdPath(schema.getExtractableFields().get(FIELD_NAME));
  }

  public FieldExtractor(String idPath) {
    this.idPath = idPath;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public List<MetricResult> measure(PathCache cache)
      throws InvalidJsonException {
    FieldCounter<String> resultMap = new FieldCounter<>();
    if (idPath != null)
      extractSingleField(cache, resultMap, idPath, FIELD_NAME);

    if (schema != null) {
      String path;
      for (String fieldName : schema.getExtractableFields().keySet()) {
          path = schema.getExtractableFields().get(fieldName);
        extractSingleField(cache, resultMap, path, fieldName);
      }
    }
    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), resultMap).withNoCompression());
  }

  private void extractSingleField(PathCache cache, FieldCounter<String> resultMap, String path, String fieldName) {
    List<XmlFieldInstance> values = cache.get(path);
    String value = null;
    if (values == null || values.isEmpty() || values.get(0) == null || values.get(0).getValue() == null) {
      value = nullValue;
    } else {
      value = values.get(0).getValue();
    }
    resultMap.put(fieldName, value);
  }

  public String getIdPath() {
    return idPath;
  }

  public void setIdPath(String idPath) {
    this.idPath = idPath;
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    if (idPath != null)
      headers.add(FIELD_NAME);

    if (schema != null)
      for (String fieldName : schema.getExtractableFields().keySet())
        headers.add(FileUtils.escape(fieldName));

    return headers;
  }
}
