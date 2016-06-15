package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
public class JsonPathCacheTest {

	Object jsonDoc;
	String jsonString;

	public JsonPathCacheTest() throws IOException, URISyntaxException {
		String fileName = "problem-catalog/long-subject.json";
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		jsonString = lines.get(0);
		// jsonDoc = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
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
	public void testSimpleValue() throws IOException, URISyntaxException {
		String jsonPath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNotNull(instances);
		assertEquals(1, instances.size());
		assertEquals("fiancata di biroccio", instances.get(0).getValue());
		assertNull(instances.get(0).getLanguage());
		assertNull(instances.get(0).getResource());
	}

	@Test
	public void testNonexistingField() throws IOException, URISyntaxException {
		String jsonPath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title2']";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNull(instances);
	}

	@Test
	public void testResourceField() throws IOException, URISyntaxException {
		String jsonPath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isReferencedBy']";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNotNull(instances);
		assertEquals(2, instances.size());
		assertEquals("http://sirpac.cultura.marche.it/web/Ricerca.aspx?ids=33334",
				  instances.get(0).getResource());
		assertNull(instances.get(0).getLanguage());
		assertNull(instances.get(0).getValue());
	}

	@Test
	public void testAbout() throws IOException, URISyntaxException {
		String jsonPath = "$.['edm:ProvidedCHO'][0]['@about']";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNotNull(instances);
		assertEquals(1, instances.size());
		assertEquals("http://data.europeana.eu/item/07602/5CFC6E149961A1630BAD5C65CE3A683DEB6285A0",
				  instances.get(0).getValue());
		assertNull(instances.get(0).getLanguage());
		assertNull(instances.get(0).getResource());
	}

	@Test
	public void testLanguage() throws URISyntaxException, IOException {
		jsonString = FileUtils.readFirstLine("problem-catalog/same-title-and-description.json");
		String jsonPath = "$.['edm:Place'][0]['skos:prefLabel']";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNotNull(instances);
		assertEquals(117, instances.size());
		assertEquals("Holani", instances.get(0).getValue());
		assertEquals("to", instances.get(0).getLanguage());
		assertNull(instances.get(0).getResource());
	}

	/**
	 * Issue #5
	 */
	@Test
	public void testArrayInInnerArray() throws URISyntaxException, IOException {
		jsonString = FileUtils.readFirstLine("issue-examples/issue5-array-in-innerarray.json");
		String jsonPath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:created']";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNotNull(instances);
		assertEquals(1, instances.size());
		assertEquals("sec. 45 - 40 a. Chr.", instances.get(0).getValue());
		assertNull(instances.get(0).getLanguage());
		assertNull(instances.get(0).getResource());
	}

	/**
	 * Issue #6
	 * Unhandled object type: java.util.LinkedHashMap, 
	 *[record ID: 08541/10442_01_75365, path: $.['ore:Aggregation'][0]['edm:dataProvider'][0]]
	 */
	@Test
	public void testDataProviderHash() throws URISyntaxException, IOException {
		jsonString = FileUtils.readFirstLine("issue-examples/issue6-handling-missing-provider.json");
		String jsonPath = "$.['ore:Aggregation'][0]['edm:dataProvider'][0]";

		JsonPathCache cache = new JsonPathCache(jsonString);
		List<EdmFieldInstance> instances = cache.get(jsonPath);

		assertNotNull(instances);
		assertEquals(1, instances.size());
		assertEquals("Εθνικό Κέντρο Τεκμηρίωσης (ΕΚΤ)", instances.get(0).getValue());
		assertEquals("el", instances.get(0).getLanguage());
		assertNull(instances.get(0).getResource());
	}
}
