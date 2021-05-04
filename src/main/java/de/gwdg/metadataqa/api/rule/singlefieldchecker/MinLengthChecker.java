package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;

public class MinLengthChecker extends SingleFieldChecker {

  public static final String PREFIX = "minLength";
  protected Integer minLength;

  public MinLengthChecker(JsonBranch field, Integer minLength) {
    this(field, field.getLabel(), minLength);
  }

  public MinLengthChecker(JsonBranch field, String header, Integer minLength) {
    super(field, PREFIX + ":" + header);
    this.minLength = minLength;
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
          if (instance.getValue().length() < minLength) {
            allPassed = false;
            break;
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
