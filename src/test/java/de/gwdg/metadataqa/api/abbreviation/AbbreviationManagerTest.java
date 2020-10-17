package de.gwdg.metadataqa.api.abbreviation;

import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Test;

import java.io.*;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class AbbreviationManagerTest {

  private final String testFile = "src/test/resources/abbreviation/test.csv";

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

  @Test
  public void getOrDefault() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.processLine("264;Preiser Records; Austria", 1, true);
    assertEquals(264, manager.getOrDefault("Preiser Records; Austria", -1).intValue());
  }

  @Test
  public void save() {
    assertFalse(new File(testFile).exists());

    AbbreviationManager manager = new AbbreviationManager();
    manager.processLine("264;Preiser Records; Austria", 1, true);

    try {
      new File(testFile).createNewFile();
      manager.save(testFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(new File(testFile).exists());

    try {
      assertEquals(
        "264;Preiser Records; Austria",
        FileUtils.readFirstLineFromFile(testFile));
    } catch (IOException e) {
      e.printStackTrace();
    }

    new File(testFile).delete();
    assertFalse(new File(testFile).exists());
  }

  @Test
  public void searchById_normal() {
    AbbreviationManager manager = new AbbreviationManager();
    manager.processLine("264;Preiser Records; Austria", 1, true);
    assertEquals("Preiser Records; Austria", manager.searchById(264));
  }

  @Test
  public void lookup_havingFile() {
    OutputStream logCapturingStream = new ByteArrayOutputStream();
    Logger LOGGER = Logger.getLogger(AbbreviationManager.class.getCanonicalName());
    Handler[] handlers = LOGGER.getParent().getHandlers();
    StreamHandler customLogHandler = new StreamHandler(logCapturingStream, handlers[0].getFormatter());
    LOGGER.addHandler(customLogHandler);

    AbbreviationManager manager = new DummyAbbreviationManager();
    manager.lookup("New value");

    customLogHandler.flush();
    String msg = logCapturingStream.toString();
    String expected = "INFO: new entry: New value (size: 3940 -> 3941) abbreviations/data-providers.txt";
    assertTrue(msg.contains(expected));
  }

  private class DummyAbbreviationManager extends AbbreviationManager {
    public DummyAbbreviationManager() {
      super();
      initialize("abbreviations/data-providers.txt");
    }
  }
}
