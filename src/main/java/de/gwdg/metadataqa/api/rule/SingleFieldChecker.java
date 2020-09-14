package de.gwdg.metadataqa.api.rule;

import de.gwdg.metadataqa.api.json.JsonBranch;

import java.util.regex.Pattern;

public abstract class SingleFieldChecker implements RuleChecker {

  protected JsonBranch field;
  protected String header;

  public SingleFieldChecker(JsonBranch field, String header) {
    this.field = field;
    this.header = "pattern:" + header;
  }

  @Override
  public String getHeader() {
    return header;
  }

}
