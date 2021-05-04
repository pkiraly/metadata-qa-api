package de.gwdg.metadataqa.api.similarity;

import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Clustering functionality.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Clustering {

  private JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();
  private Map<String, Cluster> clusterIndex = new HashMap<>();
  private double treshold;

  public Clustering(List<String> patterns, double treshold) {
    this.treshold = treshold;
    initializeClusters(patterns);
    makeClusters();
  }

  public Map<String, Cluster> getClusterIndex() {
    return clusterIndex;
  }

  public List<List<String>> getClusters() {
    List<List<String>> clusters = new ArrayList<>();
    for (Cluster cluster : clusterIndex.values()) {
      clusters.add(cluster.getTermList());
    }
    return clusters;
  }

  private void initializeClusters(List<String> patterns) {
    for (var i = 0; i < patterns.size(); i++) {
      String pattern = patterns.get(i);
      Term term = getOrCreateTerm(pattern);
      for (int j = i + 1; j < patterns.size(); j++) {
        String otherPattern = patterns.get(j);
        Term otherTerm = getOrCreateTerm(otherPattern);
        double distance = jaroWinkler.apply(pattern, otherPattern);
        if (distance >= treshold) {
          term.setDistance(otherTerm, distance);
          otherTerm.setDistance(term, distance);
        }
      }
    }
  }

  private void makeClusters() {
    List<Cluster> clusts = new ArrayList<>(clusterIndex.values());
    for (var i = 0; i < clusts.size(); i++) {
      Cluster a = clusts.get(i);
      if (a.isActive()) {
        for (int j = i + 1; j < clusts.size(); j++) {
          Cluster b = clusts.get(j);
          if (b.isActive() && a.isSimilarTo(b, treshold)) {
            a.merge(b);
            b.setActive(false);
          }
        }
      }
    }
    removePassiveClusters();
  }

  private void removePassiveClusters() {
    List<String> removableIds = new ArrayList<>();
    for (Map.Entry<String, Cluster> cluster : clusterIndex.entrySet()) {
      if (!cluster.getValue().isActive()) {
        removableIds.add(cluster.getKey());
      }
    }
    if (!removableIds.isEmpty()) {
      for (String id : removableIds) {
        clusterIndex.remove(id);
      }
    }
  }

  private Term getOrCreateTerm(String pattern) {
    Term term = null;
    if (clusterIndex.containsKey(pattern)) {
      term = clusterIndex.get(pattern).getTerms().get(0);
    } else {
      term = new Term(pattern);
      clusterIndex.put(pattern, new Cluster(term));
    }
    return term;
  }
}
