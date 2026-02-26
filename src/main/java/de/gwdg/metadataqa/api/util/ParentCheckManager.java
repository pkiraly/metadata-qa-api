package de.gwdg.metadataqa.api.util;

import java.util.ArrayList;
import java.util.List;

public class ParentCheckManager {
  Boolean naIfNoParent;
  boolean parentCheckHappened = false;
  // Boolean parentCheckPassed;
  List<Boolean> statuses = new ArrayList<>();

  public ParentCheckManager(boolean naIfNoParent) {
    this.naIfNoParent = naIfNoParent;
  }

  public Boolean getNaIfNoParent() {
    return naIfNoParent;
  }

  public Boolean getParentCheckPassed() {
    return hasParent(); //parentCheckPassed;
  }

  public void add(boolean dependenciesPassed) {
    statuses.add(dependenciesPassed);
    parentCheckHappened = true;
  }

  public boolean hasParent() {
    return statuses.contains(true);
  }

  @Override
  public String toString() {
    return "ParentCheckManager{" +
      "naIfNoParent=" + naIfNoParent +
      ", parentCheckHappened=" + parentCheckHappened +
      ", parentCheckPassed=" + getParentCheckPassed() +
      ", statuses=" + statuses +
      '}';
  }
}
