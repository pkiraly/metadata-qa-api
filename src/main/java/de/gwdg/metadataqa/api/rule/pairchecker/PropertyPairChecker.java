package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;

public abstract class PropertyPairChecker extends BaseRuleChecker {

  private static final long serialVersionUID = -6579708841667005135L;
  protected JsonBranch field1;
  protected JsonBranch field2;
  protected String header;

  public PropertyPairChecker(JsonBranch field1, JsonBranch field2, String header) {
    if (field1 == null)
      throw new IllegalArgumentException("field1 should not be null");
    if (field2 == null)
      throw new IllegalArgumentException("field2 should not be null");

    this.field1 = field1;
    this.field2 = field2;
    this.header = header;
  }

  @Override
  public String getHeader() {
    return header;
  }
}
