package de.gwdg.metadataqa.api.cli;

import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.io.reader.CSVRecordReader;
import de.gwdg.metadataqa.api.io.reader.JSONArrayRecordReader;
import de.gwdg.metadataqa.api.io.reader.JSONRecordReader;
import de.gwdg.metadataqa.api.io.reader.RecordReader;
import de.gwdg.metadataqa.api.io.reader.XMLRecordReader;
import de.gwdg.metadataqa.api.io.writer.CSVJSONResultWriter;
import de.gwdg.metadataqa.api.io.writer.CSVResultWriter;
import de.gwdg.metadataqa.api.io.writer.JSONResultWriter;
import de.gwdg.metadataqa.api.io.writer.ResultWriter;
import de.gwdg.metadataqa.api.schema.Schema;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class RecordFactory {

  private RecordFactory() {
  }

  public static RecordReader getRecordReader(String inputFile,
                                             CalculatorFacade calculator,
                                             boolean gzip) throws CsvValidationException, IOException {
    return getRecordReader(inputFile, calculator, gzip, null);
  }

  public static RecordReader getRecordReader(String inputFile,
                                             CalculatorFacade calculator,
                                             boolean gzip,
                                             InputFormat inputFormat)
      throws CsvValidationException, IOException {
    final Schema schema = calculator.getSchema();

    BufferedReader inputReader = null;
    if (gzip) {
      FileInputStream fis = new FileInputStream(inputFile);
      GZIPInputStream gis = new GZIPInputStream(fis);
      InputStreamReader inputStreamReader = new InputStreamReader(gis, StandardCharsets.UTF_8);
      inputReader = new BufferedReader(inputStreamReader);
    } else {
      Path inputPath = Paths.get(inputFile);
      inputReader = Files.newBufferedReader(inputPath);
    }

    switch (schema.getFormat()) {
      case JSON:
        if (inputFormat == null || inputFormat.equals(InputFormat.NDJSON))
          return new JSONRecordReader(inputReader, calculator);
        else if (inputFormat.equals(InputFormat.JSON_ARRAY))
          return new JSONArrayRecordReader(inputReader, calculator);
        else
          throw new IllegalArgumentException("Unsupported input format: " + inputFormat);
      case XML:
        return new XMLRecordReader(inputReader, calculator);
      case CSV:
      default:
        return new CSVRecordReader(inputReader, calculator);
    }
  }

  public static ResultWriter getResultWriter(String outputFormat, String outputFile) throws IOException {

    if (outputFormat == null) {
      outputFormat = FilenameUtils.getExtension(outputFile);
    }

    switch (outputFormat) {
      case App.JSON:
      case App.NDJSON:
        return new JSONResultWriter(outputFile);
      case App.CSVJSON:
        return new CSVJSONResultWriter(outputFile);
      case App.CSV:
      default:
        return new CSVResultWriter(outputFile);
    }
  }

  public static ResultWriter getResultWriter(String outputFormat) {
    switch (outputFormat) {
      case App.NDJSON:
        return new JSONResultWriter();
      case App.CSVJSON:
        return new CSVJSONResultWriter();
      case App.CSV:
      default:
        return new CSVResultWriter();
    }
  }
}
