package de.gwdg.metadataqa.api.uniqueness;

public class SolrClientMock implements SolrClient {

  public SolrClientMock(SolrConfiguration configuration) {
  }

  @Override
  public String getSolrSearchResponse(String solrField, String value) {

    if (value.equals("*")) {
      int numFound = 0;
      switch (solrField) {
        case "dc_title_ss":
          numFound = 4000;
          break;
        case "dcterms_alternative_ss":
          numFound = 1000;
          break;
        case "dc_description_ss":
          numFound = 2000;
          break;
        default:
          break;
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
