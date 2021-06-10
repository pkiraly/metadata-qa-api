package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrCheckerTest {

  protected Schema schema;
  protected CsvPathCache cache;

  @Before
  public void setUp() throws Exception {
    schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name")
      .addField("title")
      .addField("alt")
    ;
    schema.getPathByLabel("name").setRule(Arrays.asList(new Rule().withOr(Arrays.asList(new Rule().withMinCount(1), new Rule().withMaxCount(1)))));

    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "a,b,a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void update() {
    List<RuleChecker> checkers = schema.getRuleCheckers();
    OrChecker orChecker = (OrChecker) checkers.get(0);
    assertEquals(2, orChecker.getCheckers().size());

    assertEquals(MinCountChecker.class, orChecker.getCheckers().get(0).getClass());
    MinCountChecker minCountChecker = (MinCountChecker) orChecker.getCheckers().get(0);

    assertEquals(MaxCountChecker.class, orChecker.getCheckers().get(1).getClass());
    MaxCountChecker maxCountChecker = (MaxCountChecker) orChecker.getCheckers().get(1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    orChecker.update(cache, fieldCounter);

    assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get("or:name").getType());
  }
}