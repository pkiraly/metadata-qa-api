package com.nsdr.metadataqa.api.problemcatalog;

import com.nsdr.metadataqa.api.problemcatalog.ProblemCatalog;
import com.nsdr.metadataqa.api.problemcatalog.ProblemDetector;
import com.nsdr.metadataqa.api.problemcatalog.LongSubject;
import com.nsdr.metadataqa.api.model.JsonPathCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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
public class TestLongSubject {

	public TestLongSubject() {
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
		String fileName = "problem-catalog/long-subject.json";
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		String jsonString = lines.get(0);
		JsonPathCache cache = new JsonPathCache(jsonString);

		ProblemCatalog problemCatalog = new ProblemCatalog();
		ProblemDetector detector = new LongSubject(problemCatalog);
		Map<String, Double> results = new HashMap<>();

		detector.update(cache, results);
		assertEquals((Double)1.0, (Double)results.get("LongSubject"));
	}
}
