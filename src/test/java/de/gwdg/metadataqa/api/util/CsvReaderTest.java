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

  @Test
  public void testFromFile() {
    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    try {
      // Map<String, String> values = new CSVReaderHeaderAware(
      //  new FileReader(fileName)).readMap();
      // System.err.println(values.size());
      CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(fileName));
      Map<String,String> record = null;
      do {
        record = reader.readMap();
        if (record != null) {
          System.err.println(record.get("url"));
        }
      } while (record != null);
      /*
      CSVIterator iterator = new CSVIterator(
                                );
      CSVReader reader2 = new CSVReader(new FileReader("yourfile.csv"));
      while (iterator.hasNext()) {
        String[] nextLine = iterator.next();
        System.out.println(nextLine[0] + nextLine[1] + "etc...");
      }
       */
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCalculator() {
    CalculatorFacade facade = new CalculatorFacade();
    GoogleDatasetSchema schema = new GoogleDatasetSchema();
    facade.setSchema(schema);
    CsvReader csvReader = new CsvReader();
    csvReader.setHeader(schema.getHeader());
    facade.setCsvReader(csvReader);
    facade.enableCompletenessMeasurement(true);
    facade.configure();

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
      while (iterator.hasNext()) {
        String line = toCsv(iterator.next());
        String metrics = facade.measure(line);
        System.err.println(metrics);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  private String toCsv(String[] cells) throws IOException {
    StringWriter stringWriter = new StringWriter();
    CSVWriter csvWriter = new CSVWriter(stringWriter);
    csvWriter.writeNext(cells);
    csvWriter.close();
    return stringWriter.toString().trim();
  }

}
