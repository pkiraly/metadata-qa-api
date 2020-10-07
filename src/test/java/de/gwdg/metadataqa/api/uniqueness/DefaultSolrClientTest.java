package de.gwdg.metadataqa.api.uniqueness;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultSolrClientTest {

  @Test
  public void getSolrSearchResponse() {
    DefaultSolrClient client = new DefaultSolrClient(new SolrConfiguration());
    assertEquals("http://localhost:8983/solr/europeana",
      client.getSolrBasePath());
    // client.getSolrSearchResponse("text", "*");
  }

  @Test
  public void buildUrl_simple() {
    DefaultSolrClient client = new DefaultSolrClient(new SolrConfiguration());
    assertEquals("http://localhost:8983/solr/europeana/select/?q=text:*&rows=0",
      client.buildUrl("text", "*"));
  }

  @Test
  public void buildUrl_accents() {
    DefaultSolrClient client = new DefaultSolrClient(new SolrConfiguration());
    assertEquals(
      "http://localhost:8983/solr/europeana/select/?q=text:%22Bart%C3%B3k%22&rows=0",
      client.buildUrl("text", "Bartók"));
  }

  @Test
  public void buildUrl_phrase() {
    DefaultSolrClient client = new DefaultSolrClient(new SolrConfiguration());
    assertEquals(
      "http://localhost:8983/solr/europeana/select/?q=text:%22%5C%22Bartok+Bela%5C%22%22&rows=0",
      client.buildUrl("text", "\"Bartok Bela\""));
  }

  @Test
  public void getSolrSearchPattern() {
    DefaultSolrClient client = new DefaultSolrClient(new SolrConfiguration());
    assertEquals("http://localhost:8983/solr/europeana/select/?q=%s:%%22%s%%22&rows=0",
      client.getSolrSearchPattern());
  }

  @Test
  public void getSolrSearchAllPattern() {
    DefaultSolrClient client = new DefaultSolrClient(new SolrConfiguration());
    assertEquals("http://localhost:8983/solr/europeana/select/?q=%s:%s&rows=0",
      client.getSolrSearchAllPattern());
  }
}