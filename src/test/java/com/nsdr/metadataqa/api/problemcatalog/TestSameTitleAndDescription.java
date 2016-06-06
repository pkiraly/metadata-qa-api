package com.nsdr.metadataqa.api.problemcatalog;

import com.nsdr.metadataqa.api.problemcatalog.ProblemCatalog;
import com.nsdr.metadataqa.api.problemcatalog.TitleAndDescriptionAreSame;
import com.nsdr.metadataqa.api.problemcatalog.ProblemDetector;
import com.nsdr.metadataqa.api.TestUtils;
import com.nsdr.metadataqa.api.model.JsonPathCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
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
public class TestSameTitleAndDescription {

	public TestSameTitleAndDescription() {
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
		String jsonString = TestUtils.readFirstLine("problem-catalog/same-title-and-description.json");
		JsonPathCache cache = new JsonPathCache(jsonString);

		ProblemCatalog problemCatalog = new ProblemCatalog();
		ProblemDetector detector = new TitleAndDescriptionAreSame(problemCatalog);
		Map<String, Double> results = new HashMap<>();

		detector.update(cache, results);
		assertEquals((Double)1.0, (Double)results.get("TitleAndDescriptionAreSame"));
	}
}
