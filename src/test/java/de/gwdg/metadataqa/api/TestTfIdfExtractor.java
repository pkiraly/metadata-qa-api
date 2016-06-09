package de.gwdg.metadataqa.api;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import de.gwdg.metadataqa.api.schema.EdmSchema;
import de.gwdg.metadataqa.api.uniqueness.TfIdfExtractor;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class TestTfIdfExtractor {

	public TestTfIdfExtractor() {
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
		return StringUtils.join(TestUtils.readLines(fileName), "");
	}

	@Test
	public void hello() throws URISyntaxException, IOException {
		JsonProvider jsonProvider = Configuration.defaultConfiguration().jsonProvider();
		String recordId = "2022320/3F61C612ED9C42CCB85E533B4736795E8BDC7E77";
		String jsonString = readContent("general/td-idf-response.json");
		assertEquals("{", jsonString.substring(0,1));

		TfIdfExtractor extractor = new TfIdfExtractor(new EdmSchema());
		Map<String, Double> results = extractor.extract(jsonString, recordId);
		assertEquals(6, results.size());
		assertEquals(new Double(0.0017653998874690505), results.get("dc:title:avg"));
		assertEquals(new Double(0.008826999437345252), results.get("dc:title:sum"));
		assertEquals(new Double(0), results.get("dcterms:alternative:avg"));
		assertEquals(new Double(0), results.get("dcterms:alternative:sum"));
		assertEquals(new Double(0), results.get("dc:description:avg"));
		assertEquals(new Double(0), results.get("dc:description:sum"));
	}
}
