package de.gwdg.metadataqa.api.uniqueness;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolrClientMock implements SolrClient {

  private String id;
  private Map<String, List<String>> objectMap;
  private boolean commited = false;

  public SolrClientMock(SolrConfiguration configuration) {
  }

  @Override
  public String getSolrSearchResponse(String solrField, String value) {

    if (value.equals("*")) {
      var numFound = 0;
      if ("dc_title_ss".equals(solrField)) {
        numFound = 4000;
      } else if ("dcterms_alternative_ss".equals(solrField)) {
        numFound = 1000;
      } else if ("dc_description_ss".equals(solrField)) {
        numFound = 2000;
      }
      return "{\"response\":{\"numFound\":" + numFound + "}}";
    } else if (solrField.equals("dc_title_ss")
        && value.equals("Pyrker-Oberwart, Johann Ladislaus")) {
      return "{\"response\":{\"numFound\":3}}";
    } else if (solrField.equals("url_ss") && value.equals("URL")) {
      return "{\"response\":{\"numFound\":3}}";
    } else if (solrField.equals("name_ss") && value.equals("two three")) {
      return "{\"response\":{\"numFound\":1}}";
    } else {
      System.err.printf("solrField: %s, value: %s\n", solrField, value);
    }
    return null;
  }

  @Override
  public String getSolrSearchResponse(String solrField, Set<String> values) {
    return getSolrSearchResponse(solrField, StringUtils.join(values, " --- "));
  }

  public String getTfIdfResponse(String params, String recordId) {
    return "{\"responseHeader\":{\"status\":0,\"QTime\":74}," +
      "\"response\":{\"numFound\":1,\"start\":0,\"docs\":[{\"id\":\"2022320/3F61C612ED9C42CCB85E533B4736795E8BDC7E77\"}]}," +
      "\"termVectors\":{\"warnings\":{\"noPayloads\":[\"dc_title_txt\",\"dc_description_txt\",\"dcterms_alternative_txt\"]}," +
      "\"URL\":{\"uniqueKey\":\"2022320/3F61C612ED9C42CCB85E533B4736795E8BDC7E77\"," +
      "\"url\":{" +
      "\"fleming\":{\"tf\":1,\"positions\":{\"position\":0},\"offsets\":{\"start\":0,\"end\":7},\"df\":1073,\"tf-idf\":9.319664492078285E-4}," +
      "\"huddersfield\":{\"tf\":1,\"positions\":{\"position\":4},\"offsets\":{\"start\":35,\"end\":47},\"df\":12073,\"tf-idf\":8.282945415389712E-5}," +
      "\"mair\":{\"tf\":1,\"positions\":{\"position\":1},\"offsets\":{\"start\":8,\"end\":12},\"df\":178,\"tf-idf\":0.0056179775280898875}," +
      "\"slaithwaite\":{\"tf\":1,\"positions\":{\"position\":3},\"offsets\":{\"start\":22,\"end\":33},\"df\":477,\"tf-idf\":0.0020964360587002098}," +
      "\"wedding\":{\"tf\":1,\"positions\":{\"position\":2},\"offsets\":{\"start\":13,\"end\":20},\"df\":10226,\"tf-idf\":9.778994719342852E-5}}}}}";
  }

  @Override
  public void indexMap(String id, Map<String, List<String>> objectMap) throws IOException, SolrServerException {
    this.id = id;
    this.objectMap = objectMap;
  }

  @Override
  public void commit() {
    commited = true;
  }

  @Override
  public void deleteAll() {

  }

  public String getId() {
    return id;
  }

  public Map<String, List<String>> getObjectMap() {
    return objectMap;
  }

  public boolean isCommited() {
    return commited;
  }
}
