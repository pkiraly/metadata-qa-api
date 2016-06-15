package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractor implements Calculator, Serializable {

	public String FIELD_NAME = "recordId";
	private String idPath;
	protected FieldCounter<String> resultMap;

	public FieldExtractor() {
	}

	public FieldExtractor(String idPath) {
		this.idPath = idPath;
	}

	@Override
	public void measure(JsonPathCache cache)
			throws InvalidJsonException {
		resultMap = new FieldCounter<>();
		String recordId = ((List<XmlFieldInstance>)cache.get(getIdPath())).get(0).getValue();
		cache.setRecordId(recordId);
		resultMap.put(FIELD_NAME, recordId);
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
	public String getCsv(boolean withLabel, boolean compressed) {
		System.err.println("getCsv");
		return resultMap.getList(withLabel, compressed);
	}

	@Override
	public List<String> getHeader() {
		List<String> headers = new ArrayList<>();
		headers.add(FIELD_NAME);
		return headers;
	}

}
