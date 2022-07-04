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
  private boolean allowEmptyInstances = true;

  public InstanceCounter(PathCache cache, JsonBranch field) {
    this.cache = cache;
    this.field = field;
    count();
  }

  public InstanceCounter(PathCache cache, JsonBranch field, boolean allowEmptyInstances) {
    this.cache = cache;
    this.field = field;
    this.allowEmptyInstances = allowEmptyInstances;
    count();
  }

  private void count() {
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty())
      for (XmlFieldInstance instance : instances)
        if (allowEmptyInstances || instance.hasValue()) {
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

  @Override
  public String toString() {
    return "InstanceCounter{" +
      "isNA=" + isNA +
      ", count=" + count +
      '}';
  }
}
