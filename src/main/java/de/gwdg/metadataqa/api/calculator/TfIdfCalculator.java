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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

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
  private static final HttpClient HTTP_CLIENT = new HttpClient();

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
    HttpMethod method = new GetMethod(url);
    var params = new HttpMethodParams();
    params.setIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, MEGABYTE);
    method.setParams(params);
    try {
      var statusCode = HTTP_CLIENT.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK) {
        LOGGER.severe("Method failed: " + method.getStatusLine());
      }

      var baos = new ByteArrayOutputStream();
      IOUtils.copy(method.getResponseBodyAsStream(), baos);
      jsonString = baos.toString(StandardCharsets.UTF_8);
    } catch (HttpException e) {
      LOGGER.severe("Fatal protocol violation: " + e.getMessage());
    } catch (IOException e) {
      LOGGER.severe("Fatal transport error: " + e.getMessage());
    } finally {
      method.releaseConnection();
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
