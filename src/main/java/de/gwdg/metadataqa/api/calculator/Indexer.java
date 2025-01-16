package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.calculator.solr.QaSolrClient;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.interfaces.Shutdownable;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;
import de.gwdg.metadataqa.api.uniqueness.UniquenessField;
import de.gwdg.metadataqa.api.util.IdentifierGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Indexer extends QaSolrClient implements Calculator, Shutdownable, Serializable {

  private static final Logger LOGGER = Logger.getLogger(Indexer.class.getCanonicalName());

  public static final String CALCULATOR_NAME = "indexer";
  private int generatedRecordId;
  private int indexCounter;
  private boolean isGeneratedIdentifierEnabled = false;
  private boolean useGeneratedIdentifier = true;

  public Indexer(SolrClient solrClient, Schema schema) {
    super(solrClient, schema);
    solrClient.deleteAll();
    generatedRecordId = 1;
    indexCounter = 0;
    LOGGER.info("Indexer created " + solrClient.getClass().getCanonicalName());
  }

  @Override
  public List<MetricResult> measure(Selector cache) {
    try {
      Map<String, List<String>> valuesMap = new HashMap<>();

      Set<String> extractedValues = extractValue(cache, schema.getRecordId().getPath());
      String recordId = null;
      if (isGeneratedIdentifierEnabled && useGeneratedIdentifier) {
        recordId = IdentifierGenerator.generate();
        if (!extractedValues.isEmpty())
          valuesMap.put("recordId", new ArrayList<>(extractedValues));
      } else {
        if (extractedValues.isEmpty()) {
          LOGGER.severe(String.format("Missing record ID (path: %s)", schema.getRecordId().getPath()));
          if (cache.getRecordId() != null)
            recordId = cache.getRecordId();
          else if (isGeneratedIdentifierEnabled)
            recordId = IdentifierGenerator.generate();
        } else {
          recordId = StringUtils.join(extractedValues, " --- ");
        }
      }
      cache.setRecordId(recordId);

      for (UniquenessField solrField : solrFields) {
        Set<String> values = extractValue(cache, solrField.getPath());
        if (!values.isEmpty())
          valuesMap.put(solrField.getSolrField(), new ArrayList<>(values));
      }
      solrClient.indexMap(recordId, valuesMap);
      indexCounter++;
    } catch (IOException | SolrServerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Set<String> extractValue(Selector cache, String path) {
    Set<String> values = new LinkedHashSet<>();
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
    LOGGER.info("shutDown solr. Counter: " + indexCounter);
    solrClient.commit();
    LOGGER.info("Number of indexed documents: " + UniquenessExtractor.extractNumFound(
      solrClient.getSolrSearchResponse("*", "*")));
  }

  public void enableGeneratedIdentifier() {
    this.isGeneratedIdentifierEnabled = true;
  }
}
