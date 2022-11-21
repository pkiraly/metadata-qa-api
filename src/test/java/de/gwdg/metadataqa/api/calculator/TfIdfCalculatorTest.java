package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.schema.edm.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import java.util.Arrays;
import java.util.List;

import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrClientMock;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfCalculatorTest {

  @Test
  public void testGetHeaders() {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    List<String> expected = Arrays.asList(
      "Proxy/dc:title:sum", "Proxy/dc:title:avg",
      "Proxy/dcterms:alternative:sum", "Proxy/dcterms:alternative:avg",
      "Proxy/dc:description:sum", "Proxy/dc:description:avg"
    );
    assertEquals(6, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());

    calculator = new TfIdfCalculator(new EdmFullBeanSchema());
    assertEquals(6, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());
  }

  @Test
  public void getCalculatorName() throws Exception {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    assertEquals("uniqueness", calculator.getCalculatorName());
  }

  @Test
  public void getSolrSearchPath() throws Exception {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    calculator.setSolrConfiguration(new SolrConfiguration());

    assertEquals(
      "http://localhost:8983/solr/europeana/tvrh/?q=id:\"%s\"&version=2.2&indent=on&qt=tvrh&tv=true&tv.all=true" +
      "&f.includes.tv.tf=true&tv.fl=dc_title_txt,dc_description_txt,dcterms_alternative_txt&wt=json&json.nl=map&rows=1000&fl=id",
      calculator.getSolrSearchPath());
  }

  @Test
  public void isTermCollectionEnabled() throws Exception {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    assertFalse(calculator.isTermCollectionEnabled());
  }

  @Test
  public void enableTermCollection() throws Exception {
    TfIdfCalculator calculator = new TfIdfCalculator(new EdmOaiPmhJsonSchema());
    calculator.enableTermCollection(true);
    assertTrue(calculator.isTermCollectionEnabled());
  }

  @Test
  public void emptyContructor() throws Exception {
    TfIdfCalculator calculator = new TfIdfCalculator();
    assertNotNull(calculator);
  }

  @Test
  public void measure() throws Exception {
    SolrConfiguration solrConfiguration = new SolrConfiguration("localhost", "8983", "solr");
    Schema schema = getSchema(Format.CSV);
    SolrClient solrClient = new SolrClientMock(solrConfiguration);
    CsvPathCache cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "URL,two three");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));
    cache.setRecordId(((List<XmlFieldInstance>)cache.get(schema.getRecordId().getPath())).get(0).getValue());

    TfIdfCalculator calculator = new TfIdfCalculator(schema);
    calculator.setSolrClient(solrClient);
    List<MetricResult> result = calculator.measure(cache);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(0.008826999437345252, result.get(0).getResultMap().get("url:sum"));
    assertEquals(0.0017653998874690505, result.get(0).getResultMap().get("url:avg"));
    assertEquals(0.0, result.get(0).getResultMap().get("name:sum"));
    assertEquals(0.0, result.get(0).getResultMap().get("name:avg"));
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
