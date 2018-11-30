package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.uniqueness.SolrClient;
import de.gwdg.metadataqa.api.uniqueness.UniquenessFieldCalculator;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;
import de.gwdg.metadataqa.api.uniqueness.UniquenessField;
import de.gwdg.metadataqa.api.util.CompressionLevel;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class UniquenessCalculator implements Calculator, Serializable {

	public static final String CALCULATOR_NAME = "uniqueness";

	private static final Logger logger = Logger.getLogger(UniquenessCalculator.class.getCanonicalName());

	private UniquenessExtractor extractor;
	private List<UniquenessField> solrFields;
	SolrClient solrClient;

	private static HttpClient httpClient = new HttpClient();
	private FieldCounter<Double> resultMap;

	public UniquenessCalculator() {
		SolrClient solrClient = new SolrClient();
	}

	public UniquenessCalculator(Schema schema) {
		extractor = new UniquenessExtractor();
		initialize(schema);
	}

	private void initialize(Schema schema) {
		solrFields = new ArrayList<>();
		for (String label : schema.getSolrFields().keySet()) {
			UniquenessField field = new UniquenessField(label);
			field.setJsonPath(schema.getPathByLabel(label).getAbsoluteJsonPath().replace("[*]", ""));
			String solrField = schema.getSolrFields().get(label);
			if (solrField.endsWith("_txt")) {
				solrField = solrField.substring(0, solrField.length() - 4) + "_ss";
			}
			field.setSolrField(solrField);

			String solrResponse = solrClient.getSolrSearchResponse(solrField, "*");
			int numFound = extractor.extractNumFound(solrResponse, "total");
			field.setTotal(numFound);
			field.setScoreForUniqueValue(
				UniquenessFieldCalculator.calculateScore(numFound, 1.0)
			);

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
		if (recordId.startsWith("/")) {
			recordId = recordId.substring(1);
		}

		resultMap = new FieldCounter<>();
		for (UniquenessField solrField : solrFields) {
			UniquenessFieldCalculator fieldCalculator = new UniquenessFieldCalculator(cache, recordId, solrClient, solrField);
			fieldCalculator.calculate();
			resultMap.put(solrField.getSolrField() + "/count", fieldCalculator.getAverageCount());
			resultMap.put(solrField.getSolrField() + "/score", fieldCalculator.getAverageScore());
		}
	}

	public String getTotals() {
		List<Integer> totals = new ArrayList<>();
		for (UniquenessField field : solrFields) {
			totals.add(field.getTotal());
		}
		return StringUtils.join(totals, ",");
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
}
