package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.calculator.SkippedEntryChecker;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import java.io.Serializable;
import java.util.List;

/**
 * Seleting skippable entities
 *
 * @author Péter Király <peter.kiraly at gwdg.de>

 * @param <T> the type of elements held in this object. It should be the
 *           extension of XmlFieldInstance class.
 */
public class SkippedEntitySelector<T extends XmlFieldInstance> implements Serializable {

  private static final long serialVersionUID = 6798466355837341681L;
  private SkippedEntryChecker skippedEntryChecker;

  public SkippedEntitySelector() {
    skippedEntryChecker = null;
  }

  public SkippedEntitySelector(SkippedEntryChecker skippedEntryChecker) {
    this.skippedEntryChecker = skippedEntryChecker;
  }

  public boolean isCollectionSkippable(List<String> skippableIds,
                                       DataElement collectionBranch,
                                       int i,
                                       Selector<T> cache,
                                       Object jsonFragment) {
    var skippable = false;
    DataElement identifierPath = collectionBranch.getIdentifier();
    if (!skippableIds.isEmpty() && identifierPath != null) {
      var address = String.format("%s/%d/%s", collectionBranch.getPath(), i, identifierPath.getPath());
      List<T> values = cache.get(address, identifierPath.getPath(), jsonFragment);
      var id = (skippedEntryChecker != null)
            ? skippedEntryChecker.extractId(values.get(0))
            : values.get(0).getValue();
      skippable = skippableIds.contains(id);
    }
    return skippable;
  }

  public void setSkippedEntryChecker(SkippedEntryChecker skippedEntryChecker) {
    this.skippedEntryChecker = skippedEntryChecker;
  }

  public SkippedEntryChecker getSkippedEntryChecker() {
    return skippedEntryChecker;
  }
}
