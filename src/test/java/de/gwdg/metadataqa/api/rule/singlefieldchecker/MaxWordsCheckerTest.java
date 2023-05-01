package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxWordsCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("maxWords", MaxWordsChecker.PREFIX);
  }

  @Test
  public void success() {
    MaxWordsChecker checker = new MaxWordsChecker(schema.getPathByLabel("name"), 1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxWords", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "one two three");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    MaxWordsChecker checker = new MaxWordsChecker(schema.getPathByLabel("name"), 2);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:maxWords", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }
}