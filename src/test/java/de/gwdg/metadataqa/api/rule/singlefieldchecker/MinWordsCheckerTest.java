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

import static org.junit.Assert.*;

public class MinWordsCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("minWords", MinWordsChecker.PREFIX);
  }

  @Test
  public void success() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "one two three");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    MinWordsChecker checker = new MinWordsChecker(schema.getPathByLabel("name"), 1);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:minWords", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "one two three");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    MinWordsChecker checker = new MinWordsChecker(schema.getPathByLabel("name"), 4);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:minWords", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }
}