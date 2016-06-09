package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EmptyStrings extends ProblemDetector implements Serializable {

	private static final Logger logger = Logger.getLogger(EmptyStrings.class.getCanonicalName());

	private final String NAME = "EmptyStrings";
	private final List<String> paths = Arrays.asList(
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']",
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']",
		"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']"
	);

	public EmptyStrings(ProblemCatalog problemCatalog) {
		this.problemCatalog = problemCatalog;
		this.problemCatalog.addObserver(this);
	}

	@Override
	public void update(JsonPathCache cache, Map<String, Double> results) {
		double value = 0;
		for (String path : paths) {
			List<EdmFieldInstance> subjects = cache.get(path);
			if (subjects != null && !subjects.isEmpty()) {
				if (subjects.size() > 0) {
					for (EdmFieldInstance subject : subjects) {
						if (StringUtils.isBlank(subject.getValue())) {
							value += 1;
						}
					}
				}
			}
		}
		results.put(NAME, value);
	}
}
