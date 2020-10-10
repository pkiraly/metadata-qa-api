package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.uniqueness.*;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessCalculator implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "uniqueness";

  private static final Logger LOGGER = Logger.getLogger(
      UniquenessCalculator.class.getCanonicalName()
  );
  public static final String SUFFIX = "_txt";
  public static final int SUFFIX_LENGTH = SUFFIX.length();

  private UniquenessExtractor extractor;
  private List<UniquenessField> solrFields;

  private SolrClient solrClient;

  private FieldCounter<Double> resultMap;

  public UniquenessCalculator(SolrClient solrClient) {
    this.solrClient = solrClient;
  }

  public UniquenessCalculator(SolrClient solrClient, Schema schema) {
    this(solrClient);
    extractor = new UniquenessExtractor();
    initialize(schema);
  }

  private void initialize(Schema schema) {
    solrFields = new ArrayList<>();
    for (String label : schema.getSolrFields().keySet()) {
      UniquenessField field = new UniquenessField(label);
      field.setJsonPath(
          schema.getPathByLabel(label).getAbsoluteJsonPath().replace("[*]", "")
      );
      String solrField = schema.getSolrFields().get(label);
      if (solrField.endsWith(SUFFIX)) {
        solrField = solrField.substring(0, solrField.length() - SUFFIX_LENGTH) + "_ss";
      }
      field.setSolrField(solrField);

      String solrResponse = solrClient.getSolrSearchResponse(solrField, "*");
      int numFound = extractor.extractNumFound(solrResponse, "total");
      field.setTotal(numFound);
      field.setScoreForUniqueValue(
        UniquenessFieldCalculator.calculateScore(numFound, 1.0)
      );

      solrFields.add(field);
    }
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public void measure(PathCache cache) {
    String recordId = cache.getRecordId();
    if (StringUtils.isNotBlank(recordId) && recordId.startsWith("/")) {
      recordId = recordId.substring(1);
    }

    resultMap = new FieldCounter<>();
    for (UniquenessField solrField : solrFields) {
      UniquenessFieldCalculator fieldCalculator = new UniquenessFieldCalculator(
          cache, recordId, solrClient, solrField
      );
      fieldCalculator.calculate();
      resultMap.put(
          solrField.getSolrField() + "/count",
          fieldCalculator.getAverageCount()
      );
      resultMap.put(
          solrField.getSolrField() + "/score",
          fieldCalculator.getAverageScore()
      );
    }
  }

  public String getTotals() {
    List<Integer> totals = new ArrayList<>();
    for (UniquenessField field : solrFields) {
      totals.add(field.getTotal());
    }
    return StringUtils.join(totals, ",");
  }

  @Override
  public Map<String, ? extends Object> getResultMap() {
    return resultMap.getMap();
  }

  @Override
  public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
    Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
    labelledResultMap.put(getCalculatorName(), resultMap.getMap());
    return labelledResultMap;
  }

  @Override
  public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
    return resultMap.getCsv(withLabel, compressionLevel);
  }

  @Override
  public List<Object> getCsv() {
    return resultMap.getCsv();
  }

  @Override
  public List<String> getList(boolean withLabel, CompressionLevel compressionLevel) {
    return resultMap.getList(withLabel, compressionLevel);
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (UniquenessField field : solrFields) {
      headers.add(field.getSolrField() + "/count");
      headers.add(field.getSolrField() + "/score");
    }
    return headers;
  }

  public List<UniquenessField> getSolrFields() {
    return solrFields;
  }
}
