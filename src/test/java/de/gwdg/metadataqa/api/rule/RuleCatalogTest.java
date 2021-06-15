package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RuleCatalogTest {

  Schema schema;

  @Before
  public void setUp() throws Exception {
    schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name")
      .addField("title")
      .addField("alt")
    ;
    schema.getPathByLabel("name").setRule(Arrays.asList(new Rule().withAnd(Arrays.asList(new Rule().withMinCount(1), new Rule().withMaxCount(1)))));
  }

  @Test
  public void measure() {
    CsvPathCache cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "a,b,c");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    RuleCatalog catalog = new RuleCatalog(schema);
    List<MetricResult> results = catalog.measure(cache);
    assertEquals("1,0", results.get(0).getCsv(false, CompressionLevel.ZERO));
  }

  @Test
  public void getHeader() {
    RuleCatalog catalog = new RuleCatalog(schema);
    assertEquals(List.of("rule:name:and:name:minCount:name:maxCount:3", "ruleCatalog:score"), catalog.getHeader());
  }

  @Test
  public void getCalculatorName() {
    RuleCatalog catalog = new RuleCatalog(schema);
    assertEquals("ruleCatalog", catalog.getCalculatorName());
  }
}