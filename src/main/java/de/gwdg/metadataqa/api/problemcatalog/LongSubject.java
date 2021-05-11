package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.model.EdmFieldInstance;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import org.apache.commons.lang3.StringUtils;

/**
 * Detect long subjects.
 *
 * Long means larger than 50 characters. See for example:
 * http://www.europeana.eu/portal/record/07602/5CFC6E149961A1630BAD5C65CE3A683DEB6285A0.json
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class LongSubject extends ProblemDetector implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(
      LongSubject.class.getCanonicalName()
  );

  private static final String NAME = "LongSubject";
  private static final int MAX_LENGTH = 50;
  private static final long serialVersionUID = 4666546113157987333L;

  public LongSubject(ProblemCatalog problemCatalog) {
    this.problemCatalog = problemCatalog;
    this.problemCatalog.addObserver(this);
    this.schema = problemCatalog.getSchema();
  }

  @Override
  public void update(PathCache cache, FieldCounter<Double> results) {
    double value = 0;
    List<EdmFieldInstance> subjects = cache.get(schema.getSubjectPath());
    if (subjects != null && !subjects.isEmpty())
      for (EdmFieldInstance subject : subjects)
        if (StringUtils.isNotBlank(subject.getValue())
            && subject.getValue().length() > MAX_LENGTH)
          value += 1;
    results.put(NAME, value);
  }

  @Override
  public String getHeader() {
    return NAME;
  }
}
