package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;

public class MinCountChecker extends SingleFieldChecker {

  public static final String prefix = "minCount";
  protected Integer minCount;

  public MinCountChecker(JsonBranch field, Integer minCount) {
    this(field, field.getLabel(), minCount);
  }

  public MinCountChecker(JsonBranch field, String header, Integer minCount) {
    super(field, prefix + ":" + header);
    this.minCount = minCount;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    boolean allPassed = true;
    boolean isNA = true;
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
    if (count >= minCount)
      allPassed = true;
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
