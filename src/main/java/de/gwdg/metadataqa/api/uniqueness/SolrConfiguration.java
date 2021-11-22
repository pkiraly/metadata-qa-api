package de.gwdg.metadataqa.api.uniqueness;

import java.io.Serializable;

/**
 * Data object for Solr configuration.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class SolrConfiguration implements Serializable {
  private static final long serialVersionUID = -4274994415740745591L;

  private static final String DEFAULT_SOLR_HOST = "localhost";
  private static final String DEFAULT_SOLR_PORT = "8983";
  private static final String DEFAULT_SOLR_PATH = "solr/europeana";

  private String solrHost;
  private String solrPort;
  private String solrPath;

  public SolrConfiguration() {
    this(DEFAULT_SOLR_HOST, DEFAULT_SOLR_PORT, DEFAULT_SOLR_PATH);
  }

  public SolrConfiguration(String solrHost, String solrPort, String solrPath) {
    this.solrHost = solrHost;
    this.solrPort = solrPort;
    this.solrPath = solrPath;
  }

  public String getSolrHost() {
    return solrHost;
  }

  public String getSolrPort() {
    return solrPort;
  }

  public String getSolrPath() {
    return solrPath;
  }

  public String getUrl() {
    return String.format("http://%s:%s/%s", solrHost, solrPort, solrPath);
  }
}
