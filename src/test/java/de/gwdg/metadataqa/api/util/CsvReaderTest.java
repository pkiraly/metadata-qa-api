package de.gwdg.metadataqa.api.util;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.schema.GoogleDatasetSchema;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

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

  @Test(expected = IllegalArgumentException.class)
  public void testAsMap_noHeader() {
    CsvReader reader = new CsvReader();
    Map<String, String> record = null;
    try {
      record = reader.asMap("Jim,64");
    } catch (IOException e) {
      //
    } catch (IllegalArgumentException e) {
      assertEquals("The size of columns are different than the size of headers", e.getMessage());
      throw e;
    }
    fail("This point should not be accessed");
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
  public void testAsMap_setHeaderString() {
    CsvReader reader = new CsvReader();
    Map<String, String> record = null;
    try {
      reader.setHeader("name,age");
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
    reader.setHeader(Arrays.asList("name", "age"));
    Map<String, String> record = null;
    try {
      record = reader.asMap("Jim,64");
    } catch (IOException e) {
      //
    }
    assertNotNull(record);
    assertEquals(2, record.size());
  }

  @Test
  public void testFromFile() {
    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";

    try {
      CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(fileName));
      Map<String,String> record = null;
      do {
        record = reader.readMap();
        if (record != null) {
          // System.err.println(record.get("url"));
        }
      } while (record != null);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCalculator() {
    GoogleDatasetSchema schema = new GoogleDatasetSchema();

    CalculatorFacade facade = new CalculatorFacade()
      .setSchema(schema)
      .setCsvReader(new CsvReader().setHeader(schema.getHeader()))
      .enableCompletenessMeasurement();

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(file)));
      StringBuffer result = new StringBuffer();
      while (iterator.hasNext()) {
        String line = CsvReader.toCsv(iterator.next());
        String metrics = facade.measure(line);
        result.append(metrics + "\n");
      }
      String expected = "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.411765,1.0,1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0,1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0\n"
        + "0.352941,1.0,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0\n"
        + "0.294118,1.0,1,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,0\n";
      assertEquals(expected, result.toString());

    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsHeaderAware() {
    CsvReader reader1 = new CsvReader();
    assertFalse(reader1.isHeaderAware());

    CsvReader reader2 = new CsvReader().setHeaderAware(false);
    assertFalse(reader2.isHeaderAware());

    CsvReader reader3 = new CsvReader().setHeaderAware(false);
    assertFalse(reader3.isHeaderAware());
  }
}
