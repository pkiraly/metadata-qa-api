package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.rule.singlefieldchecker.DependencyChecker;

import java.util.List;

public class AndChecker extends LogicalChecker {

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "and";

  /**
   * @param field The field
   * @param checkers The list of checkers
   */
  public AndChecker(DataElement field, List<RuleChecker> checkers) {
    this(field, field.getLabel(), checkers);
  }

  public AndChecker(DataElement field, String header, List<RuleChecker> checkers) {
    super(field,header + ":" + PREFIX + ":" + getChildrenHeader(checkers));
    this.checkers = checkers;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = false;
    List<XmlFieldInstance> instances = cache.get(field.getPath());
    if (instances != null && !instances.isEmpty()) {
      FieldCounter<RuleCheckerOutput> localResults = new FieldCounter<>();
      for (RuleChecker checker : checkers) {
        if (checker instanceof DependencyChecker)
          ((DependencyChecker)checker).update(cache, localResults, outputType, results);
        else
          checker.update(cache, localResults, outputType);
        String key = outputType.equals(RuleCheckingOutputType.BOTH) ? checker.getHeader(RuleCheckingOutputType.SCORE) : checker.getHeader();
        if (!localResults.get(key).getStatus().equals(RuleCheckingOutputStatus.PASSED)) {
          allPassed = false;
          break;
        }
      }
    } else {
      isNA = true;
    }
    addOutput(results, isNA, allPassed, outputType);

    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }
}
