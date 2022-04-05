package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DependencyChecker extends SingleFieldChecker {

  private static final Logger LOGGER = Logger.getLogger(DependencyChecker.class.getCanonicalName());

  public static final String PREFIX = "dependency";
  protected List<String> dependencies;

  public DependencyChecker(JsonBranch field, List<String> dependencies) {
    this(field, field.getLabel(), dependencies);
  }

  public DependencyChecker(JsonBranch field, String header, List<String> dependencies) {
    super(field, header + ":" + PREFIX);
    this.dependencies = dependencies;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    update(cache, results, outputType, null);
  }

  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> localResults, RuleCheckingOutputType outputType,
                     FieldCounter<RuleCheckerOutput> globalResults) {
    if (globalResults == null)
      globalResults = localResults;

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          for (String id : dependencies) {
            boolean found = false;
            for (Map.Entry<String, RuleCheckerOutput> entry : globalResults.getMap().entrySet()) {
              if (entry.getKey().endsWith(id)) {
                found = true;
                if (entry.getValue().getStatus().equals(RuleCheckingOutputStatus.FAILED)) {
                  allPassed = false;
                  break;
                }
              }
            }
            if (!found) {
              allPassed = false;
              break;
            }
          }
        }
      }
    }

    addOutput(localResults, isNA, allPassed, outputType);
  }
}
