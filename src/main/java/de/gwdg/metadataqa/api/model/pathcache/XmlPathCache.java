package de.gwdg.metadataqa.api.model.pathcache;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.PathNotFoundException;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.ExceptionUtils;
import de.gwdg.metadataqa.api.xml.OaiPmhXPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T> the type of elements held in this object. It should be the
 *           extension of XmlFieldInstance class.
 */
public class XmlPathCache<T extends XmlFieldInstance> implements PathCache {

  private static final Logger LOGGER = Logger.getLogger(
    XmlPathCache.class.getCanonicalName()
  );

  private String recordId;
  private String content;
  private final Map<String, List<T>> cache = new HashMap<>();
  private final Map<String, Object> typedCache = new HashMap<>();
  private final Map<String, Object> fragmentCache = new HashMap<>();
  OaiPmhXPath oaiPmhXPath;

  public XmlPathCache(String content) throws InvalidJsonException {
    this.content = content;
    oaiPmhXPath = new OaiPmhXPath(content);
  }

  private void set(String address, String jsonPath, Object jsonFragment, Class clazz) {
    List<T> instances = read(jsonPath, jsonFragment);
    cache.put(address, instances);
  }

  public List<T> read(String path, Object jsonFragment) {
    List<T> value = null;
    try {
      if (jsonFragment != null) {
        value = (List<T>) oaiPmhXPath.extractFieldInstanceList(jsonFragment, path);
      } else {
        value = (List<T>) oaiPmhXPath.extractFieldInstanceList(path);
      }
    } catch (PathNotFoundException e) {
      // LOGGER.severe("PathNotFound: " + jsonPath + " " + e.getLocalizedMessage() + extractRelevantPath(e));
    } catch (InvalidPathException e) {
      LOGGER.log(Level.SEVERE, "Invalid Path: {0} {1}\n{2}", new Object[]{
        path, e.getLocalizedMessage(), ExceptionUtils.extractRelevantPath(e)
      });
    }
    return value;
  }

  public List<T> get(String jsonPath) {
    return get(jsonPath, jsonPath, null, null);
  }

  // @Override
  // public Object get(String path, Class clazz) {
  //   return null;
  // }

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

  public Object getFragment(String path) {
    Object jsonFragment = null;
    if (!fragmentCache.containsKey(path)) {
      jsonFragment = oaiPmhXPath.extractNodes(path);
      fragmentCache.put(path, jsonFragment);
    } else {
      jsonFragment = fragmentCache.get(path);
    }
    return jsonFragment;
  }

  /**
   * Get a JSON fragment from cache.
   *
   * @param address - a unique address for cache
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
