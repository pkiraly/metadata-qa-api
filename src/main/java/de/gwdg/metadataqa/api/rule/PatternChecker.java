package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;
import java.util.regex.Pattern;

public class PatternChecker extends SingleFieldChecker {

  protected Pattern pattern;

  public PatternChecker(JsonBranch field, String pattern) {
    this(field, field.getLabel(), pattern);
  }

  public PatternChecker(JsonBranch field, String header, String pattern) {
    super(field, "pattern:" + header);
    this.pattern = Pattern.compile(pattern);
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
          if (!pattern.matcher(instance.getValue()).matches()) {
            allPassed = false;
            break;
          }
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

}
