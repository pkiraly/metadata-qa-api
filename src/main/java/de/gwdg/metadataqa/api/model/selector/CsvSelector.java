package de.gwdg.metadataqa.api.model.selector;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.util.CsvReader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A cache for reusing CSV paths (column names)
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T> the type of elements held in this object. It should be the
 *           extension of XmlFieldInstance class.
 */
public class CsvSelector<T extends XmlFieldInstance> extends BaseSelector<T> {

  private static final Logger LOGGER = Logger.getLogger(
    CsvSelector.class.getCanonicalName()
  );
  private static final long serialVersionUID = -545628995288633641L;

  private Map<String, String> record;

  public CsvSelector(String content) throws InvalidJsonException {
    this.content = content;
  }

  public CsvSelector(Object jsonDocument) {
    // this.parsedDocument = jsonDocument;
  }

  public CsvSelector(CsvReader csvReader, List<String> input) {
    record = csvReader.createMap(input);
  }

  @Override
  protected void set(String address, String path, Object jsonFragment, Class clazz) {
    List<T> instances = read(path, jsonFragment);
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

  public Object getFragment(String path) {
    return content;
  }

  /**
   * Get a JSON fragment from cache.
   *
   * @param address - a unique address for cache
   * @param path - a JSON path expression
   * @param jsonFragment - a JSON fragment in which the path should be searched for
   *
   * @return
   *   The JSON fragment
   */
  public Object getFragment(String address, String path, Object jsonFragment) {
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
