package de.gwdg.metadataqa.api.uniqueness;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

public class SolrClient {

	private static final Logger logger = Logger.getLogger(SolrClient.class.getCanonicalName());

	private final String USER_AGENT = "Custom Java application";
	private final int VALUE_LIMIT = 50;

	private final static String SOLR_HOST = "localhost";
	private final static String SOLR_PORT = "8983";
	private final static String SOLR_PATH = "solr/europeana";

	private static final String SOLR_SEARCH_ALL_PARAMS = "select/?q=%s:%s&rows=0";
	private static final String SOLR_SEARCH_PARAMS = "select/?q=%s:%%22%s%%22&rows=0";

	private String solrHost;
	private String solrPort;
	private String solrPath;

	private String solrBasePath;
	private String solrSearchPattern;
	private String solrSearchAllPattern;

	public SolrClient() {
	}

	public String getSolrSearchResponse(String solrField, String value) {
		String jsonString = null;

		String url = buildUrl(solrField, value);
		jsonString = connect(url, solrField, value);
		return jsonString;
	}

	private String buildUrl(String solrField, String value) {
		String url;
		if (value.equals("*")) {
			url = String.format(getSolrSearchAllPattern(), solrField, value);
		} else {
			try {
				value = value.replace("\"", "\\\"");
				String encodedValue = URLEncoder.encode(value, "UTF-8");
				url = String.format(getSolrSearchPattern(), solrField, encodedValue);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				url = String.format(getSolrSearchPattern(), solrField, value);
			}
		}
		return url;
	}

	private String connect(String url, String solrField, String value) {

		URL fragmentPostUrl = null;
		String record = null;
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
					if (urlConnection.getResponseCode() == 200) {
						InputStream in = new BufferedInputStream(urlConnection.getInputStream());
						record = readStream(in);
					} else {
						int lenght = urlConnection.getContentLength();
						logger.severe(String.format("%s: %s returned code %d. Solr responde: %s",
							solrField,
							(value.length() < VALUE_LIMIT ? value : value.substring(0, VALUE_LIMIT) + "..."),
							urlConnection.getResponseCode(),
							(lenght == 0 ? "" : readStream(new BufferedInputStream(urlConnection.getInputStream())))
						));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// add request header
		return record;
	}

	private String readStream(InputStream in) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(in));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	public String getSolrBasePath() {
		if (solrBasePath == null) {
			this.solrBasePath = String.format("http://%s:%s/%s",
				(StringUtils.isBlank(solrHost) ? SOLR_HOST : solrHost),
				(StringUtils.isBlank(solrPort) ? SOLR_PORT : solrPort),
				(StringUtils.isBlank(solrPath) ? SOLR_PATH : solrPath)
			);
		}
		return this.solrBasePath ;
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

	public String getSolrHost() {
		return solrHost;
	}

	public void setSolrHost(String solrHost) {
		this.solrHost = solrHost;
	}

	public String getSolrPort() {
		return solrPort;
	}

	public void setSolrPort(String solrPort) {
		this.solrPort = solrPort;
	}

	public String getSolrPath() {
		return solrPath;
	}

	public void setSolrPath(String solrPath) {
		this.solrPath = solrPath;
	}

	public void setSolr(String solrHost, String solrPort, String solrPath) {
		this.solrHost = solrHost;
		this.solrPort = solrPort;
		this.solrPath = solrPath;
	}
}
