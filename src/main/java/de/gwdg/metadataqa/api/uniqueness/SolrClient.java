package de.gwdg.metadataqa.api.uniqueness;

import java.io.Serializable;

public interface SolrClient extends Serializable {
  String getSolrSearchResponse(String solrField, String value);
}
