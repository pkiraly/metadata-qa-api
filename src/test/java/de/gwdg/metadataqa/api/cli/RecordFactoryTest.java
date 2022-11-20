package de.gwdg.metadataqa.api.cli;

import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.io.reader.RecordReader;
import de.gwdg.metadataqa.api.io.writer.ResultWriter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.schema.BaseSchema;
import de.gwdg.metadataqa.api.schema.Format;
import de.gwdg.metadataqa.api.schema.Schema;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordFactoryTest {
  private final static String BASE_DIR = "src/test/resources";
  private final static String OUTPUT_DIR = BASE_DIR + "/output";

  @Test
  public void getRecordReader_csv() throws IOException, CsvValidationException {
    RecordReader reader = RecordFactory.getRecordReader(BASE_DIR + "/csv/meemoo-simple.csv", getCalculator(Format.CSV), false);
    assertNotNull(reader);
    assertEquals("CSVRecordReader", reader.getClass().getSimpleName());
  }

  @Test
  public void getRecordReader_csv_gz() throws IOException, CsvValidationException {
    RecordReader reader = RecordFactory.getRecordReader(BASE_DIR + "/csv/meemoo-simple.csv.gz", getCalculator(Format.CSV), true);
    assertNotNull(reader);
    assertEquals("CSVRecordReader", reader.getClass().getSimpleName());
    Map<String, List<MetricResult>> result = reader.next();
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  public void getRecordReader_json() throws IOException, CsvValidationException {
    RecordReader reader = RecordFactory.getRecordReader(BASE_DIR + "/json/meemoo-simple.ndjson", getCalculator(Format.JSON), false);
    assertNotNull(reader);
    assertEquals("JSONRecordReader", reader.getClass().getSimpleName());
  }

  @Test
  public void getRecordReader_xml() throws IOException, CsvValidationException {
    RecordReader reader = RecordFactory.getRecordReader(BASE_DIR + "/xml/meemoo-simple.xml", getCalculator(Format.XML), false);
    assertNotNull(reader);
    assertEquals("XMLRecordReader", reader.getClass().getSimpleName());
  }

  @Test
  public void getResultWriter_csv_stdout() throws IOException {
    ResultWriter writer = RecordFactory.getResultWriter("csv");
    assertNotNull(writer);
    assertEquals("CSVResultWriter", writer.getClass().getSimpleName());
  }

  @Test
  public void getResultWriter_csv_file() throws IOException {
    ResultWriter writer = RecordFactory.getResultWriter("csv", OUTPUT_DIR + "/output.csv");
    assertNotNull(writer);
    assertEquals("CSVResultWriter", writer.getClass().getSimpleName());
  }

  @Test
  public void getResultWriter_json_stdout() throws IOException {
    ResultWriter writer = RecordFactory.getResultWriter("ndjson");
    assertNotNull(writer);
    assertEquals("JSONResultWriter", writer.getClass().getSimpleName());
  }

  @Test
  public void getResultWriter_json_file() throws IOException {
    ResultWriter writer = RecordFactory.getResultWriter("ndjson", OUTPUT_DIR + "/output.json");
    assertNotNull(writer);
    assertEquals("JSONResultWriter", writer.getClass().getSimpleName());
  }

  @Test
  public void getResultWriter_csvjson_stdout() throws IOException {
    ResultWriter writer = RecordFactory.getResultWriter("csvjson");
    assertNotNull(writer);
    assertEquals("CSVJSONResultWriter", writer.getClass().getSimpleName());
  }

  @Test
  public void getResultWriter_csvjson_file() throws IOException {
    ResultWriter writer = RecordFactory.getResultWriter("csvjson", OUTPUT_DIR + "/output.csvjson");
    assertNotNull(writer);
    assertEquals("CSVJSONResultWriter", writer.getClass().getSimpleName());
  }

  private CalculatorFacade getCalculator(Format format) {
    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableFieldExtractor()
      .disableCompletenessMeasurement();

    return new CalculatorFacade(config).setSchema(getSchema(format));
  }

  private Schema getSchema(Format format) {
    return new BaseSchema()
      .setFormat(format)
      .addField(new JsonBranch("url").setExtractable())
      .addField(new JsonBranch("name").setExtractable());
  }
}