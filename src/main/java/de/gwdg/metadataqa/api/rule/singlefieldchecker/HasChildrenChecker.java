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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

public class HasChildrenChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 1114999259831619599L;
  public static final String PREFIX = "hasValue";
  protected List<String> children;

  private static final Logger LOGGER = Logger.getLogger(HasChildrenChecker.class.getCanonicalName());

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
  public void update(Selector selector,
                     FieldCounter<RuleCheckerOutput> results,
                     RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(String.format("%s %s, field: %s, path(s): %s, children: %s, scope: %s", this.getClass().getSimpleName(), this.id, field.getLabel(), field.getPath(), children, getScope()));

    var isNA = true;
    int passCount = 0;
    boolean hasFailed = false;
    if (selector.getClass().equals(XmlSelector.class)) {
      List<Node> nodes = (List<Node>) selector.getFragment(field.getPath());
      if (!nodes.isEmpty()) {
        isNA = false;
        int i = 0;
        for (Node node : nodes) {
          boolean hasAllChildren = true;
          for (String childPath : children) {
            List values = selector.get(childPath + Math.random(), childPath, node);
            if (values.isEmpty()) {
              hasFailed = true;
              hasAllChildren = false;
              if (scopeIsAllOf())
                break;
            }
          } // children
          if (hasAllChildren)
            passCount++;
          else  if (scopeIsAllOf())
            break;
        } // nodes
      }
    }
    boolean allPassed = isPassed(passCount, hasFailed);
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }

}
