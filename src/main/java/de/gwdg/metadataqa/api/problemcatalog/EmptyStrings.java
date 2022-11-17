package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import java.io.Serializable;
import java.util.List;

import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import org.apache.commons.lang3.StringUtils;

/**
 * Empty string detector
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EmptyStrings extends ProblemDetector implements Serializable {

  private static final String NAME = "EmptyStrings";
  private static final long serialVersionUID = 2943902241047693938L;

  public EmptyStrings(ProblemCatalog problemCatalog) {
    this.problemCatalog = problemCatalog;
    this.problemCatalog.addObserver(this);
    this.schema = problemCatalog.getSchema();
  }

  @Override
  public void update(PathCache cache, FieldCounter<Double> results) {
    double value = 0;
    for (String path : schema.getEmptyStringPaths()) {
      List<EdmFieldInstance> subjects = cache.get(path);
      if (subjects != null && !subjects.isEmpty())
        for (EdmFieldInstance subject : subjects)
          if (StringUtils.isBlank(subject.getValue()))
            value += 1;
    }
    results.put(NAME, value);
  }

  @Override
  public String getHeader() {
    return NAME;
  }

}
