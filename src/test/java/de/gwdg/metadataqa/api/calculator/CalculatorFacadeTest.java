package de.gwdg.metadataqa.api.calculator;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.*;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
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
    MeasurementConfiguration configuration = new MeasurementConfiguration(true, true, true, false, true);
    CalculatorFacade calculatorFacade = new CalculatorFacade(configuration);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    String expected = "0.184,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0,0.0,0.0,0.0";
    assertEquals(expected, calculatorFacade.measure(FileUtils.readFirstLineFromResource("general/test.json")));
  }

  @Test
  public void testNoAbbreviate_map() throws URISyntaxException, IOException {
    MeasurementConfiguration configuration = new MeasurementConfiguration(true, true, true, false, true);
    CalculatorFacade calculatorFacade = new CalculatorFacade(configuration);
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    Map<String, Object> result = calculatorFacade.measureAsMap(
      FileUtils.readFirstLineFromResource("general/test.json")
    );
    assertTrue(result instanceof Map);
    assertEquals(263, result.size());
    assertEquals(0.184, result.get("completeness:TOTAL"));
  }

  @Test
  public void testNoAbbreviate_list() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(new MeasurementConfiguration(true, true, true, false, true));
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
  public void testNoAbbreviate_list2() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(new MeasurementConfiguration(true, true, true, false, true));
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();

    String result = calculatorFacade.measureAsJson(
      FileUtils.readFirstLineFromResource("general/test.json")
    );
    assertTrue(result instanceof String);
    assertEquals(3344, result.length());
    assertEquals(
      "{\"completeness\":{\"ProvidedCHO/rdf:about\":1,\"Proxy/rdf:about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0," +
        "\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1," +
        "\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0," +
        "\"Proxy/dc:subject\":5,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0," +
        "\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1," +
        "\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0," +
        "\"Proxy/edm:type\":1,\"Proxy/edm:europeanaProxy\":1,\"Proxy/edm:year\":0,\"Proxy/edm:userTag\":0,\"Proxy/ore:proxyIn\":1," +
        "\"Proxy/ore:proxyFor\":1,\"Proxy/dcterms:conformsTo\":0,\"Proxy/dcterms:hasFormat\":1,\"Proxy/dcterms:hasVersion\":0," +
        "\"Proxy/dcterms:isFormatOf\":0,\"Proxy/dcterms:isReferencedBy\":0,\"Proxy/dcterms:isReplacedBy\":0,\"Proxy/dcterms:isRequiredBy\":0," +
        "\"Proxy/dcterms:isVersionOf\":0,\"Proxy/dcterms:references\":0,\"Proxy/dcterms:replaces\":0,\"Proxy/dcterms:requires\":0," +
        "\"Proxy/dcterms:tableOfContents\":0,\"Proxy/edm:currentLocation\":0,\"Proxy/edm:hasMet\":0,\"Proxy/edm:hasType\":0," +
        "\"Proxy/edm:incorporates\":0,\"Proxy/edm:isDerivativeOf\":0,\"Proxy/edm:isRelatedTo\":0,\"Proxy/edm:isRepresentationOf\":0," +
        "\"Proxy/edm:isSimilarTo\":0,\"Proxy/edm:isSuccessorOf\":0,\"Proxy/edm:realizes\":0,\"Proxy/edm:wasPresentAt\":0," +
        "\"Aggregation/rdf:about\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1," +
        "\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0," +
        "\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1,\"Aggregation/edm:intermediateProvider\":0," +
        "\"Place/rdf:about\":0,\"Place/wgs84:lat\":0,\"Place/wgs84:long\":0,\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":0," +
        "\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0,\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":0," +
        "\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Agent/rdf:about\":0,\"Agent/edm:begin\":0,\"Agent/edm:end\":0," +
        "\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0,\"Agent/foaf:name\":0,\"Agent/dc:date\":0," +
        "\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0,\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0," +
        "\"Agent/rdaGr2:placeOfDeath\":0,\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0," +
        "\"Agent/rdaGr2:gender\":0,\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0," +
        "\"Agent/skos:prefLabel\":0,\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0," +
        "\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0," +
        "\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0," +
        "\"Concept/rdf:about\":1,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0," +
        "\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0," +
        "\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":12," +
        "\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0}," +
        "\"problemCatalog\":{\"LongSubject\":0.0,\"TitleAndDescriptionAreSame\":0.0,\"EmptyStrings\":0.0}}",
      result
    );
  }

  @Test
  public void testNoAbbreviate_listOfObject() throws URISyntaxException, IOException {
    CalculatorFacade calculatorFacade = new CalculatorFacade(new MeasurementConfiguration(true, true, true, false, true));
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
    CalculatorFacade calculatorFacade = new CalculatorFacade(new MeasurementConfiguration(true, true, true, false, true));
    calculatorFacade.setSchema(new EdmOaiPmhJsonSchema());
    calculatorFacade.configure();
    String expected = "0.184,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0,0.0,0.0,0.0";
    assertEquals(expected, calculatorFacade.measure(FileUtils.readFirstLineFromResource("general/test.json")));
  }

  @Test
  public void testChanged() {
    MeasurementConfiguration configuration = new MeasurementConfiguration()
      .disableFieldExtractor()
      .disableTfIdfMeasurement();
    ;
    configuration.withSolrConfiguration("localhost", "8983", "solr/europeana");


    CalculatorFacade calculator = new CalculatorFacade(configuration)
      .setSchema(new EdmOaiPmhJsonSchema())
      // .disableFieldExtractor()
    ;

    assertFalse(configuration.isTfIdfMeasurementEnabled());
    calculator.configure();
    List<Calculator> calculators = calculator.getCalculators();
    assertEquals(1, calculators.size());

    calculator.conditionalConfiguration();
    calculators = calculator.getCalculators();
    assertEquals(1, calculators.size());

    calculator.conditionalConfiguration();
    calculators = calculator.getCalculators();
    assertEquals(1, calculators.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTfIdfWithWrongConfiguration() {
    MeasurementConfiguration configuration = new MeasurementConfiguration().enableTfIdfMeasurement();
    CalculatorFacade calculator = new CalculatorFacade(configuration)
      .setSchema(new EdmOaiPmhJsonSchema());
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
    List<Object> expected = Arrays.asList(0.35294117647058826, 1.0, true, true, false, true, false, false, false, false, true, false, false, true, true, false, false, false, false, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0);
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
    expected.put("completeness:TOTAL", 0.35294117647058826);
    expected.put("completeness:MANDATORY", 1.0);
    expected.put("existence:url", true);
    expected.put("existence:name", true);
    expected.put("existence:alternateName", false);
    expected.put("existence:description", true);
    expected.put("existence:variablesMeasured", false);
    expected.put("existence:measurementTechnique", false);
    expected.put("existence:sameAs", false);
    expected.put("existence:doi", false);
    expected.put("existence:identifier", true);
    expected.put("existence:author", false);
    expected.put("existence:isAccessibleForFree", false);
    expected.put("existence:dateModified", true);
    expected.put("existence:distribution", true);
    expected.put("existence:spatialCoverage", false);
    expected.put("existence:provider", false);
    expected.put("existence:funder", false);
    expected.put("existence:temporalCoverage", false);
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
    assertEquals(0.35294117647058826, result.get("completeness:TOTAL"));
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

    MeasurementConfiguration config = new MeasurementConfiguration()
      .enableCompletenessMeasurement()
      .enableFieldCardinalityMeasurement();

    CalculatorFacade facade = new CalculatorFacade(config)
      .setSchema(schema)
      .setCsvReader(
        new CsvReader()
          .setHeader(((CsvAwareSchema) schema).getHeader()));

    MeasurementConfiguration measurementConfiguration = new MeasurementConfiguration(true, true, true, false, true);
    assertTrue((measurementConfiguration.isFieldExistenceMeasurementEnabled()));
    CalculatorFacade calculatorFacade = new CalculatorFacade(measurementConfiguration);

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
