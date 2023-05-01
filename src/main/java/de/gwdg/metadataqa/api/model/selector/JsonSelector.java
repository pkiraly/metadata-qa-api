package de.gwdg.metadataqa.api.model.selector;

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
public class JsonSelector<T extends XmlFieldInstance> extends BaseSelector<T> {

  private static final Logger LOGGER = Logger.getLogger(
    JsonSelector.class.getCanonicalName()
  );
  private static final long serialVersionUID = -7087854432160794878L;

  private final Object document;
  private static final JsonProvider JSON_PROVIDER = Configuration.defaultConfiguration().jsonProvider();

  public JsonSelector(String content) throws InvalidJsonException {
    this.content = content;
    this.document = JSON_PROVIDER.parse(content);
  }

  public JsonSelector(Object jsonDocument) {
    this.document = jsonDocument;
  }

  @Override
  protected void set(String address, String path, Object jsonFragment, Class clazz) {
    List<T> instances = null;
    Object value = read(path, jsonFragment);
    if (value != null) {
      if (clazz == null) {
        instances = (List<T>) JsonUtils.extractFieldInstanceList(value, recordId, path);
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

  public Object read(String path, Object jsonFragment) {
    Object value = null;
    try {
      if (jsonFragment != null) {
        value = JsonPath.read(jsonFragment, path);
      } else {
        value = JsonPath.read(document, path);
      }
    } catch (PathNotFoundException e) {
      // LOGGER.severe("PathNotFound: " + path + " " + e.getLocalizedMessage() + extractRelevantPath(e));
    } catch (InvalidPathException e) {
      LOGGER.log(Level.SEVERE, "Invalid Path: {0} {1}\n{2}", new Object[]{
        path, e.getLocalizedMessage(), ExceptionUtils.extractRelevantPath(e)
      });
    }
    return value;
  }

  public Object getFragment(String path) {
    Object jsonFragment = null;
    if (!fragmentCache.containsKey(path)) {
      jsonFragment = read(path, null);
      fragmentCache.put(path, jsonFragment);
    } else {
      jsonFragment = fragmentCache.get(path);
    }
    return jsonFragment;
  }
}
