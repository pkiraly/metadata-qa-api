package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;

import java.util.List;

public class InstanceCounter {
  boolean isNA = true;
  int count = 0;
  private final PathCache cache;
  private final JsonBranch field;

  public InstanceCounter(PathCache cache, JsonBranch field) {
    this.cache = cache;
    this.field = field;
    count();
  }

  private void count() {
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty())
      for (XmlFieldInstance instance : instances)
        if (instance.hasValue()) {
          count++;
          isNA = false;
        }
  }

  public boolean isNA() {
    return isNA;
  }

  public int getCount() {
    return count;
  }
}
