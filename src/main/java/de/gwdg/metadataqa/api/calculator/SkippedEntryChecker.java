package de.gwdg.metadataqa.api.calculator;

import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;

import java.io.Serializable;
import java.util.List;

/**
 * An interface to detect skippable entities
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface SkippedEntryChecker extends Serializable {
  List<String> getSkippableCollectionIds(Selector cache);
  <T extends XmlFieldInstance> String extractId(T value);
}
