package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.lang3.StringUtils;

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
  private List<String> canonicalFieldList;
  private List<String> profiles;
  private Map<String, Row> rowIndex;
  private int i = 0;

  private static final List<String> MANDATORY_FIELDS = Arrays.asList(
    "dc:title", "dc:description", "dc:type", "dc:coverage",
    "dcterms:spatial", "dc:subject", "edm:rights",
    "Aggregation/edm:rights",
    "Aggregation/edm:provider", "Aggregation/edm:dataProvider",
    "Aggregation/edm:isShownAt", "Aggregation/edm:isShownBy"
  );

  private static final List<String> FUNCTIONAL_FIELDS = Arrays.asList(
    "dc:contributor", "dc:creator", "dc:date", "dc:format",
    "dc:identifier", "dc:language", "dc:publisher", "dc:relation",
    "dc:rights", "dc:source", "dcterms:alternative", "dcterms:created",
    "dcterms:extent", "dcterms:hasPart", "dcterms:isPartOf",
    "dcterms:issued", "dcterms:medium", "dcterms:provenance",
    "dcterms:temporal", "edm:isNextInSequence", "edm:type",
    "Aggregation/edm:hasView", "Aggregation/edm:object"
  );

  public ProfileReader(List<String> canonicalFieldList, List<String> profiles) {
    this.canonicalFieldList = canonicalFieldList;
    this.profiles = profiles;
    rowIndex = new HashMap<>();
  }

  public Map<List<Row>, Double> buildCluster() {
    return buildCluster(DEFAULT_TRESHOLD);
  }

  public Map<List<Row>, Double> buildCluster(double treshold) {
    List<String> patterns = createPatternList();

    Clustering clustering = new Clustering(patterns, treshold);
    List<List<String>> clusters = clustering.getClusters();

    Map<List<Row>, Double> sortableClusters = new HashMap<>();
    for (List<String> terms : clusters) {
      double sum = 0.0;
      Map<String, Row> sortableTerms = new HashMap<>();
      for (String term : terms) {
        Row row = rowIndex.get(term);
        sum += row.percent;
        sortableTerms.put(term, row);
      }

      List<Row> sortedTerms = sortTerms(sortableTerms);
      sortableClusters.put(sortedTerms, sum);
    }

    return sortClusters(sortableClusters);
  }

  private Map<List<Row>, Double> sortClusters(Map<List<Row>, Double> sortableClusters) {
    Map<List<Row>, Double> sortedClusters = sortableClusters.
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

  private List<Row> sortTerms(Map<String, Row> sortableTerms) {
    return sortableTerms.
          entrySet().
          stream().
          sorted(
            (e1, e2) -> e2.getValue().getPercent().compareTo(
              e1.getValue().getPercent()
            )
          ).
          map(e -> (Row) e.getValue()).
          collect(
            Collectors.
              toList()
          );
  }

  public int count(List<Row> rows) {
    int sum = 0;
    for (Row row : rows) {
      sum += row.getCount();
    }
    return sum;
  }

  public List<String> createPatternList() {
    List<String> patterns = new ArrayList<>();
    for (String line : profiles) {
      Row row = new Row(Arrays.asList(line.split(",")));
      patterns.add(row.getBinary());
      rowIndex.put(row.getBinary(), row);
    }
    return patterns;
  }

  private String fieldListToBinary(List<String> actual) {
    List<String> binary = new ArrayList<>();
    for (String field : canonicalFieldList) {
      String bit = actual.contains(field) ? "1" : "0";
      if (MANDATORY_FIELDS.contains(field)) {
        bit = bit + bit + bit;
      } else if (FUNCTIONAL_FIELDS.contains(field)) {
        bit = bit + bit;
      }
      binary.add(bit);
    }
    return StringUtils.join(binary, "");
  }

  /**
   * Value object representing a row in the file which represents a profile.
   */
  public class Row {
    private List<String> record;
    private String fields;
    private List<String> fieldList;
    private String binary;
    private Integer length;
    private Integer count;
    private Integer total;
    private Double percent;

    public Row(List<String> rec) {
      record = rec;
      fields = rec.get(0);
      length = Integer.parseInt(rec.get(1));
      count = Integer.parseInt(rec.get(2));
      total = Integer.parseInt(rec.get(3));
      percent = Double.parseDouble(rec.get(4));

      fieldList = Arrays.asList(fields.split(";"));
      binary = fieldListToBinary(fieldList);
    }

    public String asCsv() {
      return StringUtils.join(record, ",");
    }

    public String getFields() {
      return fields;
    }

    public String getBinary() {
      return binary;
    }

    public List<String> getFieldList() {
      return fieldList;
    }

    public Integer getLength() {
      return length;
    }

    public Integer getCount() {
      return count;
    }

    public Double getPercent() {
      return percent;
    }
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
      List<String> patterns = profileReader.createPatternList();
      for (String pattern : patterns) {
        System.out.println(pattern);
      }
    } else {
      Map<List<ProfileReader.Row>, Double> sortedClusters = profileReader.buildCluster();

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
