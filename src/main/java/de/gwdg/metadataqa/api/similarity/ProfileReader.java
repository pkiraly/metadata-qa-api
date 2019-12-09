package de.gwdg.metadataqa.api.similarity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Profile reader.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class ProfileReader {

  public static final double DEFAULT_TRESHOLD = 0.97;
  private List<String> profiles;
  private Map<String, RecordPattern> rowIndex;
  private int i = 0;
  private BinaryMaker binaryMaker;

  public ProfileReader(List<String> canonicalFieldList, List<String> profiles) {
    binaryMaker = new BinaryMaker(canonicalFieldList);
    this.profiles = profiles;
    rowIndex = new HashMap<>();
  }

  public Map<List<RecordPattern>, Double> buildCluster() {
    return buildCluster(DEFAULT_TRESHOLD);
  }

  public Map<List<RecordPattern>, Double> buildCluster(double treshold) {
    List<String> binaryPatterns = createBinaryPatternList();

    Clustering clustering = new Clustering(binaryPatterns, treshold);
    List<List<String>> clusters = clustering.getClusters();

    Map<List<RecordPattern>, Double> sortableClusters = new HashMap<>();
    for (List<String> terms : clusters) {
      double sum = 0.0;
      Map<String, RecordPattern> sortableTerms = new HashMap<>();
      for (String term : terms) {
        RecordPattern row = rowIndex.get(term);
        sum += row.getPercent();
        sortableTerms.put(term, row);
      }

      List<RecordPattern> sortedTerms = sortTerms(sortableTerms);
      sortableClusters.put(sortedTerms, sum);
    }

    return sortClusters(sortableClusters);
  }

  private Map<List<RecordPattern>, Double> sortClusters(Map<List<RecordPattern>, Double> sortableClusters) {
    Map<List<RecordPattern>, Double> sortedClusters = sortableClusters.
      entrySet().
      stream().
      sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).
      collect(Collectors.
        toMap(
          Map.Entry::getKey,
          Map.Entry::getValue,
          (e1, e2) -> e1,
          LinkedHashMap::new
        )
      );
    return sortedClusters;
  }

  public int getNext() {
    return i++;
  }

  private List<RecordPattern> sortTerms(Map<String, RecordPattern> sortableTerms) {
    return sortableTerms.
          entrySet().
          stream().
          sorted(
            (e1, e2) -> e2.getValue().getPercent().compareTo(
              e1.getValue().getPercent()
            )
          ).
          map(e -> (RecordPattern) e.getValue()).
          collect(
            Collectors.
              toList()
          );
  }

  public int count(List<RecordPattern> rows) {
    int sum = 0;
    for (RecordPattern row : rows) {
      sum += row.getCount();
    }
    return sum;
  }

  public List<String> createBinaryPatternList() {
    List<String> binaryPatterns = new ArrayList<>();
    for (String line : profiles) {
      RecordPattern row = new RecordPattern(binaryMaker, Arrays.asList(line.split(",")));
      binaryPatterns.add(row.getBinary());
      rowIndex.put(row.getBinary(), row);
    }
    return binaryPatterns;
  }

  public static void main(String[] args) throws IOException {
    String fieldListFile = args[0];
    String profileFile = args[1];

    boolean produceList = (args.length > 2 && args[2].equals("list"));

    List<String> fieldLines = Files.readAllLines(
      Paths.get(fieldListFile), Charset.defaultCharset()
    );

    List<String> canonicalFieldList = Arrays.asList(fieldLines.get(0).split(";"));

    List<String> profiles = Files.readAllLines(
      Paths.get(profileFile), Charset.defaultCharset()
    );

    ProfileReader profileReader = new ProfileReader(canonicalFieldList, profiles);
    if (produceList) {
      List<String> binaryPatterns = profileReader.createBinaryPatternList();
      for (String binaryPattern : binaryPatterns) {
        System.out.println(binaryPattern);
      }
    } else {
      Map<List<RecordPattern>, Double> sortedClusters = profileReader.buildCluster();

      int idx = 0;
      sortedClusters.
        entrySet().
        stream().
        forEach((cluster) -> {
          int i = profileReader.getNext();
          // int sum = profileReader.count(cluster.getKey());
          // System.out.printf("#%d=%d\n", i, sum);
          cluster.
            getKey().
            forEach((row) -> {
              System.out.printf("%d,%s\n", i, row.asCsv());
            });
        });
    }
  }
}
