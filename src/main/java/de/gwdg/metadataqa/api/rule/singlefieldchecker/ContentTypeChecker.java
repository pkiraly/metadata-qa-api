package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class ContentTypeChecker extends SingleFieldChecker {

  private static final Logger LOGGER = Logger.getLogger(ContentTypeChecker.class.getCanonicalName());

  private int timeout = 1000;
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
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(timeout); // 15 sec
            urlConnection.setReadTimeout(timeout);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 404) {
              LOGGER.warning(String.format("%s: is inaccessible", instance.getValue()));
              allPassed = false;
              break;
            }
            String contentType = urlConnection.getHeaderField("Content-Type");
            if (contentType == null || StringUtils.isBlank(contentType)) {
              LOGGER.warning(String.format("%s: undetectable content type '%s'. Header: %s", instance.getValue(), contentType, urlConnection.getHeaderFields()));
              allPassed = false;
              break;
            }
            contentType = contentType.replaceAll("; ?charset.*$", "");
            if (!fixedValues.contains(contentType)) {
              LOGGER.warning(String.format("%s: content type '%s' did not match expectation (rule id: %s)", instance.getValue(), contentType, getId()));
              allPassed = false;
              break;
            }
          } catch (SocketTimeoutException e) {
            LOGGER.warning(String.format("%s is not accessible (connection timeout)", instance.getValue()));
            // e.printStackTrace();
            allPassed = false;
            break;
          } catch (MalformedURLException e) {
            LOGGER.warning(String.format("%s is not accessible (Malformed URL)", instance.getValue()));
            allPassed = false;
            break;
            // e.printStackTrace();
          } catch (IOException e) {
            LOGGER.warning(String.format("%s is not accessible (%s)", instance.getValue(), e.getMessage()));
            allPassed = false;
            break;
            // e.printStackTrace();
          }
        }
      }
    }

    addOutput(results, isNA, allPassed, outputType);
  }
}
