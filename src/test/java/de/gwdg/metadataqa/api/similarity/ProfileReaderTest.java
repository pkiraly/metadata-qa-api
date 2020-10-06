package de.gwdg.metadataqa.api.similarity;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class ProfileReaderTest {

  String fieldsFile = "profiles/d-989.profile-field-counts.csv";
  String profileFile = "profiles/d-989.profile-patterns.csv";
  private List<String> canonicalFieldList;
  private List<String> profiles;

  @Before
  public void setUp() {
    try {
      canonicalFieldList = ProfileReader.parseFieldCountLine(FileUtils.readFirstLineFromResource(fieldsFile));
      profiles = FileUtils.readLinesFromResource(profileFile);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testExtraction() {
    ProfileReader profileReader = new ProfileReader(canonicalFieldList, profiles);
    Map<List<RecordPattern>, Double> sortedClusters = profileReader.buildCluster();
    assertEquals(5, sortedClusters.size());
    List<Map.Entry<List<RecordPattern>, Double>> clusters =
        new ArrayList(sortedClusters.entrySet());
    assertEquals(5, clusters.size());
    Map.Entry<List<RecordPattern>, Double> cluster = clusters.get(0);
    List<RecordPattern> patterns = cluster.getKey();
    assertEquals(2, patterns.size());
    RecordPattern pattern = patterns.get(0);
    assertEquals(14, (int) pattern.getNumberOfFields());
    assertEquals(2555, (int) pattern.getCount());
    // assertEquals(2060, (int) pattern.getTotal());
    assertEquals("11111111111111111111001111111111111", pattern.getBinary());
    assertEquals(
        "dc:title;dc:description;dc:creator;dc:contributor;dc:type;dc:identifier;dc:language;dc:coverage;dc:subject;dcterms:extent;dcterms:medium;dcterms:isPartOf;dc:format;edm:type",
        pattern.getFields());
    assertEquals(32.6059213884635, pattern.getPercent(), 0.00001);
    assertEquals(49.91066870852475, cluster.getValue(), 0.0001);
    assertEquals(3911, getSum(clusters.get(0).getKey()));

    assertEquals(32.503828483920365, clusters.get(1).getValue(), 0.0001);
    assertEquals(2547, getSum(clusters.get(1).getKey()));
    assertEquals(14.752424706482898, clusters.get(2).getValue(), 0.0001);
    assertEquals(1156, getSum(clusters.get(2).getKey()));
    assertEquals(2.2460438999489534, clusters.get(3).getValue(), 0.0001);
    assertEquals(176, getSum(clusters.get(3).getKey()));
    assertEquals(0.587034201123022, clusters.get(4).getValue(), 0.0001);
    assertEquals(46, getSum(clusters.get(4).getKey()));

    sortedClusters.entrySet().stream().forEach((pCluster) -> {
      int i = profileReader.getNext();
      int sum = profileReader.count(pCluster.getKey());
      // System.err.printf("#%d=%d\n", i, sum);
      pCluster.
          getKey().
          forEach((pRow) -> {
            // System.err.printf("%d,%s\n", i, pRow.asCsv());
          });
        // System.err.printf("=%.2f%%\n", cluster.getValue());
      });
  }

  private int getSum(List<RecordPattern> patterns) {
    int sum = 0;
    for (RecordPattern pattern : patterns) {
      sum += pattern.getCount();
    }
    return sum;
  }

  @Test
  public void test_parseFieldCountFile() throws IOException, URISyntaxException {
    String fieldsFile = "profiles/d-989.profile-field-counts.csv";
    assertEquals(
      Arrays.asList("dc:description", "dc:creator", "dc:contributor", "dc:type",
        "dc:identifier",  "dc:language", "dc:coverage", "dc:subject", "dc:date",
        "dcterms:extent", "dcterms:medium", "dcterms:isPartOf", "dc:format",
        "edm:type", "dc:title"
      ),
      ProfileReader.parseFieldCountLine(FileUtils.readFirstLineFromResource(fieldsFile))
    );
  }


}
