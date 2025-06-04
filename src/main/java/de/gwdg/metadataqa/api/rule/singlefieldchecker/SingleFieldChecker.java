package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.XmlSelector;
import de.gwdg.metadataqa.api.rule.BaseRuleChecker;

import java.util.List;

public abstract class SingleFieldChecker extends BaseRuleChecker {

  protected DataElement field;

  public SingleFieldChecker(DataElement field, String header) {
    this.field = field;
    this.header = header;
  }

  protected List<XmlFieldInstance> selectInstances(Selector selector) {
    if (hasValuePath() && selector instanceof XmlSelector) {
      String path = field.getPath() + "/" + getValuePath();
      return selector.get(path);
    } else {
      return selector.get(field);
    }
  }
}
