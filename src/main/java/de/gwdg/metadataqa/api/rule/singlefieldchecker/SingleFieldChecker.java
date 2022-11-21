package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;

public abstract class SingleFieldChecker extends BaseRuleChecker {

  protected DataElement field;

  public SingleFieldChecker(DataElement field, String header) {
    this.field = field;
    this.header = header;
  }
}
