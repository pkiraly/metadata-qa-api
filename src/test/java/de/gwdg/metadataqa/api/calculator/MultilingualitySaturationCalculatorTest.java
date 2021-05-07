package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.schema.edm.EdmOaiPmhJsonSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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
public class MultilingualitySaturationCalculatorTest {

  public MultilingualitySaturationCalculatorTest() {
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
  public void testConstructor() {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    assertNotNull(calculator);
  }

  @Test
  public void testMeasure() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test.json"));
    calculator.measure(cache);
    assertNotNull(calculator.getCsv(false, CompressionLevel.NORMAL));
    assertEquals("0.5,-1.0,-1.0,-1.0,-1.0,-1.0,0.0,0.0,-1.0,-1.0,-1.0,-1.0,0.666667,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.75,-1.0,-1.0,0.0,-1.0,-1.0,-1.0,-1.0,0.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.75,0.5,0.0,-1.0,-1.0,0.75,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.722222,-1.0,-1.0,0.565217", calculator.getCsv(false, CompressionLevel.NORMAL));

    cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test-place.json"));
    calculator.measure(cache);
    assertNotNull(calculator.getCsv(false, CompressionLevel.NORMAL));
    assertEquals("0.0,-1.0,0.0,0.0,-1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,-1.0,-1.0,0.0,0.0,-1.0,-1.0,0.0,-1.0,0.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.75,0.0,0.0,-1.0,-1.0,0.75,-1.0,0.666667,-1.0,0.6,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.0,0.0,0.6,-1.0,-1.0,-1.0,0.5,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.514563", calculator.getCsv(false, CompressionLevel.NORMAL));
  }

