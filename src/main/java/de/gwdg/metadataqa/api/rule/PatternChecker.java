package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;
import java.util.regex.Pattern;

public class PatternChecker implements RuleChecker {

  private JsonBranch field;
  private Pattern pattern;
  private String header;

  public PatternChecker(JsonBranch field, String pattern, String header) {
    this.field = field;
    this.pattern = Pattern.compile(pattern);
    this.header = "pattern:" + header;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    double result = 0.0;
    boolean allPassed = true;
    boolean isNA = true;
    for (XmlFieldInstance instance : (List<XmlFieldInstance>) cache.get(field.getJsonPath())) {
      if (instance.hasValue()) {
        isNA = false;
        if (!pattern.matcher(instance.getValue()).matches()) {
          allPassed = false;
          break;
        }
      }
    }
    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

  @Override
  public String getHeader() {
    return header;
  }

}
