package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HasValueCheckerTest {

  @Test
  public void passed() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name");

    CsvPathCache cache = (CsvPathCache) PathCacheFactory.getInstance(Format.CSV, "a");
    cache.setCsvReader(
      new CsvReader()
        .setHeader(((CsvAwareSchema) schema).getHeader())
    );

    HasValueChecker checker = new HasValueChecker(schema.getPathByLabel("name"), "a");

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals(RuleCheckingOutput.PASSED, fieldCounter.get("hasValue:name"));
  }

  @Test
  public void failed() {
    Schema schema = new BaseSchema().setFormat(Format.CSV).addField("name");

    HasValueChecker checker = new HasValueChecker(schema.getPathByLabel("name"), "b");

    CsvPathCache cache = (CsvPathCache) PathCacheFactory.getInstance(Format.CSV, "a");
    cache.setCsvReader(new CsvReader().setHeader(((CsvAwareSchema) schema).getHeader()));

    FieldCounter fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals(RuleCheckingOutput.FAILED, fieldCounter.get("hasValue:name"));
  }

}
