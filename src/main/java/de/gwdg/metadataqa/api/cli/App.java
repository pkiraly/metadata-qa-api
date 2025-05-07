package de.gwdg.metadataqa.api.cli;

import com.jayway.jsonpath.InvalidJsonException;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.io.reader.XMLRecordReader;
import de.gwdg.metadataqa.api.io.writer.ResultWriter;
import de.gwdg.metadataqa.api.configuration.ConfigurationReader;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.io.reader.RecordReader;
import de.gwdg.metadataqa.api.schema.Schema;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class App {

  private static final Logger logger = Logger.getLogger(App.class.getCanonicalName());

  private static final String appName = "mqa";
  private static final String appHeader = "Command-line application for the Metadata Quality API (https://github.com/pkiraly/metadata-qa-api). Read line-based metadata records and output quality assessment results using various metrics.";

  // constants
  public static final String NDJSON = "ndjson";
  public static final String CSVJSON = "csvjson";
  public static final String CSV = "csv";
  public static final String JSON = "json";
  public static final String YAML = "yaml";

  // Arguments
  private static final String INPUT_FILE = "input";
  private static final String INPUT_FORMAT = "inputFormat";
  private static final String OUTPUT_FILE = "output";
  private static final String OUTPUT_FORMAT = "outputFormat";
  private static final String SCHEMA_CONFIG = "schema";
  private static final String SCHEMA_FORMAT = "schemaFormat";
  private static final String MEASUREMENTS_CONFIG = "measurements";
  private static final String HEADERS_CONFIG = "headers";
  private static final String MEASUREMENTS_FORMAT = "measurementsFormat";
  private static final String GZIP_FLAG = "gzip";
  private static final String RECORD_ADDRESS = "recordAddress";

  private final Schema schema;
  private final CalculatorFacade calculator;
  private final ResultWriter outputWriter;
  private final RecordReader inputReader;
  private final String recordAddress;

  public App(CommandLine cmd) throws IOException, CsvValidationException {
    // initialize schema
    String schemaFile = cmd.getOptionValue(SCHEMA_CONFIG);
    String schemaFormat = cmd.getOptionValue(SCHEMA_FORMAT, FilenameUtils.getExtension(schemaFile));
    switch (schemaFormat) {
      case YAML:
        this.schema = ConfigurationReader.readSchemaYaml(schemaFile).asSchema();
        break;
      case JSON:
      default:
        this.schema = ConfigurationReader.readSchemaJson(schemaFile).asSchema();
    }

    // Set the fields supplied by the command line to extractable fields
    if (cmd.hasOption(HEADERS_CONFIG)) {
      String[] headers = cmd.getOptionValues(HEADERS_CONFIG);
      for (String h : headers) {
        this.schema.addExtractableField(h, this.schema.getPathByLabel(h).getPath());
      }
    }

    // initialize config
    MeasurementConfiguration measurementConfig = new MeasurementConfiguration();
    if (cmd.hasOption(MEASUREMENTS_CONFIG)) {
      String measurementFile = cmd.getOptionValue(MEASUREMENTS_CONFIG);
      String measurementFormat = cmd.getOptionValue(MEASUREMENTS_FORMAT, FilenameUtils.getExtension(measurementFile));
      switch (measurementFormat) {
        case YAML:
          measurementConfig = ConfigurationReader.readMeasurementYaml(measurementFile);
          break;
        case JSON:
        default:
          measurementConfig = ConfigurationReader.readMeasurementJson(measurementFile);
      }
    }

    // initialize calculator
    this.calculator = new CalculatorFacade(measurementConfig);
    // set the schema which describes the source
    calculator.setSchema(schema);

    // initialize input
    String inputFile = cmd.getOptionValue(INPUT_FILE);
    InputFormat inputFormat = InputFormat.byCode(cmd.getOptionValue(INPUT_FORMAT));
    this.inputReader = RecordFactory.getRecordReader(inputFile, calculator, cmd.hasOption(GZIP_FLAG), inputFormat);

    // initialize output
    String outFormat = cmd.getOptionValue(OUTPUT_FORMAT, NDJSON);
    // write to std out if no file was given
    this.outputWriter = cmd.hasOption(OUTPUT_FILE)
      ? RecordFactory.getResultWriter(outFormat, cmd.getOptionValue(OUTPUT_FILE))
      : RecordFactory.getResultWriter(outFormat);

    this.recordAddress = (cmd.hasOption(RECORD_ADDRESS) && StringUtils.isNotBlank(cmd.getOptionValue(RECORD_ADDRESS)))
      ? cmd.getOptionValue(RECORD_ADDRESS)
      : null;
    if (inputReader instanceof XMLRecordReader && recordAddress != null)
      ((XMLRecordReader)inputReader).setRecordAddress(this.recordAddress);
  }

  public static void main(String[] args) {

    // Take input file
    final Options options = buildOptions();

    // create the parser
    CommandLineParser parser = new DefaultParser();

    // create the formatter
    HelpFormatter formatter = new HelpFormatter();

    try {
      // parse the command line arguments
      CommandLine cmd = parser.parse(options, args);
      new App(cmd).run();
    } catch (MissingOptionException ex) {
      List<String> missingOptions = new ArrayList<>();
      for (String option : (List<String>) ex.getMissingOptions()) {
        missingOptions.add(String.format("--%s (-%s)", options.getOption(option).getLongOpt(), option));
      }
      formatter.printHelp(appName, appHeader, options, "ERROR\nMissing options: \n  " + StringUtils.join(missingOptions, "\n  "), true);
      System.exit(1);
    } catch (MissingArgumentException ex) {
      Option missingOption = ex.getOption();
      String err = String.format("%s for option --%s (-%s)", missingOption.getArgName(), missingOption.getLongOpt(), missingOption.getOpt());
      formatter.printHelp(appName, appHeader, options, "ERROR\nMissing arguments: " + err, true);
      System.exit(1);
    } catch (Exception ex) {
      formatter.printHelp(appName, appHeader, options, "Error: " + ex.getMessage(), true);
      ex.printStackTrace();
      System.exit(1);
    }
  }

  private static Options buildOptions() {
    final Options options = new Options();

    Option inputOption = Option.builder("i")
      .numberOfArgs(1)
      .argName("file")
      .required(true)
      .longOpt(INPUT_FILE)
      .desc("Input file.")
      .build();

    Option inputFormatOption = Option.builder("n")
      .numberOfArgs(1)
      .argName("inputFormat")
      .required(false)
      .longOpt(INPUT_FORMAT)
      .desc("Format of the input: json, ndjson (new line delimited JSON), json-array (JSON file that contains an array of objects). Default: ndjson.")
      .build();

    Option outputOption = Option.builder("o")
      .numberOfArgs(1)
      .argName("file")
      .required(false)
      .longOpt(OUTPUT_FILE)
      .desc("Output file.")
      .build();

    Option outputFormatOption = Option.builder("f")
      .numberOfArgs(1)
      .argName("format")
      .required(false)
      .longOpt(OUTPUT_FORMAT)
      .desc("Format of the output: json, ndjson (new line delimited JSON), csv, csvjson (json encoded in csv; useful for RDB bulk loading). Default: ndjson.")
      .build();

    Option schemaConfigOption = Option.builder("s")
      .numberOfArgs(1)
      .argName("file")
      .required(true)
      .longOpt(SCHEMA_CONFIG)
      .desc("Schema file to run assessment against.")
      .build();

    Option schemaFormatOption = Option.builder("v")
      .numberOfArgs(1)
      .argName("format")
      .required(false)
      .longOpt(SCHEMA_FORMAT)
      .desc("Format of schema file: json, yaml. Default: based on file extension, else json.")
      .build();

    Option measurementsConfigOption = Option.builder("m")
      .numberOfArgs(1)
      .argName("file")
      .required(true)
      .longOpt(MEASUREMENTS_CONFIG)
      .desc("Configuration file for measurements.")
      .build();

    Option measurementsFormatOption = Option.builder("w")
      .numberOfArgs(1)
      .argName("format")
      .required(false)
      .longOpt(MEASUREMENTS_FORMAT)
      .desc("Format of measurements config file: json, yaml. Default: based on file extension, else json.")
      .build();

    Option headersOption = Option.builder("h")
      .hasArgs()
      .required(false)
      .longOpt(HEADERS_CONFIG)
      .desc("Headers to copy from source")
      .build();

    Option gzipOption = Option.builder("z")
      .numberOfArgs(0)
      .required(false)
      .longOpt(GZIP_FLAG)
      .desc("Flag to indicate that input is gzipped.")
      .build();

    Option recordAddressOption = Option.builder("r")
      .numberOfArgs(1)
      .argName("path")
      .required(false)
      .longOpt(RECORD_ADDRESS)
      .desc("An XPath or JSONPath expression to separate individual records in an XML or JSON files.")
      .build();

    options.addOption(inputOption);
    options.addOption(inputFormatOption);
    options.addOption(outputOption);
    options.addOption(outputFormatOption);
    options.addOption(schemaConfigOption);
    options.addOption(schemaFormatOption);
    options.addOption(measurementsConfigOption);
    options.addOption(measurementsFormatOption);
    options.addOption(headersOption);
    options.addOption(gzipOption);
    options.addOption(recordAddressOption);
    return options;
  }

  private void run() {
    long counter = 0;
    try {
      // print header
      List<String> header = calculator.getHeader();
      outputWriter.writeHeader(header);

      while (inputReader.hasNext()) {
        Map<String, List<MetricResult>> measurement = inputReader.next();
        outputWriter.writeResult(measurement);

        // update process
        counter++;
        if (counter % 50 == 0) {
          logger.info(String.format("Processed %s records. ", counter));
        }
      }
      logger.info(String.format("Assessment completed successfully with %s records. ", counter));
      outputWriter.close();
    } catch (InvalidJsonException | IOException e) {
      logger.severe(String.format("Assessment failed with %s records. ", counter));
      logger.severe(e.getMessage());
      e.printStackTrace();
    }
  }
}
