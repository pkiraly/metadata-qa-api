package de.gwdg.metadataqa.api.calculator;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.SolrClientMock;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class UniquenessCalculatorTest {

  private UniquenessCalculator calculator;
  private Schema schema;
  private SolrClient solrClient;
  private String jsonString;
  private PathCache cache;

  @Before
  public void setUp() throws IOException, URISyntaxException {
    solrClient = new SolrClientMock(
      new SolrConfiguration("localhost", "8983", "solr")
    );
    schema = new EdmOaiPmhJsonSchema();
    calculator = new UniquenessCalculator(solrClient, schema);
    jsonString = FileUtils.readFirstLineFromResource("general/test.json");
    cache = new JsonPathCache(jsonString);
  }

  @Test
  public void getCalculatorName() throws Exception {
    assertEquals("uniqueness", calculator.getCalculatorName());
  }

  @Test
  public void measure() throws Exception {
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
    calculator.measure(cache);
    assertEquals("4000,1000,2000", calculator.getTotals());
  }

  @Test
  public void getResultMap() throws Exception {
    calculator.measure(cache);
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("dc_title_ss/count", 3.0);
    map.put("dc_title_ss/score", 0.7111542257564604);
    map.put("dcterms_alternative_ss/count", 0.0);
    map.put("dcterms_alternative_ss/score", 0.0);
    map.put("dc_description_ss/count", 0.0);
    map.put("dc_description_ss/score", 0.0);
    assertEquals(map, calculator.getResultMap());
  }

  @Test
  public void getLabelledResultMap() throws Exception {
    calculator.measure(cache);
    Map<String, Object> uniqueness = new LinkedHashMap<>();
    uniqueness.put("dc_title_ss/count", 3.0);
    uniqueness.put("dc_title_ss/score", 0.7111542257564604);
    uniqueness.put("dcterms_alternative_ss/count", 0.0);
    uniqueness.put("dcterms_alternative_ss/score", 0.0);
    uniqueness.put("dc_description_ss/count", 0.0);
    uniqueness.put("dc_description_ss/score", 0.0);
    Map<String, Map<String, Object>> map = new LinkedHashMap<>();
    map.put("uniqueness", uniqueness);
    assertEquals(map, calculator.getLabelledResultMap());
  }

  @Test
  public void getCsv() throws Exception {
    calculator.measure(cache);
    assertEquals(Arrays.asList(3.0, 0.7111542257564604, 0.0, 0.0, 0.0, 0.0), calculator.getCsv());
  }

  @Test
  public void getHeader() throws Exception {
    calculator.measure(cache);
    assertEquals(
      Arrays.asList(
        "dc_title_ss/count", "dc_title_ss/score", "dcterms_alternative_ss/count", "dcterms_alternative_ss/score",
        "dc_description_ss/count", "dc_description_ss/score"
      ),
      calculator.getHeader()
    );
  }

  @Test
  public void getSolrFields() throws Exception {
    assertEquals(4000, calculator.getSolrFields().get(0).getTotal());
    assertEquals(1000, calculator.getSolrFields().get(1).getTotal());
    assertEquals(2000, calculator.getSolrFields().get(2).getTotal());
  }

  public class TestInsideFacade {
    private CalculatorFacade facade;

    @Before
    public void setUp() {
      facade = new CalculatorFacade()
        .setSchema(schema)
        .setSolrClient(solrClient)
        .enableFieldExtractor()
        .disableFieldExistenceMeasurement()
        .disableCompletenessMeasurement()
        .disableFieldCardinalityMeasurement()
        .enableUniquenessMeasurement();

      facade.configure();
    }

    @Test
    public void getCalculatorName() throws Exception {
      assertEquals(2, facade.getCalculators().size());
      assertEquals("fieldExtractor", facade.getCalculators().get(0).getCalculatorName());
      assertEquals("uniqueness", facade.getCalculators().get(1).getCalculatorName());
    }

    @Test
    public void measure() throws Exception {
      facade.measure(jsonString);
      Map<String, ?> result = facade.getLabelledResults().get("uniqueness");
      assertEquals(3.000000, result.get("dc_title_ss/count"));
      assertEquals(
        "\"recordId\":92062/BibliographicResource_1000126015451,"
          + "\"dataset\":92062_Ag_EU_TEL_a0480_Austria,"
          + "\"dataProvider\":Österreichische Nationalbibliothek - Austrian National Library,"
          + "\"dc_title_ss/count\":3.000000,\"dc_title_ss/score\":0.711154,"
          + "\"dcterms_alternative_ss/count\":0.000000,\"dcterms_alternative_ss/score\":0.000000,"
          + "\"dc_description_ss/count\":0.000000,\"dc_description_ss/score\":0.000000",
        facade.getCsv(true, CompressionLevel.ZERO)
      );
    }

    @Test
    public void getSolrFields() throws Exception {
      UniquenessCalculator uniquenessCalculator =
          (UniquenessCalculator) facade.getCalculators().get(1);
      assertEquals(4000, uniquenessCalculator.getSolrFields().get(0).getTotal());
      assertEquals(1000, uniquenessCalculator.getSolrFields().get(1).getTotal());
      assertEquals(2000, uniquenessCalculator.getSolrFields().get(2).getTotal());
    }
  }
}
