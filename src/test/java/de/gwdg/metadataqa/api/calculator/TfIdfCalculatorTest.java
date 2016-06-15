package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.schema.EdmSchema;
import java.util.Arrays;
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
public class TfIdfCalculatorTest {
	
	public TfIdfCalculatorTest() {
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
	public void testGetHeaders() {
		TfIdfCalculator calculator = new TfIdfCalculator(new EdmSchema());
		List<String> expected = Arrays.asList("dc:title:sum", "dc:title:avg", "dcterms:alternative:sum", "dcterms:alternative:avg", "dc:description:sum", "dc:description:avg");
		assertEquals(6, calculator.getHeader().size());
		assertEquals(expected, calculator.getHeader());
	}
}
