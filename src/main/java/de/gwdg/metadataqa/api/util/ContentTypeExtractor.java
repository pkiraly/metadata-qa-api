package de.gwdg.metadataqa.api.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class ContentTypeExtractor {
  private static final Logger LOGGER = Logger.getLogger(ContentTypeExtractor.class.getCanonicalName());
  public static final int DEFAULT_TIMEOUT = 5000;
  private static final List<String> HEADER_KEYS = List.of("Content-Type", "content-type");

  private int timeout;
  private boolean debug = false;

  public ContentTypeExtractor() {
    this.timeout = DEFAULT_TIMEOUT;
  }

  public ContentTypeExtractor(int timeout) {
    this.timeout = timeout;
  }

  public String getContentType(String url) throws IOException {
    String contentType = null;
    URL urlObj = new URL(url);
    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

    urlConnection.setConnectTimeout(timeout);
    urlConnection.setReadTimeout(timeout);
    urlConnection.connect();
    int responseCode = urlConnection.getResponseCode();
    if (responseCode == 200) {
      // String rawContentType = extractContentType.getHeaderField("Content-Type");
      String rawContentType = extractContentType(urlConnection);
      if (rawContentType != null && StringUtils.isNotBlank(rawContentType))
        contentType = rawContentType.replaceAll("; ?charset.*$", "");
    } else if (responseCode == 301 // Moved Permanently
            || responseCode == 302 // Found
            || responseCode == 303 // See Other
            || responseCode == 304 // Not Modified
            || responseCode == 307 // Temporary Redirect
            || responseCode == 308 // Permanent Redirect
    ) {
      String location = urlConnection.getHeaderField("Location");
      return getContentType(location);
    } else {
      LOGGER.warning(String.format("URL %s returns unhandled status code: %d.\n", url, responseCode));
    }
    return contentType;
  }

  private String extractContentType(HttpURLConnection urlConnection) {
    String contentType = null;
    for (String key : HEADER_KEYS) {
      contentType = urlConnection.getHeaderField(key);
      if (StringUtils.isNotBlank(contentType))
        break;
    }
    return contentType;
  }

  public void setDebug() {
    this.debug = true;
  }
}
