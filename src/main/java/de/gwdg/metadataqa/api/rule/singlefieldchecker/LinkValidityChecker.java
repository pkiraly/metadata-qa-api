package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.LinkValidator;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class LinkValidityChecker extends SingleFieldChecker {

  private static final Logger LOGGER = Logger.getLogger(LinkValidityChecker.class.getCanonicalName());

  public static final String PREFIX = "validLink";
  protected LinkValidator linkValidator;
  protected Boolean expectedValue;

  /**
   * @param field The data element to check
   * @param expectedValue Is the link expected to be valid?
   */
  public LinkValidityChecker(DataElement field, Boolean expectedValue) {
    this(field, field.getLabel(), expectedValue, LinkValidator.DEFAULT_TIMEOUT);
  }

  public LinkValidityChecker(DataElement field, String header, Boolean expectedValue, int timeout) {
    super(field, header + ":" + PREFIX);
    this.expectedValue = expectedValue;
    linkValidator = new LinkValidator(timeout);
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    int instanceCount = 0;
    int failureCount = 0;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          if (countInstances())
            instanceCount++;
          isNA = false;
          try {
            boolean isValid = linkValidator.isValid(instance.getValue());
            if (isDebug())
              LOGGER.info(String.format("value: '%s' -> '%s'", instance.getValue(), isValid));
            if (isValid != expectedValue) {
              allPassed = false;
              if (countInstances())
                failureCount++;
            }
          } catch (IOException e) {
            LOGGER.warning(String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()));
            allPassed = false;
            if (countInstances())
              failureCount++;
          }
          if (!countInstances() && !allPassed)
            break;
        }
      }
    }

    addOutput(results, isNA, allPassed, outputType, instanceCount, failureCount);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }
}
