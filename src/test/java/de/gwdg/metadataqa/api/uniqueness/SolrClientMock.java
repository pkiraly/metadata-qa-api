package de.gwdg.metadataqa.api.uniqueness;

public class SolrClientMock implements SolrClient {

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
    } else {
      System.err.printf("solrField: %s, value: %s\n", solrField, value);
    }
    return null;
  }
}
