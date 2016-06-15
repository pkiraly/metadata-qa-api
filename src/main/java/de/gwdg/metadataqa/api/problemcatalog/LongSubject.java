package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * See for example:
 * http://www.europeana.eu/portal/record/07602/5CFC6E149961A1630BAD5C65CE3A683DEB6285A0.json
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LongSubject extends ProblemDetector implements Serializable {

	private static final Logger logger = Logger.getLogger(LongSubject.class.getCanonicalName());

	private final String NAME = "LongSubject";
	private final String PATH = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']";
	private final int MAX_LENGTH = 50;

	public LongSubject(ProblemCatalog problemCatalog) {
		this.problemCatalog = problemCatalog;
		this.problemCatalog.addObserver(this);
	}

	@Override
	public void update(JsonPathCache cache, FieldCounter<Double> results) {
		double value = 0;
		List<EdmFieldInstance> subjects = cache.get(PATH);
		if (subjects != null && !subjects.isEmpty()) {
			if (subjects.size() > 0) {
				for (EdmFieldInstance subject : subjects) {
					if (StringUtils.isNotBlank(subject.getValue())
							&& subject.getValue().length() > MAX_LENGTH) {
						value += 1;
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
