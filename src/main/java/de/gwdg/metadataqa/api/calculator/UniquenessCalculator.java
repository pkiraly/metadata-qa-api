package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;
import de.gwdg.metadataqa.api.uniqueness.UniquenessField;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessCalculator implements Calculator, Serializable {

	public static final String CALCULATOR_NAME = "uniqueness";

	private static final Logger logger = Logger.getLogger(UniquenessCalculator.class.getCanonicalName());

	private final static String SOLR_HOST = "localhost";
	private final static String SOLR_PORT = "8983";
	private final static String SOLR_PATH = "solr/europeana";
	private final String USER_AGENT = "Custom Java application";
	private final int VALUE_LIMIT = 50;

	private String solrHost;
	private String solrPort;
	private String solrPath;
	private String solrBasePath;
	private String solrSearchPattern;
	private String solrSearchAllPattern;
	private UniquenessExtractor extractor;
	private List<UniquenessField> solrFields;

	private static final String SOLR_SEARCH_ALL_PARAMS = "select/?q=%s:%s&rows=0";
	private static final String SOLR_SEARCH_PARAMS = "select/?q=%s:%%22%s%%22&rows=0";

	private static HttpClient httpClient = new HttpClient();
	private FieldCounter<Double> resultMap;
	private Schema schema;

	public UniquenessCalculator() {
	}

	public UniquenessCalculator(Schema schema) {
		this.schema = schema;
		extractor = new UniquenessExtractor(schema);
		solrFields = new ArrayList<>();
		for (String label : this.schema.getSolrFields().keySet()) {
			UniquenessField field = new UniquenessField(label);
			field.setJsonPath(schema.getPathByLabel(label).getAbsoluteJsonPath().replace("[*]", ""));
			String solrField = schema.getSolrFields().get(label);
			if (solrField.endsWith("_txt"))
				solrField = solrField.substring(0, solrField.length() - 4) + "_ss";
			field.setSolrField(solrField);

			String solrResponse = getSolrSearchResponse(solrField, "*");
			int numFound = extractor.extractNumFound(solrResponse, "total");
			field.setTotal(numFound);
			field.setScoreForUniqueValue(calculateScore(numFound, 1.0));

			solrFields.add(field);
		}
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	@Override
	public void measure(JsonPathCache cache) {
		String recordId = cache.getRecordId();
		if (recordId.startsWith("/"))
			recordId = recordId.substring(1);

		resultMap = new FieldCounter<>();
		for (UniquenessField solrField : solrFields) {
			// logger.info(solrField.getJsonPath());
			List<XmlFieldInstance> values = cache.get(solrField.getJsonPath());
			List<Double> numbers = new ArrayList<>();
			List<Double> counts = new ArrayList<>();
			if (values != null) {
				for (XmlFieldInstance fieldInstance : values) {
					String value = fieldInstance.getValue();
					if (StringUtils.isNotBlank(value)) {
						String solrResponse = getSolrSearchResponse(solrField.getSolrField(), value);
						int count = extractor.extractNumFound(solrResponse, recordId);
						double score = calculateScore(solrField.getTotal(), count) / solrField.getScoreForUniqueValue();
						logger.info(String.format("%f/%d -> %f", count, solrField.getTotal(), score));

						counts.add((double)count);
						numbers.add(score);
					}
				}
			}
			resultMap.put(solrField.getSolrField() + "/count", getAverage(counts));
			resultMap.put(solrField.getSolrField() + "/score", getAverage(numbers));
		}
	}

	private String getSolrSearchResponse(String solrField, String value) {
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

	@Override
	public Map<String, ? extends Object> getResultMap() {
		return resultMap.getMap();
	}

	@Override
	public Map<String, Map<String, ? extends Object>> getLabelledResultMap() {
		Map<String, Map<String, ? extends Object>> labelledResultMap = new LinkedHashMap<>();
		labelledResultMap.put(getCalculatorName(), resultMap.getMap());
		return labelledResultMap;
	}

	@Override
	public String getCsv(boolean withLabel, CompressionLevel compressionLevel) {
		return resultMap.getList(withLabel, compressionLevel);
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		for (UniquenessField field : solrFields) {
			headers.add(field.getSolrField() + "/count");
			headers.add(field.getSolrField() + "/score");
		}
		return headers;
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

	public double calculateScore(double total, double actual) {
		double score = Math.log(1 + (total - actual + 0.5) / (actual + 0.5));
		return score;
	}

	private Double getAverage(List<Double> numbers) {
		Double result = 0.0;
		if (!numbers.isEmpty()) {
			if (numbers.size() == 1) {
				result = numbers.get(0);
			} else {
				double total = 0;
				for (double number : numbers)
					total += number;
				result = total / numbers.size();
			}
		}
		return result;
	}

}
