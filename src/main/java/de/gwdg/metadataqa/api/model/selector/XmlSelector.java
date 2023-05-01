package de.gwdg.metadataqa.api.model.selector;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.PathNotFoundException;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.ExceptionUtils;
import de.gwdg.metadataqa.api.xml.XPathWrapper;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cache for reusing XPath expressions
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T> the type of elements held in this object. It should be the
 *           extension of XmlFieldInstance class.
 */
public class XmlSelector<T extends XmlFieldInstance> extends BaseSelector<T> {

  private static final Logger LOGGER = Logger.getLogger(
    XmlSelector.class.getCanonicalName()
  );
  private static final long serialVersionUID = 3351744750302199667L;

  XPathWrapper xPathWrapper;

  public XmlSelector(String content) throws InvalidJsonException {
    this.content = content;
    xPathWrapper = new XPathWrapper(content);
  }

  protected void set(String address, String path, Object jsonFragment, Class clazz) {
    List<T> instances = read(path, jsonFragment);
    cache.put(address, instances);
  }

  public List<T> read(String path, Object jsonFragment) {
    List<T> value = null;
    try {
      if (jsonFragment != null) {
        value = (List<T>) xPathWrapper.extractFieldInstanceList(jsonFragment, path);
      } else {
        value = (List<T>) xPathWrapper.extractFieldInstanceList(path);
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

  public Object getFragment(String path) {
    Object jsonFragment = null;
    if (!fragmentCache.containsKey(path)) {
      jsonFragment = xPathWrapper.extractNodes(path);
      fragmentCache.put(path, jsonFragment);
    } else {
      jsonFragment = fragmentCache.get(path);
    }
    return jsonFragment;
  }
}
