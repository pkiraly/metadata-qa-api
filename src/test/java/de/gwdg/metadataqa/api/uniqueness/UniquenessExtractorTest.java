package de.gwdg.metadataqa.api.uniqueness;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessExtractorTest {

	public UniquenessExtractorTest() {
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

	public String readContent(String fileName) throws URISyntaxException, IOException {
		// Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		// List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		return StringUtils.join(FileUtils.readLines(fileName), "");
	}

	@Test
	public void hello() throws URISyntaxException, IOException {
		JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
		String recordId = "2022320/3F61C612ED9C42CCB85E533B4736795E8BDC7E77";
		String jsonString = readContent("general/uniqueness-response.json");
		assertEquals("{", jsonString.substring(0,1));

		UniquenessExtractor extractor = new UniquenessExtractor(new EdmOaiPmhXmlSchema());
		int numFound = extractor.extractNumFound(jsonString, recordId);
		assertEquals(199, numFound);
	}

	@Test
	public void hello2() throws URISyntaxException, IOException {
		JsonPathCache cache = new JsonPathCache(FileUtils.readFirstLine("general/edm-fullbean.json"));
		// String path = "$.['proxies'][?(@['europeanaProxy'] == false)][*]['dcTitle']";
		String path = "$.['proxies'][?(@['europeanaProxy'] == false)]['dcTitle']";
		List<XmlFieldInstance> values = (List<XmlFieldInstance>) cache.get(path);
		System.err.println(values);
	}

}
