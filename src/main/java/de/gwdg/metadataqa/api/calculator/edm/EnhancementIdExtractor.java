package de.gwdg.metadataqa.api.calculator.edm;

import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EnhancementIdExtractor implements Serializable {

	private static final List<String> KNOWN_PROPERTIES = Arrays.asList(
		"@about", "edm:europeanaProxy", "ore:proxyFor", "ore:proxyIn", "edm:type"
	);
	private static final String PATH = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'true')]";

	public static List<String> extractIds(JsonPathCache cache) {
		List<String> enhancementIds = new ArrayList<>();
		Object rawJsonFragment = cache.getFragment(PATH);
		List<Object> jsonFragments = Converter.jsonObjectToList(rawJsonFragment);
		Map<String, Object> jsonFragment = (Map)jsonFragments.get(0);
		for (String key : jsonFragment.keySet()) {
			if (!KNOWN_PROPERTIES.contains(key)) {
				List<EdmFieldInstance> values = (List<EdmFieldInstance>) 
					JsonUtils.extractFieldInstanceList(jsonFragment.get(key), null, null);
				for (EdmFieldInstance value : values)
					enhancementIds.add(value.getResource());
			}
		}
		return enhancementIds;
	}
}
