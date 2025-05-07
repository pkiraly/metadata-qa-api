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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AndCheckerTest {

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
    schema.getPathByLabel("name")
      .setRule(Arrays.asList(
        new Rule().withAnd(Arrays.asList(
          new Rule().withMinCount(1),
          new Rule().withMaxCount(1)))
          .withNaScore(-1)
      ));
    schema.getPathByLabel("name").getRules().get(0).setDebug(true);

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "a,b,a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));
  }

  @Test
  public void header() {
    assertEquals("name:and:name:minCount:name:maxCount", schema.getRuleCheckers().get(0).getHeaderWithoutId());
  }

  @Test
  public void update() {
    List<RuleChecker> checkers = schema.getRuleCheckers();
    AndChecker andChecker = (AndChecker) checkers.get(0);
    assertEquals(2, andChecker.getCheckers().size());
    assertEquals(-1, (int) andChecker.getNaScore());
    assertTrue(andChecker.isDebug());

    assertEquals(MinCountChecker.class, andChecker.getCheckers().get(0).getClass());
    MinCountChecker minCountChecker = (MinCountChecker) andChecker.getCheckers().get(0);
    assertNull(minCountChecker.getNaScore());
    assertTrue(minCountChecker.isDebug());

    assertEquals(MaxCountChecker.class, andChecker.getCheckers().get(1).getClass());
    MaxCountChecker maxCountChecker = (MaxCountChecker) andChecker.getCheckers().get(1);
    assertNull(maxCountChecker.getNaScore());
    assertTrue(maxCountChecker.isDebug());

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    andChecker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(andChecker.getIdOrHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    schema.getPathByLabel("name").setRule(Arrays.asList(new Rule().withAnd(Arrays.asList(new Rule().withMinCount(1), new Rule().withMinLength(10)))));

    List<RuleChecker> checkers = schema.getRuleCheckers();
    AndChecker andChecker = (AndChecker) checkers.get(0);
    assertEquals(2, andChecker.getCheckers().size());

    assertEquals(MinCountChecker.class, andChecker.getCheckers().get(0).getClass());
    MinCountChecker minCountChecker = (MinCountChecker) andChecker.getCheckers().get(0);

    assertEquals(MinLengthChecker.class, andChecker.getCheckers().get(1).getClass());
    MinLengthChecker maxCountChecker = (MinLengthChecker) andChecker.getCheckers().get(1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    andChecker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(andChecker.getIdOrHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void withDependency() {
    schema.getPathByLabel("name")
      .setRule(Arrays.asList(
        new Rule().withAnd(Arrays.asList(
            new Rule().withMinCount(1),
            new Rule().withMaxCount(1)))
          .withNaScore(-1)
          .withId("Q1"),
        new Rule().withAnd(Arrays.asList(
          new Rule().withDependencies(List.of("Q1")),
          new Rule().withHasValue("a")))
          .withId("Q2")
      ));

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), ",b,a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    for (RuleChecker checker : schema.getRuleCheckers()) {
      checker.update(cache, fieldCounter, RuleCheckingOutputType.STATUS);
    }
    System.err.println(fieldCounter);
    // FieldCounter{fieldMap={
    //   name:and:name:minCount:name:maxCount:Q1:status=0,
    //   name:and:name:minCount:name:maxCount:Q1:score=0,
    //   name:and:name:dependency:name:hasValue:Q2:status=0,
    //   name:and:name:dependency:name:hasValue:Q2:score=0
    // }}

  }
}