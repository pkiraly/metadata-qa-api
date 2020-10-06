package de.gwdg.metadataqa.api.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilsTest {

  @Test
  public void testReadLinesFromFile() throws IOException {
    String fileName = new File(".").getAbsolutePath()
                    + "/src/test/resources/profiles/d-989.profile-field-counts.csv";
    List<String> lines = FileUtils.readLinesFromFile(fileName);
    assertEquals(1, lines.size());
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      lines.get(0));
  }

  @Test
  public void testReadLines() throws IOException, URISyntaxException {
    List<String> lines = FileUtils.readLines("profiles/d-989.profile-field-counts.csv");
    assertEquals(1, lines.size());
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      lines.get(0));
  }

  @Test
  public void testReadFirstLineFromFile() throws IOException {
    String fileName = new File(".").getAbsolutePath()
      + "/src/test/resources/profiles/d-989.profile-field-counts.csv";
    String line = FileUtils.readFirstLineFromFile(fileName);
    assertNotNull(line);
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      line);
  }

  @Test
  public void testReadFirstLine() throws IOException, URISyntaxException {
    String line = FileUtils.readFirstLine("profiles/d-989.profile-field-counts.csv");
    assertNotNull(line);
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      line);
  }
}