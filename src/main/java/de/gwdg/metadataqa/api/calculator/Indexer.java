package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.calculator.solr.QaSolrClient;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.interfaces.Shutdownable;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessField;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Indexer extends QaSolrClient implements Calculator, Shutdownable, Serializable {

  private static final Logger LOGGER = Logger.getLogger(Indexer.class.getCanonicalName());

  public static final String CALCULATOR_NAME = "indexer";

  public Indexer(SolrClient solrClient, Schema schema) {
    super(solrClient, schema);
    solrClient.deleteAll();
  }

  @Override
  public List<MetricResult> measure(Selector cache) {
    try {
      List<String> extractedValues = extractValue(cache, schema.getRecordId().getPath());
      if (extractedValues.isEmpty())
        throw new RuntimeException(String.format("Missing record ID (path: %s)", schema.getRecordId().getPath()));
      String recordId = extractedValues.get(0);

      Map<String, List<String>> resultMap = new HashMap<>();
      for (UniquenessField solrField : solrFields) {
        List<String> values = extractValue(cache, solrField.getPath());
        if (!values.isEmpty())
          resultMap.put(solrField.getSolrField(), values);
      }
      solrClient.indexMap(recordId, resultMap);
    } catch (IOException | SolrServerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<String> extractValue(Selector cache, String path) {
    List<String> values = new ArrayList<>();
    List<XmlFieldInstance> instances = cache.get(path);
    if (instances != null && !instances.isEmpty())
      for (XmlFieldInstance instance : instances)
        if (instance.hasValue())
          values.add(instance.getValue());
    return values;
  }

  @Override
  public List<String> getHeader() {
    return new ArrayList<>();
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public void shutDown() {
    LOGGER.info("shutDown");
    solrClient.commit();
  }
}