  @Test
  public void testCountersGetLanguageMap() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test.json"));
    calculator.measure(cache);
    String languages = calculator.getCsv(true, CompressionLevel.NORMAL);
    assertNotNull(languages);
    assertEquals(
      "\"Proxy/dc:title\":0.5,\"Proxy/dcterms:alternative\":-1.0,\"Proxy/dc:description\":-1.0,\"Proxy/dc:creator\":-1.0,\"Proxy/dc:publisher\":-1.0,\"Proxy/dc:contributor\":-1.0,\"Proxy/dc:type\":0.0,\"Proxy/dc:identifier\":0.0,\"Proxy/dc:language\":-1.0,\"Proxy/dc:coverage\":-1.0,\"Proxy/dcterms:temporal\":-1.0,\"Proxy/dcterms:spatial\":-1.0,\"Proxy/dc:subject\":0.666667,\"Proxy/dc:date\":-1.0,\"Proxy/dcterms:created\":-1.0,\"Proxy/dcterms:issued\":-1.0,\"Proxy/dcterms:extent\":-1.0,\"Proxy/dcterms:medium\":-1.0,\"Proxy/dcterms:provenance\":-1.0,\"Proxy/dcterms:hasPart\":-1.0,\"Proxy/dcterms:isPartOf\":0.75,\"Proxy/dc:format\":-1.0,\"Proxy/dc:source\":-1.0,\"Proxy/dc:rights\":0.0,\"Proxy/dc:relation\":-1.0,\"Proxy/edm:year\":-1.0,\"Proxy/edm:userTag\":-1.0,\"Proxy/dcterms:conformsTo\":-1.0,\"Proxy/dcterms:hasFormat\":0.0,\"Proxy/dcterms:hasVersion\":-1.0,\"Proxy/dcterms:isFormatOf\":-1.0,\"Proxy/dcterms:isReferencedBy\":-1.0,\"Proxy/dcterms:isReplacedBy\":-1.0,\"Proxy/dcterms:isRequiredBy\":-1.0,\"Proxy/dcterms:isVersionOf\":-1.0,\"Proxy/dcterms:references\":-1.0,\"Proxy/dcterms:replaces\":-1.0,\"Proxy/dcterms:requires\":-1.0,\"Proxy/dcterms:tableOfContents\":-1.0,\"Proxy/edm:currentLocation\":-1.0,\"Proxy/edm:hasMet\":-1.0,\"Proxy/edm:hasType\":-1.0,\"Proxy/edm:incorporates\":-1.0,\"Proxy/edm:isDerivativeOf\":-1.0,\"Proxy/edm:isRelatedTo\":-1.0,\"Proxy/edm:isRepresentationOf\":-1.0,\"Proxy/edm:isSimilarTo\":-1.0,\"Proxy/edm:isSuccessorOf\":-1.0,\"Proxy/edm:realizes\":-1.0,\"Proxy/edm:wasPresentAt\":-1.0,\"Aggregation/edm:rights\":0.75,\"Aggregation/edm:provider\":0.5,\"Aggregation/edm:dataProvider\":0.0,\"Aggregation/dc:rights\":-1.0,\"Aggregation/edm:ugc\":-1.0,\"Aggregation/edm:aggregatedCHO\":0.75,\"Aggregation/edm:intermediateProvider\":-1.0,\"Place/dcterms:isPartOf\":-1.0,\"Place/dcterms:hasPart\":-1.0,\"Place/skos:prefLabel\":-1.0,\"Place/skos:altLabel\":-1.0,\"Place/skos:note\":-1.0,\"Agent/edm:begin\":-1.0,\"Agent/edm:end\":-1.0,\"Agent/edm:hasMet\":-1.0,\"Agent/edm:isRelatedTo\":-1.0,\"Agent/owl:sameAs\":-1.0,\"Agent/foaf:name\":-1.0,\"Agent/dc:date\":-1.0,\"Agent/dc:identifier\":-1.0,\"Agent/rdaGr2:dateOfBirth\":-1.0,\"Agent/rdaGr2:placeOfBirth\":-1.0,\"Agent/rdaGr2:dateOfDeath\":-1.0,\"Agent/rdaGr2:placeOfDeath\":-1.0,\"Agent/rdaGr2:dateOfEstablishment\":-1.0,\"Agent/rdaGr2:dateOfTermination\":-1.0,\"Agent/rdaGr2:gender\":-1.0,\"Agent/rdaGr2:professionOrOccupation\":-1.0,\"Agent/rdaGr2:biographicalInformation\":-1.0,\"Agent/skos:prefLabel\":-1.0,\"Agent/skos:altLabel\":-1.0,\"Agent/skos:note\":-1.0,\"Timespan/edm:begin\":-1.0,\"Timespan/edm:end\":-1.0,\"Timespan/dcterms:isPartOf\":-1.0,\"Timespan/dcterms:hasPart\":-1.0,\"Timespan/edm:isNextInSequence\":-1.0,\"Timespan/owl:sameAs\":-1.0,\"Timespan/skos:prefLabel\":-1.0,\"Timespan/skos:altLabel\":-1.0,\"Timespan/skos:note\":-1.0,\"Concept/skos:broader\":-1.0,\"Concept/skos:narrower\":-1.0,\"Concept/skos:related\":-1.0,\"Concept/skos:broadMatch\":-1.0,\"Concept/skos:narrowMatch\":-1.0,\"Concept/skos:relatedMatch\":-1.0,\"Concept/skos:exactMatch\":-1.0,\"Concept/skos:closeMatch\":-1.0,\"Concept/skos:notation\":-1.0,\"Concept/skos:inScheme\":-1.0,\"Concept/skos:prefLabel\":0.722222,\"Concept/skos:altLabel\":-1.0,\"Concept/skos:note\":-1.0,\"multilingualitySaturation:normalized\":0.565217",
      languages
    );
  }

  @Test
  public void testCountersGetLanguageMapForPLace() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test-place.json"));
    calculator.measure(cache);
    String languages = calculator.getCsv(true, CompressionLevel.NORMAL);
    assertNotNull(languages);
    assertEquals(
      "\"Proxy/dc:title\":0.0,\"Proxy/dcterms:alternative\":-1.0,\"Proxy/dc:description\":0.0,\"Proxy/dc:creator\":0.0,\"Proxy/dc:publisher\":-1.0,\"Proxy/dc:contributor\":0.0,\"Proxy/dc:type\":0.0,\"Proxy/dc:identifier\":0.0,\"Proxy/dc:language\":0.0,\"Proxy/dc:coverage\":0.0,\"Proxy/dcterms:temporal\":0.0,\"Proxy/dcterms:spatial\":0.0,\"Proxy/dc:subject\":0.0,\"Proxy/dc:date\":-1.0,\"Proxy/dcterms:created\":-1.0,\"Proxy/dcterms:issued\":0.0,\"Proxy/dcterms:extent\":0.0,\"Proxy/dcterms:medium\":-1.0,\"Proxy/dcterms:provenance\":-1.0,\"Proxy/dcterms:hasPart\":0.0,\"Proxy/dcterms:isPartOf\":-1.0,\"Proxy/dc:format\":0.0,\"Proxy/dc:source\":-1.0,\"Proxy/dc:rights\":-1.0,\"Proxy/dc:relation\":-1.0,\"Proxy/edm:year\":-1.0,\"Proxy/edm:userTag\":-1.0,\"Proxy/dcterms:conformsTo\":-1.0,\"Proxy/dcterms:hasFormat\":-1.0,\"Proxy/dcterms:hasVersion\":-1.0,\"Proxy/dcterms:isFormatOf\":-1.0,\"Proxy/dcterms:isReferencedBy\":-1.0,\"Proxy/dcterms:isReplacedBy\":-1.0,\"Proxy/dcterms:isRequiredBy\":-1.0,\"Proxy/dcterms:isVersionOf\":-1.0,\"Proxy/dcterms:references\":-1.0,\"Proxy/dcterms:replaces\":-1.0,\"Proxy/dcterms:requires\":-1.0,\"Proxy/dcterms:tableOfContents\":-1.0,\"Proxy/edm:currentLocation\":-1.0,\"Proxy/edm:hasMet\":-1.0,\"Proxy/edm:hasType\":-1.0,\"Proxy/edm:incorporates\":-1.0,\"Proxy/edm:isDerivativeOf\":-1.0,\"Proxy/edm:isRelatedTo\":-1.0,\"Proxy/edm:isRepresentationOf\":-1.0,\"Proxy/edm:isSimilarTo\":-1.0,\"Proxy/edm:isSuccessorOf\":-1.0,\"Proxy/edm:realizes\":-1.0,\"Proxy/edm:wasPresentAt\":-1.0,\"Aggregation/edm:rights\":0.75,\"Aggregation/edm:provider\":0.0,\"Aggregation/edm:dataProvider\":0.0,\"Aggregation/dc:rights\":-1.0,\"Aggregation/edm:ugc\":-1.0,\"Aggregation/edm:aggregatedCHO\":0.75,\"Aggregation/edm:intermediateProvider\":-1.0,\"Place/dcterms:isPartOf\":0.666667,\"Place/dcterms:hasPart\":-1.0,\"Place/skos:prefLabel\":0.6,\"Place/skos:altLabel\":-1.0,\"Place/skos:note\":-1.0,\"Agent/edm:begin\":-1.0,\"Agent/edm:end\":-1.0,\"Agent/edm:hasMet\":-1.0,\"Agent/edm:isRelatedTo\":-1.0,\"Agent/owl:sameAs\":-1.0,\"Agent/foaf:name\":-1.0,\"Agent/dc:date\":-1.0,\"Agent/dc:identifier\":-1.0,\"Agent/rdaGr2:dateOfBirth\":-1.0,\"Agent/rdaGr2:placeOfBirth\":-1.0,\"Agent/rdaGr2:dateOfDeath\":-1.0,\"Agent/rdaGr2:placeOfDeath\":-1.0,\"Agent/rdaGr2:dateOfEstablishment\":-1.0,\"Agent/rdaGr2:dateOfTermination\":-1.0,\"Agent/rdaGr2:gender\":-1.0,\"Agent/rdaGr2:professionOrOccupation\":-1.0,\"Agent/rdaGr2:biographicalInformation\":-1.0,\"Agent/skos:prefLabel\":-1.0,\"Agent/skos:altLabel\":-1.0,\"Agent/skos:note\":-1.0,\"Timespan/edm:begin\":0.0,\"Timespan/edm:end\":0.0,\"Timespan/dcterms:isPartOf\":0.6,\"Timespan/dcterms:hasPart\":-1.0,\"Timespan/edm:isNextInSequence\":-1.0,\"Timespan/owl:sameAs\":-1.0,\"Timespan/skos:prefLabel\":0.5,\"Timespan/skos:altLabel\":-1.0,\"Timespan/skos:note\":-1.0,\"Concept/skos:broader\":-1.0,\"Concept/skos:narrower\":-1.0,\"Concept/skos:related\":-1.0,\"Concept/skos:broadMatch\":-1.0,\"Concept/skos:narrowMatch\":-1.0,\"Concept/skos:relatedMatch\":-1.0,\"Concept/skos:exactMatch\":-1.0,\"Concept/skos:closeMatch\":-1.0,\"Concept/skos:notation\":-1.0,\"Concept/skos:inScheme\":-1.0,\"Concept/skos:prefLabel\":-1.0,\"Concept/skos:altLabel\":-1.0,\"Concept/skos:note\":-1.0,\"multilingualitySaturation:normalized\":0.514563",
      languages
    );
  }

  @Test
  public void testGetLanguageMap() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test.json"));
    calculator.measure(cache);

    Map<String, Double> languages = calculator.getSaturationMap();
    assertNotNull(languages);
    assertEquals(105, languages.size());

    assertEquals(Double.valueOf(0.5), languages.get("Proxy/dc:title"));
    languages.remove("Proxy/dc:title");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:alternative"));
    languages.remove("Proxy/dcterms:alternative");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:description"));
    languages.remove("Proxy/dc:description");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:creator"));
    languages.remove("Proxy/dc:creator");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:publisher"));
    languages.remove("Proxy/dc:publisher");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:contributor"));
    languages.remove("Proxy/dc:contributor");
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:type"));
    languages.remove("Proxy/dc:type");
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:identifier"));
    languages.remove("Proxy/dc:identifier");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:language"));
    languages.remove("Proxy/dc:language");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:coverage"));
    languages.remove("Proxy/dc:coverage");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:temporal"));
    languages.remove("Proxy/dcterms:temporal");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:spatial"));
    languages.remove("Proxy/dcterms:spatial");
    assertEquals(Double.valueOf(0.6666666666666667), languages.get("Proxy/dc:subject"));
    languages.remove("Proxy/dc:subject");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:date"));
    languages.remove("Proxy/dc:date");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:created"));
    languages.remove("Proxy/dcterms:created");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:issued"));
    languages.remove("Proxy/dcterms:issued");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:extent"));
    languages.remove("Proxy/dcterms:extent");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:medium"));
    languages.remove("Proxy/dcterms:medium");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:provenance"));
    languages.remove("Proxy/dcterms:provenance");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:hasPart"));
    languages.remove("Proxy/dcterms:hasPart");
    assertEquals(Double.valueOf(0.75), languages.get("Proxy/dcterms:isPartOf"));
    languages.remove("Proxy/dcterms:isPartOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:format"));
    languages.remove("Proxy/dc:format");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:source"));
    languages.remove("Proxy/dc:source");
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:rights"));
    languages.remove("Proxy/dc:rights");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:relation"));
    languages.remove("Proxy/dc:relation");
    assertEquals(Double.valueOf(0.75), languages.get("Aggregation/edm:rights"));
    languages.remove("Aggregation/edm:rights");
    assertEquals(Double.valueOf(0.5), languages.get("Aggregation/edm:provider"));
    languages.remove("Aggregation/edm:provider");
    assertEquals(Double.valueOf(0.0), languages.get("Aggregation/edm:dataProvider"));
    languages.remove("Aggregation/edm:dataProvider");

    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:year")); languages.remove("Proxy/edm:year");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:userTag")); languages.remove("Proxy/edm:userTag");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:conformsTo")); languages.remove("Proxy/dcterms:conformsTo");
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dcterms:hasFormat")); languages.remove("Proxy/dcterms:hasFormat");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:hasVersion")); languages.remove("Proxy/dcterms:hasVersion");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:isFormatOf")); languages.remove("Proxy/dcterms:isFormatOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:isReferencedBy")); languages.remove("Proxy/dcterms:isReferencedBy");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:isReplacedBy")); languages.remove("Proxy/dcterms:isReplacedBy");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:isRequiredBy")); languages.remove("Proxy/dcterms:isRequiredBy");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:isVersionOf")); languages.remove("Proxy/dcterms:isVersionOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:references")); languages.remove("Proxy/dcterms:references");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:replaces")); languages.remove("Proxy/dcterms:replaces");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:requires")); languages.remove("Proxy/dcterms:requires");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:tableOfContents")); languages.remove("Proxy/dcterms:tableOfContents");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:currentLocation")); languages.remove("Proxy/edm:currentLocation");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:hasMet")); languages.remove("Proxy/edm:hasMet");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:hasType")); languages.remove("Proxy/edm:hasType");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:incorporates")); languages.remove("Proxy/edm:incorporates");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:isDerivativeOf")); languages.remove("Proxy/edm:isDerivativeOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:isRelatedTo")); languages.remove("Proxy/edm:isRelatedTo");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:isRepresentationOf")); languages.remove("Proxy/edm:isRepresentationOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:isSimilarTo")); languages.remove("Proxy/edm:isSimilarTo");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:isSuccessorOf")); languages.remove("Proxy/edm:isSuccessorOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:realizes")); languages.remove("Proxy/edm:realizes");
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/edm:wasPresentAt")); languages.remove("Proxy/edm:wasPresentAt");
    assertEquals(Double.valueOf(-1.0), languages.get("Aggregation/dc:rights")); languages.remove("Aggregation/dc:rights");
    assertEquals(Double.valueOf(-1.0), languages.get("Aggregation/edm:ugc")); languages.remove("Aggregation/edm:ugc");
    assertEquals(Double.valueOf(0.75), languages.get("Aggregation/edm:aggregatedCHO")); languages.remove("Aggregation/edm:aggregatedCHO");
    assertEquals(Double.valueOf(-1.0), languages.get("Aggregation/edm:intermediateProvider")); languages.remove("Aggregation/edm:intermediateProvider");
    assertEquals(Double.valueOf(-1.0), languages.get("Place/dcterms:isPartOf")); languages.remove("Place/dcterms:isPartOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Place/dcterms:hasPart")); languages.remove("Place/dcterms:hasPart");
    assertEquals(Double.valueOf(-1.0), languages.get("Place/skos:prefLabel")); languages.remove("Place/skos:prefLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Place/skos:altLabel")); languages.remove("Place/skos:altLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Place/skos:note")); languages.remove("Place/skos:note");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/edm:begin")); languages.remove("Agent/edm:begin");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/edm:end")); languages.remove("Agent/edm:end");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/edm:hasMet")); languages.remove("Agent/edm:hasMet");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/edm:isRelatedTo")); languages.remove("Agent/edm:isRelatedTo");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/owl:sameAs")); languages.remove("Agent/owl:sameAs");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/foaf:name")); languages.remove("Agent/foaf:name");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/dc:date")); languages.remove("Agent/dc:date");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/dc:identifier")); languages.remove("Agent/dc:identifier");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:dateOfBirth")); languages.remove("Agent/rdaGr2:dateOfBirth");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:placeOfBirth")); languages.remove("Agent/rdaGr2:placeOfBirth");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:dateOfDeath")); languages.remove("Agent/rdaGr2:dateOfDeath");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:placeOfDeath")); languages.remove("Agent/rdaGr2:placeOfDeath");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:dateOfEstablishment")); languages.remove("Agent/rdaGr2:dateOfEstablishment");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:dateOfTermination")); languages.remove("Agent/rdaGr2:dateOfTermination");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:gender")); languages.remove("Agent/rdaGr2:gender");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:professionOrOccupation")); languages.remove("Agent/rdaGr2:professionOrOccupation");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/rdaGr2:biographicalInformation")); languages.remove("Agent/rdaGr2:biographicalInformation");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/skos:prefLabel")); languages.remove("Agent/skos:prefLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/skos:altLabel")); languages.remove("Agent/skos:altLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Agent/skos:note")); languages.remove("Agent/skos:note");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/edm:begin")); languages.remove("Timespan/edm:begin");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/edm:end")); languages.remove("Timespan/edm:end");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/dcterms:isPartOf")); languages.remove("Timespan/dcterms:isPartOf");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/dcterms:hasPart")); languages.remove("Timespan/dcterms:hasPart");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/edm:isNextInSequence")); languages.remove("Timespan/edm:isNextInSequence");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/owl:sameAs")); languages.remove("Timespan/owl:sameAs");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/skos:prefLabel")); languages.remove("Timespan/skos:prefLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/skos:altLabel")); languages.remove("Timespan/skos:altLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Timespan/skos:note")); languages.remove("Timespan/skos:note");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:broader")); languages.remove("Concept/skos:broader");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:narrower")); languages.remove("Concept/skos:narrower");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:related")); languages.remove("Concept/skos:related");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:broadMatch")); languages.remove("Concept/skos:broadMatch");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:narrowMatch")); languages.remove("Concept/skos:narrowMatch");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:relatedMatch")); languages.remove("Concept/skos:relatedMatch");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:exactMatch")); languages.remove("Concept/skos:exactMatch");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:closeMatch")); languages.remove("Concept/skos:closeMatch");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:notation")); languages.remove("Concept/skos:notation");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:inScheme")); languages.remove("Concept/skos:inScheme");
    assertEquals(Double.valueOf(0.7222222222222222), languages.get("Concept/skos:prefLabel")); languages.remove("Concept/skos:prefLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:altLabel")); languages.remove("Concept/skos:altLabel");
    assertEquals(Double.valueOf(-1.0), languages.get("Concept/skos:note")); languages.remove("Concept/skos:note");
    assertEquals(Double.valueOf(0.5652173913043478), languages.get("multilingualitySaturation:normalized")); languages.remove("multilingualitySaturation:normalized");

    assertEquals("Language has the following values: " + StringUtils.join(languages.keySet(), ", "),
      0, languages.size());
  }

  @Test
  public void testGetLanguageMapWithPlace() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("general/test-place.json"));
    calculator.measure(cache);

    Map<String, Double> languages = calculator.getSaturationMap();
    assertNotNull(languages);
    assertEquals(105, languages.size());
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:title"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:alternative"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:description"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:creator"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:publisher"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:contributor"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:type"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:identifier"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:language"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:coverage"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dcterms:temporal"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dcterms:spatial"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:subject"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:date"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:created"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dcterms:issued"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dcterms:extent"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:medium"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:provenance"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dcterms:hasPart"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dcterms:isPartOf"));
    assertEquals(Double.valueOf(0.0), languages.get("Proxy/dc:format"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:source"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:rights"));
    assertEquals(Double.valueOf(-1.0), languages.get("Proxy/dc:relation"));

    assertEquals(Double.valueOf(0.75), languages.get("Aggregation/edm:rights"));
    assertEquals(Double.valueOf(0.0), languages.get("Aggregation/edm:provider"));
    assertEquals(Double.valueOf(0.0), languages.get("Aggregation/edm:dataProvider"));

    assertEquals(Double.valueOf(0.6666666666666667), languages.get("Place/dcterms:isPartOf"));
    assertEquals(Double.valueOf(-1.0), languages.get("Place/dcterms:hasPart"));
    assertEquals(Double.valueOf(0.6), languages.get("Place/skos:prefLabel"));
    assertEquals(Double.valueOf(-1.0), languages.get("Place/skos:altLabel"));
    assertEquals(Double.valueOf(-1.0), languages.get("Place/skos:note"));
  }

  @Test
  public void testGetHeaders() {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    List<String> expected = Arrays.asList("lang:Proxy/dc:title", "lang:Proxy/dcterms:alternative", "lang:Proxy/dc:description", "lang:Proxy/dc:creator", "lang:Proxy/dc:publisher", "lang:Proxy/dc:contributor", "lang:Proxy/dc:type", "lang:Proxy/dc:identifier", "lang:Proxy/dc:language", "lang:Proxy/dc:coverage", "lang:Proxy/dcterms:temporal", "lang:Proxy/dcterms:spatial", "lang:Proxy/dc:subject", "lang:Proxy/dc:date", "lang:Proxy/dcterms:created", "lang:Proxy/dcterms:issued", "lang:Proxy/dcterms:extent", "lang:Proxy/dcterms:medium", "lang:Proxy/dcterms:provenance", "lang:Proxy/dcterms:hasPart", "lang:Proxy/dcterms:isPartOf", "lang:Proxy/dc:format", "lang:Proxy/dc:source", "lang:Proxy/dc:rights", "lang:Proxy/dc:relation", "lang:Proxy/edm:year", "lang:Proxy/edm:userTag", "lang:Proxy/dcterms:conformsTo", "lang:Proxy/dcterms:hasFormat", "lang:Proxy/dcterms:hasVersion", "lang:Proxy/dcterms:isFormatOf", "lang:Proxy/dcterms:isReferencedBy", "lang:Proxy/dcterms:isReplacedBy", "lang:Proxy/dcterms:isRequiredBy", "lang:Proxy/dcterms:isVersionOf", "lang:Proxy/dcterms:references", "lang:Proxy/dcterms:replaces", "lang:Proxy/dcterms:requires", "lang:Proxy/dcterms:tableOfContents", "lang:Proxy/edm:currentLocation", "lang:Proxy/edm:hasMet", "lang:Proxy/edm:hasType", "lang:Proxy/edm:incorporates", "lang:Proxy/edm:isDerivativeOf", "lang:Proxy/edm:isRelatedTo", "lang:Proxy/edm:isRepresentationOf", "lang:Proxy/edm:isSimilarTo", "lang:Proxy/edm:isSuccessorOf", "lang:Proxy/edm:realizes", "lang:Proxy/edm:wasPresentAt", "lang:Aggregation/edm:rights", "lang:Aggregation/edm:provider", "lang:Aggregation/edm:dataProvider", "lang:Aggregation/dc:rights", "lang:Aggregation/edm:ugc", "lang:Aggregation/edm:aggregatedCHO", "lang:Aggregation/edm:intermediateProvider", "lang:Place/dcterms:isPartOf", "lang:Place/dcterms:hasPart", "lang:Place/skos:prefLabel", "lang:Place/skos:altLabel", "lang:Place/skos:note", "lang:Agent/edm:begin", "lang:Agent/edm:end", "lang:Agent/edm:hasMet", "lang:Agent/edm:isRelatedTo", "lang:Agent/owl:sameAs", "lang:Agent/foaf:name", "lang:Agent/dc:date", "lang:Agent/dc:identifier", "lang:Agent/rdaGr2:dateOfBirth", "lang:Agent/rdaGr2:placeOfBirth", "lang:Agent/rdaGr2:dateOfDeath", "lang:Agent/rdaGr2:placeOfDeath", "lang:Agent/rdaGr2:dateOfEstablishment", "lang:Agent/rdaGr2:dateOfTermination", "lang:Agent/rdaGr2:gender", "lang:Agent/rdaGr2:professionOrOccupation", "lang:Agent/rdaGr2:biographicalInformation", "lang:Agent/skos:prefLabel", "lang:Agent/skos:altLabel", "lang:Agent/skos:note", "lang:Timespan/edm:begin", "lang:Timespan/edm:end", "lang:Timespan/dcterms:isPartOf", "lang:Timespan/dcterms:hasPart", "lang:Timespan/edm:isNextInSequence", "lang:Timespan/owl:sameAs", "lang:Timespan/skos:prefLabel", "lang:Timespan/skos:altLabel", "lang:Timespan/skos:note", "lang:Concept/skos:broader", "lang:Concept/skos:narrower", "lang:Concept/skos:related", "lang:Concept/skos:broadMatch", "lang:Concept/skos:narrowMatch", "lang:Concept/skos:relatedMatch", "lang:Concept/skos:exactMatch", "lang:Concept/skos:closeMatch", "lang:Concept/skos:notation", "lang:Concept/skos:inScheme", "lang:Concept/skos:prefLabel", "lang:Concept/skos:altLabel", "lang:Concept/skos:note", "multilingualitySaturation:normalized");
    assertEquals(105, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());

    calculator.setResultType(MultilingualitySaturationCalculator.ResultTypes.EXTENDED);
    expected = Arrays.asList("lang:sum:Proxy/dc:title", "lang:average:Proxy/dc:title", "lang:normalized:Proxy/dc:title", "lang:sum:Proxy/dcterms:alternative", "lang:average:Proxy/dcterms:alternative", "lang:normalized:Proxy/dcterms:alternative", "lang:sum:Proxy/dc:description", "lang:average:Proxy/dc:description", "lang:normalized:Proxy/dc:description", "lang:sum:Proxy/dc:creator", "lang:average:Proxy/dc:creator", "lang:normalized:Proxy/dc:creator", "lang:sum:Proxy/dc:publisher", "lang:average:Proxy/dc:publisher", "lang:normalized:Proxy/dc:publisher", "lang:sum:Proxy/dc:contributor", "lang:average:Proxy/dc:contributor", "lang:normalized:Proxy/dc:contributor", "lang:sum:Proxy/dc:type", "lang:average:Proxy/dc:type", "lang:normalized:Proxy/dc:type", "lang:sum:Proxy/dc:identifier", "lang:average:Proxy/dc:identifier", "lang:normalized:Proxy/dc:identifier", "lang:sum:Proxy/dc:language", "lang:average:Proxy/dc:language", "lang:normalized:Proxy/dc:language", "lang:sum:Proxy/dc:coverage", "lang:average:Proxy/dc:coverage", "lang:normalized:Proxy/dc:coverage", "lang:sum:Proxy/dcterms:temporal", "lang:average:Proxy/dcterms:temporal", "lang:normalized:Proxy/dcterms:temporal", "lang:sum:Proxy/dcterms:spatial", "lang:average:Proxy/dcterms:spatial", "lang:normalized:Proxy/dcterms:spatial", "lang:sum:Proxy/dc:subject", "lang:average:Proxy/dc:subject", "lang:normalized:Proxy/dc:subject", "lang:sum:Proxy/dc:date", "lang:average:Proxy/dc:date", "lang:normalized:Proxy/dc:date", "lang:sum:Proxy/dcterms:created", "lang:average:Proxy/dcterms:created", "lang:normalized:Proxy/dcterms:created", "lang:sum:Proxy/dcterms:issued", "lang:average:Proxy/dcterms:issued", "lang:normalized:Proxy/dcterms:issued", "lang:sum:Proxy/dcterms:extent", "lang:average:Proxy/dcterms:extent", "lang:normalized:Proxy/dcterms:extent", "lang:sum:Proxy/dcterms:medium", "lang:average:Proxy/dcterms:medium", "lang:normalized:Proxy/dcterms:medium", "lang:sum:Proxy/dcterms:provenance", "lang:average:Proxy/dcterms:provenance", "lang:normalized:Proxy/dcterms:provenance", "lang:sum:Proxy/dcterms:hasPart", "lang:average:Proxy/dcterms:hasPart", "lang:normalized:Proxy/dcterms:hasPart", "lang:sum:Proxy/dcterms:isPartOf", "lang:average:Proxy/dcterms:isPartOf", "lang:normalized:Proxy/dcterms:isPartOf", "lang:sum:Proxy/dc:format", "lang:average:Proxy/dc:format", "lang:normalized:Proxy/dc:format", "lang:sum:Proxy/dc:source", "lang:average:Proxy/dc:source", "lang:normalized:Proxy/dc:source", "lang:sum:Proxy/dc:rights", "lang:average:Proxy/dc:rights", "lang:normalized:Proxy/dc:rights", "lang:sum:Proxy/dc:relation", "lang:average:Proxy/dc:relation", "lang:normalized:Proxy/dc:relation", "lang:sum:Proxy/edm:year", "lang:average:Proxy/edm:year", "lang:normalized:Proxy/edm:year", "lang:sum:Proxy/edm:userTag", "lang:average:Proxy/edm:userTag", "lang:normalized:Proxy/edm:userTag", "lang:sum:Proxy/dcterms:conformsTo", "lang:average:Proxy/dcterms:conformsTo", "lang:normalized:Proxy/dcterms:conformsTo", "lang:sum:Proxy/dcterms:hasFormat", "lang:average:Proxy/dcterms:hasFormat", "lang:normalized:Proxy/dcterms:hasFormat", "lang:sum:Proxy/dcterms:hasVersion", "lang:average:Proxy/dcterms:hasVersion", "lang:normalized:Proxy/dcterms:hasVersion", "lang:sum:Proxy/dcterms:isFormatOf", "lang:average:Proxy/dcterms:isFormatOf", "lang:normalized:Proxy/dcterms:isFormatOf", "lang:sum:Proxy/dcterms:isReferencedBy", "lang:average:Proxy/dcterms:isReferencedBy", "lang:normalized:Proxy/dcterms:isReferencedBy", "lang:sum:Proxy/dcterms:isReplacedBy", "lang:average:Proxy/dcterms:isReplacedBy", "lang:normalized:Proxy/dcterms:isReplacedBy", "lang:sum:Proxy/dcterms:isRequiredBy", "lang:average:Proxy/dcterms:isRequiredBy", "lang:normalized:Proxy/dcterms:isRequiredBy", "lang:sum:Proxy/dcterms:isVersionOf", "lang:average:Proxy/dcterms:isVersionOf", "lang:normalized:Proxy/dcterms:isVersionOf", "lang:sum:Proxy/dcterms:references", "lang:average:Proxy/dcterms:references", "lang:normalized:Proxy/dcterms:references", "lang:sum:Proxy/dcterms:replaces", "lang:average:Proxy/dcterms:replaces", "lang:normalized:Proxy/dcterms:replaces", "lang:sum:Proxy/dcterms:requires", "lang:average:Proxy/dcterms:requires", "lang:normalized:Proxy/dcterms:requires", "lang:sum:Proxy/dcterms:tableOfContents", "lang:average:Proxy/dcterms:tableOfContents", "lang:normalized:Proxy/dcterms:tableOfContents", "lang:sum:Proxy/edm:currentLocation", "lang:average:Proxy/edm:currentLocation", "lang:normalized:Proxy/edm:currentLocation", "lang:sum:Proxy/edm:hasMet", "lang:average:Proxy/edm:hasMet", "lang:normalized:Proxy/edm:hasMet", "lang:sum:Proxy/edm:hasType", "lang:average:Proxy/edm:hasType", "lang:normalized:Proxy/edm:hasType", "lang:sum:Proxy/edm:incorporates", "lang:average:Proxy/edm:incorporates", "lang:normalized:Proxy/edm:incorporates", "lang:sum:Proxy/edm:isDerivativeOf", "lang:average:Proxy/edm:isDerivativeOf", "lang:normalized:Proxy/edm:isDerivativeOf", "lang:sum:Proxy/edm:isRelatedTo", "lang:average:Proxy/edm:isRelatedTo", "lang:normalized:Proxy/edm:isRelatedTo", "lang:sum:Proxy/edm:isRepresentationOf", "lang:average:Proxy/edm:isRepresentationOf", "lang:normalized:Proxy/edm:isRepresentationOf", "lang:sum:Proxy/edm:isSimilarTo", "lang:average:Proxy/edm:isSimilarTo", "lang:normalized:Proxy/edm:isSimilarTo", "lang:sum:Proxy/edm:isSuccessorOf", "lang:average:Proxy/edm:isSuccessorOf", "lang:normalized:Proxy/edm:isSuccessorOf", "lang:sum:Proxy/edm:realizes", "lang:average:Proxy/edm:realizes", "lang:normalized:Proxy/edm:realizes", "lang:sum:Proxy/edm:wasPresentAt", "lang:average:Proxy/edm:wasPresentAt", "lang:normalized:Proxy/edm:wasPresentAt", "lang:sum:Aggregation/edm:rights", "lang:average:Aggregation/edm:rights", "lang:normalized:Aggregation/edm:rights", "lang:sum:Aggregation/edm:provider", "lang:average:Aggregation/edm:provider", "lang:normalized:Aggregation/edm:provider", "lang:sum:Aggregation/edm:dataProvider", "lang:average:Aggregation/edm:dataProvider", "lang:normalized:Aggregation/edm:dataProvider", "lang:sum:Aggregation/dc:rights", "lang:average:Aggregation/dc:rights", "lang:normalized:Aggregation/dc:rights", "lang:sum:Aggregation/edm:ugc", "lang:average:Aggregation/edm:ugc", "lang:normalized:Aggregation/edm:ugc", "lang:sum:Aggregation/edm:aggregatedCHO", "lang:average:Aggregation/edm:aggregatedCHO", "lang:normalized:Aggregation/edm:aggregatedCHO", "lang:sum:Aggregation/edm:intermediateProvider", "lang:average:Aggregation/edm:intermediateProvider", "lang:normalized:Aggregation/edm:intermediateProvider", "lang:sum:Place/dcterms:isPartOf", "lang:average:Place/dcterms:isPartOf", "lang:normalized:Place/dcterms:isPartOf", "lang:sum:Place/dcterms:hasPart", "lang:average:Place/dcterms:hasPart", "lang:normalized:Place/dcterms:hasPart", "lang:sum:Place/skos:prefLabel", "lang:average:Place/skos:prefLabel", "lang:normalized:Place/skos:prefLabel", "lang:sum:Place/skos:altLabel", "lang:average:Place/skos:altLabel", "lang:normalized:Place/skos:altLabel", "lang:sum:Place/skos:note", "lang:average:Place/skos:note", "lang:normalized:Place/skos:note", "lang:sum:Agent/edm:begin", "lang:average:Agent/edm:begin", "lang:normalized:Agent/edm:begin", "lang:sum:Agent/edm:end", "lang:average:Agent/edm:end", "lang:normalized:Agent/edm:end", "lang:sum:Agent/edm:hasMet", "lang:average:Agent/edm:hasMet", "lang:normalized:Agent/edm:hasMet", "lang:sum:Agent/edm:isRelatedTo", "lang:average:Agent/edm:isRelatedTo", "lang:normalized:Agent/edm:isRelatedTo", "lang:sum:Agent/owl:sameAs", "lang:average:Agent/owl:sameAs", "lang:normalized:Agent/owl:sameAs", "lang:sum:Agent/foaf:name", "lang:average:Agent/foaf:name", "lang:normalized:Agent/foaf:name", "lang:sum:Agent/dc:date", "lang:average:Agent/dc:date", "lang:normalized:Agent/dc:date", "lang:sum:Agent/dc:identifier", "lang:average:Agent/dc:identifier", "lang:normalized:Agent/dc:identifier", "lang:sum:Agent/rdaGr2:dateOfBirth", "lang:average:Agent/rdaGr2:dateOfBirth", "lang:normalized:Agent/rdaGr2:dateOfBirth", "lang:sum:Agent/rdaGr2:placeOfBirth", "lang:average:Agent/rdaGr2:placeOfBirth", "lang:normalized:Agent/rdaGr2:placeOfBirth", "lang:sum:Agent/rdaGr2:dateOfDeath", "lang:average:Agent/rdaGr2:dateOfDeath", "lang:normalized:Agent/rdaGr2:dateOfDeath", "lang:sum:Agent/rdaGr2:placeOfDeath", "lang:average:Agent/rdaGr2:placeOfDeath", "lang:normalized:Agent/rdaGr2:placeOfDeath", "lang:sum:Agent/rdaGr2:dateOfEstablishment", "lang:average:Agent/rdaGr2:dateOfEstablishment", "lang:normalized:Agent/rdaGr2:dateOfEstablishment", "lang:sum:Agent/rdaGr2:dateOfTermination", "lang:average:Agent/rdaGr2:dateOfTermination", "lang:normalized:Agent/rdaGr2:dateOfTermination", "lang:sum:Agent/rdaGr2:gender", "lang:average:Agent/rdaGr2:gender", "lang:normalized:Agent/rdaGr2:gender", "lang:sum:Agent/rdaGr2:professionOrOccupation", "lang:average:Agent/rdaGr2:professionOrOccupation", "lang:normalized:Agent/rdaGr2:professionOrOccupation", "lang:sum:Agent/rdaGr2:biographicalInformation", "lang:average:Agent/rdaGr2:biographicalInformation", "lang:normalized:Agent/rdaGr2:biographicalInformation", "lang:sum:Agent/skos:prefLabel", "lang:average:Agent/skos:prefLabel", "lang:normalized:Agent/skos:prefLabel", "lang:sum:Agent/skos:altLabel", "lang:average:Agent/skos:altLabel", "lang:normalized:Agent/skos:altLabel", "lang:sum:Agent/skos:note", "lang:average:Agent/skos:note", "lang:normalized:Agent/skos:note", "lang:sum:Timespan/edm:begin", "lang:average:Timespan/edm:begin", "lang:normalized:Timespan/edm:begin", "lang:sum:Timespan/edm:end", "lang:average:Timespan/edm:end", "lang:normalized:Timespan/edm:end", "lang:sum:Timespan/dcterms:isPartOf", "lang:average:Timespan/dcterms:isPartOf", "lang:normalized:Timespan/dcterms:isPartOf", "lang:sum:Timespan/dcterms:hasPart", "lang:average:Timespan/dcterms:hasPart", "lang:normalized:Timespan/dcterms:hasPart", "lang:sum:Timespan/edm:isNextInSequence", "lang:average:Timespan/edm:isNextInSequence", "lang:normalized:Timespan/edm:isNextInSequence", "lang:sum:Timespan/owl:sameAs", "lang:average:Timespan/owl:sameAs", "lang:normalized:Timespan/owl:sameAs", "lang:sum:Timespan/skos:prefLabel", "lang:average:Timespan/skos:prefLabel", "lang:normalized:Timespan/skos:prefLabel", "lang:sum:Timespan/skos:altLabel", "lang:average:Timespan/skos:altLabel", "lang:normalized:Timespan/skos:altLabel", "lang:sum:Timespan/skos:note", "lang:average:Timespan/skos:note", "lang:normalized:Timespan/skos:note", "lang:sum:Concept/skos:broader", "lang:average:Concept/skos:broader", "lang:normalized:Concept/skos:broader", "lang:sum:Concept/skos:narrower", "lang:average:Concept/skos:narrower", "lang:normalized:Concept/skos:narrower", "lang:sum:Concept/skos:related", "lang:average:Concept/skos:related", "lang:normalized:Concept/skos:related", "lang:sum:Concept/skos:broadMatch", "lang:average:Concept/skos:broadMatch", "lang:normalized:Concept/skos:broadMatch", "lang:sum:Concept/skos:narrowMatch", "lang:average:Concept/skos:narrowMatch", "lang:normalized:Concept/skos:narrowMatch", "lang:sum:Concept/skos:relatedMatch", "lang:average:Concept/skos:relatedMatch", "lang:normalized:Concept/skos:relatedMatch", "lang:sum:Concept/skos:exactMatch", "lang:average:Concept/skos:exactMatch", "lang:normalized:Concept/skos:exactMatch", "lang:sum:Concept/skos:closeMatch", "lang:average:Concept/skos:closeMatch", "lang:normalized:Concept/skos:closeMatch", "lang:sum:Concept/skos:notation", "lang:average:Concept/skos:notation", "lang:normalized:Concept/skos:notation", "lang:sum:Concept/skos:inScheme", "lang:average:Concept/skos:inScheme", "lang:normalized:Concept/skos:inScheme", "lang:sum:Concept/skos:prefLabel", "lang:average:Concept/skos:prefLabel", "lang:normalized:Concept/skos:prefLabel", "lang:sum:Concept/skos:altLabel", "lang:average:Concept/skos:altLabel", "lang:normalized:Concept/skos:altLabel", "lang:sum:Concept/skos:note", "lang:average:Concept/skos:note", "lang:normalized:Concept/skos:note", "multilingualitySaturation:sum", "multilingualitySaturation:average", "multilingualitySaturation:normalized");
    assertEquals(315, calculator.getHeader().size());
    assertEquals(expected, calculator.getHeader());

  }

  @Test
  public void testIssue8MultipleSameLanguages() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("issue-examples/issue8-multiple-same-languages.json"));
    calculator.measure(cache);
    Map<String, Double> languages = calculator.getSaturationMap();
    assertEquals(Double.valueOf(0.5), languages.get("Proxy/dc:title"));
    assertEquals(Double.valueOf(0.7058823529411764), languages.get("Place/skos:prefLabel"));
  }

  @Test
  public void testComplexResponse() throws URISyntaxException, IOException {
    MultilingualitySaturationCalculator calculator = new MultilingualitySaturationCalculator(new EdmOaiPmhJsonSchema());
    calculator.setResultType(MultilingualitySaturationCalculator.ResultTypes.EXTENDED);
    JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLineFromResource("issue-examples/issue8-multiple-same-languages.json"));
    calculator.measure(cache);
    String csv = calculator.getCsv(true, CompressionLevel.NORMAL);
    assertEquals("\"Proxy/dc:title:sum\":1.0,\"Proxy/dc:title:average\":1.0,\"Proxy/dc:title:normalized\":0.5,\"Proxy/dcterms:alternative:sum\":-1.0,\"Proxy/dcterms:alternative:average\":-1.0,\"Proxy/dcterms:alternative:normalized\":-1.0,\"Proxy/dc:description:sum\":1.0,\"Proxy/dc:description:average\":1.0,\"Proxy/dc:description:normalized\":0.5,\"Proxy/dc:creator:sum\":0.0,\"Proxy/dc:creator:average\":0.0,\"Proxy/dc:creator:normalized\":0.0,\"Proxy/dc:publisher:sum\":0.0,\"Proxy/dc:publisher:average\":0.0,\"Proxy/dc:publisher:normalized\":0.0,\"Proxy/dc:contributor:sum\":-1.0,\"Proxy/dc:contributor:average\":-1.0,\"Proxy/dc:contributor:normalized\":-1.0,\"Proxy/dc:type:sum\":3.0,\"Proxy/dc:type:average\":3.0,\"Proxy/dc:type:normalized\":0.75,\"Proxy/dc:identifier:sum\":0.0,\"Proxy/dc:identifier:average\":0.0,\"Proxy/dc:identifier:normalized\":0.0,\"Proxy/dc:language:sum\":0.0,\"Proxy/dc:language:average\":0.0,\"Proxy/dc:language:normalized\":0.0,\"Proxy/dc:coverage:sum\":2.0,\"Proxy/dc:coverage:average\":2.0,\"Proxy/dc:coverage:normalized\":0.666667,\"Proxy/dcterms:temporal:sum\":-1.0,\"Proxy/dcterms:temporal:average\":-1.0,\"Proxy/dcterms:temporal:normalized\":-1.0,\"Proxy/dcterms:spatial:sum\":0.0,\"Proxy/dcterms:spatial:average\":0.0,\"Proxy/dcterms:spatial:normalized\":0.0,\"Proxy/dc:subject:sum\":3.0,\"Proxy/dc:subject:average\":3.0,\"Proxy/dc:subject:normalized\":0.75,\"Proxy/dc:date:sum\":-1.0,\"Proxy/dc:date:average\":-1.0,\"Proxy/dc:date:normalized\":-1.0,\"Proxy/dcterms:created:sum\":0.0,\"Proxy/dcterms:created:average\":0.0,\"Proxy/dcterms:created:normalized\":0.0,\"Proxy/dcterms:issued:sum\":-1.0,\"Proxy/dcterms:issued:average\":-1.0,\"Proxy/dcterms:issued:normalized\":-1.0,\"Proxy/dcterms:extent:sum\":2.0,\"Proxy/dcterms:extent:average\":2.0,\"Proxy/dcterms:extent:normalized\":0.666667,\"Proxy/dcterms:medium:sum\":-1.0,\"Proxy/dcterms:medium:average\":-1.0,\"Proxy/dcterms:medium:normalized\":-1.0,\"Proxy/dcterms:provenance:sum\":1.0,\"Proxy/dcterms:provenance:average\":1.0,\"Proxy/dcterms:provenance:normalized\":0.5,\"Proxy/dcterms:hasPart:sum\":-1.0,\"Proxy/dcterms:hasPart:average\":-1.0,\"Proxy/dcterms:hasPart:normalized\":-1.0,\"Proxy/dcterms:isPartOf:sum\":-1.0,\"Proxy/dcterms:isPartOf:average\":-1.0,\"Proxy/dcterms:isPartOf:normalized\":-1.0,\"Proxy/dc:format:sum\":3.0,\"Proxy/dc:format:average\":3.0,\"Proxy/dc:format:normalized\":0.75,\"Proxy/dc:source:sum\":-1.0,\"Proxy/dc:source:average\":-1.0,\"Proxy/dc:source:normalized\":-1.0,\"Proxy/dc:rights:sum\":2.0,\"Proxy/dc:rights:average\":2.0,\"Proxy/dc:rights:normalized\":0.666667,\"Proxy/dc:relation:sum\":-1.0,\"Proxy/dc:relation:average\":-1.0,\"Proxy/dc:relation:normalized\":-1.0,\"Proxy/edm:year:sum\":-1.0,\"Proxy/edm:year:average\":-1.0,\"Proxy/edm:year:normalized\":-1.0,\"Proxy/edm:userTag:sum\":-1.0,\"Proxy/edm:userTag:average\":-1.0,\"Proxy/edm:userTag:normalized\":-1.0,\"Proxy/dcterms:conformsTo:sum\":-1.0,\"Proxy/dcterms:conformsTo:average\":-1.0,\"Proxy/dcterms:conformsTo:normalized\":-1.0,\"Proxy/dcterms:hasFormat:sum\":-1.0,\"Proxy/dcterms:hasFormat:average\":-1.0,\"Proxy/dcterms:hasFormat:normalized\":-1.0,\"Proxy/dcterms:hasVersion:sum\":-1.0,\"Proxy/dcterms:hasVersion:average\":-1.0,\"Proxy/dcterms:hasVersion:normalized\":-1.0,\"Proxy/dcterms:isFormatOf:sum\":-1.0,\"Proxy/dcterms:isFormatOf:average\":-1.0,\"Proxy/dcterms:isFormatOf:normalized\":-1.0,\"Proxy/dcterms:isReferencedBy:sum\":0.0,\"Proxy/dcterms:isReferencedBy:average\":0.0,\"Proxy/dcterms:isReferencedBy:normalized\":0.0,\"Proxy/dcterms:isReplacedBy:sum\":-1.0,\"Proxy/dcterms:isReplacedBy:average\":-1.0,\"Proxy/dcterms:isReplacedBy:normalized\":-1.0,\"Proxy/dcterms:isRequiredBy:sum\":-1.0,\"Proxy/dcterms:isRequiredBy:average\":-1.0,\"Proxy/dcterms:isRequiredBy:normalized\":-1.0,\"Proxy/dcterms:isVersionOf:sum\":-1.0,\"Proxy/dcterms:isVersionOf:average\":-1.0,\"Proxy/dcterms:isVersionOf:normalized\":-1.0,\"Proxy/dcterms:references:sum\":-1.0,\"Proxy/dcterms:references:average\":-1.0,\"Proxy/dcterms:references:normalized\":-1.0,\"Proxy/dcterms:replaces:sum\":-1.0,\"Proxy/dcterms:replaces:average\":-1.0,\"Proxy/dcterms:replaces:normalized\":-1.0,\"Proxy/dcterms:requires:sum\":-1.0,\"Proxy/dcterms:requires:average\":-1.0,\"Proxy/dcterms:requires:normalized\":-1.0,\"Proxy/dcterms:tableOfContents:sum\":-1.0,\"Proxy/dcterms:tableOfContents:average\":-1.0,\"Proxy/dcterms:tableOfContents:normalized\":-1.0,\"Proxy/edm:currentLocation:sum\":-1.0,\"Proxy/edm:currentLocation:average\":-1.0,\"Proxy/edm:currentLocation:normalized\":-1.0,\"Proxy/edm:hasMet:sum\":-1.0,\"Proxy/edm:hasMet:average\":-1.0,\"Proxy/edm:hasMet:normalized\":-1.0,\"Proxy/edm:hasType:sum\":-1.0,\"Proxy/edm:hasType:average\":-1.0,\"Proxy/edm:hasType:normalized\":-1.0,\"Proxy/edm:incorporates:sum\":-1.0,\"Proxy/edm:incorporates:average\":-1.0,\"Proxy/edm:incorporates:normalized\":-1.0,\"Proxy/edm:isDerivativeOf:sum\":-1.0,\"Proxy/edm:isDerivativeOf:average\":-1.0,\"Proxy/edm:isDerivativeOf:normalized\":-1.0,\"Proxy/edm:isRelatedTo:sum\":-1.0,\"Proxy/edm:isRelatedTo:average\":-1.0,\"Proxy/edm:isRelatedTo:normalized\":-1.0,\"Proxy/edm:isRepresentationOf:sum\":-1.0,\"Proxy/edm:isRepresentationOf:average\":-1.0,\"Proxy/edm:isRepresentationOf:normalized\":-1.0,\"Proxy/edm:isSimilarTo:sum\":-1.0,\"Proxy/edm:isSimilarTo:average\":-1.0,\"Proxy/edm:isSimilarTo:normalized\":-1.0,\"Proxy/edm:isSuccessorOf:sum\":-1.0,\"Proxy/edm:isSuccessorOf:average\":-1.0,\"Proxy/edm:isSuccessorOf:normalized\":-1.0,\"Proxy/edm:realizes:sum\":-1.0,\"Proxy/edm:realizes:average\":-1.0,\"Proxy/edm:realizes:normalized\":-1.0,\"Proxy/edm:wasPresentAt:sum\":-1.0,\"Proxy/edm:wasPresentAt:average\":-1.0,\"Proxy/edm:wasPresentAt:normalized\":-1.0,\"Aggregation/edm:rights:sum\":3.0,\"Aggregation/edm:rights:average\":3.0,\"Aggregation/edm:rights:normalized\":0.75,\"Aggregation/edm:provider:sum\":0.0,\"Aggregation/edm:provider:average\":0.0,\"Aggregation/edm:provider:normalized\":0.0,\"Aggregation/edm:dataProvider:sum\":0.0,\"Aggregation/edm:dataProvider:average\":0.0,\"Aggregation/edm:dataProvider:normalized\":0.0,\"Aggregation/dc:rights:sum\":2.0,\"Aggregation/dc:rights:average\":2.0,\"Aggregation/dc:rights:normalized\":0.666667,\"Aggregation/edm:ugc:sum\":-1.0,\"Aggregation/edm:ugc:average\":-1.0,\"Aggregation/edm:ugc:normalized\":-1.0,\"Aggregation/edm:aggregatedCHO:sum\":3.0,\"Aggregation/edm:aggregatedCHO:average\":3.0,\"Aggregation/edm:aggregatedCHO:normalized\":0.75,\"Aggregation/edm:intermediateProvider:sum\":-1.0,\"Aggregation/edm:intermediateProvider:average\":-1.0,\"Aggregation/edm:intermediateProvider:normalized\":-1.0,\"Place/dcterms:isPartOf:sum\":-1.0,\"Place/dcterms:isPartOf:average\":-1.0,\"Place/dcterms:isPartOf:normalized\":-1.0,\"Place/dcterms:hasPart:sum\":-1.0,\"Place/dcterms:hasPart:average\":-1.0,\"Place/dcterms:hasPart:normalized\":-1.0,\"Place/skos:prefLabel:sum\":2.4,\"Place/skos:prefLabel:average\":2.4,\"Place/skos:prefLabel:normalized\":0.705882,\"Place/skos:altLabel:sum\":-1.0,\"Place/skos:altLabel:average\":-1.0,\"Place/skos:altLabel:normalized\":-1.0,\"Place/skos:note:sum\":-1.0,\"Place/skos:note:average\":-1.0,\"Place/skos:note:normalized\":-1.0,\"Agent/edm:begin:sum\":-1.0,\"Agent/edm:begin:average\":-1.0,\"Agent/edm:begin:normalized\":-1.0,\"Agent/edm:end:sum\":-1.0,\"Agent/edm:end:average\":-1.0,\"Agent/edm:end:normalized\":-1.0,\"Agent/edm:hasMet:sum\":-1.0,\"Agent/edm:hasMet:average\":-1.0,\"Agent/edm:hasMet:normalized\":-1.0,\"Agent/edm:isRelatedTo:sum\":-1.0,\"Agent/edm:isRelatedTo:average\":-1.0,\"Agent/edm:isRelatedTo:normalized\":-1.0,\"Agent/owl:sameAs:sum\":-1.0,\"Agent/owl:sameAs:average\":-1.0,\"Agent/owl:sameAs:normalized\":-1.0,\"Agent/foaf:name:sum\":-1.0,\"Agent/foaf:name:average\":-1.0,\"Agent/foaf:name:normalized\":-1.0,\"Agent/dc:date:sum\":-1.0,\"Agent/dc:date:average\":-1.0,\"Agent/dc:date:normalized\":-1.0,\"Agent/dc:identifier:sum\":-1.0,\"Agent/dc:identifier:average\":-1.0,\"Agent/dc:identifier:normalized\":-1.0,\"Agent/rdaGr2:dateOfBirth:sum\":-1.0,\"Agent/rdaGr2:dateOfBirth:average\":-1.0,\"Agent/rdaGr2:dateOfBirth:normalized\":-1.0,\"Agent/rdaGr2:placeOfBirth:sum\":-1.0,\"Agent/rdaGr2:placeOfBirth:average\":-1.0,\"Agent/rdaGr2:placeOfBirth:normalized\":-1.0,\"Agent/rdaGr2:dateOfDeath:sum\":-1.0,\"Agent/rdaGr2:dateOfDeath:average\":-1.0,\"Agent/rdaGr2:dateOfDeath:normalized\":-1.0,\"Agent/rdaGr2:placeOfDeath:sum\":-1.0,\"Agent/rdaGr2:placeOfDeath:average\":-1.0,\"Agent/rdaGr2:placeOfDeath:normalized\":-1.0,\"Agent/rdaGr2:dateOfEstablishment:sum\":-1.0,\"Agent/rdaGr2:dateOfEstablishment:average\":-1.0,\"Agent/rdaGr2:dateOfEstablishment:normalized\":-1.0,\"Agent/rdaGr2:dateOfTermination:sum\":-1.0,\"Agent/rdaGr2:dateOfTermination:average\":-1.0,\"Agent/rdaGr2:dateOfTermination:normalized\":-1.0,\"Agent/rdaGr2:gender:sum\":-1.0,\"Agent/rdaGr2:gender:average\":-1.0,\"Agent/rdaGr2:gender:normalized\":-1.0,\"Agent/rdaGr2:professionOrOccupation:sum\":1.0,\"Agent/rdaGr2:professionOrOccupation:average\":0.5,\"Agent/rdaGr2:professionOrOccupation:normalized\":0.333333,\"Agent/rdaGr2:biographicalInformation:sum\":-1.0,\"Agent/rdaGr2:biographicalInformation:average\":-1.0,\"Agent/rdaGr2:biographicalInformation:normalized\":-1.0,\"Agent/skos:prefLabel:sum\":0.0,\"Agent/skos:prefLabel:average\":0.0,\"Agent/skos:prefLabel:normalized\":0.0,\"Agent/skos:altLabel:sum\":-1.0,\"Agent/skos:altLabel:average\":-1.0,\"Agent/skos:altLabel:normalized\":-1.0,\"Agent/skos:note:sum\":-1.0,\"Agent/skos:note:average\":-1.0,\"Agent/skos:note:normalized\":-1.0,\"Timespan/edm:begin:sum\":-1.0,\"Timespan/edm:begin:average\":-1.0,\"Timespan/edm:begin:normalized\":-1.0,\"Timespan/edm:end:sum\":-1.0,\"Timespan/edm:end:average\":-1.0,\"Timespan/edm:end:normalized\":-1.0,\"Timespan/dcterms:isPartOf:sum\":-1.0,\"Timespan/dcterms:isPartOf:average\":-1.0,\"Timespan/dcterms:isPartOf:normalized\":-1.0,\"Timespan/dcterms:hasPart:sum\":-1.0,\"Timespan/dcterms:hasPart:average\":-1.0,\"Timespan/dcterms:hasPart:normalized\":-1.0,\"Timespan/edm:isNextInSequence:sum\":-1.0,\"Timespan/edm:isNextInSequence:average\":-1.0,\"Timespan/edm:isNextInSequence:normalized\":-1.0,\"Timespan/owl:sameAs:sum\":-1.0,\"Timespan/owl:sameAs:average\":-1.0,\"Timespan/owl:sameAs:normalized\":-1.0,\"Timespan/skos:prefLabel:sum\":-1.0,\"Timespan/skos:prefLabel:average\":-1.0,\"Timespan/skos:prefLabel:normalized\":-1.0,\"Timespan/skos:altLabel:sum\":-1.0,\"Timespan/skos:altLabel:average\":-1.0,\"Timespan/skos:altLabel:normalized\":-1.0,\"Timespan/skos:note:sum\":-1.0,\"Timespan/skos:note:average\":-1.0,\"Timespan/skos:note:normalized\":-1.0,\"Concept/skos:broader:sum\":3.0,\"Concept/skos:broader:average\":1.0,\"Concept/skos:broader:normalized\":0.5,\"Concept/skos:narrower:sum\":3.0,\"Concept/skos:narrower:average\":1.0,\"Concept/skos:narrower:normalized\":0.5,\"Concept/skos:related:sum\":-1.0,\"Concept/skos:related:average\":-1.0,\"Concept/skos:related:normalized\":-1.0,\"Concept/skos:broadMatch:sum\":-1.0,\"Concept/skos:broadMatch:average\":-1.0,\"Concept/skos:broadMatch:normalized\":-1.0,\"Concept/skos:narrowMatch:sum\":-1.0,\"Concept/skos:narrowMatch:average\":-1.0,\"Concept/skos:narrowMatch:normalized\":-1.0,\"Concept/skos:relatedMatch:sum\":-1.0,\"Concept/skos:relatedMatch:average\":-1.0,\"Concept/skos:relatedMatch:normalized\":-1.0,\"Concept/skos:exactMatch:sum\":-1.0,\"Concept/skos:exactMatch:average\":-1.0,\"Concept/skos:exactMatch:normalized\":-1.0,\"Concept/skos:closeMatch:sum\":-1.0,\"Concept/skos:closeMatch:average\":-1.0,\"Concept/skos:closeMatch:normalized\":-1.0,\"Concept/skos:notation:sum\":-1.0,\"Concept/skos:notation:average\":-1.0,\"Concept/skos:notation:normalized\":-1.0,\"Concept/skos:inScheme:sum\":-1.0,\"Concept/skos:inScheme:average\":-1.0,\"Concept/skos:inScheme:normalized\":-1.0,\"Concept/skos:prefLabel:sum\":2.3,\"Concept/skos:prefLabel:average\":0.766667,\"Concept/skos:prefLabel:normalized\":0.433962,\"Concept/skos:altLabel:sum\":2.3,\"Concept/skos:altLabel:average\":0.766667,\"Concept/skos:altLabel:normalized\":0.433962,\"Concept/skos:note:sum\":0.0,\"Concept/skos:note:average\":0.0,\"Concept/skos:note:normalized\":0.0,\"multilingualitySaturation:sum\":40.0,\"multilingualitySaturation:average\":1.37931,\"multilingualitySaturation:normalized\":0.57971", csv);

    csv = calculator.getCsv(false, CompressionLevel.NORMAL);
    assertEquals("1.0,1.0,0.5,-1.0,-1.0,-1.0,1.0,1.0,0.5,0.0,0.0,0.0,0.0,0.0,0.0,-1.0,-1.0,-1.0,3.0,3.0,0.75,0.0,0.0,0.0,0.0,0.0,0.0,2.0,2.0,0.666667,-1.0,-1.0,-1.0,0.0,0.0,0.0,3.0,3.0,0.75,-1.0,-1.0,-1.0,0.0,0.0,0.0,-1.0,-1.0,-1.0,2.0,2.0,0.666667,-1.0,-1.0,-1.0,1.0,1.0,0.5,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,3.0,3.0,0.75,-1.0,-1.0,-1.0,2.0,2.0,0.666667,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,0.0,0.0,0.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,3.0,3.0,0.75,0.0,0.0,0.0,0.0,0.0,0.0,2.0,2.0,0.666667,-1.0,-1.0,-1.0,3.0,3.0,0.75,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,2.4,2.4,0.705882,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,1.0,0.5,0.333333,-1.0,-1.0,-1.0,0.0,0.0,0.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,3.0,1.0,0.5,3.0,1.0,0.5,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,-1.0,2.3,0.766667,0.433962,2.3,0.766667,0.433962,0.0,0.0,0.0,40.0,1.37931,0.57971", csv);

    csv = calculator.getCsv(false, CompressionLevel.WITHOUT_TRAILING_ZEROS);
    assertEquals("1,1,0.5,-1,-1,-1,1,1,0.5,0,0,0,0,0,0,-1,-1,-1,3,3,0.75,0,0,0,0,0,0,2,2,0.666667,-1,-1,-1,0,0,0,3,3,0.75,-1,-1,-1,0,0,0,-1,-1,-1,2,2,0.666667,-1,-1,-1,1,1,0.5,-1,-1,-1,-1,-1,-1,3,3,0.75,-1,-1,-1,2,2,0.666667,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,3,0.75,0,0,0,0,0,0,2,2,0.666667,-1,-1,-1,3,3,0.75,-1,-1,-1,-1,-1,-1,-1,-1,-1,2.4,2.4,0.705882,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,0.5,0.333333,-1,-1,-1,0,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,1,0.5,3,1,0.5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,2.3,0.766667,0.433962,2.3,0.766667,0.433962,0,0,0,40,1.37931,0.57971", csv);
  }
}
