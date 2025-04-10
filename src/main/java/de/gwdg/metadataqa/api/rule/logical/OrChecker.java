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
import de.gwdg.metadataqa.api.rule.singlefieldchecker.MinCountChecker;

import java.util.List;
import java.util.logging.Logger;

public class OrChecker extends LogicalChecker {

  private static final Logger LOGGER = Logger.getLogger(OrChecker.class.getCanonicalName());

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "or";

  /**
   * @param field The field
   * @param checkers The list of checkers
   */
  public OrChecker(DataElement field, List<RuleChecker> checkers) {
    this(field, field.getLabel(), checkers);
  }

  public OrChecker(DataElement field, String header, List<RuleChecker> checkers) {
    super(field, header + ":" + PREFIX + ":" + getChildrenHeader(checkers));
    this.checkers = checkers;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    // if NA and dependency checker FAILED -> FAILED
    var allPassed = false;
    var isNA = false;
    RuleCheckerOutput output = null;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      FieldCounter<RuleCheckerOutput> localResults = new FieldCounter<>();
      for (RuleChecker checker : checkers) {
        if (checker instanceof DependencyChecker) {
          ((DependencyChecker) checker).update(cache, localResults, outputType, results);
        } else {
          checker.update(cache, localResults, outputType);
        }
        String key = outputType.equals(RuleCheckingOutputType.BOTH)
                   ? checker.getIdOrHeader(RuleCheckingOutputType.SCORE)
                   : checker.getIdOrHeader();
        if (localResults.get(key).getStatus().equals(RuleCheckingOutputStatus.PASSED)) {
          allPassed = true;
          break;
        }
      }
    } else {
      isNA = true;
      for (RuleChecker checker : checkers) {
        if (checker instanceof MinCountChecker) {
          MinCountChecker minCountChecker = (MinCountChecker) checker;
          if (!minCountChecker.isEmptyInstancesAllowed() || minCountChecker.getMinCount() > 0)
            allPassed = false;
        }
        else if (alwaysCheckDependencies && checker instanceof DependencyChecker) {
          DependencyChecker dependencyChecker = (DependencyChecker) checker;
          boolean dependenciesPassed = dependencyChecker.getResult(outputType, results);
          if (dependenciesPassed == false)
            output = new RuleCheckerOutput(this, RuleCheckingOutputStatus.FAILED);
          else
            output = new RuleCheckerOutput(this, RuleCheckingOutputStatus.PASSED);
        }

        if (!allPassed)
          break;
      }
    }
    if (output != null) {
      addOutput(results, output, outputType);
    } else {
      addOutput(results, isNA, allPassed, outputType);
    }
    if (isDebug()) {
      RuleCheckingOutputStatus status = output != null
        ? output.getStatus()
        : RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory());
      LOGGER.info(String.format("%s %s) isNA: %s, allPassed: %s, result: %s", this.getClass().getSimpleName(), this.id, isNA, allPassed, status));
    }
  }
}
