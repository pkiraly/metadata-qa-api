package de.gwdg.metadataqa.api.io.reader;

import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.io.IOTestBase;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XMLRecordReaderTest extends IOTestBase {

  String inputFile = "src/test/resources/xml/meemoo-simple.xml";
  RecordReader reader;

  @Before
  public void setUp() throws Exception {
    BufferedReader inputReader = Files.newBufferedReader(Paths.get(inputFile));
    reader = new XMLRecordReader(inputReader, getCalculator(Format.XML)).setRecordAddress("records/record");
  }

  @Test
  public void hasNext() {
    assertTrue(reader.hasNext());
  }

  @Test
  public void next() {
    Map<String, List<MetricResult>> result = reader.next();
    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
    assertEquals(1, result.size());
    List<MetricResult> fields = result.get("fieldExtractor");
    assertEquals(1, fields.size());
    assertEquals("https://neurovault.org/images/384958/", fields.get(0).getResultMap().get("record/url"));
    assertEquals("massivea uditory lexical decision", fields.get(0).getResultMap().get("record/name"));
  }

  protected Schema getSchema(Format format) {
    Schema schema = new BaseSchema()
      .setFormat(format)
      .addField(new DataElement("record/url").setExtractable())
      .addField(new DataElement("record/name").setExtractable());
    return schema;
  }
}