package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.DependencyChecker;

import java.util.List;

public class NotChecker extends LogicalChecker {

  private static final long serialVersionUID = -1304107444331551638L;
  public static final String PREFIX = "not";

  /**
   * @param field The field
   * @param checkers The list of checkers
   */
  public NotChecker(JsonBranch field, List<RuleChecker> checkers) {
    this(field, field.getLabel(), checkers);
  }

  public NotChecker(JsonBranch field, String header, List<RuleChecker> checkers) {
    super(field,header + ":" + PREFIX + ":" + getChildrenHeader(checkers));
    this.checkers = checkers;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    var allPassed = true;
    var isNA = false;
    FieldCounter<RuleCheckerOutput> localResults = new FieldCounter<>();
    for (RuleChecker checker : checkers) {
      if (checker instanceof DependencyChecker)
        ((DependencyChecker)checker).update(cache, localResults, outputType, results);
      else
        checker.update(cache, localResults, outputType);
      String key = outputType.equals(RuleCheckingOutputType.BOTH) ? checker.getHeader(RuleCheckingOutputType.SCORE) : checker.getHeader();
      if (localResults.get(key).getStatus().equals(RuleCheckingOutputStatus.PASSED)) {
        allPassed = false;
        break;
      }
    }

    addOutput(results, isNA, allPassed, outputType);
  }
}
