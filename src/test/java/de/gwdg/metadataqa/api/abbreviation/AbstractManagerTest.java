package de.gwdg.metadataqa.api.abbreviation;

import de.gwdg.metadataqa.api.abbreviation.AbbreviationManager;
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
public class AbstractManagerTest {
	
	public AbstractManagerTest() {
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
	public void testProcessLineWithParse() {
		AbbreviationManager manager = new AbbreviationManager();
		manager.processLine("264;Preiser Records; Austria", 1, true);
		assertEquals(1, manager.getData().keySet().size());
		assertTrue(manager.getData().containsKey("Preiser Records; Austria"));
		assertEquals(264, (int)manager.getData().get("Preiser Records; Austria"));
	}

	@Test
	public void testProcessLineWithOutParse() {
		AbbreviationManager manager = new AbbreviationManager();
		manager.processLine("264;Preiser Records; Austria", 1, false);
		assertEquals(1, manager.getData().keySet().size());
		assertTrue(manager.getData().containsKey("264;Preiser Records; Austria"));
		assertEquals(1, (int)manager.getData().get("264;Preiser Records; Austria"));
	}
}
