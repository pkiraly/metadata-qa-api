package de.gwdg.metadataqa.api.uniqueness;

import org.junit.Test;

import static org.junit.Assert.*;

public class SolrConfigurationTest {

  @Test
  public void constructor_withDeafults() {
    SolrConfiguration config = new SolrConfiguration();
    assertEquals("localhost", config.getSolrHost());
    assertEquals("8983", config.getSolrPort());
    assertEquals("solr/europeana", config.getSolrPath());
  }

  @Test
  public void constructor_withArbitraryValues() {
    SolrConfiguration config = new SolrConfiguration(
      "196.168.0.1", "8080", "custom-path");
    assertEquals("196.168.0.1", config.getSolrHost());
    assertEquals("8080", config.getSolrPort());
    assertEquals("custom-path", config.getSolrPath());
  }

}