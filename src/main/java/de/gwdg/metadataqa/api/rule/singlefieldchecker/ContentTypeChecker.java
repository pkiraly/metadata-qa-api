package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
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

  public ContentTypeChecker(JsonBranch field, List<String> contentType) {
    this(field, field.getLabel(), contentType);
  }

  public ContentTypeChecker(JsonBranch field, String header, List<String> fixedValues) {
    super(field, header + ":" + PREFIX);
    this.fixedValues = fixedValues;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          try {
            String contentType = ContentTypeExtractor.getContentType(instance.getValue());
            if (isDebug())
              LOGGER.info(String.format("value: '%s' -> '%s'", instance.getValue(), contentType));
            if (contentType == null || !fixedValues.contains(contentType)) {
              allPassed = false;
            }
          } catch (IOException e) {
            allPassed = false;
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
