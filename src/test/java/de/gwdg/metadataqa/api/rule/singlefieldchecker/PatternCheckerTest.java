package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatternCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("pattern", PatternChecker.PREFIX);
  }

  @Test
  public void success() {
    PatternChecker checker = new PatternChecker(schema.getPathByLabel("name"), "^a$");

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:pattern", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:pattern:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void failure() {
    PatternChecker checker = new PatternChecker(schema.getPathByLabel("name"), "^b$");

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:pattern", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:pattern:\\d+$").matcher(checker.getHeader()).matches());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get(checker.getHeader()).getType());
  }
}