package de.gwdg.metadataqa.api.abbreviation;

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
    assertEquals(264, (int)manager.lookup("Preiser Records; Austria"));
  }

  @Test
  public void testProcessLineWithOutParse() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.processLine("264;Preiser Records; Austria", 1, false);
    assertEquals(1, manager.getData().keySet().size());
    assertTrue(manager.getData().containsKey("264;Preiser Records; Austria"));
    assertEquals(1, (int)manager.getData().get("264;Preiser Records; Austria"));
    assertEquals(1, (int)manager.lookup("264;Preiser Records; Austria"));
  }

  @Test
  public void testInitialize() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.initialize("abbreviations/data-providers.txt");
    assertEquals(3940, manager.getData().keySet().size());

    manager.initialize("abbreviations/data-providers.txt", false);
    assertEquals(3940, manager.getData().keySet().size());
  }

  @Test
  public void testInitializeWithNonExistingFile() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.initialize("abbreviations/non-existing.txt");
    assertEquals(0, manager.getData().keySet().size());
  }

  @Test
  public void testLookupNonExistent() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.processLine("264;Preiser Records; Austria", 1, true);
    assertEquals(1, manager.getData().keySet().size());
    assertEquals(2, (int)manager.lookup("Non existent key"));
  }

  @Test
  public void testProcessLineWithoutColon() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.processLine("Preiser Records - Austria", 1, true);
    assertEquals(1, manager.getData().keySet().size());
    assertEquals(1, (int)manager.lookup("Preiser Records - Austria"));
    assertEquals(2, (int)manager.lookup("Non existent key"));
  }
}
