package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class EqualityChecker extends PropertyPairChecker {

  private static final long serialVersionUID = -5363342097255677979L;
  public static final String PREFIX = "equals";
  protected String fixedValue;

  public EqualityChecker(DataElement field1, DataElement field2) {
    super(field1, field2, PREFIX);
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    int matches = 0;
    List<XmlFieldInstance> instances1 = cache.get(field1.getAbsolutePath().replace("[*]", ""));
    List<XmlFieldInstance> instances2 = cache.get(field2.getAbsolutePath().replace("[*]", ""));
    if (instances1 != null && !instances1.isEmpty() && instances2 != null && !instances2.isEmpty()) {
      for (XmlFieldInstance instance1 : instances1) {
        isNA = false;
        if (instance1.hasValue()) {
          boolean hasValue2 = false;
          for (XmlFieldInstance instance2 : instances2) {
            if (isDebug())
              LOGGER.info(String.format("%s %s values: '%s' vs '%s'", this.getClass().getSimpleName(), this.id, instance1.getValue(), instance2.getValue()));
            if (instance2.hasValue()) {
              hasValue2 = true;
              if (!instance1.getValue().equals(instance2.getValue())) {
                allPassed = false;
                break;
              } else {
                matches++;
              }
            }
            if (!hasValue2)
              isNA = true;
          }
        }
      }
    }
    if (scopeIsAnyOf() && !isNA && matches > 0)
      allPassed = true;
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }

}
