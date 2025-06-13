package de.gwdg.metadataqa.api.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class LinkValidator {
  private static final Logger LOGGER = Logger.getLogger(LinkValidator.class.getCanonicalName());
  public static final int DEFAULT_TIMEOUT = 5000;
  private int timeout;

  public LinkValidator() {
    this(DEFAULT_TIMEOUT);
  }

  public LinkValidator(int timeout) {
    this.timeout = timeout;
  }

  public boolean isValid(String url) throws IOException {
    URL urlObj = new URL(url);
    HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

    urlConnection.setConnectTimeout(timeout);
    urlConnection.setReadTimeout(timeout);
    urlConnection.connect();
    int responseCode = urlConnection.getResponseCode();
    if (responseCode == 200) {
      return true;
    } else if (responseCode == 301 || responseCode == 302 || responseCode == 303) {
      String location = urlConnection.getHeaderField("Location");
      return isValid(location);
    } else {
      LOGGER.warning(String.format("URL %s returns unhandled status code: %d.\n", url, responseCode));
    }
    return false;
  }

}
