package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;

public class MaxCountChecker extends SingleFieldChecker {

  public static final String PREFIX = "maxCount";
  protected Integer maxCount;

  public MaxCountChecker(JsonBranch field, int maxCount) {
    this(field, field.getLabel(), maxCount);
  }

  public MaxCountChecker(JsonBranch field, String header, int maxCount) {
    super(field, PREFIX + ":" + header);
    this.maxCount = maxCount;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = (List<XmlFieldInstance>) cache.get(field.getJsonPath());
    int count = 0;
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          count++;
          isNA = false;
        }
      }
    }
    if (count <= maxCount)
      allPassed = true;
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
