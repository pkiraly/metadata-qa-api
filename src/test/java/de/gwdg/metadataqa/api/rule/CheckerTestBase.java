package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.model.selector.SelectorFactory;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.CsvAwareSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Before;

public class CheckerTestBase {

  protected Schema schema;
  protected CsvSelector cache;

  @Before
  public void setUp() throws Exception {
    schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("name")
    ;

    cache = (CsvSelector) SelectorFactory.getInstance(schema.getFormat(), "a");
    cache.setCsvReader(new CsvReader().setHeader( ((CsvAwareSchema) schema).getHeader() ));
  }

}
