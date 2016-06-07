package com.nsdr.metadataqa.api.calculator;

import com.nsdr.metadataqa.api.json.JsonBranch;
import com.nsdr.metadataqa.api.json.FieldGroup;
import com.nsdr.metadataqa.api.counter.Counters;
import com.nsdr.metadataqa.api.interfaces.Calculator;
import com.nsdr.metadataqa.api.model.JsonPathCache;
import com.jayway.jsonpath.InvalidJsonException;
import com.nsdr.metadataqa.api.model.XmlFieldInstance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.nsdr.metadataqa.api.schema.Schema;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T>
 */
public class CompletenessCalculator<T extends XmlFieldInstance> implements Calculator, Serializable {

	private static final Logger LOGGER = Logger.getLogger(CompletenessCalculator.class.getCanonicalName());

	private String inputFileName;

	// private Counters counters;
	private List<String> missingFields;
	private List<String> emptyFields;
	private List<String> existingFields;
	private Schema branches;

	private boolean verbose = false;

	private static final List<FieldGroup> FIELD_GROUPS = new ArrayList<>();

	static {
	}

	public CompletenessCalculator() {
		// this.recordID = null;
	}

	public CompletenessCalculator(String recordID) {
		// this.recordID = recordID;
	}

	public CompletenessCalculator(Schema branches) {
		this.branches = branches;
	}

	@Override
	public void measure(JsonPathCache cache, Counters counters) throws InvalidJsonException {
		// Object document = JSON_PROVIDER.parse(jsonString);
		if (verbose) {
			missingFields = new ArrayList<>();
			emptyFields = new ArrayList<>();
			existingFields = new ArrayList<>();
		}

		for (JsonBranch jsonBranch : branches.getPaths()) {
			evaluateJsonBranch(jsonBranch, cache, counters);
		}

		for (FieldGroup fieldGroup : branches.getFieldGroups()) {
			boolean existing = false;
			for (String field : fieldGroup.getFields()) {
				if (counters.getExistenceMap().get(field) == true) {
					existing = true;
					break;
				}
			}
			counters.increaseInstance(fieldGroup.getCategory(), existing);
		}
	}

	public void evaluateJsonBranch(JsonBranch jsonBranch, JsonPathCache cache, Counters counters) {
		List<T> values = cache.get(jsonBranch.getJsonPath());
		counters.increaseTotal(jsonBranch.getCategories());
		if (values != null && !values.isEmpty()) {
			counters.increaseInstance(jsonBranch.getCategories());
			counters.addExistence(jsonBranch.getLabel(), true);
			counters.addInstance(jsonBranch.getLabel(), values.size());
			if (verbose) {
				existingFields.add(jsonBranch.getLabel());
			}
		} else {
			counters.addExistence(jsonBranch.getLabel(), false);
			counters.addInstance(jsonBranch.getLabel(), 0);
			if (verbose) {
				missingFields.add(jsonBranch.getLabel());
			}
		}
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public List<String> getMissingFields() {
		return missingFields;
	}

	public List<String> getEmptyFields() {
		return emptyFields;
	}

	public List<String> getExistingFields() {
		return existingFields;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getInputFileName() {
		return inputFileName;
	}

}
