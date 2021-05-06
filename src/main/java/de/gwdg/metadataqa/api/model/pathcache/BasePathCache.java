package de.gwdg.metadataqa.api.model.pathcache;

import de.gwdg.metadataqa.api.model.XmlFieldInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasePathCache<T extends XmlFieldInstance> implements PathCache {

  protected final Map<String, List<T>> cache = new HashMap<>();
  protected final Map<String, Object> typedCache = new HashMap<>();

  abstract void set(String address, String jsonPath, Object jsonFragment, Class clazz);

  public List<T> get(String jsonPath) {
    return get(jsonPath, jsonPath, null, null);
  }

  public <E> E get(String jsonPath, Class<E> clazz) {
    if (!typedCache.containsKey(jsonPath)) {
      set(jsonPath, jsonPath, null, clazz);
    }
    return (E) typedCache.get(jsonPath);
  }

  public List<T> get(String address, String jsonPath, Object jsonFragment) {
    return get(address, jsonPath, jsonFragment, null);
  }

  public List<T> get(String address, String jsonPath, Object jsonFragment, Class clazz) {
    if (!cache.containsKey(address)) {
      set(address, jsonPath, jsonFragment, clazz);
    }
    return cache.get(address);
  }
}
