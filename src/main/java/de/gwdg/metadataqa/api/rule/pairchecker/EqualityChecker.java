package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;

public class EqualityChecker extends PropertyPairChecker {

  public static final String prefix = "equals";
  protected String fixedValue;

  public EqualityChecker(JsonBranch field1, JsonBranch field2) {
    this(field1, field2, field1.getLabel() + "-" + field2.getLabel());
  }

  public EqualityChecker(JsonBranch field1, JsonBranch field2, String header) {
    super(field1, field2, prefix + ":" + header);
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    double result = 0.0;
    boolean allPassed = true;
    boolean isNA = true;
    // List<XmlFieldInstance> instances = (List<XmlFieldInstance>) cache.get(field.getJsonPath());
    List<XmlFieldInstance> instances1 = (List<XmlFieldInstance>) cache.get(field1.getAbsoluteJsonPath().replace("[*]", ""));
    List<XmlFieldInstance> instances2 = (List<XmlFieldInstance>) cache.get(field2.getAbsoluteJsonPath().replace("[*]", ""));
    if (instances1 != null && !instances1.isEmpty() && instances2 != null && !instances2.isEmpty()) {
      for (XmlFieldInstance instance1 : instances1) {
        if (instance1.hasValue()) {
          isNA = false;
          for (XmlFieldInstance instance2 : instances2) {
            if (instance2.hasValue() && !instance1.getValue().equals(instance2.getValue())) {
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
