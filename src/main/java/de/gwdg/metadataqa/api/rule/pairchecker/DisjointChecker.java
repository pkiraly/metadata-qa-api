package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class DisjointChecker extends PropertyPairChecker {

  private static final long serialVersionUID = -2921501305139849002L;
  public static final String PREFIX = "disjoint";

  public DisjointChecker(JsonBranch field1, JsonBranch field2) {
    super(field1, field2, PREFIX);
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = false;
    List<XmlFieldInstance> instances1 = cache.get(field1.getAbsoluteJsonPath().replace("[*]", ""));
    List<XmlFieldInstance> instances2 = cache.get(field2.getAbsoluteJsonPath().replace("[*]", ""));
    if (instances1 != null && !instances1.isEmpty() && instances2 != null && !instances2.isEmpty()) {
      for (XmlFieldInstance instance1 : instances1) {
        if (instance1.hasValue()) {
          for (XmlFieldInstance instance2 : instances2) {
            if (instance2.hasValue() && instance1.getValue().equals(instance2.getValue())) {
              allPassed = false;
              break;
            }
          }
        }
      }
    }
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info("result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }
}
