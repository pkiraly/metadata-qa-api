package de.gwdg.metadataqa.api.model.pathcache;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T> the type of elements held in this object. It should be the
 *           extension of XmlFieldInstance class.
 */
public class CsvPathCache<T extends XmlFieldInstance> implements PathCache {

  private static final Logger LOGGER = Logger.getLogger(
    CsvPathCache.class.getCanonicalName()
  );

  private String content;
  private String recordId;
  private final Map<String, List<T>> cache = new HashMap<>();
  private final Map<String, Object> typedCache = new HashMap<>();
  private final Map<String, Object> fragmentCache = new HashMap<>();
  private Map<String, String> record;

  public CsvPathCache(String content) throws InvalidJsonException {
    this.content = content;
  }

  public CsvPathCache(Object jsonDocument) {
    // this.parsedDocument = jsonDocument;
  }

  public CsvPathCache(CsvReader csvReader, List<String> input) {
    record = csvReader.createMap(input);
  }

  private void set(String address, String jsonPath, Object jsonFragment, Class clazz) {
    List<T> instances = read(jsonPath, jsonFragment);
    cache.put(address, instances);
  }

  public List<T> read(String path, Object jsonFragment) {
    List<T> list = null;
    if (record.containsKey(path) && StringUtils.isNotBlank(record.get(path)))
      list = (List<T>) Arrays.asList(new XmlFieldInstance(record.get(path)));
    // else
    //   LOGGER.severe("PathNotFound: " + path);

    return list;
  }

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

  public Object getFragment(String path) {
    return content;
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
    return content;
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

  public void setCsvReader(CsvReader csvReader) {
    try {
      record = csvReader.asMap(content);
    } catch (IOException e) {
      LOGGER.warning(e.getLocalizedMessage());
    }
  }
}
