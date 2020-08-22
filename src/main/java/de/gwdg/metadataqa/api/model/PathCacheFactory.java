package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.pathcache.XmlPathCache;
import de.gwdg.metadataqa.api.schema.Format;

public class PathCacheFactory {

  public static PathCache<? extends XmlFieldInstance> getInstance(Format format,
                                                                  String content) {
    PathCache cache = null;
    switch (format) {
      case JSON: cache = new JsonPathCache<>(content); break;
      case XML:  cache = new XmlPathCache<>(content); break;
      case CSV:  cache = new CsvPathCache<>(content); break;
      default: throw new IllegalArgumentException("Unrecognized format: " + format);
    }
    return cache;
  }
}
