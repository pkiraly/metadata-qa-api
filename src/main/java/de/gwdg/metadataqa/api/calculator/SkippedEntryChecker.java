package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface SkippedEntryChecker {
	public List<String> getSkippableCollectionIds(JsonPathCache jsonPathCache);
	public <T extends XmlFieldInstance> String extractId(T value);
}