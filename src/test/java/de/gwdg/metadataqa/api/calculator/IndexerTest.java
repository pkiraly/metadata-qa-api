package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClientMock;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class IndexerTest {

  Indexer indexer;
  CsvPathCache cache;
  SolrClientMock solrClient;

  @Before
  public void setUp() throws Exception {
    SolrConfiguration solrConfiguration = new SolrConfiguration("localhost", "8983", "solr");
    Schema schema = getSchema(Format.CSV);
    solrClient = new SolrClientMock(solrConfiguration);
    indexer = new Indexer(solrClient, schema);
    assertNotNull(indexer);

    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "URL,two three");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));
  }

  @Test
  public void measure() {
    indexer.measure(cache);
    assertEquals("URL", solrClient.getId());
    assertTrue(solrClient.getObjectMap().containsKey("name_ss"));
    assertEquals(List.of("two three"), solrClient.getObjectMap().get("name_ss"));
  }

  @Test
  public void getHeader() {
    assertEquals(List.of(), indexer.getHeader());
  }

  @Test
  public void getCalculatorName() {
    assertEquals("indexer", indexer.getCalculatorName());
  }

  @Test
  public void shutDown() {
    indexer.shutDown();
    assertTrue(solrClient.isCommited());
  }

  private Schema getSchema(Format format) {
    BaseSchema schema = new BaseSchema()
      .setFormat(format)
      .addField(new DataElement("url").setExtractable().setIndexField("url"))
      .addField(new DataElement("name").setExtractable().setIndexField("name"));
    schema.setRecordId(schema.getPathByLabel("url"));
    return schema;
  }

}