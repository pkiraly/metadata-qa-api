package de.gwdg.metadataqa.api.uniqueness;

public interface SolrClient {
  String getSolrSearchResponse(String solrField, String value);
}
