package de.gwdg.metadataqa.api.rule.logical;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;
import de.gwdg.metadataqa.api.rule.RuleChecker;

import java.util.List;

public abstract class LogicalChecker extends BaseRuleChecker {

  protected JsonBranch field;
  protected String header;
  protected List<RuleChecker> checkers;

  public LogicalChecker(JsonBranch field, String header) {
    this.field = field;
    this.header = header;
  }

  @Override
  public String getHeader() {
    return header;
  }

  public List<RuleChecker> getCheckers() {
    return checkers;
  }
}
