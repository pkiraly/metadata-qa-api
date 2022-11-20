package de.gwdg.metadataqa.api.cli;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AppTest {

  private final static String BASE_DIR = "src/test/resources/";
  private final static String OUTPUT_DIR = BASE_DIR + "output/";
  private final static String outputFileName = OUTPUT_DIR + "output.csv";
  File outputFile;

  @Before
  public void setUp() throws Exception {
    outputFile = new File(outputFileName);
  }

  public void tearDown() throws Exception {
    if (outputFile.exists())
      outputFile.delete();
  }

  @Test
  public void good_yaml() throws IOException {
    App.main(new String[]{
      "--input", BASE_DIR + "csv/meemoo-simple.csv",
      "--schema", BASE_DIR + "configuration/schema/simple-meemoo.yaml",
      "--measurements", BASE_DIR + "configuration/measurement/simple-meemoo.yaml",
      "--outputFormat", "csv",
      "--output", outputFileName,
    });

    assertTrue(outputFile.exists());

    List<String> output = FileUtils.readLinesFromFile(outputFileName);
    assertEquals(3, output.size());
    assertEquals("\"url\",\"name\"", output.get(0).trim());
    assertEquals("\"https://neurovault.org/images/384958/\",\"massivea uditory lexical decision\"", output.get(1).trim());
    assertEquals("\"https://neurovault.org/images/93390/\",\"Language in the aging brain\"", output.get(2).trim());
  }

  @Test
  public void good_json() throws IOException {
    App.main(new String[]{
      "--input", BASE_DIR + "csv/meemoo-simple.csv",
      "--schema", BASE_DIR + "configuration/schema/simple-meemoo.json",
      "--measurements", BASE_DIR + "configuration/measurement/simple-meemoo.json",
      "--outputFormat", "csv",
      "--output", outputFileName,
    });

    assertTrue(outputFile.exists());

    List<String> output = FileUtils.readLinesFromFile(outputFileName);
    assertEquals(3, output.size());
    assertEquals("\"url\",\"name\"", output.get(0).trim());
    assertEquals("\"https://neurovault.org/images/384958/\",\"massivea uditory lexical decision\"", output.get(1).trim());
    assertEquals("\"https://neurovault.org/images/93390/\",\"Language in the aging brain\"", output.get(2).trim());
  }

  @Test
  public void missingOptions() throws Exception {
    int status = SystemLambda.catchSystemExit(() -> {
        App.main(new String[]{
          "--input", BASE_DIR + "csv/meemoo-simple.csv",
          "--schema", BASE_DIR + "configuration/schema/simple-meemoo.yaml"
        });
      });
    assertEquals(1, status);

    assertFalse(outputFile.exists());
  }

  @Test
  public void missingArguments() throws Exception {
    int status = SystemLambda.catchSystemExit(() -> {
      App.main(new String[]{
        "--input", BASE_DIR + "csv/meemoo-simple.csv",
        "--schema", BASE_DIR + "configuration/schema/simple-meemoo.yaml",
        "--measurements", BASE_DIR + "configuration/measurement/simple-meemoo.yaml",
        "--outputFormat"
      });
    });
    assertEquals(1, status);

    assertFalse(outputFile.exists());
  }
}