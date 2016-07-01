package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * example: 2023702/35D943DF60D779EC9EF31F5DFF4E337385AC7C37
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TitleAndDescriptionAreSame extends ProblemDetector implements Serializable {

	private static final Logger logger = Logger.getLogger(TitleAndDescriptionAreSame.class.getCanonicalName());

	private final String NAME = "TitleAndDescriptionAreSame";

	@SuppressWarnings("LeakingThisInConstructor")
	public TitleAndDescriptionAreSame(ProblemCatalog problemCatalog) {
		this.problemCatalog = problemCatalog;
		this.problemCatalog.addObserver(this);
		this.schema = problemCatalog.getSchema();
	}

	@Override
	public void update(JsonPathCache cache, FieldCounter<Double> results) {
		double value = 0;
		List<EdmFieldInstance> titles = cache.get(schema.getTitlePath());
		if (titles != null && !titles.isEmpty()) {
			List<EdmFieldInstance> descriptions = cache.get(schema.getDescriptionPath());
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

	@Override
	public String getHeader() {
		return NAME;
	}
}
