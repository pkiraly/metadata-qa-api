package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.calculator.solr.QaSolrClient;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessField;
import de.gwdg.metadataqa.api.uniqueness.UniquenessFieldCalculator;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Uniqueness calculator
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessCalculator extends QaSolrClient implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "uniqueness";

  private FieldCounter<Double> resultMap;

  public UniquenessCalculator(SolrClient solrClient, Schema schema) {
    super(solrClient, schema);
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public List<MetricResult> measure(Selector cache) {
    String recordId = cache.getRecordId();
    if (StringUtils.isNotBlank(recordId) && recordId.startsWith("/")) {
      recordId = recordId.substring(1);
    }

    resultMap = new FieldCounter<>();
    for (UniquenessField solrField : solrFields) {
      var fieldCalculator = new UniquenessFieldCalculator(
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
    return List.of(new FieldCounterBasedResult<Double>(getCalculatorName(), resultMap));
  }

  public String getTotals() {
    List<Integer> totals = new ArrayList<>();
    for (UniquenessField field : solrFields) {
      totals.add(field.getTotal());
    }
    return StringUtils.join(totals, ",");
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
