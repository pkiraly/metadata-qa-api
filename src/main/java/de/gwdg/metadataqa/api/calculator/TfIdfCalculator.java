package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.TfIdf;
import de.gwdg.metadataqa.api.uniqueness.TfIdfExtractor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TfIdfCalculator implements Calculator, Serializable {

	private static final Logger logger = Logger.getLogger(TfIdfCalculator.class.getCanonicalName());

	private String CALCULATOR_NAME = "uniquness";

	private static final String SOLR_SEARCH_PATH = "http://localhost:8983/solr/europeana/tvrh/?q=id:\"%s\""
			  + "&version=2.2&indent=on&qt=tvrh&tv=true&tv.all=true&f.includes.tv.tf=true"
			  + "&tv.fl=dc_title_txt,dc_description_txt,dcterms_alternative_txt"
			  + "&wt=json&json.nl=map&rows=1000&fl=id";
	private static final HttpClient httpClient = new HttpClient();
	private Map<String, List<TfIdf>> termsCollection;
	private boolean doCollectTerms = false;
	private FieldCounter<Double> resultMap;
	private Schema schema;

	public TfIdfCalculator() {
	}

	public TfIdfCalculator(Schema schema) {
		this.schema = schema;
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	@Override
	public void measure(JsonPathCache cache) {
		String solrJsonResponse = getSolrResponse(cache.getRecordId());
		TfIdfExtractor extractor = new TfIdfExtractor(schema);
		resultMap = extractor.extract(solrJsonResponse, cache.getRecordId(), doCollectTerms);
		// counters.setTfIdfList(resultMap);
		termsCollection = extractor.getTermsCollection();
	}

	private String getSolrResponse(String recordId) {
		String jsonString = null;

		String url = String.format(SOLR_SEARCH_PATH, recordId).replace("\"", "%22");
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

		return jsonString;
	}

	public Map<String, List<TfIdf>> getTermsCollection() {
		return termsCollection;
	}

	public void setDoCollectTerms(boolean doCollectTerms) {
		this.doCollectTerms = doCollectTerms;
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
	public String getCsv(boolean withLabel, boolean compressed) {
		return resultMap.getList(withLabel, compressed);
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		for (String field : schema.getSolrFields().keySet()) {
			headers.add(field + ":sum");
			headers.add(field + ":avg");
		}
		return headers;
	}
}
