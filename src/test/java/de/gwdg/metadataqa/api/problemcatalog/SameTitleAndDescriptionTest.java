package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.util.FileUtils;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import java.io.IOException;
import java.net.URISyntaxException;
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
public class SameTitleAndDescriptionTest {

	public SameTitleAndDescriptionTest() {
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
	public void hello() throws IOException, URISyntaxException {
		String jsonString = FileUtils.readFirstLine("problem-catalog/same-title-and-description.json");
		JsonPathCache cache = new JsonPathCache(jsonString);

		ProblemCatalog problemCatalog = new ProblemCatalog();
		ProblemDetector detector = new TitleAndDescriptionAreSame(problemCatalog);
		FieldCounter<Double> results = new FieldCounter<>();

		detector.update(cache, results);
		assertEquals((Double)1.0, (Double)results.get("TitleAndDescriptionAreSame"));
	}

	@Test
	public void testGetHeaders() {
		ProblemCatalog catalog = new ProblemCatalog();
		TitleAndDescriptionAreSame detector = new TitleAndDescriptionAreSame(catalog);

		assertNotNull(detector.getHeader());
		assertEquals("TitleAndDescriptionAreSame", detector.getHeader());
	}

}
