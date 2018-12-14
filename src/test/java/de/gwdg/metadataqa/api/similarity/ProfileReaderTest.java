package de.gwdg.metadataqa.api.similarity;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import de.gwdg.metadataqa.api.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class ProfileReaderTest {

  private List<String> canonicalFieldList;
  private List<String> profiles;

  @Before
  public void setUp() {
    try {
      canonicalFieldList = Arrays.asList(FileUtils.readLines("profiles/d988-fields.csv").get(0).split(";"));
      profiles = FileUtils.readLines("profiles/d988-profiles.csv");
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
    assertEquals(8, sortedClusters.size());
    List<Map.Entry<List<RecordPattern>, Double>> clusters =
        new ArrayList(sortedClusters.entrySet());
    assertEquals(8, clusters.size());
    Map.Entry<List<RecordPattern>, Double> cluster = clusters.get(0);
    List<RecordPattern> patterns = cluster.getKey();
    assertEquals(5, patterns.size());
    RecordPattern pattern = patterns.get(0);
    assertEquals(10, (int) pattern.getLength());
    assertEquals(738, (int) pattern.getCount());
    assertEquals(2060, (int) pattern.getTotal());
    assertEquals("000111111111110011111111111", pattern.getBinary());
    assertEquals(
        "dc:title;dc:creator;dc:publisher;dc:type;dc:identifier;dc:language;dc:subject;dcterms:issued;edm:type;dcterms:isVersionOf",
        pattern.getFields());
    assertEquals(35.8252427184466, pattern.getPercent(), 0.00001);
    assertEquals(71.74757281553397, cluster.getValue(), 0.0001);
    assertEquals(1478, getSum(clusters.get(0).getKey()));

    assertEquals(21.844660194174757, clusters.get(1).getValue(), 0.0001);
    assertEquals(450, getSum(clusters.get(1).getKey()));
    assertEquals(3.4951456310679614, clusters.get(2).getValue(), 0.0001);
    assertEquals(72, getSum(clusters.get(2).getKey()));
    assertEquals(1.5048543689320388, clusters.get(3).getValue(), 0.0001);
    assertEquals(31, getSum(clusters.get(3).getKey()));
    assertEquals(0.8737864077669903, clusters.get(4).getValue(), 0.0001);
    assertEquals(18, getSum(clusters.get(4).getKey()));
    assertEquals(0.4368932038834952, clusters.get(5).getValue(), 0.0001);
    assertEquals(9, getSum(clusters.get(5).getKey()));
    assertEquals(0.04854368932038835, clusters.get(6).getValue(), 0.0001);
    assertEquals(1, getSum(clusters.get(6).getKey()));
    assertEquals(0.04854368932038835, clusters.get(7).getValue(), 0.0001);
    assertEquals(1, getSum(clusters.get(7).getKey()));

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
}
