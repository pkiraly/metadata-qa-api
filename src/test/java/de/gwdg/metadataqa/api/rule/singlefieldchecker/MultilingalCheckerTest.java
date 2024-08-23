package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.counter.FieldCounter;
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

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultilingalCheckerTest extends CheckerTestBase {
  private Selector cache;

  @Before
  public void setUp() throws Exception {
    schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/rules/multilingual/isMultilingual.yaml")
      .asSchema();
    cache = new JsonSelector(FileUtils
      .readFirstLineFromResource("configuration/schema/rules/multilingual/multilingual.json"));
  }

  @Test
  public void success() {
    MultilingualChecker checker = new MultilingualChecker(schema.getPathByLabel("description"));

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

}
