package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;

public class DisjointChecker extends PropertyPairChecker {

  public static final String PREFIX = "disjoint";

  public DisjointChecker(JsonBranch field1, JsonBranch field2) {
    this(field1, field2, field1.getLabel() + "-" + field2.getLabel());
  }

  public DisjointChecker(JsonBranch field1, JsonBranch field2, String header) {
    super(field1, field2, PREFIX + ":" + header);
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    var allPassed = true;
    var isNA = false;
    List<XmlFieldInstance> instances1 = cache.get(field1.getAbsoluteJsonPath().replace("[*]", ""));
    List<XmlFieldInstance> instances2 = cache.get(field2.getAbsoluteJsonPath().replace("[*]", ""));
    if (instances1 != null && !instances1.isEmpty() && instances2 != null && !instances2.isEmpty()) {
      for (XmlFieldInstance instance1 : instances1) {
        if (instance1.hasValue()) {
          for (XmlFieldInstance instance2 : instances2) {
            if (instance2.hasValue() && instance1.getValue().equals(instance2.getValue())) {
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
