package de.gwdg.metadataqa.api.model;

import de.gwdg.metadataqa.api.model.pathcache.CsvPathCache;
import de.gwdg.metadataqa.api.model.pathcache.JsonPathCache;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.model.pathcache.XmlPathCache;
import de.gwdg.metadataqa.api.schema.Format;

public class PathCacheFactory {

  private PathCacheFactory() {}

  public static PathCache<? extends XmlFieldInstance> getInstance(Format format,
                                                                  String content) {
    PathCache<? extends XmlFieldInstance> cache;
    if (format == Format.JSON) {
      cache = new JsonPathCache<>(content);
    } else if (format == Format.XML) {
      cache = new XmlPathCache<>(content);
    } else if (format == Format.CSV) {
      cache = new CsvPathCache<>(content);
    } else {
      throw new IllegalArgumentException("Unrecognized format: " + format);
    }
    return cache;
  }
}
