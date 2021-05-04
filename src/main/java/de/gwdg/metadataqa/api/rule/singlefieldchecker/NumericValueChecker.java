package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;

import java.util.List;
import java.util.logging.Logger;

public class NumericValueChecker extends SingleFieldChecker {

  private static final Logger LOGGER = Logger.getLogger(NumericValueChecker.class.getCanonicalName());

  public enum TYPE {
    MIN_INCLUSIVE("minInclusive"),
    MAX_INCLUSIVE("maxInclusive"),
    MIN_EXCLUSIVE("minExclusive"),
    MAX_EXCLUSIVE("maxExclusive");

    private String prefix;

    TYPE(String prefix) {
      this.prefix = prefix;
    }
  }

  protected double limit;
  protected TYPE type;

  public NumericValueChecker(JsonBranch field, int limit, TYPE type) {
    this(field, (double) limit, type);
  }

  public NumericValueChecker(JsonBranch field, double limit, TYPE type) {
    super(field, type.prefix + ":" + field.getLabel());
    this.type = type;
    this.limit = limit;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    boolean allPassed = true;
    boolean isNA = true;
    List<XmlFieldInstance> instances = (List<XmlFieldInstance>) cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          String stringValue = instance.getValue();
          isNA = false;
          try {
            double value = Double.parseDouble(stringValue);
            allPassed = checkValue(value);
          } catch (NumberFormatException e) {
            allPassed = false;
          }
          if (!allPassed)
            break;
        }
      }
    }

    results.put(header, RuleCheckingOutput.create(isNA, allPassed));
  }

  private boolean checkValue(double value) {
    boolean allPassed = true;
    switch (type) {
      case MIN_INCLUSIVE: if (value < limit)  allPassed = false; break;
      case MAX_INCLUSIVE: if (value > limit)  allPassed = false; break;
      case MIN_EXCLUSIVE: if (value <= limit) allPassed = false; break;
      case MAX_EXCLUSIVE: if (value >= limit) allPassed = false; break;
      default: break;
    }
    return allPassed;
  }
}
