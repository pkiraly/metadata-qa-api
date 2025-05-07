package de.gwdg.metadataqa.api.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilsTest {

  @Test
  public void readLinesFromFile() throws IOException {
    String fileName = new File(".").getAbsolutePath()
      + "/src/test/resources/profiles/d-989.profile-field-counts.csv";
    List<String> lines = FileUtils.readLinesFromFile(fileName);
    assertEquals(1, lines.size());
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      lines.get(0));
  }

  @Test
  public void readLinesFromResource() throws IOException, URISyntaxException {
    List<String> lines = FileUtils.readLinesFromResource("profiles/d-989.profile-field-counts.csv");
    assertEquals(1, lines.size());
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      lines.get(0));
  }

  @Test
  public void readContentFromResource() throws IOException, URISyntaxException {
    String content = FileUtils.readContentFromResource("profiles/d-989.profile-field-counts.csv");
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      content);
  }

  @Test
  public void readFirstLineFromFile() throws IOException {
    String fileName = new File(".").getAbsolutePath()
      + "/src/test/resources/profiles/d-989.profile-field-counts.csv";
    String line = FileUtils.readFirstLineFromFile(fileName);
    assertNotNull(line);
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      line);
  }

  @Test
  public void readFirstLineFromResource() throws IOException, URISyntaxException {
    String line = FileUtils.readFirstLineFromResource("profiles/d-989.profile-field-counts.csv");
    assertNotNull(line);
    assertEquals("d-989,\"dc:description=7836,dc:creator=7836,dc:contributor=7836,dc:type=7836,dc:identifier=7836,dc:language=7836,dc:coverage=4289,dc:subject=7824,dc:date=1578,dcterms:extent=7836,dcterms:medium=4877,dcterms:isPartOf=7836,dc:format=4877,edm:type=7836,dc:title=7836\"",
      line);
  }

  @Test
  public void readFromUrl() throws IOException {
    String content = FileUtils.readFromUrl("https://github.com/pkiraly/metadata-qa-api");
    assertTrue(content.contains("Metadata Quality Assessment Framework API"));
  }

  @Test
  public void getPath_happyPath() throws IOException, URISyntaxException {
    Path path = FileUtils.getPath("profiles/d-989.profile-field-counts.csv");
    assertNotNull(path);
    assertEquals("d-989.profile-field-counts.csv", path.getFileName().toString());
    int count = path.getNameCount();
    assertEquals("d-989.profile-field-counts.csv", path.getName(count - 1).toString());
    assertEquals("profiles", path.getName(count - 2).toString());
    assertEquals("test-classes", path.getName(count - 3).toString());
    assertEquals("target", path.getName(count - 4).toString());
    assertEquals("metadata-qa-api", path.getName(count - 5).toString());
  }

  @Test
  public void getPath_exception() {
    Exception e = assertThrows(IOException.class, () -> {
      Path path = FileUtils.getPath("profiles/dummy.csv");
    });
    assertEquals("File profiles/dummy.csv is not available", e.getMessage());
  }

  @Test
  public void Constructor_instantiationIsImpossible() throws NoSuchMethodException {
    Constructor<FileUtils> constructor = FileUtils.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);

    Exception e = assertThrows(InstantiationException.class, () -> {
      FileUtils u = constructor.newInstance();
    });
    assertNull(e.getMessage());
  }
}