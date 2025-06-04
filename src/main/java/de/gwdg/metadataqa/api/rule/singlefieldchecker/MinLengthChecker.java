package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class MinLengthChecker extends SingleFieldChecker {

  private static final long serialVersionUID = -1842497411816592850L;
  public static final String PREFIX = "minLength";
  protected Integer minLength;

  public MinLengthChecker(DataElement field, Integer minLength) {
    this(field, field.getLabel(), minLength);
  }

  public MinLengthChecker(DataElement field, String header, Integer minLength) {
    super(field, header + ":" + PREFIX);
    this.minLength = minLength;
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var isNA = true;
    boolean hasFailed = false;
    int passCount = 0;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        isNA = false;
        if (instance.hasValue()) {
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
          if (instance.getValue().length() < minLength) {
            hasFailed = true;
          } else {
            passCount++;
          }
        } else {
          hasFailed = true;
        }
        if (hasFailed && scopeIsAllOf())
          break;
      }
    }
    boolean allPassed = isPassed(passCount, hasFailed);
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }
}
