package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.MarcJsonSchema;
import de.gwdg.metadataqa.api.util.FileUtils;
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
public class MarcCompletenessCalculatorTest {

	JsonPathCache cache;
	CompletenessCalculator calculator;

	public MarcCompletenessCalculatorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
		calculator = new CompletenessCalculator(new MarcJsonSchema());
		cache = new JsonPathCache(FileUtils.readFirstLine("general/marc.json"));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCompleteness() throws URISyntaxException, IOException {

		calculator.measure(cache);
		FieldCounter<Double> fieldCounter = calculator.getCompletenessCounter().getFieldCounter();
		FieldCounter<Boolean> existenceCounter = calculator.getExistenceCounter();
		FieldCounter<Integer> cardinalityCounter = calculator.getCardinalityCounter();

		System.err.println(fieldCounter.getList(true));
		System.err.println(existenceCounter.getList(true));
		System.err.println(cardinalityCounter.getList(true));

		assertEquals((Double) 0.184, fieldCounter.get("TOTAL"));
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

	}
}
