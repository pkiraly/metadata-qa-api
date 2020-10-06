package de.gwdg.metadataqa.api.similarity;

import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RecordPatternTest {

  @Test
  public void testConstructor() throws IOException, URISyntaxException {
    String fieldsFile = "profiles/d-989.profile-field-counts.csv";
    String profileFile = "profiles/d-989.profile-patterns.csv";
    List<String> canonicalFieldList = ProfileReader.parseFieldCountLine(FileUtils.readFirstLineFromResource(fieldsFile));;
    List<String> profiles = FileUtils.readLinesFromResource(profileFile);

    BinaryMaker binaryMaker = new BinaryMaker(canonicalFieldList);
    RecordPattern row = new RecordPattern(binaryMaker, Arrays.asList(profiles.get(0).trim().split(",")));
    assertEquals("d-989", row.getId());
    assertEquals(
      "dc:title;dc:description;dc:creator;dc:contributor;dc:type;dc:identifier;dc:language;dc:coverage;dc:subject;dcterms:extent;dcterms:medium;dcterms:isPartOf;dc:format;edm:type",
      row.getFields());
    assertEquals(Integer.valueOf(14), row.getNumberOfFields());
    assertEquals(Integer.valueOf(2555), row.getCount());
    assertEquals(Double.valueOf(32.6059213884635), row.getPercent());
    assertEquals(Arrays.asList(
      "dc:title", "dc:description", "dc:creator", "dc:contributor", "dc:type", "dc:identifier",
      "dc:language", "dc:coverage", "dc:subject", "dcterms:extent", "dcterms:medium",
      "dcterms:isPartOf", "dc:format", "edm:type"
    ), row.getFieldList());

    assertEquals("11111111111111111111001111111111111", row.getBinary());
  }
}