package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.util.FileUtils;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
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

	@Test
	public void testNoAbbreviate() throws URISyntaxException, IOException {
		CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
		calculatorFacade.setSchema(new EdmOaiPmhXmlSchema());
		calculatorFacade.configure();
		String expected = "0.184,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0,0.0,0.0,0.0";
		assertEquals(expected, calculatorFacade.measure(FileUtils.readFirstLine("general/test.json")));
	}

	@Test
	public void testWithAbbreviate() throws URISyntaxException, IOException {
		CalculatorFacade calculatorFacade = new CalculatorFacade(true, true, true, false, true);
		calculatorFacade.setSchema(new EdmOaiPmhXmlSchema());
		calculatorFacade.configure();
		String expected = "0.184,1.0,0.181818,0.388889,0.272727,0.5,0.357143,0.75,0.363636,0.4,1,1,1,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,1,1,0,0,0,0,5,0,0,0,0,0,0,0,1,0,0,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,12,0,0,0.0,0.0,0.0";
		assertEquals(expected, calculatorFacade.measure(FileUtils.readFirstLine("general/test.json")));
	}

	@Test
	public void emptyConstructor() {
		CalculatorFacade calculator = new CalculatorFacade();
		assertTrue(calculator.isFieldExistenceMeasurementEnabled());
		assertTrue(calculator.isFieldCardinalityMeasurementEnabled());
		assertTrue(calculator.isCompletenessMeasurementEnabled());
		assertFalse(calculator.isTfIdfMeasurementEnabled());
		assertFalse(calculator.isProblemCatalogMeasurementEnabled());
		assertFalse(calculator.isLanguageMeasurementEnabled());
		assertFalse(calculator.collectTfIdfTerms());
		assertFalse(calculator.completenessCollectFields());
	}

	@Test
	public void testChanged() {
		CalculatorFacade calculator = new CalculatorFacade();
		calculator.setSchema(new EdmOaiPmhXmlSchema());
		calculator.enableFieldExtractor(false);

		assertFalse(calculator.isTfIdfMeasurementEnabled());
		calculator.configure();
		List<Calculator> calculators = calculator.getCalculators();
		assertEquals(1, calculators.size());

		calculator.enableTfIdfMeasurement(true);
		calculator.changed();
		calculators = calculator.getCalculators();
		assertEquals(2, calculators.size());

		calculator.changed();
		calculators = calculator.getCalculators();
		assertEquals(2, calculators.size());
	}
}
