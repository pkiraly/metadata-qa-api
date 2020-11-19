package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;

public class DisjointChecker extends PropertyPairChecker {

  public static final String prefix = "disjoint";

  public DisjointChecker(JsonBranch field1, JsonBranch field2) {
    this(field1, field2, field1.getLabel());
  }

  public DisjointChecker(JsonBranch field1, JsonBranch field2, String header) {
    super(field1, field2, prefix + ":" + header);
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    double result = 0.0;
    boolean allPassed = true;
    boolean isNA = true;
    List<XmlFieldInstance> instances1 = (List<XmlFieldInstance>) cache.get(field1.getJsonPath());
    List<XmlFieldInstance> instances2 = (List<XmlFieldInstance>) cache.get(field2.getJsonPath());
    if (instances1 != null && !instances1.isEmpty()) {
      for (XmlFieldInstance instance1 : instances1) {
        if (instance1.hasValue()) {
          isNA = false;
          for (XmlFieldInstance instance2 : instances2) {
            if (instance1.getValue().equals(instance2.getValue())) {
              allPassed = false;
              break;
            }
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }
}
