package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.schema.ApplicationScope;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LanguageTagCheckerTest extends CheckerTestBase {
  private Selector cache;

  @Before
  public void setUp() throws Exception {
    schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/rules/languageTag/hasLanguageTag.yaml")
      .asSchema();
  }

  @Test
  public void allOf_all() {
    setCache("multilingual-all.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void allOf_one() {
    setCache("multilingual-one.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void allOf_none() {
    setCache("multilingual-none.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.FAILED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void oneOf_one() {
    setCache("multilingual-one.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"))
      .withScope(ApplicationScope.oneOf);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void oneOf_all() {
    setCache("multilingual-all.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"))
      .withScope(ApplicationScope.oneOf);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.FAILED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void oneOf_none_fails() {
    setCache("multilingual-none.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"))
      .withScope(ApplicationScope.oneOf);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.FAILED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void anyOf_one_passes() {
    setCache("multilingual-one.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"))
      .withScope(ApplicationScope.anyOf);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void anyOf_all_passed() {
    setCache("multilingual-all.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"))
      .withScope(ApplicationScope.anyOf);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.PASSED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  @Test
  public void anyOf_none_failed() {
    setCache("multilingual-none.json");
    LanguageTagChecker checker = new LanguageTagChecker(schema.getPathByLabel("description"))
      .withScope(ApplicationScope.anyOf);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("description", checker.getHeaderWithoutId());
    assertTrue(Pattern.compile("^description:\\d+$").matcher(checker.getHeader()).matches());
    Assert.assertEquals(
      RuleCheckingOutputStatus.FAILED,
      fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus()
    );
  }

  private void setCache(String filename) {
    try {
      cache = new JsonSelector(FileUtils.readFirstLineFromResource("configuration/schema/rules/languageTag/" + filename));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
