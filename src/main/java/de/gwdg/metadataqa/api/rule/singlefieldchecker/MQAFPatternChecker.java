package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.configuration.schema.ApplicationScope;
import de.gwdg.metadataqa.api.configuration.schema.MQAFPattern;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;

public class MQAFPatternChecker extends SingleFieldChecker {

  private static final long serialVersionUID = -1432138574479246596L;
  public static final String PREFIX = "pattern";
  protected MQAFPattern mqafPattern;

  public MQAFPatternChecker(DataElement field, MQAFPattern mqafPattern) {
    this(field, field.getLabel(), mqafPattern);
  }

  public MQAFPatternChecker(DataElement field, String header, MQAFPattern mqafPattern) {
    super(field, header + ":" + PREFIX);
    this.mqafPattern = mqafPattern;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      boolean onePassed = false;
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
          if (!mqafPattern.getCompiledPattern().matcher(instance.getValue()).find()) {
            if (mqafPattern.getScope().equals(ApplicationScope.allOf)) {
              allPassed = false;
              break;
            }
          } else {
            // TODO: implement ApplicationScope.oneOf
            onePassed = true;
          }
        }
      }
      if (!onePassed)
        allPassed = false;
    }
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }

}
