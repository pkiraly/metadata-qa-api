package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.problemcatalog.FieldCounterBasedResult;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.uniqueness.TfIdf;
import de.gwdg.metadataqa.api.uniqueness.TfIdfExtractor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * TF-IDF calculator
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfCalculator implements Calculator, Serializable {

  public static final String CALCULATOR_NAME = "uniqueness";
  private static final int MEGABYTE = 1024 * 1024;

  private static final Logger LOGGER = Logger.getLogger(TfIdfCalculator.class.getCanonicalName());

  private static final String SOLR_SEARCH_PARAMS = "tvrh/"
        + "?q=id:\"%s\""
        + "&version=2.2"
        + "&indent=on"
        + "&qt=tvrh"
        + "&tv=true"
        + "&tv.all=true"
        + "&f.includes.tv.tf=true"
        + "&tv.fl=dc_title_txt,dc_description_txt,dcterms_alternative_txt"
        + "&wt=json"
        + "&json.nl=map"
        + "&rows=1000"
        + "&fl=id";
  private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

  private SolrConfiguration solrConfiguration;
  private String solrSearchPath;

  private Map<String, List<TfIdf>> termsCollection;
  private boolean termCollectionEnabled = false;
  private Schema schema;
  private SolrClient solrClient;

  public TfIdfCalculator() {
  }

  public TfIdfCalculator(Schema schema) {
    this.schema = schema;
  }

  @Override
  public String getCalculatorName() {
    return CALCULATOR_NAME;
  }

  @Override
  public List<MetricResult> measure(Selector cache) {
    String recordId = cache.getRecordId();
    if (recordId.startsWith("/")) {
      recordId = recordId.substring(1);
    }

    String solrJsonResponse = solrClient != null
      ? solrClient.getTfIdfResponse(String.format(SOLR_SEARCH_PARAMS, recordId).replace("\"", "%22"), recordId)
      : getSolrResponse(recordId);
    var extractor = new TfIdfExtractor(schema);
    FieldCounter<Double> resultMap = extractor.extract(solrJsonResponse, recordId, termCollectionEnabled);
    termsCollection = extractor.getTermsCollection();
    return List.of(new FieldCounterBasedResult<>(getCalculatorName(), resultMap));
  }

  private String getSolrResponse(String recordId) {
    String jsonString = null;

    String url = String.format(getSolrSearchPath(), recordId).replace("\"", "%22");

    HttpGet httpGet = new HttpGet(url);
    try {
      CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
      try {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          jsonString = EntityUtils.toString(entity);
        }
      } catch (ParseException e) {
        throw new RuntimeException(e);
      } finally {
        response.close();
      }
    } catch (IOException e) {
      LOGGER.severe("Fatal transport error: " + e.getMessage());
    }

    return jsonString;
  }

  public Map<String, List<TfIdf>> getTermsCollection() {
    return termsCollection;
  }

  public void enableTermCollection(boolean enableTermCollection) {
    this.termCollectionEnabled = enableTermCollection;
  }

  public boolean isTermCollectionEnabled() {
    return termCollectionEnabled;
  }

  @Override
  public List<String> getHeader() {
    List<String> headers = new ArrayList<>();
    for (DataElement dataElement : schema.getIndexFields()) {
      headers.add(dataElement.getLabel() + ":sum");
      headers.add(dataElement.getLabel() + ":avg");
    }
    return headers;
  }

  public void setSolrConfiguration(final SolrConfiguration pSolrConfiguration) {
    this.solrConfiguration = pSolrConfiguration;
  }

  public String getSolrSearchPath() {
    if (solrSearchPath == null) {
      this.solrSearchPath = String.format(
        "http://%s:%s/%s/%s",
        solrConfiguration.getSolrHost(),
        solrConfiguration.getSolrPort(),
        solrConfiguration.getSolrPath(),
        SOLR_SEARCH_PARAMS
      );
    }
    return this.solrSearchPath;
  }

  public void setSolrClient(SolrClient solrClient) {
    this.solrClient = solrClient;
  }
}
