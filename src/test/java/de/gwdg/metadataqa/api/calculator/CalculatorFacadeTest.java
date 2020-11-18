package de.gwdg.metadataqa.api.calculator;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.*;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.util.FileUtils;
import de.gwdg.metadataqa.api.interfaces.Calculator;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CalculatorFacadeTest {

  public CalculatorFacadeTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testNoAbbreviate() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    String expected = "0.184,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0,0.0,0.0,0.0";
    assertEquals(expected, calculatorFacade.measure(FileUtils.readFirstLineFromResource("general/test.json")));
  }

  @Test
  public void testNoAbbreviate_map() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    Map<String, Object> result = calculatorFacade.measureAsMap(
      FileUtils.readFirstLineFromResource("general/test.json")
    );
    assertTrue(result instanceof Map);
    assertEquals(263, result.size());
    assertEquals(0.184, result.get("TOTAL"));
  }

  @Test
  public void testNoAbbreviate_list() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();

    List<String> result = calculatorFacade.measureAsList(
      FileUtils.readFirstLineFromResource("general/test.json")
    );
    assertTrue(result instanceof List);
    assertEquals(263, result.size());
    assertEquals("0.184", result.get(0));
  }

  @Test
  public void testNoAbbreviate_listOfObject() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();

    List<Object> result = calculatorFacade.measureAsListOfObjects(
      FileUtils.readFirstLineFromResource("general/test.json")
    );
    assertTrue(result instanceof List);
    assertEquals(263, result.size());
    assertEquals(0.184, result.get(0));
  }

  @Test
  public void testWithAbbreviate() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    String expected = "0.184,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0,0.0,0.0,0.0";
    assertEquals(expected, calculatorFacade.measure(FileUtils.readFirstLineFromResource("general/test.json")));
  }

  @Test
  public void emptyConstructor() {
    CalculatorFacade calculator = new CalculatorFacade();
    assertTrue(calculator.isFieldExistenceMeasurementEnabled());
    assertTrue(calculator.isFieldCardinalityMeasurementEnabled());
    assertTrue(calculator.isCompletenessMeasurementEnabled());
    assertFalse(calculator.isTfIdfMeasurementEnabled());
    assertFalse(calculator.isProblemCatalogMeasurementEnabled());
    assertFalse(calculator.isLanguageMeasurementEnabled());
    assertFalse(calculator.collectTfIdfTerms());
    assertFalse(calculator.completenessCollectFields());
  }

  @Test
  public void testChanged() {
    CalculatorFacade calculator = new CalculatorFacade()
      .setSchema(new EdmOaiPmhJsonSchema())
      .disableFieldExtractor();

    assertFalse(calculator.isTfIdfMeasurementEnabled());
    calculator.configure();
    List<Calculator> calculators = calculator.getCalculators();
    assertEquals(1, calculators.size());

    calculator.enableTfIdfMeasurement();
    calculator.configureSolr("localhost", "8983", "solr/europeana");
    calculator.conditionalConfiguration();
    calculators = calculator.getCalculators();
    assertEquals(2, calculators.size());

    calculator.conditionalConfiguration();
    calculators = calculator.getCalculators();
    assertEquals(2, calculators.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTfIdfWithWrongConfiguration() {
    CalculatorFacade calculator = new CalculatorFacade()
      .setSchema(new EdmOaiPmhJsonSchema())
      .enableTfIdfMeasurement();
    calculator.conditionalConfiguration();

    List<Calculator> calculators = calculator.getCalculators();

    assertEquals(2, calculators.size());
  }

  @Test
  public void testNoAbbreviate_measureCsv() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    String metrics = facade.measure(Arrays.asList(iterator.next()));
    String expected = "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0";
    assertEquals(expected, metrics);

    // assertTrue(result instanceof List);
    // assertEquals(263, result.size());
    // assertEquals("0.184", result.get(0));
  }

  @Test
  public void testNoAbbreviate_measureCsvAsList() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    List<String> result = facade.measureAsList(Arrays.asList(iterator.next()));
    List<String> expected = Arrays.asList("0.352941", "1.0", "1", "1", "0", "1", "0", "0", "0", "0", "1", "0", "0", "1", "1", "0", "0", "0", "0", "1", "1", "0", "1", "0", "0", "0", "0", "1", "0", "0", "1", "1", "0", "0", "0", "0");
    assertEquals(expected, result);

    assertTrue(result instanceof List);
    assertEquals(36, result.size());
    assertEquals("0.352941", result.get(0));
  }

  @Test
  public void testNoAbbreviate_measureCsvAsListOfObjects() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    List<Object> result = facade.measureAsListOfObjects(Arrays.asList(iterator.next()));
    List<Object> expected = Arrays.asList(0.35294117647058826, 1.0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0);
    assertEquals(expected, result);

    assertTrue(result instanceof List);
    assertEquals(36, result.size());
    assertEquals(0.35294117647058826, result.get(0));
  }

  @Test
  public void testNoAbbreviate_measureCsvAsMap() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    Map<String, Object> result = facade.measureAsMap(Arrays.asList(iterator.next()));
    Map<String, Object> expected = new LinkedHashMap<>();
    expected.put("TOTAL", 0.35294117647058826);
    expected.put("MANDATORY", 1.0);
    expected.put("existence:url", 1);
    expected.put("existence:name", 1);
    expected.put("existence:alternateName", 0);
    expected.put("existence:description", 1);
    expected.put("existence:variablesMeasured", 0);
    expected.put("existence:measurementTechnique", 0);
    expected.put("existence:sameAs", 0);
    expected.put("existence:doi", 0);
    expected.put("existence:identifier", 1);
    expected.put("existence:author", 0);
    expected.put("existence:isAccessibleForFree", 0);
    expected.put("existence:dateModified", 1);
    expected.put("existence:distribution", 1);
    expected.put("existence:spatialCoverage", 0);
    expected.put("existence:provider", 0);
    expected.put("existence:funder", 0);
    expected.put("existence:temporalCoverage", 0);
    expected.put("cardinality:url", 1);
    expected.put("cardinality:name", 1);
    expected.put("cardinality:alternateName", 0);
    expected.put("cardinality:description", 1);
    expected.put("cardinality:variablesMeasured", 0);
    expected.put("cardinality:measurementTechnique", 0);
    expected.put("cardinality:sameAs", 0);
    expected.put("cardinality:doi", 0);
    expected.put("cardinality:identifier", 1);
    expected.put("cardinality:author", 0);
    expected.put("cardinality:isAccessibleForFree", 0);
    expected.put("cardinality:dateModified", 1);
    expected.put("cardinality:distribution", 1);
    expected.put("cardinality:spatialCoverage", 0);
    expected.put("cardinality:provider", 0);
    expected.put("cardinality:funder", 0);
    expected.put("cardinality:temporalCoverage", 0);

    assertEquals(expected, result);

    assertTrue(result instanceof Map);
    assertEquals(36, result.size());
    assertEquals(0.35294117647058826, result.get("TOTAL"));
  }

  private CalculatorFacade createCalculatorFacadeForCsv() {
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

    CalculatorFacade facade = new CalculatorFacade()
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()))
      .enableCompletenessMeasurement()
      .enableFieldCardinalityMeasurement();

    CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    return facade;
  }

  private CSVIterator createCsvIterator() throws IOException {
    String fileName = "src/test/resources/csv/dataset_metadata_2020_08_17-head.csv";
    CSVIterator iterator = null;
    try {
      iterator = new CSVIterator(new CSVReaderHeaderAware(new FileReader(fileName)));
    } catch (CsvValidationException e) {
      e.printStackTrace();
    }
    return iterator;
  }
}
