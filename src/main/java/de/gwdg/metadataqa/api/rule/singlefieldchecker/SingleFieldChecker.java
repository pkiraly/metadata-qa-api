package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;

public abstract class SingleFieldChecker extends BaseRuleChecker {

  protected JsonBranch field;

  public SingleFieldChecker(JsonBranch field, String header) {
    this.field = field;
    this.header = header;
  }
}
