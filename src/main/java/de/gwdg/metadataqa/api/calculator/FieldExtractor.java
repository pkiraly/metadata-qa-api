package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractor implements Calculator, Serializable {

	private static final String CALCULATOR_NAME = "fields";

	public String FIELD_NAME = "recordId";
	private String idPath;
	protected FieldCounter<String> resultMap;
	protected Schema schema;

	public FieldExtractor() {
	}

	public FieldExtractor(Schema schema) {
		this.schema = schema;
		setIdPath(schema.getExtractableFields().get(FIELD_NAME));
	}

	public FieldExtractor(String idPath) {
		this.idPath = idPath;
	}

	@Override
	public String getCalculatorName() {
		return CALCULATOR_NAME;
	}

	@Override
	public void measure(JsonPathCache cache)
			throws InvalidJsonException {
		resultMap = new FieldCounter<>();
		String recordId = ((List<XmlFieldInstance>)cache.get(getIdPath())).get(0).getValue();
		cache.setRecordId(recordId);
		resultMap.put(FIELD_NAME, recordId);
		if (schema != null) {
			String path;
			for (String fieldName : schema.getExtractableFields().keySet()) {
				if (!fieldName.equals(FIELD_NAME)) {
					path = schema.getExtractableFields().get(fieldName);
					resultMap.put(
						fieldName,
						((List<XmlFieldInstance>)cache.get(path)).get(0).getValue()
					);
				}
			}
		}
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
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
		return resultMap.getList(withLabel, false); // the extracted fields should never be compressed!
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		headers.add(FIELD_NAME);
		return headers;
	}

}
