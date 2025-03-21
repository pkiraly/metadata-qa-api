package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class EnumerationChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 5185953247558241405L;
  public static final String PREFIX = "in";
  protected List<String> fixedValues;

  public EnumerationChecker(DataElement field, List<String> fixedValues) {
    this(field, field.getLabel(), fixedValues);
  }

  public EnumerationChecker(DataElement field, String header, List<String> fixedValues) {
    super(field, header + ":" + PREFIX);
    this.fixedValues = fixedValues;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
          if (!fixedValues.contains(instance.getValue())) {
            allPassed = false;
            break;
          }
        }
      }
    }
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }
}
