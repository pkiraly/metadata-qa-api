package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PatternChecker extends SingleFieldChecker {

  private static final long serialVersionUID = -1432138574479246596L;
  public static final String PREFIX = "pattern";

  private static final Logger LOGGER = Logger.getLogger(PatternChecker.class.getCanonicalName());

  protected Pattern pattern;

  public PatternChecker(DataElement field, String pattern) {
    this(field, field.getLabel(), pattern);
  }

  public PatternChecker(DataElement field, String header, String pattern) {
    super(field, header + ":" + PREFIX);
    this.pattern = Pattern.compile(pattern);
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    if (this.hasValuePath())
      LOGGER.info("valuePath: " + this.getValuePath());
    var isNA = true;
    boolean hasFailed = false;
    int passCount = 0;

    List<XmlFieldInstance> instances = selectInstances(selector); // selector.get(field)
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        isNA = false;
        if (instance.hasValue()) {
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
          if (!pattern.matcher(instance.getValue()).find()) {
            hasFailed = true;
            if (scopeIsAllOf()) {
              break;
            }
          } else {
            passCount++;
          }
        }
      }
    }
    boolean allPassed = isPassed(passCount, hasFailed);
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }

}
