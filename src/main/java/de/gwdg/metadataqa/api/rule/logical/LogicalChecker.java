package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicalChecker extends BaseRuleChecker {

  protected DataElement field;
  protected List<RuleChecker> checkers;

  /**
   * If the values is NA, check dependencies. and that will decide if it passes or not
   */
  protected boolean alwaysCheckDependencies = false;

  public LogicalChecker(DataElement field, String header) {
    this.field = field;
    this.header = header;
  }

  public List<RuleChecker> getCheckers() {
    return checkers;
  }

  protected static String getChildrenHeader(List<RuleChecker> checkers) {
    List<String> headers = new ArrayList<>();
    for (RuleChecker checker : checkers)
      headers.add(checker.getHeaderWithoutId());
    return StringUtils.join(headers, ":");
  }

  public void setAlwaysCheckDependencies(boolean alwaysCheckDependencies) {
    this.alwaysCheckDependencies = alwaysCheckDependencies;
  }
}
