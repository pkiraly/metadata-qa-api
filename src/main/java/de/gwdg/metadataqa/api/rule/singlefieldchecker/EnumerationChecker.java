package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class EnumerationChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 5185953247558241405L;
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
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results) {
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
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
    results.put(header, new RuleCheckerOutput(this, isNA, allPassed));
  }

}
