package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.calculator.CalculatorFacade;
import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.TestUtils;
import de.gwdg.metadataqa.api.counter.Counters;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
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
public class CalculatorFacadeTest {

	public CalculatorFacadeTest() {
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

	private void run(CalculatorFacade calculatorFacade, String expected)
			throws URISyntaxException, IOException {
		String jsonRecord = TestUtils.readFirstLine("general/test.json");
		calculatorFacade.configure();

		try {
			Counters counters = new Counters();
			calculatorFacade.configureCounter(counters);

			JsonPathCache<EdmFieldInstance> cache = new JsonPathCache<>(jsonRecord);

			for (Calculator calculator : calculatorFacade.getCalculators()) {
				calculator.measure(cache, counters);
			}

			// return the result of calculations
			String csv = counters.getFullResults(false, true);
			assertEquals(expected, csv);
			// store csv to somewhere
		} catch (InvalidJsonException e) {
			// log problem
		}
	}

	@Test
	public void testNoAbbreviate() throws URISyntaxException, IOException {
		CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
		String expected = "0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0.0,0.0,0.0";
		run(calculatorFacade, expected);
	}

	@Test
	public void testWithAbbreviate() throws URISyntaxException, IOException {
		CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
		String expected = "0.4,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0.0,0.0,0.0";
		run(calculatorFacade, expected);
	}

	@Test
	public void emptyConstructor() {
		CalculatorFacade calculator = new CalculatorFacade();
		assertTrue(calculator.runFieldExistence());
		assertTrue(calculator.runFieldCardinality());
		assertTrue(calculator.runCompleteness());
		assertFalse(calculator.runTfIdf());
		assertFalse(calculator.runProblemCatalog());
		assertFalse(calculator.runLanguage());
		assertFalse(calculator.collectTfIdfTerms());
		assertFalse(calculator.completenessCollectFields());
	}

	@Test
	public void testChanged() {
		CalculatorFacade calculator = new CalculatorFacade();

		assertFalse(calculator.runTfIdf());
		calculator.configure();
		List<Calculator> calculators = calculator.getCalculators();
		assertEquals(1, calculators.size());

		calculator.runTfIdf(true);
		calculator.changed();
		calculators = calculator.getCalculators();
		assertEquals(2, calculators.size());

		calculator.changed();
		calculators = calculator.getCalculators();
		assertEquals(2, calculators.size());
	}
}
