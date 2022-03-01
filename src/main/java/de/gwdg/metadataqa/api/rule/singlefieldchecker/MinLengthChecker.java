package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class MinLengthChecker extends SingleFieldChecker {

  private static final long serialVersionUID = -1842497411816592850L;
  public static final String PREFIX = "minLength";
  protected Integer minLength;

  public MinLengthChecker(JsonBranch field, Integer minLength) {
    this(field, field.getLabel(), minLength);
  }

  public MinLengthChecker(JsonBranch field, String header, Integer minLength) {
    super(field, header + ":" + PREFIX);
    this.minLength = minLength;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
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
    addOutput(results, isNA, allPassed, outputType);
  }

}
