package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.calculator.FieldExtractor;
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
public class EmptyStringsTest {
	
	public EmptyStringsTest() {
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
		ProblemCatalog catalog = new ProblemCatalog();
		EmptyStrings detector = new EmptyStrings(catalog);

		assertNotNull(detector.getHeader());
		assertEquals("EmptyStrings", detector.getHeader());
	}
}
