package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * example: 2023702/35D943DF60D779EC9EF31F5DFF4E337385AC7C37
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TitleAndDescriptionAreSame extends ProblemDetector implements Serializable {

	private static final Logger logger = Logger.getLogger(TitleAndDescriptionAreSame.class.getCanonicalName());

	private final String NAME = "TitleAndDescriptionAreSame";
	private final String title = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']";
	private final String description = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']";

	@SuppressWarnings("LeakingThisInConstructor")
	public TitleAndDescriptionAreSame(ProblemCatalog problemCatalog) {
		this.problemCatalog = problemCatalog;
		this.problemCatalog.addObserver(this);
	}

	@Override
	public void update(JsonPathCache cache, Map<String, Double> results) {
		double value = 0;
		List<EdmFieldInstance> titles = cache.get(title);
		if (titles != null && !titles.isEmpty()) {
			List<EdmFieldInstance> descriptions = cache.get(description);
			if (descriptions != null && !descriptions.isEmpty()) {
				if (titles.size() > 0) {
					if (descriptions.size() > 0) {
						for (EdmFieldInstance title : titles) {
							if (descriptions.contains(title)) {
								value = 1;
								break;
							}
						}
					}
				}
			}
		}
		results.put(NAME, value);
	}
}
