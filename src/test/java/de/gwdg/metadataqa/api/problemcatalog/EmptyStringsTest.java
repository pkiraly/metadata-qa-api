package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.schema.EdmOaiPmhXmlSchema;
import de.gwdg.metadataqa.api.schema.EdmSchema;
import de.gwdg.metadataqa.api.schema.Schema;
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
		EdmSchema schema = new EdmOaiPmhXmlSchema();
		ProblemCatalog catalog = new ProblemCatalog(schema);
		EmptyStrings detector = new EmptyStrings(catalog);

		assertNotNull(detector.getHeader());
		assertEquals("EmptyStrings", detector.getHeader());
	}
}
