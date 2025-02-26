package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.XmlSelector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import org.w3c.dom.Node;

import java.util.List;

public class HasChildrenChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "hasValue";
  protected List<String> children;

  /**
   * @param field The field
   * @param children The list of children (XPath)
   */
  public HasChildrenChecker(DataElement field, List<String> children) {
    this(field, field.getLabel(), children);
  }

  public HasChildrenChecker(DataElement field, String header, List<String> children) {
    super(field, header + ":" + PREFIX);
    this.children = children;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + " field: " + field);

    var allPassed = false;
    var isNA = true;
    if (cache.getClass().equals(XmlSelector.class)) {
      Object fragment = cache.getFragment(field.getPath());
      List<Node> nodes = (List<Node>) cache.getFragment(field.getPath());
      if (!nodes.isEmpty()) {
        isNA = false;
        for (Node node : nodes) {
          for (String childPath : children) {
            List values = cache.get(childPath, childPath, node);
            if (values.isEmpty()) {
              allPassed = false;
              break;
            } else {
              allPassed = true;
            }
          }
          if (!allPassed)
            break;
        }
      }
    }
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }

}
