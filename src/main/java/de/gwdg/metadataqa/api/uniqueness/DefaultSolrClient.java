package de.gwdg.metadataqa.api.uniqueness;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Solr client.
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class DefaultSolrClient implements SolrClient, Serializable {

  private static final Logger LOGGER = Logger.getLogger(DefaultSolrClient.class.getCanonicalName());
  private static final String CONNECTION_ERROR_MESSAGE = "Error with connecting to %s: %s";

  private static final String USER_AGENT = "Custom Java application";
  private static final int VALUE_LIMIT = 50;

  private static final String SOLR_SEARCH_ALL_PARAMS = "select/?q=%s:%s&rows=0";
  private static final String SOLR_SEARCH_PARAMS = "select/?q=%s:%%22%s%%22&rows=0";

  private String solrBasePath;
  private String solrSearchPattern;
  private String solrSearchAllPattern;
  private SolrConfiguration solrConfiguration;
  private Http2SolrClient solr;
  boolean trimId = true;

  public DefaultSolrClient(SolrConfiguration solrConfiguration) {
    this.solrConfiguration = solrConfiguration;
    solr = new Http2SolrClient.Builder(solrConfiguration.getUrl()).build();
  }

  public String getSolrSearchResponse(String solrField, String value) {
    String url = buildUrl(solrField, value);
    return connect(url, solrField, value);
  }

  public String getSolrSearchResponse(String solrField, Set<String> values) {
    String url = buildUrl(solrField, values);
    return connect(url, solrField, StringUtils.join(values, " --- "));
  }

  @Override
  public String getTfIdfResponse(String params, String recordId) {
    return connect(getSolrBasePath() + params, "tf-idf", recordId);
  }

  public String buildUrl(String solrField, String value) {
    String url;
    if (value.equals("*")) {
      url = String.format(getSolrSearchAllPattern(), solrField, value);
    } else {
      try {
        value = value.replace("\"", "\\\"");
        String encodedValue = URLEncoder.encode(value, "UTF-8");
        url = String.format(getSolrSearchPattern(), solrField, encodedValue);
      } catch (UnsupportedEncodingException e) {
        LOGGER.log(Level.WARNING, "buildUrl", e);
        url = String.format(getSolrSearchPattern(), solrField, value);
      }
    }
    return url;
  }

  private String buildUrl(String solrField, Set<String> values) {
    String url;
      try {
        List<String> all = new ArrayList<>();
        for (String value : values) {
          all.add('"' + value.replace("\"", "\\\"") + '"');
        }
        String value = String.format("(%s)", StringUtils.join(all, " AND "));
        String encodedValue = URLEncoder.encode(value, "UTF-8");
        url = String.format(getSolrSearchAllPattern(), solrField, encodedValue);
      } catch (UnsupportedEncodingException e) {
        LOGGER.log(Level.WARNING, "buildUrl", e);
        url = String.format(getSolrSearchPattern(), solrField, values.toArray()[0]);
      }
    return url;
  }

  private String connect(String url, String solrField, String value) {
    URL fragmentPostUrl = null;
    String rawSolrResponse = null;
    try {
      fragmentPostUrl = new URL(url);
      HttpURLConnection urlConnection = null;
      try {
        urlConnection = (HttpURLConnection) fragmentPostUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", USER_AGENT);
        urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        urlConnection.setDoOutput(true);
        try {
          if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            rawSolrResponse = readStream(in);
          } else {
            int lenght = urlConnection.getContentLength();
            String shortenedValue = value.length() < VALUE_LIMIT ? value : value.substring(0, VALUE_LIMIT) + "...";
            String solrResponse = lenght == 0 ? "" : readStream(new BufferedInputStream(urlConnection.getInputStream()));
            LOGGER.severe(String.format("%s: %s returned code %d. Solr responde: %s",
              solrField, shortenedValue, urlConnection.getResponseCode(), solrResponse
            ));
          }
        } catch (IOException e) {
          LOGGER.severe(String.format(CONNECTION_ERROR_MESSAGE, url, e.getMessage()));
        }
      } catch (IOException e) {
        LOGGER.severe(String.format(CONNECTION_ERROR_MESSAGE, url, e.getMessage()));
      } finally {
        if (urlConnection != null) {
          urlConnection.disconnect();
        }
      }
    } catch (MalformedURLException e) {
      LOGGER.severe(String.format(CONNECTION_ERROR_MESSAGE, url, e.getMessage()));
    }

    // add request header
    return rawSolrResponse;
  }

  private String readStream(InputStream in) throws IOException {
    var rd = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    var result = new StringBuilder();
    var line = "";
    while ((line = rd.readLine()) != null) {
      result.append(line);
    }

    return result.toString();
  }

  public String getSolrBasePath() {
    if (solrBasePath == null) {
      this.solrBasePath = String.format("http://%s:%s/%s",
        solrConfiguration.getSolrHost(),
        solrConfiguration.getSolrPort(),
        solrConfiguration.getSolrPath()
      );
    }
    return this.solrBasePath;
  }

  public String getSolrSearchPattern() {
    if (solrSearchPattern == null) {
      this.solrSearchPattern = String.format(
        "%s/%s", getSolrBasePath(), SOLR_SEARCH_PARAMS
      );
    }
    return this.solrSearchPattern;
  }

  public String getSolrSearchAllPattern() {
    if (solrSearchAllPattern == null) {
      this.solrSearchAllPattern = String.format(
        "%s/%s", getSolrBasePath(), SOLR_SEARCH_ALL_PARAMS
      );
    }
    return this.solrSearchAllPattern;
  }

  public void indexMap(String id, Map<String, List<String>> objectMap) throws IOException, SolrServerException {
    SolrInputDocument document = new SolrInputDocument();
    document.addField("id", (trimId ? id.trim() : id));
    for (Map.Entry<String, List<String>> entry : objectMap.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value != null) {
        if (!key.endsWith("_sni") && !key.endsWith("_ss"))
          key += "_ss";
        document.addField(key, value);
      }
    }

    try {
      solr.add(document);
    } catch (BaseHttpSolrClient.RemoteSolrException ex) {
      LOGGER.log(Level.WARNING, String.format("Solr input document: %s", document.toString()));
      LOGGER.log(Level.WARNING, "Commit exception", ex);
    }
  }

  @Override
  public void commit() {
    try {
      solr.commit();
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteAll() {
    LOGGER.info("deleteAll");
    try {
      solr.deleteByQuery("*:*");
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
    }
    commit();
  }

  // TODO create index programatically
}
