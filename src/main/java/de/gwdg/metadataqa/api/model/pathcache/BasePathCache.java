package de.gwdg.metadataqa.api.model.pathcache;

import de.gwdg.metadataqa.api.model.XmlFieldInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasePathCache<T extends XmlFieldInstance> implements PathCache {

  protected String recordId;
  protected String content;

  protected final Map<String, List<T>> cache = new HashMap<>();
  protected final Map<String, Object> typedCache = new HashMap<>();
  protected final Map<String, Object> fragmentCache = new HashMap<>();

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

  /**
   * Get a JSON fragment from cache.
   *
   * @param address - a unique address for cahce
   * @param jsonPath - a JSON path expression
   * @param jsonFragment - a JSON fragment in which the path should be searched for
   *
   * @return
   *   The JSON fragment
   */
  public Object getFragment(String address, String jsonPath, Object jsonFragment) {
    Object jsonFragment2 = null;
    if (!fragmentCache.containsKey(address)) {
      jsonFragment2 = read(jsonPath, jsonFragment);
      fragmentCache.put(address, jsonFragment2);
    } else {
      jsonFragment2 = fragmentCache.get(address);
    }
    return jsonFragment2;
  }

  /**
   * Gets the record identifier.
   *
   * @return
   *   The record identifier
   */
  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  public Map<String, List<T>> getCache() {
    return cache;
  }

  public Map<String, Object> getFragmentCache() {
    return fragmentCache;
  }

  public String getContent() {
    return content;
  }
}
