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
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
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

	private String solrHost;
	private String solrPort;
	private String solrPath;
	private String solrBasePath;
	private String solrSearchPattern;
	private UniquenessExtractor extractor;
	private Map<String, String> label2Path = new HashMap<>();
	private List<UniquenessField> solrFields;

	private static final String SOLR_SEARCH_ALL_PARAMS = "select/?q=%s:%s&rows=0";
	private static final String SOLR_SEARCH_PARAMS = "select/?q=%s:%%22%s%%22&rows=0";

	private static final HttpClient httpClient = new HttpClient();
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
			field.setJsonPath(schema.getPathByLabel(label).getAbsoluteJsonPath());
			String solrField = schema.getSolrFields().get(label);
			if (solrField.endsWith("_txt"))
				solrField = solrField.substring(0, solrField.length() - 4) + "_ss";
			field.setSolrField(solrField);

			String solrResponse = getSolrSearchResponse(solrField, "*");
			int numFound = extractor.extractNumFound(solrResponse, "total");
			field.setTotal(numFound);

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
			String path = label2Path.get(solrField.getJsonPath());
			List<XmlFieldInstance> values = cache.get(path);
			List<Integer> numbers = new ArrayList<>();
			for (XmlFieldInstance fieldInstance : values) {
				String value = fieldInstance.getValue();
				if (StringUtils.isNotBlank(value)) {
					String solrResponse = getSolrSearchResponse(solrField.getSolrField(), value);
					numbers.add(extractor.extractNumFound(solrResponse, recordId));
				}
			}
			Double result = 0.0;
			if (!numbers.isEmpty()) {
				if (numbers.size() == 1) {
					result = (double)numbers.get(0);
				} else {
					double total = 0;
					for (int number : numbers)
						total += number;
					result = total / numbers.size();
				}
			}
			resultMap.put(solrField.getSolrField(), result);
		}
	}

	private String getSolrSearchResponse(String solrField, String value) {
		String jsonString = null;

		String url;
		if (value.equals("*")) {
			url = String.format(getSolrSearchAllPattern(), solrField, value);
		} else {
			logger.info(getSolrSearchPattern());
			logger.info(solrField);
			logger.info(value);
			url = String.format(getSolrSearchPattern(), solrField, value);
		}
		logger.info(url);
		// String url = String.format(getSolrSearchPattern(), solrField, value).replace("\"", "%22");
		HttpMethod method = new GetMethod(url);
		HttpMethodParams params = new HttpMethodParams();
		params.setIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, 1024 * 1024);
		method.setParams(params);
		try {
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.severe("Method failed: " + method.getStatusLine());
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(method.getResponseBodyAsStream(), baos);
			byte[] responseBody = baos.toByteArray();

			jsonString = new String(responseBody, Charset.forName("UTF-8"));
		} catch (HttpException e) {
			logger.severe("Fatal protocol violation: " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Fatal transport error: " + e.getMessage());
		} finally {
			method.releaseConnection();
		}

		logger.info(jsonString);

		return jsonString;
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
			headers.add(field.getSolrField());
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
		if (solrSearchPattern == null) {
			this.solrSearchPattern = String.format(
				"%s/%s", getSolrBasePath(), SOLR_SEARCH_ALL_PARAMS
			);
		}
		return this.solrSearchPattern;
	}
}
