package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;

public class EqualityChecker extends SingleFieldChecker {

  public static final String prefix = "equals";
  protected String fixedValue;

  public EqualityChecker(JsonBranch field, String fixedValue) {
    this(field, field.getLabel(), fixedValue);
  }

  public EqualityChecker(JsonBranch field, String header, String fixedValue) {
    super(field, prefix + ":" + header);
    this.fixedValue = fixedValue;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    double result = 0.0;
    boolean allPassed = true;
    boolean isNA = true;
    List<XmlFieldInstance> instances = (List<XmlFieldInstance>) cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (!fixedValue.equals(instance.getValue())) {
            allPassed = false;
            break;
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
