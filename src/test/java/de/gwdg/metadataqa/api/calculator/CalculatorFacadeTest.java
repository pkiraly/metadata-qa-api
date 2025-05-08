package de.gwdg.metadataqa.api.calculator;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import de.gwdg.metadataqa.api.configuration.MeasurementConfiguration;
import de.gwdg.metadataqa.api.interfaces.MetricResult;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.Category;
import de.gwdg.metadataqa.api.schema.*;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.util.CsvReader;
import de.gwdg.metadataqa.api.util.FileUtils;
import de.gwdg.metadataqa.api.interfaces.Calculator;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CalculatorFacadeTest {

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
    assertEquals(7388, result.length());
    assertEquals(
      "{\"completeness\":{\"completeness\":{\"TOTAL\":0.184,\"MANDATORY\":1.0,\"DESCRIPTIVENESS\":0.18181818181818182," +
        "\"SEARCHABILITY\":0.3888888888888889,\"CONTEXTUALIZATION\":0.2727272727272727,\"IDENTIFICATION\":0.5," +
        "\"BROWSING\":0.35714285714285715,\"VIEWING\":0.75,\"REUSABILITY\":0.36363636363636365,\"MULTILINGUALITY\":0.4}," +
        "\"existence\":{\"ProvidedCHO/rdf:about\":true,\"Proxy/rdf:about\":true,\"Proxy/dc:title\":true,\"Proxy/dcterms:alternative\":false," +
        "\"Proxy/dc:description\":false,\"Proxy/dc:creator\":false,\"Proxy/dc:publisher\":false,\"Proxy/dc:contributor\":false," +
        "\"Proxy/dc:type\":true,\"Proxy/dc:identifier\":true,\"Proxy/dc:language\":false,\"Proxy/dc:coverage\":false," +
        "\"Proxy/dcterms:temporal\":false,\"Proxy/dcterms:spatial\":false,\"Proxy/dc:subject\":true,\"Proxy/dc:date\":false," +
        "\"Proxy/dcterms:created\":false,\"Proxy/dcterms:issued\":false,\"Proxy/dcterms:extent\":false,\"Proxy/dcterms:medium\":false," +
        "\"Proxy/dcterms:provenance\":false,\"Proxy/dcterms:hasPart\":false,\"Proxy/dcterms:isPartOf\":true,\"Proxy/dc:format\":false," +
        "\"Proxy/dc:source\":false,\"Proxy/dc:rights\":true,\"Proxy/dc:relation\":false,\"Proxy/edm:isNextInSequence\":false," +
        "\"Proxy/edm:type\":true,\"Proxy/edm:europeanaProxy\":true,\"Proxy/edm:year\":false,\"Proxy/edm:userTag\":false," +
        "\"Proxy/ore:proxyIn\":true,\"Proxy/ore:proxyFor\":true,\"Proxy/dcterms:conformsTo\":false,\"Proxy/dcterms:hasFormat\":true," +
        "\"Proxy/dcterms:hasVersion\":false,\"Proxy/dcterms:isFormatOf\":false,\"Proxy/dcterms:isReferencedBy\":false," +
        "\"Proxy/dcterms:isReplacedBy\":false,\"Proxy/dcterms:isRequiredBy\":false,\"Proxy/dcterms:isVersionOf\":false," +
        "\"Proxy/dcterms:references\":false,\"Proxy/dcterms:replaces\":false,\"Proxy/dcterms:requires\":false," +
        "\"Proxy/dcterms:tableOfContents\":false,\"Proxy/edm:currentLocation\":false,\"Proxy/edm:hasMet\":false," +
        "\"Proxy/edm:hasType\":false,\"Proxy/edm:incorporates\":false,\"Proxy/edm:isDerivativeOf\":false,\"Proxy/edm:isRelatedTo\":false," +
        "\"Proxy/edm:isRepresentationOf\":false,\"Proxy/edm:isSimilarTo\":false,\"Proxy/edm:isSuccessorOf\":false," +
        "\"Proxy/edm:realizes\":false,\"Proxy/edm:wasPresentAt\":false,\"Aggregation/rdf:about\":true,\"Aggregation/edm:rights\":true," +
        "\"Aggregation/edm:provider\":true,\"Aggregation/edm:dataProvider\":true,\"Aggregation/edm:isShownAt\":true," +
        "\"Aggregation/edm:isShownBy\":true,\"Aggregation/edm:object\":true,\"Aggregation/edm:hasView\":false," +
        "\"Aggregation/dc:rights\":false,\"Aggregation/edm:ugc\":false,\"Aggregation/edm:aggregatedCHO\":true," +
        "\"Aggregation/edm:intermediateProvider\":false,\"Place/rdf:about\":false,\"Place/wgs84:lat\":false," +
        "\"Place/wgs84:long\":false,\"Place/wgs84:alt\":false,\"Place/dcterms:isPartOf\":false,\"Place/wgs84_pos:lat_long\":false," +
        "\"Place/dcterms:hasPart\":false,\"Place/owl:sameAs\":false,\"Place/skos:prefLabel\":false,\"Place/skos:altLabel\":false," +
        "\"Place/skos:note\":false,\"Agent/rdf:about\":false,\"Agent/edm:begin\":false,\"Agent/edm:end\":false," +
        "\"Agent/edm:hasMet\":false,\"Agent/edm:isRelatedTo\":false,\"Agent/owl:sameAs\":false,\"Agent/foaf:name\":false," +
        "\"Agent/dc:date\":false,\"Agent/dc:identifier\":false,\"Agent/rdaGr2:dateOfBirth\":false,\"Agent/rdaGr2:placeOfBirth\":false," +
        "\"Agent/rdaGr2:dateOfDeath\":false,\"Agent/rdaGr2:placeOfDeath\":false,\"Agent/rdaGr2:dateOfEstablishment\":false," +
        "\"Agent/rdaGr2:dateOfTermination\":false,\"Agent/rdaGr2:gender\":false,\"Agent/rdaGr2:professionOrOccupation\":false," +
        "\"Agent/rdaGr2:biographicalInformation\":false,\"Agent/skos:prefLabel\":false,\"Agent/skos:altLabel\":false," +
        "\"Agent/skos:note\":false,\"Timespan/rdf:about\":false,\"Timespan/edm:begin\":false,\"Timespan/edm:end\":false," +
        "\"Timespan/dcterms:isPartOf\":false,\"Timespan/dcterms:hasPart\":false,\"Timespan/edm:isNextInSequence\":false," +
        "\"Timespan/owl:sameAs\":false,\"Timespan/skos:prefLabel\":false,\"Timespan/skos:altLabel\":false," +
        "\"Timespan/skos:note\":false,\"Concept/rdf:about\":true,\"Concept/skos:broader\":false,\"Concept/skos:narrower\":false," +
        "\"Concept/skos:related\":false,\"Concept/skos:broadMatch\":false,\"Concept/skos:narrowMatch\":false," +
        "\"Concept/skos:relatedMatch\":false,\"Concept/skos:exactMatch\":false,\"Concept/skos:closeMatch\":false," +
        "\"Concept/skos:notation\":false,\"Concept/skos:inScheme\":false,\"Concept/skos:prefLabel\":true," +
        "\"Concept/skos:altLabel\":false,\"Concept/skos:note\":false},\"cardinality\":{\"ProvidedCHO/rdf:about\":1," +
        "\"Proxy/rdf:about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0," +
        "\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1," +
        "\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0," +
        "\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":5,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0," +
        "\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0," +
        "\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1," +
        "\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Proxy/edm:europeanaProxy\":1," +
        "\"Proxy/edm:year\":0,\"Proxy/edm:userTag\":0,\"Proxy/ore:proxyIn\":1,\"Proxy/ore:proxyFor\":1,\"Proxy/dcterms:conformsTo\":0," +
        "\"Proxy/dcterms:hasFormat\":1,\"Proxy/dcterms:hasVersion\":0,\"Proxy/dcterms:isFormatOf\":0,\"Proxy/dcterms:isReferencedBy\":0," +
        "\"Proxy/dcterms:isReplacedBy\":0,\"Proxy/dcterms:isRequiredBy\":0,\"Proxy/dcterms:isVersionOf\":0," +
        "\"Proxy/dcterms:references\":0,\"Proxy/dcterms:replaces\":0,\"Proxy/dcterms:requires\":0,\"Proxy/dcterms:tableOfContents\":0," +
        "\"Proxy/edm:currentLocation\":0,\"Proxy/edm:hasMet\":0,\"Proxy/edm:hasType\":0,\"Proxy/edm:incorporates\":0," +
        "\"Proxy/edm:isDerivativeOf\":0,\"Proxy/edm:isRelatedTo\":0,\"Proxy/edm:isRepresentationOf\":0,\"Proxy/edm:isSimilarTo\":0," +
        "\"Proxy/edm:isSuccessorOf\":0,\"Proxy/edm:realizes\":0,\"Proxy/edm:wasPresentAt\":0,\"Aggregation/rdf:about\":1," +
        "\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1," +
        "\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1," +
        "\"Aggregation/edm:hasView\":0,\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1," +
        "\"Aggregation/edm:intermediateProvider\":0,\"Place/rdf:about\":0,\"Place/wgs84:lat\":0,\"Place/wgs84:long\":0," +
        "\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":0,\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0," +
        "\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":0,\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Agent/rdf:about\":0," +
        "\"Agent/edm:begin\":0,\"Agent/edm:end\":0,\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0," +
        "\"Agent/foaf:name\":0,\"Agent/dc:date\":0,\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0," +
        "\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0,\"Agent/rdaGr2:placeOfDeath\":0," +
        "\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0,\"Agent/rdaGr2:gender\":0," +
        "\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0,\"Agent/skos:prefLabel\":0," +
        "\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0," +
        "\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0," +
        "\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0," +
        "\"Concept/rdf:about\":1,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0," +
        "\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0," +
        "\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":12," +
        "\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0}},\"problemCatalog\":{\"problemCatalog\":{\"LongSubject\":0.0," +
        "\"TitleAndDescriptionAreSame\":0.0,\"EmptyStrings\":0.0}}}",
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

  @Test
  public void testTfIdfWithWrongConfiguration() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
        MeasurementConfiguration configuration = new MeasurementConfiguration().enableTfIdfMeasurement();
        CalculatorFacade calculator = new CalculatorFacade(configuration)
          .setSchema(new EdmOaiPmhJsonSchema());
        calculator.conditionalConfiguration();

        List<Calculator> calculators = calculator.getCalculators();
    });
    assertEquals("If TF-IDF measurement is enabled, Solr configuration should not be null.", e.getMessage());
  }

  @Test
  public void testNoAbbreviate_measureCsv() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    String metrics = facade.measure(Arrays.asList(iterator.next()));
    String expected = "0.352941,1.0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,0,0,1,1,0,0,0,0";
    assertEquals(expected, metrics);
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

  @Test
  public void testNoAbbreviate_measureAsJson() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    String metrics = facade.measureAsJson(Arrays.asList(iterator.next()));
    String expected = "{\"completeness\":{\"completeness\":{\"TOTAL\":0.35294117647058826,\"MANDATORY\":1.0},\"existence\":{\"url\":true,\"name\":true,\"alternateName\":false,\"description\":true,\"variablesMeasured\":false,\"measurementTechnique\":false,\"sameAs\":false,\"doi\":false,\"identifier\":true,\"author\":false,\"isAccessibleForFree\":false,\"dateModified\":true,\"distribution\":true,\"spatialCoverage\":false,\"provider\":false,\"funder\":false,\"temporalCoverage\":false},\"cardinality\":{\"url\":1,\"name\":1,\"alternateName\":0,\"description\":1,\"variablesMeasured\":0,\"measurementTechnique\":0,\"sameAs\":0,\"doi\":0,\"identifier\":1,\"author\":0,\"isAccessibleForFree\":0,\"dateModified\":1,\"distribution\":1,\"spatialCoverage\":0,\"provider\":0,\"funder\":0,\"temporalCoverage\":0}}}";
    assertEquals(expected, metrics);
  }

  @Test
  public void testNoAbbreviate_measureAsMetricResult() throws URISyntaxException, IOException {
    CalculatorFacade facade = createCalculatorFacadeForCsv();
    CSVIterator iterator = createCsvIterator();

    Map<String, List<MetricResult>> metrics = facade.measureAsMetricResult(Arrays.asList(iterator.next()));
    assertTrue(metrics instanceof Map);
    assertEquals(1, metrics.size());
    assertEquals("completeness", metrics.keySet().iterator().next());
    assertEquals(3, metrics.get("completeness").size());
    assertEquals("completeness", metrics.get("completeness").get(0).getName());
    assertEquals(Map.of("TOTAL", 0.35294117647058826, "MANDATORY", 1.0), metrics.get("completeness").get(0).getResultMap());

    assertEquals("existence", metrics.get("completeness").get(1).getName());
    assertEquals(
      Set.of("url", "name", "alternateName", "description", "variablesMeasured", "measurementTechnique", "sameAs", "doi",
      "identifier", "author", "isAccessibleForFree", "dateModified", "distribution", "spatialCoverage", "provider",
      "funder", "temporalCoverage"),
      metrics.get("completeness").get(1).getResultMap().keySet());
    assertEquals(
      List.of(true, true, false, true, false, false, false, false, true, false, false, true, true, false, false, false, false),
      new ArrayList(metrics.get("completeness").get(1).getResultMap().values()));

    assertEquals("cardinality", metrics.get("completeness").get(2).getName());
    assertEquals(
      Set.of("url", "name", "alternateName", "description", "variablesMeasured", "measurementTechnique", "sameAs", "doi",
        "identifier", "author", "isAccessibleForFree", "dateModified", "distribution", "spatialCoverage", "provider",
        "funder", "temporalCoverage"),
      metrics.get("completeness").get(2).getResultMap().keySet());
    assertEquals(
      List.of(1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0),
      new ArrayList(metrics.get("completeness").get(2).getResultMap().values()));
  }

  private CalculatorFacade createCalculatorFacadeForCsv() {
    Schema schema = new BaseSchema()
      .setFormat(Format.CSV)
      .addField(new DataElement("url").setCategories(Category.MANDATORY).setExtractable())
      .addField(new DataElement("name"))
      .addField(new DataElement("alternateName"))
      .addField(new DataElement("description"))
      .addField(new DataElement("variablesMeasured"))
      .addField(new DataElement("measurementTechnique"))
      .addField(new DataElement("sameAs"))
      .addField(new DataElement("doi"))
      .addField(new DataElement("identifier"))
      .addField(new DataElement("author"))
      .addField(new DataElement("isAccessibleForFree"))
      .addField(new DataElement("dateModified"))
      .addField(new DataElement("distribution"))
      .addField(new DataElement("spatialCoverage"))
      .addField(new DataElement("provider"))
      .addField(new DataElement("funder"))
      .addField(new DataElement("temporalCoverage"));

    assertTrue(schema.getPathByLabel("url").isMandatory());

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
