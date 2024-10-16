package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Field extractor
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
  public List<MetricResult> measure(Selector cache)
      throws InvalidJsonException {
    FieldCounter<String> resultMap = new FieldCounter<>();
    if (idPath != null)
      extractSingleField(cache, resultMap, idPath, FIELD_NAME);

    if (schema != null) {
      String path;
      DataElement dataELement;
      for (String fieldName : schema.getExtractableFields().keySet()) {
        if (idPath == null || !fieldName.equals(FIELD_NAME)) {
          dataELement = schema.getPathByLabel(fieldName);
          path = schema.getExtractableFields().get(fieldName);
          extractSingleField(cache, resultMap, path, fieldName, dataELement);
        }
      }
    }
    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), resultMap).withNoCompression());
  }

  private void extractSingleField(Selector cache, FieldCounter<String> resultMap, String path, String fieldName) {
    extractSingleField(cache, resultMap, path, fieldName,null);
  }

  private void extractSingleField(Selector cache,
                                    FieldCounter<String> resultMap,
                                    String path,
                                    String fieldName,
                                    DataElement dataELement) {
    List<XmlFieldInstance> fieldInstances;
    if (dataELement != null) {
      fieldInstances = cache.get(dataELement);
    } else {
      fieldInstances = cache.get(path);
    }
    String value = null;
    if (fieldInstances == null || fieldInstances.isEmpty() || fieldInstances.get(0) == null) {
      value = nullValue;
    } else {
      Set<String> values = new LinkedHashSet<>();
      for (XmlFieldInstance instance : fieldInstances) {
        boolean isEdm = instance instanceof EdmFieldInstance;
        if (isEdm && ((EdmFieldInstance)instance).getResource() != null) {
          value = ((EdmFieldInstance) instance).getResource();
        } else if (instance.getValue() != null) {
          value = instance.getValue();
        }
        // if (!values.contains(values))
        if (StringUtils.isNotBlank(value))
          values.add(value);
      }
      value = StringUtils.join(values, " --- ");
    }
    // LOGGER.info("value: " + value);
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
        if (idPath == null || !fieldName.equals(FIELD_NAME))
          headers.add(FileUtils.escape(fieldName));

    return headers;
  }
}
