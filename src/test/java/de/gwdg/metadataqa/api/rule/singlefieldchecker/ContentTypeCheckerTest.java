package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.CheckerTestBase;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ContentTypeCheckerTest extends CheckerTestBase {

  @Test
  public void prefix() {
    assertEquals("contentType", ContentTypeChecker.PREFIX);
  }

  @Test
  public void success() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "http://vb.uni-wuerzburg.de/ub/books/mchf91/folio-std/DE-20__M_ch_f_91__0003__0002r.jpg");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ContentTypeChecker checker = new ContentTypeChecker(schema.getPathByLabel("name"),
      Arrays.asList("image/jpeg", "image/png", "image/tiff", "image/tiff-fx", "image/gif", "image/svg+xml", "application/pdf"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:contentType", checker.getHeaderWithoutId());
    Assert.assertEquals(RuleCheckingOutputType.PASSED, fieldCounter.get(checker.getHeader()).getType());
  }

  @Test
  public void failure() {
    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "http://creativecommons.org/licenses/by-nc-sa/4.0/");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));

    ContentTypeChecker checker = new ContentTypeChecker(schema.getPathByLabel("name"),
      Arrays.asList("c", "d"));

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter);

    assertEquals(1, fieldCounter.size());
    assertEquals("name:contentType", checker.getHeaderWithoutId());
    assertEquals(RuleCheckingOutputType.FAILED, fieldCounter.get(checker.getHeader()).getType());
  }
}