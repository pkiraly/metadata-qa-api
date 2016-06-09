package de.gwdg.metadataqa.api.calculator;


import de.gwdg.metadataqa.api.counter.Counters;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import com.jayway.jsonpath.InvalidJsonException;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldExtractor implements Calculator, Serializable {

	private String idPath;

	public FieldExtractor() {
	}

	public FieldExtractor(String idPath) {
		this.idPath = idPath;
	}

	@Override
	public void measure(JsonPathCache cache, Counters counters)
			throws InvalidJsonException {
		counters.setRecordId(
			((List<XmlFieldInstance>)cache.get(getIdPath()))
				.get(0).getValue());
		cache.setRecordId(counters.getRecordId());
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}
}
