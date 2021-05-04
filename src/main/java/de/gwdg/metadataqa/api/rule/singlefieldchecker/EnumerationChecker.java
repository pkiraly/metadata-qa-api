package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;

public class EnumerationChecker extends SingleFieldChecker {

  public static final String PREFIX = "in";
  protected List<String> fixedValues;

  public EnumerationChecker(JsonBranch field, List<String> fixedValues) {
    this(field, field.getLabel(), fixedValues);
  }

  public EnumerationChecker(JsonBranch field, String header, List<String> fixedValues) {
    super(field, PREFIX + ":" + header);
    this.fixedValues = fixedValues;
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
          if (!fixedValues.contains(instance.getValue())) {
            allPassed = false;
            break;
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
