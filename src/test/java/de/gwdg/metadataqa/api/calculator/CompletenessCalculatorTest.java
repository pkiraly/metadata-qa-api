package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.FieldGroup;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlLimitedSchema;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CompletenessCalculatorTest {

	JsonPathCache cache;
	CompletenessCalculator calculator;

	public CompletenessCalculatorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
		calculator = new CompletenessCalculator(new EdmOaiPmhXmlSchema());
		cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCompleteness() throws URISyntaxException, IOException {

		calculator.measure(cache);
		FieldCounter<Double> fieldCounter = calculator.getCompletenessCounter().getFieldCounter();
		FieldCounter<Boolean> existenceCounter = calculator.getExistenceCounter();
		FieldCounter<Integer> cardinalityCounter = calculator.getInstanceCounter();
		// calculator.getExistenceMap();
		assertEquals((Double) 0.17708333333333334, fieldCounter.get("TOTAL"));
		// printCheck(JsonBranch.Category.VIEWING, calculator);
		assertEquals((Double) 0.75, fieldCounter.get("VIEWING"));
		// printCheck(JsonBranch.Category.CONTEXTUALIZATION, calculator);
		assertEquals((Double) 0.2727272727272727, fieldCounter.get("CONTEXTUALIZATION"));
		// printCheck(JsonBranch.Category.MANDATORY, calculator);
		assertEquals((Double) 1.0, fieldCounter.get("MANDATORY"));
		// printCheck(JsonBranch.Category.MULTILINGUALITY, calculator);
		assertEquals((Double) 0.4, fieldCounter.get("MULTILINGUALITY"));
		// printCheck(JsonBranch.Category.DESCRIPTIVENESS, calculator);
		assertEquals((Double) 0.18181818181818182, fieldCounter.get("DESCRIPTIVENESS"));
		// printCheck(JsonBranch.Category.BROWSING, calculator);
		assertEquals((Double) 0.35714285714285715, fieldCounter.get("BROWSING"));
		// printCheck(JsonBranch.Category.IDENTIFICATION, calculator);
		assertEquals((Double) 0.5, fieldCounter.get("IDENTIFICATION"));
		// printCheck(JsonBranch.Category.SEARCHABILITY, calculator);
		assertEquals((Double) 0.3888888888888889, fieldCounter.get("SEARCHABILITY"));
		// printCheck(JsonBranch.Category.REUSABILITY, calculator);
		assertEquals((Double) 0.36363636363636365, fieldCounter.get("REUSABILITY"));

		String expected = "0.177083,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0";

		assertEquals(expected, calculator.getCsv(false, true));
	}

	private List<String> getFieldsByCategory(JsonBranch.Category category, Schema schema) {
		List<String> labels = new ArrayList<>();
		for (JsonBranch branch : schema.getPaths()) {
			if (branch.getCategories().contains(category)) {
				labels.add(branch.getLabel());
			}
		}

		for (FieldGroup group : schema.getFieldGroups()) {
			if (group.getCategory().equals(category)) {
				for (String field : group.getFields()) {
					if (!labels.contains(field)) {
						labels.add(field);
					}
				}
			}
		}
		return labels;
	}

	private void printCheck(JsonBranch.Category category, CompletenessCalculator calculator) {
		EdmOaiPmhXmlSchema schema = new EdmOaiPmhXmlSchema();

		FieldCounter<Double> fieldCounter = calculator.getCompletenessCounter().getFieldCounter();
		FieldCounter<Boolean> existenceCounter = calculator.getExistenceCounter();
		FieldCounter<Integer> cardinalityCounter = calculator.getInstanceCounter();

		List<String> fields = getFieldsByCategory(category, schema);
		System.err.println(category + ": " + StringUtils.join(fields, ", "));
		System.err.println(calculator.getCompletenessCounter().get(category.name()).toString());
		int i = 0;
		for (String field : fields) {
			System.err.println(
					  String.format(
								 "%d) field %s: %s, %d",
								 ++i,
								 field,
								 existenceCounter.get(field).toString(),
								 cardinalityCounter.get(field)
					  )
			);
		}
	}

	@Test
	public void testExistenceMap() throws URISyntaxException, IOException {
		calculator.collectFields(true);

		calculator.measure(cache);
		Map<String, Boolean> existenceMap = calculator.getExistenceMap();
		assertEquals(96, existenceMap.size());

		int existingFieldCounter = 0;
		for (boolean existing : existenceMap.values()) {
			if (existing) {
				existingFieldCounter++;
			}
		}
		assertEquals(17, existingFieldCounter);

		assertTrue(existenceMap.get("edm:ProvidedCHO/@about"));

		assertTrue(existenceMap.get("Proxy/dc:title"));
		assertFalse(existenceMap.get("Proxy/dcterms:alternative"));
		assertFalse(existenceMap.get("Proxy/dc:description"));
		assertFalse(existenceMap.get("Proxy/dc:creator"));
		assertFalse(existenceMap.get("Proxy/dc:publisher"));
		assertFalse(existenceMap.get("Proxy/dc:contributor"));
		assertTrue(existenceMap.get("Proxy/dc:type"));
		assertTrue(existenceMap.get("Proxy/dc:identifier"));
		assertFalse(existenceMap.get("Proxy/dc:language"));
		assertFalse(existenceMap.get("Proxy/dc:coverage"));
		assertFalse(existenceMap.get("Proxy/dcterms:temporal"));
		assertFalse(existenceMap.get("Proxy/dcterms:spatial"));
		assertTrue(existenceMap.get("Proxy/dc:subject"));
		assertFalse(existenceMap.get("Proxy/dc:date"));
		assertFalse(existenceMap.get("Proxy/dcterms:created"));
		assertFalse(existenceMap.get("Proxy/dcterms:issued"));
		assertFalse(existenceMap.get("Proxy/dcterms:extent"));
		assertFalse(existenceMap.get("Proxy/dcterms:medium"));
		assertFalse(existenceMap.get("Proxy/dcterms:provenance"));
		assertFalse(existenceMap.get("Proxy/dcterms:hasPart"));
		assertTrue(existenceMap.get("Proxy/dcterms:isPartOf"));
		assertFalse(existenceMap.get("Proxy/dc:format"));
		assertFalse(existenceMap.get("Proxy/dc:source"));
		assertTrue(existenceMap.get("Proxy/dc:rights"));
		assertFalse(existenceMap.get("Proxy/dc:relation"));
		assertFalse(existenceMap.get("Proxy/edm:isNextInSequence"));
		assertTrue(existenceMap.get("Proxy/edm:type"));

		assertTrue(existenceMap.get("Aggregation/edm:rights"));
		assertTrue(existenceMap.get("Aggregation/edm:provider"));
		assertTrue(existenceMap.get("Aggregation/edm:dataProvider"));
		assertTrue(existenceMap.get("Aggregation/edm:isShownAt"));
		assertTrue(existenceMap.get("Aggregation/edm:isShownBy"));
		assertTrue(existenceMap.get("Aggregation/edm:object"));
		assertFalse(existenceMap.get("Aggregation/edm:hasView"));

		assertFalse(existenceMap.get("Place/wgs84:lat"));
		assertFalse(existenceMap.get("Place/wgs84:long"));
		assertFalse(existenceMap.get("Place/wgs84:alt"));
		assertFalse(existenceMap.get("Place/dcterms:isPartOf"));
		assertFalse(existenceMap.get("Place/wgs84_pos:lat_long"));
		assertFalse(existenceMap.get("Place/dcterms:hasPart"));
		assertFalse(existenceMap.get("Place/owl:sameAs"));
		assertFalse(existenceMap.get("Place/skos:prefLabel"));
		assertFalse(existenceMap.get("Place/skos:altLabel"));
		assertFalse(existenceMap.get("Place/skos:note"));
		assertFalse(existenceMap.get("Place/rdf:about"));

		assertFalse(existenceMap.get("Agent/rdf:about"));
		assertFalse(existenceMap.get("Agent/edm:begin"));
		assertFalse(existenceMap.get("Agent/edm:end"));
		assertFalse(existenceMap.get("Agent/edm:hasMet"));
		assertFalse(existenceMap.get("Agent/edm:isRelatedTo"));
		assertFalse(existenceMap.get("Agent/owl:sameAs"));
		assertFalse(existenceMap.get("Agent/foaf:name"));
		assertFalse(existenceMap.get("Agent/dc:date"));
		assertFalse(existenceMap.get("Agent/dc:identifier"));
		assertFalse(existenceMap.get("Agent/rdaGr2:dateOfBirth"));
		assertFalse(existenceMap.get("Agent/rdaGr2:placeOfBirth"));
		assertFalse(existenceMap.get("Agent/rdaGr2:dateOfDeath"));
		assertFalse(existenceMap.get("Agent/rdaGr2:placeOfDeath"));
		assertFalse(existenceMap.get("Agent/rdaGr2:dateOfEstablishment"));
		assertFalse(existenceMap.get("Agent/rdaGr2:dateOfTermination"));
		assertFalse(existenceMap.get("Agent/rdaGr2:gender"));
		assertFalse(existenceMap.get("Agent/rdaGr2:professionOrOccupation"));
		assertFalse(existenceMap.get("Agent/rdaGr2:biographicalInformation"));
		assertFalse(existenceMap.get("Agent/skos:prefLabel"));
		assertFalse(existenceMap.get("Agent/skos:altLabel"));
		assertFalse(existenceMap.get("Agent/skos:note"));

		assertFalse(existenceMap.get("Timespan/rdf:about"));
		assertFalse(existenceMap.get("Timespan/edm:begin"));
		assertFalse(existenceMap.get("Timespan/edm:end"));
		assertFalse(existenceMap.get("Timespan/dcterms:isPartOf"));
		assertFalse(existenceMap.get("Timespan/dcterms:hasPart"));
		assertFalse(existenceMap.get("Timespan/edm:isNextInSequence"));
		assertFalse(existenceMap.get("Timespan/owl:sameAs"));
		assertFalse(existenceMap.get("Timespan/skos:prefLabel"));
		assertFalse(existenceMap.get("Timespan/skos:altLabel"));
		assertFalse(existenceMap.get("Timespan/skos:note"));

		assertTrue(existenceMap.get("Concept/rdf:about"));
		assertFalse(existenceMap.get("Concept/skos:broader"));
		assertFalse(existenceMap.get("Concept/skos:narrower"));
		assertFalse(existenceMap.get("Concept/skos:related"));
		assertFalse(existenceMap.get("Concept/skos:broadMatch"));
		assertFalse(existenceMap.get("Concept/skos:narrowMatch"));
		assertFalse(existenceMap.get("Concept/skos:relatedMatch"));
		assertFalse(existenceMap.get("Concept/skos:exactMatch"));
		assertFalse(existenceMap.get("Concept/skos:closeMatch"));
		assertFalse(existenceMap.get("Concept/skos:notation"));
		assertFalse(existenceMap.get("Concept/skos:inScheme"));
		assertTrue(existenceMap.get("Concept/skos:prefLabel"));
		assertFalse(existenceMap.get("Concept/skos:altLabel"));
		assertFalse(existenceMap.get("Concept/skos:note"));

	}

	@Test
	public void testExistenceList() throws URISyntaxException, IOException {
		calculator.collectFields(true);
		calculator.measure(cache);
		List<Integer> expected = Arrays.asList(new Integer[]{1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0});
		assertEquals(96, calculator.getExistenceCounter().size());
		assertEquals(expected, calculator.getExistenceCounter().getList());
	}

	@Test
	public void testGetHeaders() {
		calculator = new CompletenessCalculator(new EdmOaiPmhXmlSchema());
		List<String> headers = calculator.getHeader();
		List<String> expected = Arrays.asList("completeness:TOTAL", "completeness:MANDATORY", "completeness:DESCRIPTIVENESS", "completeness:SEARCHABILITY", "completeness:CONTEXTUALIZATION", "completeness:IDENTIFICATION", "completeness:BROWSING", "completeness:VIEWING", "completeness:REUSABILITY", "completeness:MULTILINGUALITY", "existence:edm:ProvidedCHO/@about", "existence:Proxy/dc:title", "existence:Proxy/dcterms:alternative", "existence:Proxy/dc:description", "existence:Proxy/dc:creator", "existence:Proxy/dc:publisher", "existence:Proxy/dc:contributor", "existence:Proxy/dc:type", "existence:Proxy/dc:identifier", "existence:Proxy/dc:language", "existence:Proxy/dc:coverage", "existence:Proxy/dcterms:temporal", "existence:Proxy/dcterms:spatial", "existence:Proxy/dc:subject", "existence:Proxy/dc:date", "existence:Proxy/dcterms:created", "existence:Proxy/dcterms:issued", "existence:Proxy/dcterms:extent", "existence:Proxy/dcterms:medium", "existence:Proxy/dcterms:provenance", "existence:Proxy/dcterms:hasPart", "existence:Proxy/dcterms:isPartOf", "existence:Proxy/dc:format", "existence:Proxy/dc:source", "existence:Proxy/dc:rights", "existence:Proxy/dc:relation", "existence:Proxy/edm:isNextInSequence", "existence:Proxy/edm:type", "existence:Aggregation/edm:rights", "existence:Aggregation/edm:provider", "existence:Aggregation/edm:dataProvider", "existence:Aggregation/edm:isShownAt", "existence:Aggregation/edm:isShownBy", "existence:Aggregation/edm:object", "existence:Aggregation/edm:hasView", "existence:Aggregation/dc:rights", "existence:Aggregation/edm:ugc", "existence:Aggregation/edm:aggregatedCHO", "existence:Aggregation/edm:intermediateProvider", "existence:Aggregation/rdf:about", "existence:Place/wgs84:lat", "existence:Place/wgs84:long", "existence:Place/wgs84:alt", "existence:Place/dcterms:isPartOf", "existence:Place/wgs84_pos:lat_long", "existence:Place/dcterms:hasPart", "existence:Place/owl:sameAs", "existence:Place/skos:prefLabel", "existence:Place/skos:altLabel", "existence:Place/skos:note", "existence:Place/rdf:about", "existence:Agent/rdf:about", "existence:Agent/edm:begin", "existence:Agent/edm:end", "existence:Agent/edm:hasMet", "existence:Agent/edm:isRelatedTo", "existence:Agent/owl:sameAs", "existence:Agent/foaf:name", "existence:Agent/dc:date", "existence:Agent/dc:identifier", "existence:Agent/rdaGr2:dateOfBirth", "existence:Agent/rdaGr2:placeOfBirth", "existence:Agent/rdaGr2:dateOfDeath", "existence:Agent/rdaGr2:placeOfDeath", "existence:Agent/rdaGr2:dateOfEstablishment", "existence:Agent/rdaGr2:dateOfTermination", "existence:Agent/rdaGr2:gender", "existence:Agent/rdaGr2:professionOrOccupation", "existence:Agent/rdaGr2:biographicalInformation", "existence:Agent/skos:prefLabel", "existence:Agent/skos:altLabel", "existence:Agent/skos:note", "existence:Timespan/rdf:about", "existence:Timespan/edm:begin", "existence:Timespan/edm:end", "existence:Timespan/dcterms:isPartOf", "existence:Timespan/dcterms:hasPart", "existence:Timespan/edm:isNextInSequence", "existence:Timespan/owl:sameAs", "existence:Timespan/skos:prefLabel", "existence:Timespan/skos:altLabel", "existence:Timespan/skos:note", "existence:Concept/rdf:about", "existence:Concept/skos:broader", "existence:Concept/skos:narrower", "existence:Concept/skos:related", "existence:Concept/skos:broadMatch", "existence:Concept/skos:narrowMatch", "existence:Concept/skos:relatedMatch", "existence:Concept/skos:exactMatch", "existence:Concept/skos:closeMatch", "existence:Concept/skos:notation", "existence:Concept/skos:inScheme", "existence:Concept/skos:prefLabel", "existence:Concept/skos:altLabel", "existence:Concept/skos:note", "cardinality:edm:ProvidedCHO/@about", "cardinality:Proxy/dc:title", "cardinality:Proxy/dcterms:alternative", "cardinality:Proxy/dc:description", "cardinality:Proxy/dc:creator", "cardinality:Proxy/dc:publisher", "cardinality:Proxy/dc:contributor", "cardinality:Proxy/dc:type", "cardinality:Proxy/dc:identifier", "cardinality:Proxy/dc:language", "cardinality:Proxy/dc:coverage", "cardinality:Proxy/dcterms:temporal", "cardinality:Proxy/dcterms:spatial", "cardinality:Proxy/dc:subject", "cardinality:Proxy/dc:date", "cardinality:Proxy/dcterms:created", "cardinality:Proxy/dcterms:issued", "cardinality:Proxy/dcterms:extent", "cardinality:Proxy/dcterms:medium", "cardinality:Proxy/dcterms:provenance", "cardinality:Proxy/dcterms:hasPart", "cardinality:Proxy/dcterms:isPartOf", "cardinality:Proxy/dc:format", "cardinality:Proxy/dc:source", "cardinality:Proxy/dc:rights", "cardinality:Proxy/dc:relation", "cardinality:Proxy/edm:isNextInSequence", "cardinality:Proxy/edm:type", "cardinality:Aggregation/edm:rights", "cardinality:Aggregation/edm:provider", "cardinality:Aggregation/edm:dataProvider", "cardinality:Aggregation/edm:isShownAt", "cardinality:Aggregation/edm:isShownBy", "cardinality:Aggregation/edm:object", "cardinality:Aggregation/edm:hasView", "cardinality:Aggregation/dc:rights", "cardinality:Aggregation/edm:ugc", "cardinality:Aggregation/edm:aggregatedCHO", "cardinality:Aggregation/edm:intermediateProvider", "cardinality:Aggregation/rdf:about", "cardinality:Place/wgs84:lat", "cardinality:Place/wgs84:long", "cardinality:Place/wgs84:alt", "cardinality:Place/dcterms:isPartOf", "cardinality:Place/wgs84_pos:lat_long", "cardinality:Place/dcterms:hasPart", "cardinality:Place/owl:sameAs", "cardinality:Place/skos:prefLabel", "cardinality:Place/skos:altLabel", "cardinality:Place/skos:note", "cardinality:Place/rdf:about", "cardinality:Agent/rdf:about", "cardinality:Agent/edm:begin", "cardinality:Agent/edm:end", "cardinality:Agent/edm:hasMet", "cardinality:Agent/edm:isRelatedTo", "cardinality:Agent/owl:sameAs", "cardinality:Agent/foaf:name", "cardinality:Agent/dc:date", "cardinality:Agent/dc:identifier", "cardinality:Agent/rdaGr2:dateOfBirth", "cardinality:Agent/rdaGr2:placeOfBirth", "cardinality:Agent/rdaGr2:dateOfDeath", "cardinality:Agent/rdaGr2:placeOfDeath", "cardinality:Agent/rdaGr2:dateOfEstablishment", "cardinality:Agent/rdaGr2:dateOfTermination", "cardinality:Agent/rdaGr2:gender", "cardinality:Agent/rdaGr2:professionOrOccupation", "cardinality:Agent/rdaGr2:biographicalInformation", "cardinality:Agent/skos:prefLabel", "cardinality:Agent/skos:altLabel", "cardinality:Agent/skos:note", "cardinality:Timespan/rdf:about", "cardinality:Timespan/edm:begin", "cardinality:Timespan/edm:end", "cardinality:Timespan/dcterms:isPartOf", "cardinality:Timespan/dcterms:hasPart", "cardinality:Timespan/edm:isNextInSequence", "cardinality:Timespan/owl:sameAs", "cardinality:Timespan/skos:prefLabel", "cardinality:Timespan/skos:altLabel", "cardinality:Timespan/skos:note", "cardinality:Concept/rdf:about", "cardinality:Concept/skos:broader", "cardinality:Concept/skos:narrower", "cardinality:Concept/skos:related", "cardinality:Concept/skos:broadMatch", "cardinality:Concept/skos:narrowMatch", "cardinality:Concept/skos:relatedMatch", "cardinality:Concept/skos:exactMatch", "cardinality:Concept/skos:closeMatch", "cardinality:Concept/skos:notation", "cardinality:Concept/skos:inScheme", "cardinality:Concept/skos:prefLabel", "cardinality:Concept/skos:altLabel", "cardinality:Concept/skos:note");
		assertEquals(expected, headers);
	}

	@Test
	public void testExistenceFlag() throws URISyntaxException, IOException {
		calculator.measure(cache);
		calculator.setExistence(false);
		calculator.setCardinality(false);
		assertEquals("\"TOTAL\":0.177083,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000",
			calculator.getCsv(true, false));
		assertEquals("0.177083,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000",
			calculator.getCsv(false, false));
		assertEquals("0.177083,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4",
			calculator.getCsv(false, true));

		calculator.setExistence(true);
		assertEquals("\"TOTAL\":0.177083,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":1,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0,\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1,\"Aggregation/edm:intermediateProvider\":0,\"Aggregation/rdf:about\":0,\"Place/wgs84:lat\":0,\"Place/wgs84:long\":0,\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":0,\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0,\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":0,\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Place/rdf:about\":0,\"Agent/rdf:about\":0,\"Agent/edm:begin\":0,\"Agent/edm:end\":0,\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0,\"Agent/foaf:name\":0,\"Agent/dc:date\":0,\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0,\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0,\"Agent/rdaGr2:placeOfDeath\":0,\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0,\"Agent/rdaGr2:gender\":0,\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0,\"Agent/skos:prefLabel\":0,\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0,\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0,\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0,\"Concept/rdf:about\":1,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0,\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0,\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":1,\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0",
			calculator.getCsv(true, false));
		assertEquals("0.177083,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0",
			calculator.getCsv(false, false));
		assertEquals("0.177083,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0",
			calculator.getCsv(false, true));
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testInvalidRecord() throws URISyntaxException, IOException {

		thrown.expect(InvalidJsonException.class);
		thrown.expectMessage("Unexpected character (:) at position 28");

		cache = new JsonPathCache(FileUtils.readFirstLine("general/invalid.json"));
		calculator.measure(cache);
		fail("Should throw an exception if the JSON string is invalid.");
	}

	@Test
	public void testFullResults() {
		calculator.measure(cache);
		System.err.println("testFullResults");
		assertEquals("\"TOTAL\":0.177083,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":1,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0,\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1,\"Aggregation/edm:intermediateProvider\":0,\"Aggregation/rdf:about\":0,\"Place/wgs84:lat\":0,\"Place/wgs84:long\":0,\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":0,\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0,\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":0,\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Place/rdf:about\":0,\"Agent/rdf:about\":0,\"Agent/edm:begin\":0,\"Agent/edm:end\":0,\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0,\"Agent/foaf:name\":0,\"Agent/dc:date\":0,\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0,\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0,\"Agent/rdaGr2:placeOfDeath\":0,\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0,\"Agent/rdaGr2:gender\":0,\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0,\"Agent/skos:prefLabel\":0,\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0,\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0,\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0,\"Concept/rdf:about\":1,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0,\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0,\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":1,\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":5,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0,\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1,\"Aggregation/edm:intermediateProvider\":0,\"Aggregation/rdf:about\":0,\"Place/wgs84:lat\":0,\"Place/wgs84:long\":0,\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":0,\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0,\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":0,\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Place/rdf:about\":0,\"Agent/rdf:about\":0,\"Agent/edm:begin\":0,\"Agent/edm:end\":0,\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0,\"Agent/foaf:name\":0,\"Agent/dc:date\":0,\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0,\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0,\"Agent/rdaGr2:placeOfDeath\":0,\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0,\"Agent/rdaGr2:gender\":0,\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0,\"Agent/skos:prefLabel\":0,\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0,\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0,\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0,\"Concept/rdf:about\":1,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0,\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0,\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":12,\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0",
			calculator.getCsv(true, false));

		assertEquals("0.177083,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0",
			calculator.getCsv(false, false));
		assertEquals("0.177083,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0",
		calculator.getCsv(false, true));
	}

	@Test
	public void testPlaces() throws URISyntaxException, IOException {

		cache = new JsonPathCache(FileUtils.readFirstLine("general/test-place.json"));
		calculator.measure(cache);

		assertEquals("\"TOTAL\":0.281250,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.636364,\"SEARCHABILITY\":0.722222,\"CONTEXTUALIZATION\":0.727273,\"IDENTIFICATION\":0.700000,\"BROWSING\":0.571429,\"VIEWING\":0.250000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.800000,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":1,\"Proxy/dc:creator\":1,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":1,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":1,\"Proxy/dc:coverage\":1,\"Proxy/dcterms:temporal\":1,\"Proxy/dcterms:spatial\":1,\"Proxy/dc:subject\":1,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":1,\"Proxy/dcterms:extent\":1,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":1,\"Proxy/dcterms:isPartOf\":0,\"Proxy/dc:format\":1,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":0,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":0,\"Aggregation/edm:object\":0,\"Aggregation/edm:hasView\":0,\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1,\"Aggregation/edm:intermediateProvider\":0,\"Aggregation/rdf:about\":0,\"Place/wgs84:lat\":1,\"Place/wgs84:long\":1,\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":1,\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0,\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":1,\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Place/rdf:about\":1,\"Agent/rdf:about\":0,\"Agent/edm:begin\":0,\"Agent/edm:end\":0,\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0,\"Agent/foaf:name\":0,\"Agent/dc:date\":0,\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0,\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0,\"Agent/rdaGr2:placeOfDeath\":0,\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0,\"Agent/rdaGr2:gender\":0,\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0,\"Agent/skos:prefLabel\":0,\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0,\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0,\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0,\"Concept/rdf:about\":0,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0,\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0,\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":0,\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":11,\"Proxy/dc:creator\":1,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":1,\"Proxy/dc:type\":3,\"Proxy/dc:identifier\":10,\"Proxy/dc:language\":1,\"Proxy/dc:coverage\":2,\"Proxy/dcterms:temporal\":2,\"Proxy/dcterms:spatial\":2,\"Proxy/dc:subject\":3,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":1,\"Proxy/dcterms:extent\":1,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":1,\"Proxy/dcterms:isPartOf\":0,\"Proxy/dc:format\":1,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":0,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":0,\"Aggregation/edm:object\":0,\"Aggregation/edm:hasView\":0,\"Aggregation/dc:rights\":0,\"Aggregation/edm:ugc\":0,\"Aggregation/edm:aggregatedCHO\":1,\"Aggregation/edm:intermediateProvider\":0,\"Aggregation/rdf:about\":0,\"Place/wgs84:lat\":2,\"Place/wgs84:long\":2,\"Place/wgs84:alt\":0,\"Place/dcterms:isPartOf\":2,\"Place/wgs84_pos:lat_long\":0,\"Place/dcterms:hasPart\":0,\"Place/owl:sameAs\":0,\"Place/skos:prefLabel\":128,\"Place/skos:altLabel\":0,\"Place/skos:note\":0,\"Place/rdf:about\":3,\"Agent/rdf:about\":0,\"Agent/edm:begin\":0,\"Agent/edm:end\":0,\"Agent/edm:hasMet\":0,\"Agent/edm:isRelatedTo\":0,\"Agent/owl:sameAs\":0,\"Agent/foaf:name\":0,\"Agent/dc:date\":0,\"Agent/dc:identifier\":0,\"Agent/rdaGr2:dateOfBirth\":0,\"Agent/rdaGr2:placeOfBirth\":0,\"Agent/rdaGr2:dateOfDeath\":0,\"Agent/rdaGr2:placeOfDeath\":0,\"Agent/rdaGr2:dateOfEstablishment\":0,\"Agent/rdaGr2:dateOfTermination\":0,\"Agent/rdaGr2:gender\":0,\"Agent/rdaGr2:professionOrOccupation\":0,\"Agent/rdaGr2:biographicalInformation\":0,\"Agent/skos:prefLabel\":0,\"Agent/skos:altLabel\":0,\"Agent/skos:note\":0,\"Timespan/rdf:about\":0,\"Timespan/edm:begin\":0,\"Timespan/edm:end\":0,\"Timespan/dcterms:isPartOf\":0,\"Timespan/dcterms:hasPart\":0,\"Timespan/edm:isNextInSequence\":0,\"Timespan/owl:sameAs\":0,\"Timespan/skos:prefLabel\":0,\"Timespan/skos:altLabel\":0,\"Timespan/skos:note\":0,\"Concept/rdf:about\":0,\"Concept/skos:broader\":0,\"Concept/skos:narrower\":0,\"Concept/skos:related\":0,\"Concept/skos:broadMatch\":0,\"Concept/skos:narrowMatch\":0,\"Concept/skos:relatedMatch\":0,\"Concept/skos:exactMatch\":0,\"Concept/skos:closeMatch\":0,\"Concept/skos:notation\":0,\"Concept/skos:inScheme\":0,\"Concept/skos:prefLabel\":0,\"Concept/skos:altLabel\":0,\"Concept/skos:note\":0",
			calculator.getCsv(true, false));

		/*
		TODO: write test agains this:
		values: $.['edm:Place'][*]['wgs84:lat'], 2
values: $.['edm:Place'][*]['wgs84:long'], 2
values: $.['edm:Place'][*]['wgs84:alt'], 0
values: $.['edm:Place'][*]['dcterms:isPartOf'], 2
values: $.['edm:Place'][*]['wgs84_pos:lat_long'], 0
values: $.['edm:Place'][*]['dcterms:hasPart'], 0
values: $.['edm:Place'][*]['owl:sameAs'], 0
values: $.['edm:Place'][*]['skos:prefLabel'], 128
values: $.['edm:Place'][*]['skos:altLabel'], 0
values: $.['edm:Place'][*]['skos:note'], 0
values: $.['edm:Place'][*]['@about'], 3

		*/
	}

}
