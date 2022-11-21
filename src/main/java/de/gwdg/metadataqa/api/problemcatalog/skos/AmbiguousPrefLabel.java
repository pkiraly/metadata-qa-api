package de.gwdg.metadataqa.api.problemcatalog.skos;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.problemcatalog.ProblemCatalog;
import de.gwdg.metadataqa.api.problemcatalog.ProblemDetector;
import de.gwdg.metadataqa.api.schema.Schema;
import de.gwdg.metadataqa.api.util.Converter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Checking ambigous PrefLabel
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class AmbiguousPrefLabel extends ProblemDetector implements Serializable {

  private static final String NAME = "AmbiguousPrefLabel";
  private static final List<String> LABELS = Arrays.asList(
    "Agent/skos:prefLabel",
    "Concept/skos:prefLabel",
    "Place/skos:prefLabel",
    "Timespan/skos:prefLabel"
  );
  private static final long serialVersionUID = 7644339329704804931L;

  public AmbiguousPrefLabel(ProblemCatalog problemCatalog) {
    this.problemCatalog = problemCatalog;
    this.problemCatalog.addObserver(this);
    this.schema = problemCatalog.getSchema();
  }

  @Override
  public void update(PathCache cache, FieldCounter<Double> results) {
    var value = 0;
    for (String label : LABELS) {
      DataElement branch = ((Schema) schema).getPathByLabel(label);
      String parentPath = branch.getParent().getPath();
      Object rawEntityFragment = cache.getFragment(parentPath);
      if (rawEntityFragment != null) {
        List<Object> entities = Converter.jsonObjectToList(rawEntityFragment, (Schema) schema);
        for (var i = 0; i < entities.size(); i++) {
          value += countPerEntity(i, branch, cache);
        }
      }
    }
    results.put(NAME, (double) value);
  }

  private int countPerEntity(int entityCounter, DataElement branch, PathCache cache) {
    List<EdmFieldInstance> subjects = cache.get(branch.getAbsolutePath(entityCounter));
    Map<String, Integer> labelCounter = countLabelsPerFields(subjects);
    return countAmbiguousPrefLabels(labelCounter);
  }

  private Map<String, Integer> countLabelsPerFields(List<EdmFieldInstance> subjects) {
    Map<String, Integer> labelCounter = new HashMap<>();
    for (EdmFieldInstance subject : subjects) {
      if (subject.getLanguage() != null) {
        labelCounter.computeIfAbsent(subject.getLanguage(), s -> 0);
        labelCounter.put(subject.getLanguage(), labelCounter.get(subject.getLanguage()) + 1);
      }
    }
    return labelCounter;
  }

  private int countAmbiguousPrefLabels(Map<String, Integer> labelCounter) {
    var value = 0;

    for (Map.Entry<String, Integer> entry : labelCounter.entrySet()) {
      if (entry.getValue() > 1) {
        value++;
      }
    }

    return value;
  }

  @Override
  public String getHeader() {
    return NAME;
  }

}
