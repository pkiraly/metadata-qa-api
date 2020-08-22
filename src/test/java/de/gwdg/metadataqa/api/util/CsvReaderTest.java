package de.gwdg.metadataqa.api.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CsvReaderTest {

  @Test
  public void testExternalLibraryAPI() {
    String[] header = new String[]{"name", "age"};
    CSVParser parser = new CSVParser();
    String[] columns = null;
    try {
      columns = parser.parseLine("Jim,64");
    } catch (IOException e) {
      //
    }
    assertEquals(header.length, columns.length);
    assertEquals(2, columns.length);
  }

  @Test
  public void testAsArray() {
    CsvReader reader = new CsvReader();
    String[] columns = null;
    try {
      columns = reader.asArray("Jim,64");
    } catch (IOException e) {
      //
    }
    assertNotNull(columns);
    assertEquals(2, columns.length);
  }

  @Test
  public void testAsMap_noHeader() {
    CsvReader reader = new CsvReader();
    Map<String, String> record = null;
    try {
      record = reader.asMap("Jim,64");
    } catch (IOException e) {
      //
    }
    assertNotNull(record);
    assertEquals(0, record.size());
  }

  @Test
  public void testAsMap_withHeader_asArray() {
    CsvReader reader = new CsvReader();
    reader.setHeader(new String[]{"name", "age"});
    Map<String, String> record = null;
    try {
      record = reader.asMap("Jim,64");
    } catch (IOException e) {
      //
    }
    assertNotNull(record);
    assertEquals(2, record.size());
    assertEquals("Jim", record.get("name"));
    assertEquals("64", record.get("age"));
  }

  @Test
  public void testAsMap_withHeader_asList() {
    CsvReader reader = new CsvReader();
    reader.setHeader(Arrays.asList("name", "age"));
    Map<String, String> record = null;
    try {
      record = reader.asMap("Jim,64");
    } catch (IOException e) {
      //
    }
    assertNotNull(record);
    assertEquals(2, record.size());
    assertEquals("Jim", record.get("name"));
    assertEquals("64", record.get("age"));
  }

  @Test
  public void testHeaders_asArray() {
    CsvReader reader = new CsvReader();
    reader.setHeader(new String[]{"name", "age"});
    assertEquals(2, reader.getHeader().size());
    assertEquals("name", reader.getHeader().get(0));
    assertEquals("age", reader.getHeader().get(1));
  }

  @Test
  public void testHeaders_asList() {
    CsvReader reader = new CsvReader();
    reader.setHeader(Arrays.asList("name", "age"));
    assertEquals(2, reader.getHeader().size());
    assertEquals("name", reader.getHeader().get(0));
    assertEquals("age", reader.getHeader().get(1));
  }

  @Test
  public void testAsMap_createWithParser() {
    CsvReader reader = new CsvReader(
      new CSVParserBuilder()
         .withSeparator(',')
         .withQuoteChar('"')
         .withEscapeChar('\\')
         .build());
    Map<String, String> record = null;
    try {
      record = reader.asMap("Jim,64");
    } catch (IOException e) {
      //
    }
    assertNotNull(record);
    assertEquals(0, record.size());
  }
}
