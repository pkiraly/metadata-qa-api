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
import java.util.regex.Pattern;

public class ContentTypeChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 6376677768166208863L;
  public static final String PREFIX = "contentType";

  private static final Logger LOGGER = Logger.getLogger(ContentTypeChecker.class.getCanonicalName());

  protected List<String> expectedContentTypes;
  private ContentTypeExtractor contentTypeExtractor;
  private Pattern skippableUrl;

  public ContentTypeChecker(DataElement field, List<String> expectedContentTypes) {
    this(field, field.getLabel(), expectedContentTypes, ContentTypeExtractor.DEFAULT_TIMEOUT);
  }

  public ContentTypeChecker(DataElement field, String header, List<String> expectedContentTypes, int timeout) {
    super(field, header + ":" + PREFIX);
    this.expectedContentTypes = expectedContentTypes;
    contentTypeExtractor = new ContentTypeExtractor(timeout);
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug()) {
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ", debug=" + isDebug());
      // contentTypeExtractor.setDebug();
    }

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
          String url = instance.getValue();
          if (skippableUrl == null || !skippableUrl.matcher(url).find()) {
            try {
              String contentType = contentTypeExtractor.getContentType(url);
              if (isDebug())
                LOGGER.info(String.format("value: '%s' -> '%s'", url, contentType));
              if (contentType == null || !expectedContentTypes.contains(contentType)) {
                allPassed = false;
                if (countInstances())
                  failureCount++;
              }
            } catch (IOException e) {
              LOGGER.warning(String.format("%s: %s (url: %s)", e.getClass().getSimpleName(), e.getMessage(), url));
              allPassed = false;
              if (countInstances())
                failureCount++;
            }
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

  public void setSkippableUrl(String skippableUrl) {
    this.skippableUrl = Pattern.compile(skippableUrl);
  }

  @Override
  public void setDebug() {
    super.setDebug();
    contentTypeExtractor.setDebug();
  }
}
