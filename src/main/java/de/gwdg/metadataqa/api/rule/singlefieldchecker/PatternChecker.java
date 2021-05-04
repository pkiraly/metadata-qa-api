package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;
import java.util.regex.Pattern;

public class PatternChecker extends SingleFieldChecker {

  public static final String PREFIX = "pattern";
  protected Pattern pattern;

  public PatternChecker(JsonBranch field, String pattern) {
    this(field, field.getLabel(), pattern);
  }

  public PatternChecker(JsonBranch field, String header, String pattern) {
    super(field, PREFIX + ":" + header);
    this.pattern = Pattern.compile(pattern);
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    var result = 0.0;
    var allPassed = true;
    var isNA = true;
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
