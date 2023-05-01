package de.gwdg.metadataqa.api.model.selector;

import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.CsvSelector;
import de.gwdg.metadataqa.api.model.selector.JsonSelector;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.model.selector.XmlSelector;
import de.gwdg.metadataqa.api.schema.Format;

public class SelectorFactory {

  private SelectorFactory() {}

  public static Selector<? extends XmlFieldInstance> getInstance(Format format,
                                                                 String content) {
    Selector<? extends XmlFieldInstance> cache;
    if (format == Format.JSON) {
      cache = new JsonSelector<>(content);
    } else if (format == Format.XML) {
      cache = new XmlSelector<>(content);
    } else if (format == Format.CSV) {
      cache = new CsvSelector<>(content);
    } else {
      throw new IllegalArgumentException("Unrecognized format: " + format);
    }
    return cache;
  }
}
