package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.schema.Format;

public class PathCacheFactory {

  public static PathCache<? extends XmlFieldInstance> getInstance(Format format, String jsonRecord) {
    PathCache cache = null;
    switch (format) {
      case JSON: cache = new JsonPathCache<>(jsonRecord); break;
      case XML: cache = new XmlPathCache<>(jsonRecord); break;
      default: throw new IllegalArgumentException();
    }
    return cache;
  }
}
