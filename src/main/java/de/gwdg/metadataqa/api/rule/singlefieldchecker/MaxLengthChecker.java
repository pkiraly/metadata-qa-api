package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;

public class MaxLengthChecker extends SingleFieldChecker {

  public static final String PREFIX = "maxLength";
  protected Integer maxLength;

  public MaxLengthChecker(JsonBranch field, Integer maxLength) {
    this(field, field.getLabel(), maxLength);
  }

  public MaxLengthChecker(JsonBranch field, String header, Integer maxLength) {
    super(field, PREFIX + ":" + header);
    this.maxLength = maxLength;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = (List<XmlFieldInstance>) cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (instance.getValue().length() > maxLength) {
            allPassed = false;
            break;
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
