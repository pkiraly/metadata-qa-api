package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractor extends BaseCalculator<String> implements Calculator, Serializable {

  private static final Logger LOGGER = Logger.getLogger(FieldExtractor.class.getCanonicalName());

  public static final String CALCULATOR_NAME = "fieldExtractor";
  public static final String FIELD_NAME = "recordId";

  private String idPath;
  protected Schema schema;

  public FieldExtractor() {
  }

  public FieldExtractor(Schema schema) {
    this.schema = schema;
    if (schema.getExtractableFields() == null
       || schema.getExtractableFields().isEmpty()
       || !schema.getExtractableFields().containsKey(FIELD_NAME)) {
      throw new IllegalArgumentException(String.format(
          "Schema should contain '%s' among the extractable fields", FIELD_NAME));
    }
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
  public void measure(PathCache cache)
      throws InvalidJsonException {
    resultMap = new FieldCounter<String>();
    List<XmlFieldInstance> instances = cache.get(getIdPath());
    if (instances == null || instances.isEmpty()) {
      LOGGER.severe("No record ID in " + cache.getContent());
      resultMap.put(FIELD_NAME, "");
      return;
    }
    String recordId = instances.get(0).getValue().trim();
    cache.setRecordId(recordId);
    resultMap.put(FIELD_NAME, recordId);
    if (schema != null) {
      String path;
      for (String fieldName : schema.getExtractableFields().keySet()) {
        if (!fieldName.equals(FIELD_NAME)) {
          path = schema.getExtractableFields().get(fieldName);
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
    }
  }

  public String getIdPath() {
    return idPath;
  }

  public void setIdPath(String idPath) {
    this.idPath = idPath;
  }

  @Override
  public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
    return resultMap.getCsv(withLabel, CompressionLevel.ZERO); // the extracted fields should never be compressed!
  }

  @Override
  public List<String> getList(boolean withLabel, CompressionLevel compressionLevel) {
    return resultMap.getList(withLabel, CompressionLevel.ZERO); // the extracted fields should never be compressed!
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    headers.add(FIELD_NAME);
    return headers;
  }
}
