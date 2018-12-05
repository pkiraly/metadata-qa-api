package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.calculator.edm.EnhancementIdExtractor;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.util.FileUtils;
import java.io.IOException;
import java.net.URISyntaxException;
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
public class EnhancementExtractorTest {

	JsonPathCache cache;

	public EnhancementExtractorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
		cache = new JsonPathCache(FileUtils.readFirstLine("general/test.json"));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void hello() {
		// EnhancementIdExtractor extractor = new EnhancementIdExtractor();
		List<String> enhancementIds = EnhancementIdExtractor.extractIds(cache);
		assertTrue(!enhancementIds.isEmpty());
		assertEquals(1, enhancementIds.size());
		assertEquals("http://dbpedia.org/resource/Portrait", enhancementIds.get(0));
	}
}
