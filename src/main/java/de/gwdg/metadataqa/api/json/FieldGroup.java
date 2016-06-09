package de.gwdg.metadataqa.api.json;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class FieldGroup {

	private List<String> fields;
	private JsonBranch.Category category;

	public FieldGroup(JsonBranch.Category category, String... fields) {
		this.category = category;
		this.fields = Arrays.asList(fields);
	}

	public List<String> getFields() {
		return fields;
	}

	public JsonBranch.Category getCategory() {
		return category;
	}

}
