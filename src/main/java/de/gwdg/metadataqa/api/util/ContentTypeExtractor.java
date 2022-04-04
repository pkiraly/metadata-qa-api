package de.gwdg.metadataqa.api.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class ContentTypeExtractor {
  private static final Logger LOGGER = Logger.getLogger(ContentTypeExtractor.class.getCanonicalName());
  private static int timeout = 1000;

  public static String getContentType(String url) throws IOException {
    String contentType = null;
    URL urlObj = new URL(url);
    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

    int timeout = 1000;
    urlConnection.setConnectTimeout(timeout);
    urlConnection.setReadTimeout(timeout);
    urlConnection.connect();
    int responseCode = urlConnection.getResponseCode();
    if (responseCode == 200) {
      String rawContentType = urlConnection.getHeaderField("Content-Type");
      if (rawContentType != null && StringUtils.isNotBlank(rawContentType))
        contentType = rawContentType.replaceAll("; ?charset.*$", "");
    } else {
      LOGGER.warning(String.format("Status code: %d.\n", responseCode));
    }
    return contentType;
  }

}
