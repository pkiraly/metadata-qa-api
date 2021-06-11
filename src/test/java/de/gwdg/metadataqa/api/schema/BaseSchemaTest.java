package de.gwdg.metadataqa.api.schema;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import de.gwdg.metadataqa.api.configuration.*;
import de.gwdg.metadataqa.api.configuration.schema.Rule;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.HasValueChecker;
import de.gwdg.metadataqa.api.rule.RuleCatalog;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class BaseSchemaTest {

  @Test
  public void testConstructor() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
      .addField(new JsonBranch("description"))
      .addField(new JsonBranch("variablesMeasured"))
      .addField(new JsonBranch("measurementTechnique"))
      .addField(new JsonBranch("sameAs"))
      .addField(new JsonBranch("doi"))
      .addField(new JsonBranch("identifier"))
      .addField(new JsonBranch("author"))
      .addField(new JsonBranch("isAccessibleForFree"))
      .addField(new JsonBranch("dateModified"))
      .addField(new JsonBranch("distribution"))
      .addField(new JsonBranch("spatialCoverage"))
      .addField(new JsonBranch("provider"))
      .addField(new JsonBranch("funder"))
      .addField(new JsonBranch("temporalCoverage"));

    assertEquals(
      Arrays.asList("url", "name", "alternateName", "description", "variablesMeasured",
        "measurementTechnique", "sameAs", "doi", "identifier", "author", "isAccessibleForFree",
        "dateModified", "distribution", "spatialCoverage", "provider", "funder", "temporalCoverage"
      ),
      ((BaseSchema) schema).getHeader()
    );

    Map<String, String> extractableFields = new HashMap<>();
    extractableFields.put("url", "url");
    assertEquals(extractableFields, schema.getExtractableFields());

    assertEquals("url", schema.getPaths().get(0).getLabel());
    assertTrue(schema.getPaths().get(0).isMandatory());
    assertFalse(schema.getPaths().get(1).isMandatory());
  }

  @Test
  public void testCalculation() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
      .addField(new JsonBranch("description"))
      .addField(new JsonBranch("variablesMeasured"))
      .addField(new JsonBranch("measurementTechnique"))
      .addField(new JsonBranch("sameAs"))
      .addField(new JsonBranch("doi"))
      .addField(new JsonBranch("identifier"))
      .addField(new JsonBranch("author"))
      .addField(new JsonBranch("isAccessibleForFree"))
      .addField(new JsonBranch("dateModified"))
      .addField(new JsonBranch("distribution"))
      .addField(new JsonBranch("spatialCoverage"))
      .addField(new JsonBranch("provider"))
      .addField(new JsonBranch("funder"))
      .addField(new JsonBranch("temporalCoverage"));

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableCompletenessMeasurement()
      .enableFieldCardinalityMeasurement();
    CalculatorFacade facade = new CalculatorFacade(config)
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()));
    // facade.configure();

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName", "existence:description",
        "existence:variablesMeasured", "existence:measurementTechnique", "existence:sameAs",
        "existence:doi", "existence:identifier", "existence:author", "existence:isAccessibleForFree",
        "existence:dateModified", "existence:distribution", "existence:spatialCoverage",
        "existence:provider", "existence:funder", "existence:temporalCoverage",
        "cardinality:url", "cardinality:name", "cardinality:alternateName",
        "cardinality:description", "cardinality:variablesMeasured",
        "cardinality:measurementTechnique", "cardinality:sameAs", "cardinality:doi",
        "cardinality:identifier", "cardinality:author", "cardinality:isAccessibleForFree",
        "cardinality:dateModified", "cardinality:distribution", "cardinality:spatialCoverage",
        "cardinality:provider", "cardinality:funder", "cardinality:temporalCoverage"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
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
  public void testCalculation_noHeader_nonexistent() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
      .addField(new JsonBranch("description"))
      .addField(new JsonBranch("variablesMeasured"))
      .addField(new JsonBranch("measurementTechnique"))
      .addField(new JsonBranch("sameAs"))
      .addField(new JsonBranch("doi"))
      .addField(new JsonBranch("identifier"))
      .addField(new JsonBranch("author"))
      .addField(new JsonBranch("isAccessibleForFree"))
      .addField(new JsonBranch("dateModified"))
      .addField(new JsonBranch("distribution"))
      .addField(new JsonBranch("spatialCoverage"))
      .addField(new JsonBranch("provider"))
      .addField(new JsonBranch("funder"))
      .addField(new JsonBranch("temporalCoverageX"));

    CalculatorFacade facade = new CalculatorFacade(new MeasurementConfiguration()
        .enableCompletenessMeasurement()
        .enableFieldCardinalityMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()));
    // facade.configure();

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName", "existence:description",
        "existence:variablesMeasured", "existence:measurementTechnique", "existence:sameAs",
        "existence:doi", "existence:identifier", "existence:author", "existence:isAccessibleForFree",
        "existence:dateModified", "existence:distribution", "existence:spatialCoverage",
        "existence:provider", "existence:funder", "existence:temporalCoverageX",
        "cardinality:url", "cardinality:name", "cardinality:alternateName",
        "cardinality:description", "cardinality:variablesMeasured",
        "cardinality:measurementTechnique", "cardinality:sameAs", "cardinality:doi",
        "cardinality:identifier", "cardinality:author", "cardinality:isAccessibleForFree",
        "cardinality:dateModified", "cardinality:distribution", "cardinality:spatialCoverage",
        "cardinality:provider", "cardinality:funder", "cardinality:temporalCoverageX"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
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
  public void testCalculation_setFirstRowAsHeader() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
      .addField(new JsonBranch("description"))
      .addField(new JsonBranch("variablesMeasured"))
      .addField(new JsonBranch("measurementTechnique"))
      .addField(new JsonBranch("sameAs"))
      .addField(new JsonBranch("doi"))
      .addField(new JsonBranch("identifier"))
      .addField(new JsonBranch("author"))
      .addField(new JsonBranch("isAccessibleForFree"))
      .addField(new JsonBranch("dateModified"))
      .addField(new JsonBranch("distribution"))
      .addField(new JsonBranch("spatialCoverage"))
      .addField(new JsonBranch("provider"))
      .addField(new JsonBranch("funder"))
      .addField(new JsonBranch("temporalCoverage"));

    CalculatorFacade facade = new CalculatorFacade(
        new MeasurementConfiguration()
          .enableCompletenessMeasurement()
          .enableFieldCardinalityMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(true);

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName", "existence:description",
        "existence:variablesMeasured", "existence:measurementTechnique", "existence:sameAs",
        "existence:doi", "existence:identifier", "existence:author", "existence:isAccessibleForFree",
        "existence:dateModified", "existence:distribution", "existence:spatialCoverage",
        "existence:provider", "existence:funder", "existence:temporalCoverage",
        "cardinality:url", "cardinality:name", "cardinality:alternateName",
        "cardinality:description", "cardinality:variablesMeasured",
        "cardinality:measurementTechnique", "cardinality:sameAs", "cardinality:doi",
        "cardinality:identifier", "cardinality:author", "cardinality:isAccessibleForFree",
        "cardinality:dateModified", "cardinality:distribution", "cardinality:spatialCoverage",
        "cardinality:provider", "cardinality:funder", "cardinality:temporalCoverage"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReader(new FileReader(fileName)));
      StringBuffer result = new StringBuffer();
      while (iterator.hasNext()) {
        String line = CsvReader.toCsv(iterator.next());
        String metrics = facade.measure(line);
        if (!metrics.isEmpty())
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
  public void testCalculation_limitFields() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("alternateName"))
    ;

    CalculatorFacade facade = new CalculatorFacade(new MeasurementConfiguration()
        .enableCompletenessMeasurement()
        .enableFieldCardinalityMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(true);

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName",
        "cardinality:url", "cardinality:name", "cardinality:alternateName"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReader(new FileReader(fileName)));
      StringBuffer result = new StringBuffer();
      while (iterator.hasNext()) {
        String line = CsvReader.toCsv(iterator.next());
        String metrics = facade.measure(line);
        if (!metrics.isEmpty())
          result.append(metrics + "\n");
      }
      String expected = "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "1.0,1.0,1,1,1,1,1,1\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n";
      assertEquals(expected, result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCalculation_nonexistentFields() throws Exception {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new JsonBranch("name"))
      .addField(new JsonBranch("nonexistent"))
    ;

    CalculatorFacade facade = new CalculatorFacade(
        new MeasurementConfiguration()
          .enableCompletenessMeasurement()
          .enableFieldCardinalityMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(true);

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:nonexistent",
        "cardinality:url", "cardinality:name", "cardinality:nonexistent"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReader(new FileReader(fileName)));
      StringBuffer result = new StringBuffer();
      while (iterator.hasNext()) {
        String line = CsvReader.toCsv(iterator.next());
        String metrics = facade.measure(line);
        if (!metrics.isEmpty())
          result.append(metrics + "\n");
      }
      String expected = "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n" +
        "0.666667,1.0,1,1,0,1,1,0\n";
      assertEquals(expected, result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void config2Schema() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/configuration.yaml")
      .asSchema();

    assertEquals(Format.JSON, schema.getFormat());
    assertEquals(3, schema.getPaths().size());
  }

  @Test
  public void config2Schema_categories() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/configuration_categories.yaml")
      .asSchema();

    assertEquals(Format.JSON, schema.getFormat());
    assertEquals(3, schema.getPaths().size());
    assertEquals(
      Arrays.asList("MANDATORY", "DESCRIPTIVENESS", "SEARCHABILITY", "IDENTIFICATION",
        "CUSTOM", "MULTILINGUALITY"),
      schema.getCategories()
    );
    assertTrue(schema.getCategories().contains("CUSTOM"));
  }

  @Test
  public void config2Schema_conflicting_categories() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/configuration_conflicting_categories.yaml")
      .asSchema();

    assertEquals(Format.JSON, schema.getFormat());
    assertEquals(3, schema.getPaths().size());
    assertEquals(
      Arrays.asList("MANDATORY", "DESCRIPTIVENESS", "SEARCHABILITY", "IDENTIFICATION",
        "CUSTOM", "MULTILINGUALITY"),
      schema.getCategories()
    );
    assertTrue(schema.getCategories().contains("CUSTOM"));
    assertEquals(
      Arrays.asList("DESCRIPTIVENESS", "SEARCHABILITY", "IDENTIFICATION",
        "MULTILINGUALITY", "CUSTOM"),
      schema.getPathByLabel("Proxy/dc:title").getCategories());
  }

  @Test
  public void fromConfigFile() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/meemoo.yaml")
      .asSchema();

    assertNotNull(schema.getRuleCheckers());
    assertEquals(3, schema.getRuleCheckers().size());

    CalculatorFacade facade = new CalculatorFacade(
        new MeasurementConfiguration()
          .enableCompletenessMeasurement()
          .enableFieldCardinalityMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()));
    // facade.configure();

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName", "existence:description",
        "existence:variablesMeasured", "existence:measurementTechnique", "existence:sameAs",
        "existence:doi", "existence:identifier", "existence:author", "existence:isAccessibleForFree",
        "existence:dateModified", "existence:distribution", "existence:spatialCoverage",
        "existence:provider", "existence:funder", "existence:temporalCoverage",
        "cardinality:url", "cardinality:name", "cardinality:alternateName",
        "cardinality:description", "cardinality:variablesMeasured",
        "cardinality:measurementTechnique", "cardinality:sameAs", "cardinality:doi",
        "cardinality:identifier", "cardinality:author", "cardinality:isAccessibleForFree",
        "cardinality:dateModified", "cardinality:distribution", "cardinality:spatialCoverage",
        "cardinality:provider", "cardinality:funder", "cardinality:temporalCoverage"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
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
  public void runRuleCatalog() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml(
        "src/test/resources/configuration/schema/meemoo.yaml"
      )
      .asSchema();

    assertNotNull(schema.getRuleCheckers());
    assertEquals(3, schema.getRuleCheckers().size());

    CalculatorFacade facade = new CalculatorFacade(
      new MeasurementConfiguration()
        .enableCompletenessMeasurement()
        .enableFieldCardinalityMeasurement()
        .enableRuleCatalogMeasurement()
      )
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()));
    facade.configure();

    assertEquals(RuleCatalog.class, facade.getCalculators().get(1).getClass());

    // [
    // de.gwdg.metadataqa.api.calculator.CompletenessCalculator@62043840,
    // de.gwdg.metadataqa.api.rule.RuleCatalog@5315b42e
    // ]

    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    File file = new File(fileName);

    List<String> outputHeader = facade.getHeader();
    assertEquals(
      Arrays.asList(
        "completeness:TOTAL", "completeness:MANDATORY",
        "existence:url", "existence:name", "existence:alternateName", "existence:description",
        "existence:variablesMeasured", "existence:measurementTechnique", "existence:sameAs",
        "existence:doi", "existence:identifier", "existence:author", "existence:isAccessibleForFree",
        "existence:dateModified", "existence:distribution", "existence:spatialCoverage",
        "existence:provider", "existence:funder", "existence:temporalCoverage",
        "cardinality:url", "cardinality:name", "cardinality:alternateName",
        "cardinality:description", "cardinality:variablesMeasured",
        "cardinality:measurementTechnique", "cardinality:sameAs", "cardinality:doi",
        "cardinality:identifier", "cardinality:author", "cardinality:isAccessibleForFree",
        "cardinality:dateModified", "cardinality:distribution", "cardinality:spatialCoverage",
        "cardinality:provider", "cardinality:funder", "cardinality:temporalCoverage",
        "rule:url:pattern:1", "rule:url:minCount:2", "rule:alternateName:pattern:3", "ruleCatalog:score"
      ),
      outputHeader
    );

    try {
      CSVIterator iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
      StringBuffer result = new StringBuffer();
      while (iterator.hasNext()) {
        String line = CsvReader.toCsv(iterator.next());
        String metrics = facade.measure(line);
        result.append(metrics + "\n");
      }
      String expected = "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,NA,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,NA,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,NA,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,NA,0\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,NA,0\n"
        + "0.411765,1.0,1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0,1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0,1,1,NA,0\n"
        + "0.352941,1.0,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,2,2\n"
        + "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,NA,0\n"
        + "0.294118,1.0,1,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1,NA,0\n";
      assertEquals(expected, result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAddField() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField("url");

    assertEquals(1, schema.getPaths().size());
    assertEquals("url", schema.getPaths().get(0).getLabel());
    assertEquals("url", schema.getPaths().get(0).getJsonPath());
  }

  @Test
  public void testAddFields() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    assertEquals(2, schema.getPaths().size());
    assertEquals("url", schema.getPaths().get(0).getLabel());
    assertEquals("url", schema.getPaths().get(0).getJsonPath());
    assertEquals("name", schema.getPaths().get(1).getLabel());
    assertEquals("name", schema.getPaths().get(1).getJsonPath());
  }

  @Test
  public void testGetRootChildrenPaths() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    assertEquals(2, schema.getRootChildrenPaths().size());
  }

  @Test
  public void testGetPathByLabel() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    assertEquals("url", schema.getPathByLabel("url").getLabel());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetNoLanguageFields() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    schema.getNoLanguageFields();
  }

  @Test
  public void getIndexFields_empty() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    List<JsonBranch> indexFields = schema.getIndexFields();
    assertNotNull(indexFields);
    assertTrue(indexFields.isEmpty());
  }

  @Test
  public void getIndexFields_nonempty() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");
    schema.getPathByLabel("url").setIndexField("url");

    List<JsonBranch> indexFields = schema.getIndexFields();
    assertNotNull(indexFields);
    assertFalse(indexFields.isEmpty());
    assertEquals(1, indexFields.size());
    assertEquals("url", indexFields.get(0).getLabel());
    assertEquals("url", indexFields.get(0).getIndexField());
  }

  @Test
  public void testAddExtractableField() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    schema.addExtractableField("url", "url");

    assertEquals(2, schema.getPaths().size());
    assertEquals(1, schema.getExtractableFields().size());
    assertEquals("url", schema.getExtractableFields().get("url"));
  }

  @Test
  public void testSetExtractableFields() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");

    Map<String, String> map = new HashMap<>();
    map.put("url", "url");
    schema.setExtractableFields(map);

    assertEquals(2, schema.getPaths().size());
    assertEquals(1, schema.getExtractableFields().size());
    assertEquals("url", schema.getExtractableFields().get("url"));
  }

  @Test
  public void testRuleCheckers() {
    Rule rule = new Rule();
    rule.setEquals("uri");
    rule.setDisjoint("uri");
    rule.setIn(Arrays.asList("a", "b"));
    rule.setMinCount(1);
    rule.setMaxCount(1);
    rule.setMinLength(1);
    rule.setMaxLength(10);
    rule.setMaxLength(10);
    rule.setPattern("^http");

    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new JsonBranch("url", "url").setRule(Arrays.asList(rule)))
      .addField(new JsonBranch("uri", "uri"))
    ;

    assertEquals(8, schema.getRuleCheckers().size());
    assertEquals("de.gwdg.metadataqa.api.rule.singlefieldchecker.PatternChecker",
      schema.getRuleCheckers().get(0).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.pairchecker.EqualityChecker",
      schema.getRuleCheckers().get(1).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.pairchecker.DisjointChecker",
      schema.getRuleCheckers().get(2).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.singlefieldchecker.EnumerationChecker",
      schema.getRuleCheckers().get(3).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker",
      schema.getRuleCheckers().get(4).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxCountChecker",
      schema.getRuleCheckers().get(5).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.singlefieldchecker.MinLengthChecker",
      schema.getRuleCheckers().get(6).getClass().getName());
    assertEquals("de.gwdg.metadataqa.api.rule.singlefieldchecker.MaxLengthChecker",
      schema.getRuleCheckers().get(7).getClass().getName());
  }

  @Test
  public void testToString() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addFields("url", "name");
    assertEquals("BaseSchema{categories=null, format=CSV}", schema.toString());
  }

  @Test
  public void testCollection() {
    Schema schema = new BaseSchema()
      .addField(new JsonBranch("author", "author").setCollection(true))
      .addField(new JsonBranch("title", "title").setCollection(false))
      ;
    assertNotNull(schema.getCollectionPaths());
    assertEquals(1, schema.getCollectionPaths().size());
    assertEquals("author", schema.getCollectionPaths().get(0).getLabel());
  }

  @Test
  public void getRuleChecker() {
    Schema schema = new BaseSchema()
      .addField(new JsonBranch("author", "author").addRule(
        new Rule().withHasValue("a")
      ))
      .addField(new JsonBranch("title", "title").setCollection(false))
      ;
    List<RuleChecker> checkers = schema.getRuleCheckers();
    assertNotNull(checkers);
    assertEquals(1, checkers.size());
    assertEquals(HasValueChecker.class, checkers.get(0).getClass());
  }

  @Test
  public void withIndexFields() throws FileNotFoundException {
    Schema schema = ConfigurationReader
      .readSchemaYaml("src/test/resources/configuration/schema/with-index-fields.yaml")
      .asSchema();

    assertNotNull(schema);
    assertNotNull(schema.getPathByLabel("url"));
    JsonBranch branch = schema.getPathByLabel("url");
    assertEquals("url", branch.getLabel());
    assertEquals("url", branch.getIndexField());
    assertNotNull(schema.getIndexFields());
    assertEquals(2, schema.getIndexFields().size());
    assertEquals("url", schema.getIndexFields().get(0).getLabel());
    assertEquals("description", schema.getIndexFields().get(1).getLabel());


  }
}