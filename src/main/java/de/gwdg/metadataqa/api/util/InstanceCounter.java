package de.gwdg.metadataqa.api.util;

import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;

import java.util.List;

public class InstanceCounter {
  boolean isNA = true;
  boolean ignoreNA = false;
  int count = 0;
  private final Selector selector;
  private final DataElement field;
  private boolean allowEmptyInstances = true;

  public InstanceCounter(Selector selector, DataElement field) {
    this.selector = selector;
    this.field = field;
    count();
  }

  public InstanceCounter(Selector selector, DataElement field, boolean allowEmptyInstances) {
    this.selector = selector;
    this.field = field;
    this.allowEmptyInstances = allowEmptyInstances;
    count();
  }

  private void count() {
    List<XmlFieldInstance> instances = selector.get(field);
    // System.err.println(instances.size());
    if (instances != null && !instances.isEmpty())
      for (XmlFieldInstance instance : instances)
        if (allowEmptyInstances || instance.hasValue()) {
          count++;
          isNA = false;
        }
  }

  public boolean isNA() {
    if (ignoreNA)
      return false;
    return isNA;
  }

  public int getCount() {
    return count;
  }

  public void ignoreNA() {
    this.ignoreNA = true;
  }

  @Override
  public String toString() {
    return "InstanceCounter{" +
      "isNA=" + isNA +
      ", count=" + count +
      '}';
  }
}
