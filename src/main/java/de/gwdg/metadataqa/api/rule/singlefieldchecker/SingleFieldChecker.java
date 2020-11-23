package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.RuleChecker;

public abstract class SingleFieldChecker implements RuleChecker {

  protected JsonBranch field;
  protected String header;

  public SingleFieldChecker(JsonBranch field, String header) {
    this.field = field;
    this.header = header;
  }

  @Override
  public String getHeader() {
    return header;
  }

}
