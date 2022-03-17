package de.gwdg.metadataqa.api.model.pathcache;

import de.gwdg.metadataqa.api.json.JsonUtils;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.ExceptionUtils;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JsonProvider;
import net.minidev.json.JSONArray;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cache for reusing JSON paths
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T> the type of elements held in this object. It should be the
 *           extension of XmlFieldInstance class.
 */
public class JsonPathCache<T extends XmlFieldInstance> extends BasePathCache<T> {

  private static final Logger LOGGER = Logger.getLogger(
    JsonPathCache.class.getCanonicalName()
  );
  private static final long serialVersionUID = -7087854432160794878L;

  private final Object document;
  private static final JsonProvider JSON_PROVIDER = Configuration.defaultConfiguration().jsonProvider();

  public JsonPathCache(String content) throws InvalidJsonException {
    this.content = content;
    this.document = JSON_PROVIDER.parse(content);
  }

  public JsonPathCache(Object jsonDocument) {
    this.document = jsonDocument;
  }

  @Override
  protected void set(String address, String jsonPath, Object jsonFragment, Class clazz) {
    List<T> instances = null;
    Object value = read(jsonPath, jsonFragment);
    if (value != null) {
      if (clazz == null) {
        instances = (List<T>) JsonUtils.extractFieldInstanceList(value, recordId, jsonPath);
      } else {
        if (value instanceof JSONArray) {
          typedCache.put(address, clazz.cast(((JSONArray) value).get(0)));
        } else {
          typedCache.put(address, value);
        }
      }
    }
    cache.put(address, instances);
  }

  public Object read(String jsonPath, Object jsonFragment) {
    Object value = null;
    try {
      if (jsonFragment != null) {
        value = JsonPath.read(jsonFragment, jsonPath);
      } else {
        value = JsonPath.read(document, jsonPath);
      }
    } catch (PathNotFoundException e) {
      // LOGGER.severe("PathNotFound: " + jsonPath + " " + e.getLocalizedMessage() + extractRelevantPath(e));
    } catch (InvalidPathException e) {
      LOGGER.log(Level.SEVERE, "Invalid Path: {0} {1}\n{2}", new Object[]{
        jsonPath, e.getLocalizedMessage(), ExceptionUtils.extractRelevantPath(e)
      });
    }
    return value;
  }

  public Object getFragment(String jsonPath) {
    Object jsonFragment = null;
    if (!fragmentCache.containsKey(jsonPath)) {
      jsonFragment = read(jsonPath, null);
      fragmentCache.put(jsonPath, jsonFragment);
    } else {
      jsonFragment = fragmentCache.get(jsonPath);
    }
    return jsonFragment;
  }
}
