package de.gwdg.metadataqa.api.uniqueness;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SolrClient extends Serializable {
  String getSolrSearchResponse(String solrField, String value);
  void indexMap(String id, Map<String, List<String>> objectMap) throws IOException, SolrServerException;
  void commit();
  void deleteAll();
}
