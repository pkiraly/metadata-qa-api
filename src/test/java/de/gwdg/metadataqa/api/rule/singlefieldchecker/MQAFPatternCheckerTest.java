package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.configuration.schema.MQAFPattern;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class MQAFPatternCheckerTest extends CheckerTestBase {
  @Test
  public void prefix() {
    assertEquals("pattern", PatternChecker.PREFIX);
  }

  @Test
  public void success() {
    MQAFPattern pattern = new MQAFPattern();
    pattern.setPattern(List.of("^a$"));
    assertEquals("^a$", pattern.getCompiledPattern().pattern());

    MQAFPatternChecker checker = new MQAFPatternChecker(schema.getPathByLabel("name"), pattern);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:pattern", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:pattern:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void success_multiple() {
    MQAFPattern pattern = new MQAFPattern();
    pattern.setPattern(List.of("^a$", "^b$", "^c$"));
    assertEquals("^a$|^b$|^c$", pattern.getCompiledPattern().pattern());

    MQAFPatternChecker checker = new MQAFPatternChecker(schema.getPathByLabel("name"), pattern);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:pattern", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:pattern:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    MQAFPattern pattern = new MQAFPattern();
    pattern.setPattern(List.of("^b$"));
    assertEquals("^b$", pattern.getCompiledPattern().pattern());

    MQAFPatternChecker checker = new MQAFPatternChecker(schema.getPathByLabel("name"), pattern);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:pattern", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:pattern:\\d+$").matcher(checker.getHeader()).matches());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void ascii() {
    List<String> list = Arrays.asList("a\u000b", "a ", "a\t", "a" + "\u000c", "a\f", "a\n", "a\u0007");
    for (String item : list) {
      ascii(item);
    }
  }

  private void ascii(String input) {
    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), input);
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    MQAFPattern pattern = new MQAFPattern();
    pattern.setPattern(List.of("^.*?[^\\p{Graph}].*?$"));

    MQAFPatternChecker checker = new MQAFPatternChecker(schema.getPathByLabel("name"), pattern);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:pattern", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^name:pattern:\\d+$").matcher(checker.getHeader()).matches());
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }
}