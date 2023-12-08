package de.gwdg.metadataqa.api.model.selector;

import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.schema.Format;

import java.util.Map;

public class SelectorFactory {

  private SelectorFactory() {}

  public static Selector<? extends XmlFieldInstance> getInstance(Format format,
                                                                 String content) {
    return getInstance(format, content, null);
  }

  public static Selector<? extends XmlFieldInstance> getInstance(Format format,
                                                                 String content,
                                                                 Map<String, String> namespaces) {
    Selector<? extends XmlFieldInstance> cache;
    if (format == Format.JSON) {
      cache = new JsonSelector<>(content);
    } else if (format == Format.XML) {
      cache = new XmlSelector<>(content, namespaces);
    } else if (format == Format.CSV) {
      cache = new CsvSelector<>(content);
    } else {
      throw new IllegalArgumentException("Unrecognized format: " + format);
    }
    return cache;
  }
}
