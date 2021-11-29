package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          try {
            URL url = new URL(instance.getValue());
            URLConnection u = url.openConnection();
            String contentType = u.getHeaderField("Content-Type").replaceAll("; ?charset.*$", "");
            if (contentType == null || StringUtils.isBlank(contentType)) {
              LOGGER.warning(String.format("undetectable content type", contentType));
              allPassed = false;
              break;
            }
            if (!fixedValues.contains(contentType)) {
              LOGGER.warning(String.format("content type '%s' did not match expectation (rule id: %s)", contentType, getId()));
              allPassed = false;
              break;
            }
          } catch (MalformedURLException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    results.put(getHeader(), new RuleCheckerOutput(this, isNA, allPassed).setOutputType(outputType));
  }
}
