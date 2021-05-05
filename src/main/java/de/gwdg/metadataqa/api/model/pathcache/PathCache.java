package de.gwdg.metadataqa.api.model.pathcache;

import de.gwdg.metadataqa.api.model.XmlFieldInstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface PathCache<T extends XmlFieldInstance> extends Serializable {

  Object read(String path, Object jsonFragment);

  List<T> get(String path);
  // <E> E get(String path, Class<E> clazz);
  List<T> get(String address, String path, Object jsonFragment);
  List<T> get(String address, String path, Object jsonFragment, Class clazz);

  Object getFragment(String path);
  Object getFragment(String address, String path, Object jsonFragment);

  String getRecordId();
  void setRecordId(String recordId);
  Map<String, List<T>> getCache();
  Map<String, Object> getFragmentCache();

  String getContent();
}
