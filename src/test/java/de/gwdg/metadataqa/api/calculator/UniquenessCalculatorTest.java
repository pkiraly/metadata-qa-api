package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrClientMock;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UniquenessCalculatorTest {

  private UniquenessCalculator calculator;

  @Before
  public void setUp() {
    SolrClient solrClient = new SolrClientMock(
      new SolrConfiguration("localhost", "8983", "solr")
    );
    Schema schema = new EdmOaiPmhXmlSchema();
    calculator = new UniquenessCalculator(solrClient, schema);
  }

  @Test
  public void getCalculatorName() throws Exception {
    assertEquals("uniqueness", calculator.getCalculatorName());
  }

  @Test
  public void measure() throws Exception {
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
    calculator.measure(cache);
    assertEquals(
      "\"dc_title_ss/count\":3.000000,\"dc_title_ss/score\":0.711154,"
      + "\"dcterms_alternative_ss/count\":0.000000,\"dcterms_alternative_ss/score\":0.000000,"
      + "\"dc_description_ss/count\":0.000000,\"dc_description_ss/score\":0.000000",
      calculator.getCsv(true, CompressionLevel.ZERO)
    );
  }

  @Test
  public void getTotals() throws Exception {
  }

  @Test
  public void getResultMap() throws Exception {
  }

  @Test
  public void getLabelledResultMap() throws Exception {
  }

  @Test
  public void getCsv() throws Exception {
  }

  @Test
  public void getHeader() throws Exception {
  }

  @Test
  public void getSolrFields() throws Exception {
    assertEquals(4000, calculator.getSolrFields().get(0).getTotal());
    assertEquals(1000, calculator.getSolrFields().get(1).getTotal());
    assertEquals(2000, calculator.getSolrFields().get(2).getTotal());
  }
}