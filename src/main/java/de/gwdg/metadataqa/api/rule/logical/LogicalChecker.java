package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicalChecker extends BaseRuleChecker {

  protected JsonBranch field;
  protected List<RuleChecker> checkers;

  public LogicalChecker(JsonBranch field, String header) {
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
}
