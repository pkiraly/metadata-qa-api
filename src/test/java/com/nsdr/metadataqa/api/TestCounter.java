package com.nsdr.metadataqa.api;

import com.nsdr.metadataqa.api.counter.Counters;
import com.nsdr.metadataqa.api.calculator.CompletenessCalculator;
import com.nsdr.metadataqa.api.model.JsonPathCache;
import com.jayway.jsonpath.InvalidJsonException;
import com.nsdr.metadataqa.api.calculator.FieldExtractor;
import com.nsdr.metadataqa.api.schema.EdmSchema;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TestCounter {

	private CompletenessCalculator completenessCalculator;
	private Counters counters;

	public TestCounter() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
		counters = new Counters();
		FieldExtractor fieldExtractor = new FieldExtractor();
		fieldExtractor.setIdPath("$.identifier");

		completenessCalculator = new CompletenessCalculator(new EdmSchema());
		// completenessCalculator.setDataProviderManager(new DataProviderManager());
		// completenessCalculator.setDatasetManager(new DatasetManager());

		JsonPathCache cache = new JsonPathCache(readFirstLine("general/test.json"));
		fieldExtractor.measure(cache, counters);
		completenessCalculator.measure(cache, counters);
	}

	public String readFirstLine(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		return lines.get(0);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testId() throws URISyntaxException, IOException {
		assertEquals("92062/BibliographicResource_1000126015451", counters.getRecordId());
	}

	@Test
	public void testDataProvider() throws URISyntaxException, IOException {
		// assertEquals("Österreichische Nationalbibliothek - Austrian National Library", counters.getField("dataProvider"));
		// assertEquals("2", counters.getField("dataProviderCode"));
	}

	@Test
	public void testDataset() throws URISyntaxException, IOException {
		// assertEquals("92062_Ag_EU_TEL_a0480_Austria", counters.getField("dataset"));
		// assertEquals("1", counters.getField("datasetCode"));
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testInvalidRecord() throws URISyntaxException, IOException {

		thrown.expect(InvalidJsonException.class);
		thrown.expectMessage("Unexpected character (:) at position 28");

		JsonPathCache cache = new JsonPathCache(readFirstLine("general/invalid.json"));
		completenessCalculator.measure(cache, counters);
		fail("Should throw an exception if the JSON string is invalid.");
	}

	@Test
	public void testFullResults() {
		counters.doReturnFieldExistenceList(false);
		assertEquals("92062/BibliographicResource_1000126015451,\"TOTAL\":0.400000,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000", counters.getFullResults(true));
		assertEquals("92062/BibliographicResource_1000126015451,0.400000,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000", counters.getFullResults(false));
		assertEquals("92062/BibliographicResource_1000126015451,0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4", counters.getFullResults(false, true));

		counters.doReturnFieldExistenceList(true);
		assertEquals("92062/BibliographicResource_1000126015451,\"TOTAL\":0.400000,\"MANDATORY\":1.000000,\"DESCRIPTIVENESS\":0.181818,\"SEARCHABILITY\":0.388889,\"CONTEXTUALIZATION\":0.272727,\"IDENTIFICATION\":0.500000,\"BROWSING\":0.357143,\"VIEWING\":0.750000,\"REUSABILITY\":0.363636,\"MULTILINGUALITY\":0.400000,\"edm:ProvidedCHO/@about\":1,\"Proxy/dc:title\":1,\"Proxy/dcterms:alternative\":0,\"Proxy/dc:description\":0,\"Proxy/dc:creator\":0,\"Proxy/dc:publisher\":0,\"Proxy/dc:contributor\":0,\"Proxy/dc:type\":1,\"Proxy/dc:identifier\":1,\"Proxy/dc:language\":0,\"Proxy/dc:coverage\":0,\"Proxy/dcterms:temporal\":0,\"Proxy/dcterms:spatial\":0,\"Proxy/dc:subject\":1,\"Proxy/dc:date\":0,\"Proxy/dcterms:created\":0,\"Proxy/dcterms:issued\":0,\"Proxy/dcterms:extent\":0,\"Proxy/dcterms:medium\":0,\"Proxy/dcterms:provenance\":0,\"Proxy/dcterms:hasPart\":0,\"Proxy/dcterms:isPartOf\":1,\"Proxy/dc:format\":0,\"Proxy/dc:source\":0,\"Proxy/dc:rights\":1,\"Proxy/dc:relation\":0,\"Proxy/edm:isNextInSequence\":0,\"Proxy/edm:type\":1,\"Aggregation/edm:rights\":1,\"Aggregation/edm:provider\":1,\"Aggregation/edm:dataProvider\":1,\"Aggregation/edm:isShownAt\":1,\"Aggregation/edm:isShownBy\":1,\"Aggregation/edm:object\":1,\"Aggregation/edm:hasView\":0", counters.getFullResults(true));
		assertEquals("92062/BibliographicResource_1000126015451,0.400000,1.000000,0.181818,0.388889,0.272727,0.500000,0.357143,0.750000,0.363636,0.400000,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0", counters.getFullResults(false));
		assertEquals("92062/BibliographicResource_1000126015451,0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0", counters.getFullResults(false, true));
	}

	@Test
	public void testCompressNumber() {
		assertEquals("0.5", Counters.compressNumber("0.50000"));
		assertEquals("0.0", Counters.compressNumber("0.00000"));
	}

	@Test
	public void testExistenceMap() throws URISyntaxException, IOException {
		completenessCalculator = new CompletenessCalculator(new EdmSchema());
		// completenessCalculator.setDataProviderManager(new DataProviderManager());
		// completenessCalculator.setDatasetManager(new DatasetManager());
		completenessCalculator.setVerbose(true);

		JsonPathCache cache = new JsonPathCache(readFirstLine("general/test.json"));
		completenessCalculator.measure(cache, counters);
		Map<String, Boolean> map = counters.getExistenceMap();
		assertEquals(35, map.size());

		int existingFieldCounter = 0;
		for (boolean existing : map.values()) {
			if (existing)
				existingFieldCounter++;
		}
		assertEquals(14, existingFieldCounter);

		assertTrue(map.get("edm:ProvidedCHO/@about"));
		assertTrue(map.get("Proxy/dc:title"));
		assertFalse(map.get("Proxy/dcterms:alternative"));
		assertFalse(map.get("Proxy/dc:description"));
		assertFalse(map.get("Proxy/dc:creator"));
		assertFalse(map.get("Proxy/dc:publisher"));
		assertFalse(map.get("Proxy/dc:contributor"));
		assertTrue(map.get("Proxy/dc:type"));
		assertTrue(map.get("Proxy/dc:identifier"));
		assertFalse(map.get("Proxy/dc:language"));
		assertFalse(map.get("Proxy/dc:coverage"));
		assertFalse(map.get("Proxy/dcterms:temporal"));
		assertFalse(map.get("Proxy/dcterms:spatial"));
		assertTrue(map.get("Proxy/dc:subject"));
		assertFalse(map.get("Proxy/dc:date"));
		assertFalse(map.get("Proxy/dcterms:created"));
		assertFalse(map.get("Proxy/dcterms:issued"));
		assertFalse(map.get("Proxy/dcterms:extent"));
		assertFalse(map.get("Proxy/dcterms:medium"));
		assertFalse(map.get("Proxy/dcterms:provenance"));
		assertFalse(map.get("Proxy/dcterms:hasPart"));
		assertTrue(map.get("Proxy/dcterms:isPartOf"));
		assertFalse(map.get("Proxy/dc:format"));
		assertFalse(map.get("Proxy/dc:source"));
		assertTrue(map.get("Proxy/dc:rights"));
		assertFalse(map.get("Proxy/dc:relation"));
		assertFalse(map.get("Proxy/edm:isNextInSequence"));
		assertTrue(map.get("Proxy/edm:type"));
		// assertFalse(map.get("Proxy/edm:rights"));
		assertTrue(map.get("Aggregation/edm:rights"));
		assertTrue(map.get("Aggregation/edm:provider"));
		assertTrue(map.get("Aggregation/edm:dataProvider"));
		assertTrue(map.get("Aggregation/edm:isShownAt"));
		assertTrue(map.get("Aggregation/edm:isShownBy"));
		assertTrue(map.get("Aggregation/edm:object"));
		assertFalse(map.get("Aggregation/edm:hasView"));
	}

	@Test
	public void testExistenceList() throws URISyntaxException, IOException {
		completenessCalculator = new CompletenessCalculator(new EdmSchema());
		completenessCalculator.setVerbose(true);
		JsonPathCache cache = new JsonPathCache(readFirstLine("general/test.json"));
		completenessCalculator.measure(cache, counters);
		List<Integer> expected = Arrays.asList(new Integer[]{1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0});
		assertEquals(35, counters.getExistenceList().size());
		assertEquals(expected, counters.getExistenceList());
	}
}
