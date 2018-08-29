package de.gwdg.metadataqa.api.json;

import de.gwdg.metadataqa.api.calculator.CompletenessCalculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.EdmFullBeanSchema;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class JsonUtilsTest {

	Object jsonDoc;
	String jsonString;
	String problemFileName = "issue-examples/issue48.json";

	public JsonUtilsTest() throws IOException, URISyntaxException {
		jsonString = FileUtils.readFirstLine(problemFileName);
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

		CompletenessCalculator calculator = new CompletenessCalculator(new EdmFullBeanSchema());
		calculator.collectFields(true);
		calculator.setExistence(true);
		calculator.setCardinality(true);

		JsonPathCache cache = new JsonPathCache(jsonString);
		calculator.measure(cache);
		calculator.getCsv(false, CompressionLevel.NORMAL);
	}
}
