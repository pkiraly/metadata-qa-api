package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.ContentTypeExtractor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ContentTypeChecker extends SingleFieldChecker {

  private static final Logger LOGGER = Logger.getLogger(ContentTypeChecker.class.getCanonicalName());

  public static final String PREFIX = "contentType";
  protected List<String> fixedValues;

  public ContentTypeChecker(DataElement field, List<String> contentType) {
    this(field, field.getLabel(), contentType);
  }

  public ContentTypeChecker(DataElement field, String header, List<String> fixedValues) {
    super(field, header + ":" + PREFIX);
    this.fixedValues = fixedValues;
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
            String contentType = ContentTypeExtractor.getContentType(instance.getValue());
            if (isDebug())
              LOGGER.info(String.format("value: '%s' -> '%s'", instance.getValue(), contentType));
            if (contentType == null || !fixedValues.contains(contentType)) {
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
