package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.PathCacheFactory;
import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.SolrClientMock;
import de.gwdg.metadataqa.api.uniqueness.SolrConfiguration;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class UniquenessCheckerTest {

  CsvPathCache cache;
  SolrClientMock solrClient;
  Schema schema;

  @Before
  public void setUp() throws Exception {
    SolrConfiguration solrConfiguration = new SolrConfiguration("localhost", "8983", "solr");
    schema = getSchema(Format.CSV);
    solrClient = new SolrClientMock(solrConfiguration);

    cache = (CsvPathCache) PathCacheFactory.getInstance(schema.getFormat(), "URL,two three");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));
  }

  @Test
  public void success() {
    UniquenessChecker checker = new UniquenessChecker(schema.getPathByLabel("name"));
    checker.setSolrClient(solrClient);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("name:uniqueness", checker.getHeaderWithoutId());
    assertEquals("name:uniqueness:0", checker.getHeader());
    assertTrue(Pattern.compile("^name:uniqueness:\\d+$").matcher(checker.getHeader()).matches());
    assertEquals(RuleCheckingOutputStatus.PASSED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  @Test
  public void failure() {
    UniquenessChecker checker = new UniquenessChecker(schema.getPathByLabel("url"));
    checker.setSolrClient(solrClient);

    FieldCounter<RuleCheckerOutput> fieldCounter = new FieldCounter<>();
    checker.update(cache, fieldCounter, RuleCheckingOutputType.BOTH);

    assertEquals(2, fieldCounter.size());
    assertEquals("url:uniqueness", checker.getHeaderWithoutId());
    assertEquals("url:uniqueness:0", checker.getHeader());
    assertTrue(Pattern.compile("^url:uniqueness:\\d+$").matcher(checker.getHeader()).matches());
    assertEquals(RuleCheckingOutputStatus.FAILED, fieldCounter.get(checker.getHeader(RuleCheckingOutputType.STATUS)).getStatus());
  }

  private Schema getSchema(Format format) {
    BaseSchema schema = new BaseSchema()
      .setFormat(format)
      .addField(new DataElement("url").setExtractable().setIndexField("url"))
      .addField(new DataElement("name").setExtractable().setIndexField("name"));
    schema.setRecordId(schema.getPathByLabel("url"));
    return schema;
  }

}