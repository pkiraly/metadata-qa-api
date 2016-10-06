package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
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
public class LanguageSaturationCalculatorTest {

	public LanguageSaturationCalculatorTest() {
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
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		assertNotNull(calculator);
	}

	@Test
	public void testMeasure() throws URISyntaxException, IOException {
		LanguageSaturationCalculator calculator = new LanguageSaturationCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);
		assertNotNull(calculator.getCsv(false, true));
		assertEquals("0.666667,0.0,0.0,0.0,0.0,0.0,0.5,0.5,0.0,0.0,0.0,0.0,0.769231,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.8,0.0,0.0,0.5,0.0,0.5,0.0,0.0,0.8,0.8,0.0,0.5,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.8,0.666667,0.5,0.0,0.0,0.8,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.785714,0.0,0.0", calculator.getCsv(false, true));

		cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);
		assertNotNull(calculator.getCsv(false, true));
		assertEquals("0.5,0.0,0.5,0.5,0.0,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.0,0.0,0.5,0.5,0.0,0.0,0.5,0.0,0.5,0.0,0.0,0.0,0.5,0.0,0.0,0.8,0.8,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.8,0.5,0.5,0.0,0.0,0.8,0.0,0.727273,0.0,0.5,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.5,0.5,0.666667,0.0,0.0,0.0,0.666667,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0", calculator.getCsv(false, true));
	}

	@Test
	public void testCountersGetLanguageMap() throws URISyntaxException, IOException {
		LanguageSaturationCalculator calculator = new LanguageSaturationCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);
		String languages = calculator.getCsv(true, true);
		assertNotNull(languages);
		assertEquals(
			"\"Proxy/dc:title\":0.666667,\"Proxy/dcterms:alternative\":0.0,\"Proxy/dc:description\":0.0,\"Proxy/dc:creator\":0.0,\"Proxy/dc:publisher\":0.0,\"Proxy/dc:contributor\":0.0,\"Proxy/dc:type\":0.5,\"Proxy/dc:identifier\":0.5,\"Proxy/dc:language\":0.0,\"Proxy/dc:coverage\":0.0,\"Proxy/dcterms:temporal\":0.0,\"Proxy/dcterms:spatial\":0.0,\"Proxy/dc:subject\":0.769231,\"Proxy/dc:date\":0.0,\"Proxy/dcterms:created\":0.0,\"Proxy/dcterms:issued\":0.0,\"Proxy/dcterms:extent\":0.0,\"Proxy/dcterms:medium\":0.0,\"Proxy/dcterms:provenance\":0.0,\"Proxy/dcterms:hasPart\":0.0,\"Proxy/dcterms:isPartOf\":0.8,\"Proxy/dc:format\":0.0,\"Proxy/dc:source\":0.0,\"Proxy/dc:rights\":0.5,\"Proxy/dc:relation\":0.0,\"Proxy/edm:europeanaProxy\":0.5,\"Proxy/edm:year\":0.0,\"Proxy/edm:userTag\":0.0,\"Proxy/ore:proxyIn\":0.8,\"Proxy/ore:proxyFor\":0.8,\"Proxy/dcterms:conformsTo\":0.0,\"Proxy/dcterms:hasFormat\":0.5,\"Proxy/dcterms:hasVersion\":0.0,\"Proxy/dcterms:isFormatOf\":0.0,\"Proxy/dcterms:isReferencedBy\":0.0,\"Proxy/dcterms:isReplacedBy\":0.0,\"Proxy/dcterms:isRequiredBy\":0.0,\"Proxy/dcterms:isVersionOf\":0.0,\"Proxy/dcterms:references\":0.0,\"Proxy/dcterms:replaces\":0.0,\"Proxy/dcterms:requires\":0.0,\"Proxy/dcterms:tableOfContents\":0.0,\"Proxy/edm:currentLocation\":0.0,\"Proxy/edm:hasMet\":0.0,\"Proxy/edm:hasType\":0.0,\"Proxy/edm:incorporates\":0.0,\"Proxy/edm:isDerivativeOf\":0.0,\"Proxy/edm:isRelatedTo\":0.0,\"Proxy/edm:isRepresentationOf\":0.0,\"Proxy/edm:isSimilarTo\":0.0,\"Proxy/edm:isSuccessorOf\":0.0,\"Proxy/edm:realizes\":0.0,\"Proxy/edm:wasPresentAt\":0.0,\"Aggregation/edm:rights\":0.8,\"Aggregation/edm:provider\":0.666667,\"Aggregation/edm:dataProvider\":0.5,\"Aggregation/dc:rights\":0.0,\"Aggregation/edm:ugc\":0.0,\"Aggregation/edm:aggregatedCHO\":0.8,\"Aggregation/edm:intermediateProvider\":0.0,\"Place/dcterms:isPartOf\":0.0,\"Place/dcterms:hasPart\":0.0,\"Place/skos:prefLabel\":0.0,\"Place/skos:altLabel\":0.0,\"Place/skos:note\":0.0,\"Agent/edm:begin\":0.0,\"Agent/edm:end\":0.0,\"Agent/edm:hasMet\":0.0,\"Agent/edm:isRelatedTo\":0.0,\"Agent/owl:sameAs\":0.0,\"Agent/foaf:name\":0.0,\"Agent/dc:date\":0.0,\"Agent/dc:identifier\":0.0,\"Agent/rdaGr2:dateOfBirth\":0.0,\"Agent/rdaGr2:placeOfBirth\":0.0,\"Agent/rdaGr2:dateOfDeath\":0.0,\"Agent/rdaGr2:placeOfDeath\":0.0,\"Agent/rdaGr2:dateOfEstablishment\":0.0,\"Agent/rdaGr2:dateOfTermination\":0.0,\"Agent/rdaGr2:gender\":0.0,\"Agent/rdaGr2:professionOrOccupation\":0.0,\"Agent/rdaGr2:biographicalInformation\":0.0,\"Agent/skos:prefLabel\":0.0,\"Agent/skos:altLabel\":0.0,\"Agent/skos:note\":0.0,\"Timespan/edm:begin\":0.0,\"Timespan/edm:end\":0.0,\"Timespan/dcterms:isPartOf\":0.0,\"Timespan/dcterms:hasPart\":0.0,\"Timespan/edm:isNextInSequence\":0.0,\"Timespan/owl:sameAs\":0.0,\"Timespan/skos:prefLabel\":0.0,\"Timespan/skos:altLabel\":0.0,\"Timespan/skos:note\":0.0,\"Concept/skos:broader\":0.0,\"Concept/skos:narrower\":0.0,\"Concept/skos:related\":0.0,\"Concept/skos:broadMatch\":0.0,\"Concept/skos:narrowMatch\":0.0,\"Concept/skos:relatedMatch\":0.0,\"Concept/skos:exactMatch\":0.0,\"Concept/skos:closeMatch\":0.0,\"Concept/skos:notation\":0.0,\"Concept/skos:inScheme\":0.0,\"Concept/skos:prefLabel\":0.785714,\"Concept/skos:altLabel\":0.0,\"Concept/skos:note\":0.0",
			languages
		);
	}

	@Test
	public void testCountersGetLanguageMapForPLace() throws URISyntaxException, IOException {
		LanguageSaturationCalculator calculator = new LanguageSaturationCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);
		String languages = calculator.getCsv(true, true);
		assertNotNull(languages);
		assertEquals(
			"\"Proxy/dc:title\":0.5,\"Proxy/dcterms:alternative\":0.0,\"Proxy/dc:description\":0.5,\"Proxy/dc:creator\":0.5,\"Proxy/dc:publisher\":0.0,\"Proxy/dc:contributor\":0.5,\"Proxy/dc:type\":0.5,\"Proxy/dc:identifier\":0.5,\"Proxy/dc:language\":0.5,\"Proxy/dc:coverage\":0.5,\"Proxy/dcterms:temporal\":0.5,\"Proxy/dcterms:spatial\":0.5,\"Proxy/dc:subject\":0.5,\"Proxy/dc:date\":0.0,\"Proxy/dcterms:created\":0.0,\"Proxy/dcterms:issued\":0.5,\"Proxy/dcterms:extent\":0.5,\"Proxy/dcterms:medium\":0.0,\"Proxy/dcterms:provenance\":0.0,\"Proxy/dcterms:hasPart\":0.5,\"Proxy/dcterms:isPartOf\":0.0,\"Proxy/dc:format\":0.5,\"Proxy/dc:source\":0.0,\"Proxy/dc:rights\":0.0,\"Proxy/dc:relation\":0.0,\"Proxy/edm:europeanaProxy\":0.5,\"Proxy/edm:year\":0.0,\"Proxy/edm:userTag\":0.0,\"Proxy/ore:proxyIn\":0.8,\"Proxy/ore:proxyFor\":0.8,\"Proxy/dcterms:conformsTo\":0.0,\"Proxy/dcterms:hasFormat\":0.0,\"Proxy/dcterms:hasVersion\":0.0,\"Proxy/dcterms:isFormatOf\":0.0,\"Proxy/dcterms:isReferencedBy\":0.0,\"Proxy/dcterms:isReplacedBy\":0.0,\"Proxy/dcterms:isRequiredBy\":0.0,\"Proxy/dcterms:isVersionOf\":0.0,\"Proxy/dcterms:references\":0.0,\"Proxy/dcterms:replaces\":0.0,\"Proxy/dcterms:requires\":0.0,\"Proxy/dcterms:tableOfContents\":0.0,\"Proxy/edm:currentLocation\":0.0,\"Proxy/edm:hasMet\":0.0,\"Proxy/edm:hasType\":0.0,\"Proxy/edm:incorporates\":0.0,\"Proxy/edm:isDerivativeOf\":0.0,\"Proxy/edm:isRelatedTo\":0.0,\"Proxy/edm:isRepresentationOf\":0.0,\"Proxy/edm:isSimilarTo\":0.0,\"Proxy/edm:isSuccessorOf\":0.0,\"Proxy/edm:realizes\":0.0,\"Proxy/edm:wasPresentAt\":0.0,\"Aggregation/edm:rights\":0.8,\"Aggregation/edm:provider\":0.5,\"Aggregation/edm:dataProvider\":0.5,\"Aggregation/dc:rights\":0.0,\"Aggregation/edm:ugc\":0.0,\"Aggregation/edm:aggregatedCHO\":0.8,\"Aggregation/edm:intermediateProvider\":0.0,\"Place/dcterms:isPartOf\":0.727273,\"Place/dcterms:hasPart\":0.0,\"Place/skos:prefLabel\":0.5,\"Place/skos:altLabel\":0.0,\"Place/skos:note\":0.0,\"Agent/edm:begin\":0.0,\"Agent/edm:end\":0.0,\"Agent/edm:hasMet\":0.0,\"Agent/edm:isRelatedTo\":0.0,\"Agent/owl:sameAs\":0.0,\"Agent/foaf:name\":0.0,\"Agent/dc:date\":0.0,\"Agent/dc:identifier\":0.0,\"Agent/rdaGr2:dateOfBirth\":0.0,\"Agent/rdaGr2:placeOfBirth\":0.0,\"Agent/rdaGr2:dateOfDeath\":0.0,\"Agent/rdaGr2:placeOfDeath\":0.0,\"Agent/rdaGr2:dateOfEstablishment\":0.0,\"Agent/rdaGr2:dateOfTermination\":0.0,\"Agent/rdaGr2:gender\":0.0,\"Agent/rdaGr2:professionOrOccupation\":0.0,\"Agent/rdaGr2:biographicalInformation\":0.0,\"Agent/skos:prefLabel\":0.0,\"Agent/skos:altLabel\":0.0,\"Agent/skos:note\":0.0,\"Timespan/edm:begin\":0.5,\"Timespan/edm:end\":0.5,\"Timespan/dcterms:isPartOf\":0.666667,\"Timespan/dcterms:hasPart\":0.0,\"Timespan/edm:isNextInSequence\":0.0,\"Timespan/owl:sameAs\":0.0,\"Timespan/skos:prefLabel\":0.666667,\"Timespan/skos:altLabel\":0.0,\"Timespan/skos:note\":0.0,\"Concept/skos:broader\":0.0,\"Concept/skos:narrower\":0.0,\"Concept/skos:related\":0.0,\"Concept/skos:broadMatch\":0.0,\"Concept/skos:narrowMatch\":0.0,\"Concept/skos:relatedMatch\":0.0,\"Concept/skos:exactMatch\":0.0,\"Concept/skos:closeMatch\":0.0,\"Concept/skos:notation\":0.0,\"Concept/skos:inScheme\":0.0,\"Concept/skos:prefLabel\":0.0,\"Concept/skos:altLabel\":0.0,\"Concept/skos:note\":0.0",
			languages
		);
	}

	@Test
	public void testGetLanguageMap() throws URISyntaxException, IOException {
		LanguageSaturationCalculator calculator = new LanguageSaturationCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
		calculator.measure(cache);

		Map<String, Double> languages = calculator.getSaturationMap();
		assertNotNull(languages);
		assertEquals(107, languages.size());
		assertEquals(new Double(0.6666666666666667), languages.get("Proxy/dc:title"));
		languages.remove("Proxy/dc:title");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:alternative"));
		languages.remove("Proxy/dcterms:alternative");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:description"));
		languages.remove("Proxy/dc:description");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:creator"));
		languages.remove("Proxy/dc:creator");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:publisher"));
		languages.remove("Proxy/dc:publisher");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:contributor"));
		languages.remove("Proxy/dc:contributor");
		assertEquals(new Double(0.5), languages.get("Proxy/dc:type"));
		languages.remove("Proxy/dc:type");
		assertEquals(new Double(0.5), languages.get("Proxy/dc:identifier"));
		languages.remove("Proxy/dc:identifier");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:language"));
		languages.remove("Proxy/dc:language");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:coverage"));
		languages.remove("Proxy/dc:coverage");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:temporal"));
		languages.remove("Proxy/dcterms:temporal");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:spatial"));
		languages.remove("Proxy/dcterms:spatial");
		assertEquals(new Double(0.7692307692307693), languages.get("Proxy/dc:subject"));
		languages.remove("Proxy/dc:subject");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:date"));
		languages.remove("Proxy/dc:date");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:created"));
		languages.remove("Proxy/dcterms:created");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:issued"));
		languages.remove("Proxy/dcterms:issued");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:extent"));
		languages.remove("Proxy/dcterms:extent");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:medium"));
		languages.remove("Proxy/dcterms:medium");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:provenance"));
		languages.remove("Proxy/dcterms:provenance");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:hasPart"));
		languages.remove("Proxy/dcterms:hasPart");
		assertEquals(new Double(0.8), languages.get("Proxy/dcterms:isPartOf"));
		languages.remove("Proxy/dcterms:isPartOf");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:format"));
		languages.remove("Proxy/dc:format");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:source"));
		languages.remove("Proxy/dc:source");
		assertEquals(new Double(0.5), languages.get("Proxy/dc:rights"));
		languages.remove("Proxy/dc:rights");
		assertEquals(new Double(0.0), languages.get("Proxy/dc:relation"));
		languages.remove("Proxy/dc:relation");
		assertEquals(new Double(0.8), languages.get("Aggregation/edm:rights"));
		languages.remove("Aggregation/edm:rights");
		assertEquals(new Double(0.6666666666666667), languages.get("Aggregation/edm:provider"));
		languages.remove("Aggregation/edm:provider");
		assertEquals(new Double(0.5), languages.get("Aggregation/edm:dataProvider"));
		languages.remove("Aggregation/edm:dataProvider");

		assertEquals(new Double(0.5), languages.get("Proxy/edm:europeanaProxy")); languages.remove("Proxy/edm:europeanaProxy");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:year")); languages.remove("Proxy/edm:year");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:userTag")); languages.remove("Proxy/edm:userTag");
		assertEquals(new Double(0.8), languages.get("Proxy/ore:proxyIn")); languages.remove("Proxy/ore:proxyIn");
		assertEquals(new Double(0.8), languages.get("Proxy/ore:proxyFor")); languages.remove("Proxy/ore:proxyFor");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:conformsTo")); languages.remove("Proxy/dcterms:conformsTo");
		assertEquals(new Double(0.5), languages.get("Proxy/dcterms:hasFormat")); languages.remove("Proxy/dcterms:hasFormat");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:hasVersion")); languages.remove("Proxy/dcterms:hasVersion");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:isFormatOf")); languages.remove("Proxy/dcterms:isFormatOf");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:isReferencedBy")); languages.remove("Proxy/dcterms:isReferencedBy");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:isReplacedBy")); languages.remove("Proxy/dcterms:isReplacedBy");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:isRequiredBy")); languages.remove("Proxy/dcterms:isRequiredBy");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:isVersionOf")); languages.remove("Proxy/dcterms:isVersionOf");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:references")); languages.remove("Proxy/dcterms:references");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:replaces")); languages.remove("Proxy/dcterms:replaces");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:requires")); languages.remove("Proxy/dcterms:requires");
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:tableOfContents")); languages.remove("Proxy/dcterms:tableOfContents");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:currentLocation")); languages.remove("Proxy/edm:currentLocation");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:hasMet")); languages.remove("Proxy/edm:hasMet");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:hasType")); languages.remove("Proxy/edm:hasType");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:incorporates")); languages.remove("Proxy/edm:incorporates");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:isDerivativeOf")); languages.remove("Proxy/edm:isDerivativeOf");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:isRelatedTo")); languages.remove("Proxy/edm:isRelatedTo");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:isRepresentationOf")); languages.remove("Proxy/edm:isRepresentationOf");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:isSimilarTo")); languages.remove("Proxy/edm:isSimilarTo");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:isSuccessorOf")); languages.remove("Proxy/edm:isSuccessorOf");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:realizes")); languages.remove("Proxy/edm:realizes");
		assertEquals(new Double(0.0), languages.get("Proxy/edm:wasPresentAt")); languages.remove("Proxy/edm:wasPresentAt");
		assertEquals(new Double(0.0), languages.get("Aggregation/dc:rights")); languages.remove("Aggregation/dc:rights");
		assertEquals(new Double(0.0), languages.get("Aggregation/edm:ugc")); languages.remove("Aggregation/edm:ugc");
		assertEquals(new Double(0.8), languages.get("Aggregation/edm:aggregatedCHO")); languages.remove("Aggregation/edm:aggregatedCHO");
		assertEquals(new Double(0.0), languages.get("Aggregation/edm:intermediateProvider")); languages.remove("Aggregation/edm:intermediateProvider");
		assertEquals(new Double(0.0), languages.get("Place/dcterms:isPartOf")); languages.remove("Place/dcterms:isPartOf");
		assertEquals(new Double(0.0), languages.get("Place/dcterms:hasPart")); languages.remove("Place/dcterms:hasPart");
		assertEquals(new Double(0.0), languages.get("Place/skos:prefLabel")); languages.remove("Place/skos:prefLabel");
		assertEquals(new Double(0.0), languages.get("Place/skos:altLabel")); languages.remove("Place/skos:altLabel");
		assertEquals(new Double(0.0), languages.get("Place/skos:note")); languages.remove("Place/skos:note");
		assertEquals(new Double(0.0), languages.get("Agent/edm:begin")); languages.remove("Agent/edm:begin");
		assertEquals(new Double(0.0), languages.get("Agent/edm:end")); languages.remove("Agent/edm:end");
		assertEquals(new Double(0.0), languages.get("Agent/edm:hasMet")); languages.remove("Agent/edm:hasMet");
		assertEquals(new Double(0.0), languages.get("Agent/edm:isRelatedTo")); languages.remove("Agent/edm:isRelatedTo");
		assertEquals(new Double(0.0), languages.get("Agent/owl:sameAs")); languages.remove("Agent/owl:sameAs");
		assertEquals(new Double(0.0), languages.get("Agent/foaf:name")); languages.remove("Agent/foaf:name");
		assertEquals(new Double(0.0), languages.get("Agent/dc:date")); languages.remove("Agent/dc:date");
		assertEquals(new Double(0.0), languages.get("Agent/dc:identifier")); languages.remove("Agent/dc:identifier");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:dateOfBirth")); languages.remove("Agent/rdaGr2:dateOfBirth");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:placeOfBirth")); languages.remove("Agent/rdaGr2:placeOfBirth");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:dateOfDeath")); languages.remove("Agent/rdaGr2:dateOfDeath");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:placeOfDeath")); languages.remove("Agent/rdaGr2:placeOfDeath");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:dateOfEstablishment")); languages.remove("Agent/rdaGr2:dateOfEstablishment");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:dateOfTermination")); languages.remove("Agent/rdaGr2:dateOfTermination");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:gender")); languages.remove("Agent/rdaGr2:gender");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:professionOrOccupation")); languages.remove("Agent/rdaGr2:professionOrOccupation");
		assertEquals(new Double(0.0), languages.get("Agent/rdaGr2:biographicalInformation")); languages.remove("Agent/rdaGr2:biographicalInformation");
		assertEquals(new Double(0.0), languages.get("Agent/skos:prefLabel")); languages.remove("Agent/skos:prefLabel");
		assertEquals(new Double(0.0), languages.get("Agent/skos:altLabel")); languages.remove("Agent/skos:altLabel");
		assertEquals(new Double(0.0), languages.get("Agent/skos:note")); languages.remove("Agent/skos:note");
		assertEquals(new Double(0.0), languages.get("Timespan/edm:begin")); languages.remove("Timespan/edm:begin");
		assertEquals(new Double(0.0), languages.get("Timespan/edm:end")); languages.remove("Timespan/edm:end");
		assertEquals(new Double(0.0), languages.get("Timespan/dcterms:isPartOf")); languages.remove("Timespan/dcterms:isPartOf");
		assertEquals(new Double(0.0), languages.get("Timespan/dcterms:hasPart")); languages.remove("Timespan/dcterms:hasPart");
		assertEquals(new Double(0.0), languages.get("Timespan/edm:isNextInSequence")); languages.remove("Timespan/edm:isNextInSequence");
		assertEquals(new Double(0.0), languages.get("Timespan/owl:sameAs")); languages.remove("Timespan/owl:sameAs");
		assertEquals(new Double(0.0), languages.get("Timespan/skos:prefLabel")); languages.remove("Timespan/skos:prefLabel");
		assertEquals(new Double(0.0), languages.get("Timespan/skos:altLabel")); languages.remove("Timespan/skos:altLabel");
		assertEquals(new Double(0.0), languages.get("Timespan/skos:note")); languages.remove("Timespan/skos:note");
		assertEquals(new Double(0.0), languages.get("Concept/skos:broader")); languages.remove("Concept/skos:broader");
		assertEquals(new Double(0.0), languages.get("Concept/skos:narrower")); languages.remove("Concept/skos:narrower");
		assertEquals(new Double(0.0), languages.get("Concept/skos:related")); languages.remove("Concept/skos:related");
		assertEquals(new Double(0.0), languages.get("Concept/skos:broadMatch")); languages.remove("Concept/skos:broadMatch");
		assertEquals(new Double(0.0), languages.get("Concept/skos:narrowMatch")); languages.remove("Concept/skos:narrowMatch");
		assertEquals(new Double(0.0), languages.get("Concept/skos:relatedMatch")); languages.remove("Concept/skos:relatedMatch");
		assertEquals(new Double(0.0), languages.get("Concept/skos:exactMatch")); languages.remove("Concept/skos:exactMatch");
		assertEquals(new Double(0.0), languages.get("Concept/skos:closeMatch")); languages.remove("Concept/skos:closeMatch");
		assertEquals(new Double(0.0), languages.get("Concept/skos:notation")); languages.remove("Concept/skos:notation");
		assertEquals(new Double(0.0), languages.get("Concept/skos:inScheme")); languages.remove("Concept/skos:inScheme");
		assertEquals(new Double(0.7857142857142857), languages.get("Concept/skos:prefLabel")); languages.remove("Concept/skos:prefLabel");
		assertEquals(new Double(0.0), languages.get("Concept/skos:altLabel")); languages.remove("Concept/skos:altLabel");
		assertEquals(new Double(0.0), languages.get("Concept/skos:note")); languages.remove("Concept/skos:note");

		assertEquals("Language has the following values: " + StringUtils.join(languages.keySet(), ", "), 
			0, languages.size());
	}

	@Test
	public void testGetLanguageMapWithPlace() throws URISyntaxException, IOException {
		LanguageSaturationCalculator calculator = new LanguageSaturationCalculator(new EdmOaiPmhXmlSchema());
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);

		Map<String, Double> languages = calculator.getSaturationMap();
		assertNotNull(languages);
		assertEquals(107, languages.size());
		assertEquals(new Double(0.5), languages.get("Proxy/dc:title"));
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:alternative"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:description"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:creator"));
		assertEquals(new Double(0.0), languages.get("Proxy/dc:publisher"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:contributor"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:type"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:identifier"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:language"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:coverage"));
		assertEquals(new Double(0.5), languages.get("Proxy/dcterms:temporal"));
		assertEquals(new Double(0.5), languages.get("Proxy/dcterms:spatial"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:subject"));
		assertEquals(new Double(0.0), languages.get("Proxy/dc:date"));
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:created"));
		assertEquals(new Double(0.5), languages.get("Proxy/dcterms:issued"));
		assertEquals(new Double(0.5), languages.get("Proxy/dcterms:extent"));
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:medium"));
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:provenance"));
		assertEquals(new Double(0.5), languages.get("Proxy/dcterms:hasPart"));
		assertEquals(new Double(0.0), languages.get("Proxy/dcterms:isPartOf"));
		assertEquals(new Double(0.5), languages.get("Proxy/dc:format"));
		assertEquals(new Double(0.0), languages.get("Proxy/dc:source"));
		assertEquals(new Double(0.0), languages.get("Proxy/dc:rights"));
		assertEquals(new Double(0.0), languages.get("Proxy/dc:relation"));

		assertEquals(new Double(0.8), languages.get("Aggregation/edm:rights"));
		assertEquals(new Double(0.5), languages.get("Aggregation/edm:provider"));
		assertEquals(new Double(0.5), languages.get("Aggregation/edm:dataProvider"));

		assertEquals(new Double(0.7272727272727273), languages.get("Place/dcterms:isPartOf"));
		assertEquals(new Double(0.0), languages.get("Place/dcterms:hasPart"));
		assertEquals(new Double(0.5), languages.get("Place/skos:prefLabel"));
		assertEquals(new Double(0.0), languages.get("Place/skos:altLabel"));
		assertEquals(new Double(0.0), languages.get("Place/skos:note"));
	}

	@Test
	public void testGetHeaders() {
		LanguageCalculator calculator = new LanguageCalculator(new EdmOaiPmhXmlSchema());
		List<String> expected = Arrays.asList("lang:Proxy/dc:title", "lang:Proxy/dcterms:alternative", "lang:Proxy/dc:description", "lang:Proxy/dc:creator", "lang:Proxy/dc:publisher", "lang:Proxy/dc:contributor", "lang:Proxy/dc:type", "lang:Proxy/dc:identifier", "lang:Proxy/dc:language", "lang:Proxy/dc:coverage", "lang:Proxy/dcterms:temporal", "lang:Proxy/dcterms:spatial", "lang:Proxy/dc:subject", "lang:Proxy/dc:date", "lang:Proxy/dcterms:created", "lang:Proxy/dcterms:issued", "lang:Proxy/dcterms:extent", "lang:Proxy/dcterms:medium", "lang:Proxy/dcterms:provenance", "lang:Proxy/dcterms:hasPart", "lang:Proxy/dcterms:isPartOf", "lang:Proxy/dc:format", "lang:Proxy/dc:source", "lang:Proxy/dc:rights", "lang:Proxy/dc:relation", "lang:Proxy/edm:europeanaProxy", "lang:Proxy/edm:year", "lang:Proxy/edm:userTag", "lang:Proxy/ore:proxyIn", "lang:Proxy/ore:proxyFor", "lang:Proxy/dcterms:conformsTo", "lang:Proxy/dcterms:hasFormat", "lang:Proxy/dcterms:hasVersion", "lang:Proxy/dcterms:isFormatOf", "lang:Proxy/dcterms:isReferencedBy", "lang:Proxy/dcterms:isReplacedBy", "lang:Proxy/dcterms:isRequiredBy", "lang:Proxy/dcterms:isVersionOf", "lang:Proxy/dcterms:references", "lang:Proxy/dcterms:replaces", "lang:Proxy/dcterms:requires", "lang:Proxy/dcterms:tableOfContents", "lang:Proxy/edm:currentLocation", "lang:Proxy/edm:hasMet", "lang:Proxy/edm:hasType", "lang:Proxy/edm:incorporates", "lang:Proxy/edm:isDerivativeOf", "lang:Proxy/edm:isRelatedTo", "lang:Proxy/edm:isRepresentationOf", "lang:Proxy/edm:isSimilarTo", "lang:Proxy/edm:isSuccessorOf", "lang:Proxy/edm:realizes", "lang:Proxy/edm:wasPresentAt", "lang:Aggregation/edm:rights", "lang:Aggregation/edm:provider", "lang:Aggregation/edm:dataProvider", "lang:Aggregation/dc:rights", "lang:Aggregation/edm:ugc", "lang:Aggregation/edm:aggregatedCHO", "lang:Aggregation/edm:intermediateProvider", "lang:Place/dcterms:isPartOf", "lang:Place/dcterms:hasPart", "lang:Place/skos:prefLabel", "lang:Place/skos:altLabel", "lang:Place/skos:note", "lang:Agent/edm:begin", "lang:Agent/edm:end", "lang:Agent/edm:hasMet", "lang:Agent/edm:isRelatedTo", "lang:Agent/owl:sameAs", "lang:Agent/foaf:name", "lang:Agent/dc:date", "lang:Agent/dc:identifier", "lang:Agent/rdaGr2:dateOfBirth", "lang:Agent/rdaGr2:placeOfBirth", "lang:Agent/rdaGr2:dateOfDeath", "lang:Agent/rdaGr2:placeOfDeath", "lang:Agent/rdaGr2:dateOfEstablishment", "lang:Agent/rdaGr2:dateOfTermination", "lang:Agent/rdaGr2:gender", "lang:Agent/rdaGr2:professionOrOccupation", "lang:Agent/rdaGr2:biographicalInformation", "lang:Agent/skos:prefLabel", "lang:Agent/skos:altLabel", "lang:Agent/skos:note", "lang:Timespan/edm:begin", "lang:Timespan/edm:end", "lang:Timespan/dcterms:isPartOf", "lang:Timespan/dcterms:hasPart", "lang:Timespan/edm:isNextInSequence", "lang:Timespan/owl:sameAs", "lang:Timespan/skos:prefLabel", "lang:Timespan/skos:altLabel", "lang:Timespan/skos:note", "lang:Concept/skos:broader", "lang:Concept/skos:narrower", "lang:Concept/skos:related", "lang:Concept/skos:broadMatch", "lang:Concept/skos:narrowMatch", "lang:Concept/skos:relatedMatch", "lang:Concept/skos:exactMatch", "lang:Concept/skos:closeMatch", "lang:Concept/skos:notation", "lang:Concept/skos:inScheme", "lang:Concept/skos:prefLabel", "lang:Concept/skos:altLabel", "lang:Concept/skos:note");
		assertEquals(107, calculator.getHeader().size());
		assertEquals(expected, calculator.getHeader());
	}
}
