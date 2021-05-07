package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.util.StringDuplicationDetector;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class DuplicatedStrings extends ProblemDetector implements Serializable {

  private static final String NAME = "DuplicatedStrings";
  private static final long serialVersionUID = -4765488439405459205L;

  public DuplicatedStrings(ProblemCatalog problemCatalog) {
    this.problemCatalog = problemCatalog;
    this.problemCatalog.addObserver(this);
    this.schema = problemCatalog.getSchema();
  }

  @Override
  public void update(PathCache cache, FieldCounter<Double> results) {
    System.err.println("DuplicatedStrings::update::" + schema.getEmptyStringPaths().size());
    double value = 0;
    for (String path : schema.getEmptyStringPaths()) {
      System.err.println(path);
      List<EdmFieldInstance> subjects = cache.get(path);
      if (subjects != null && !subjects.isEmpty()) {
        for (EdmFieldInstance subject : subjects) {
          if (StringUtils.isNotBlank(subject.getValue())) {
            if (StringDuplicationDetector.isDuplicated(subject.getValue())) {
              value += 1;
            }
          }
        }
      }
    }
    results.put(NAME, value);
  }

  @Override
  public String getHeader() {
    return NAME;
  }
}
