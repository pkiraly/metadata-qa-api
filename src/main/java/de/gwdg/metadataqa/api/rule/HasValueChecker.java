package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;

public class HasValueChecker extends SingleFieldChecker {

  public static final String prefix = "hasValue";
  protected String fixedValue;

  /**
   * @param field The field
   * @param fixedValue The fixed value  check against
   */
  public HasValueChecker(JsonBranch field, String fixedValue) {
    this(field, field.getLabel(), fixedValue);
  }

  public HasValueChecker(JsonBranch field, String header, String fixedValue) {
    super(field, prefix + ":" + header);
    this.fixedValue = fixedValue;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    boolean allPassed = false;
    boolean isNA = true;
    List<XmlFieldInstance> instances = (List<XmlFieldInstance>) cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (instance.getValue().equals(fixedValue)) {
            allPassed = true;
            break;
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
