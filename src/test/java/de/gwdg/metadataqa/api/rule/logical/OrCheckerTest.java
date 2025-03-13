package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinLengthChecker;
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
  protected CsvSelector cache;

  @Before
  public void setUp() throws Exception {
    schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name")
      .addField("title")
      .addField("alt")
    ;
    schema.getPathByLabel("name").setRule(Arrays.asList(new Rule().withOr(Arrays.asList(new Rule().withMinCount(1), new Rule().withMaxCount(1)))));

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "a,b,a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void header() {
    assertEquals("name:or:name:minCount:name:maxCount", schema.getRuleCheckers().get(0).getHeaderWithoutId());
  }

  @Test
  public void success() {
    List<RuleChecker> checkers = schema.getRuleCheckers();
    OrChecker orChecker = (OrChecker) checkers.get(0);
    assertEquals(2, orChecker.getCheckers().size());

    assertEquals(MinCountChecker.class, orChecker.getCheckers().get(0).getClass());
    MinCountChecker minCountChecker = (MinCountChecker) orChecker.getCheckers().get(0);

    assertEquals(MaxCountChecker.class, orChecker.getCheckers().get(1).getClass());
    MaxCountChecker maxCountChecker = (MaxCountChecker) orChecker.getCheckers().get(1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    orChecker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(orChecker.getIdOrHeader(RuleCheckingOutputType.STATUS)).getStatus());

    fieldCounter = new FieldCounter<>();
    orChecker.update(cache, fieldCounter, RuleCheckingOutputType.STATUS);
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(orChecker.getIdOrHeader()).getStatus());
  }

  @Test
  public void failure() {
    schema.getPathByLabel("name").setRule(Arrays.asList(new Rule().withOr(Arrays.asList(new Rule().withMinCount(2), new Rule().withMinLength(10)))));

    List<RuleChecker> checkers = schema.getRuleCheckers();
    OrChecker orChecker = (OrChecker) checkers.get(0);
    assertEquals(2, orChecker.getCheckers().size());

    assertEquals(MinCountChecker.class, orChecker.getCheckers().get(0).getClass());
    MinCountChecker minCountChecker = (MinCountChecker) orChecker.getCheckers().get(0);

    assertEquals(MinLengthChecker.class, orChecker.getCheckers().get(1).getClass());
    MinLengthChecker minLengthChecker = (MinLengthChecker) orChecker.getCheckers().get(1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    orChecker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(orChecker.getIdOrHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }
}