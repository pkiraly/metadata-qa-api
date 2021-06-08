package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import de.gwdg.metadataqa.api.util.InstanceCounter;

public class MaxCountChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 3259638493041988749L;
  public static final String PREFIX = "maxCount";
  protected Integer maxCount;

  public MaxCountChecker(JsonBranch field, int maxCount) {
    this(field, field.getLabel(), maxCount);
  }

  public MaxCountChecker(JsonBranch field, String header, int maxCount) {
    super(field, PREFIX + ":" + header);
    this.maxCount = maxCount;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    var allPassed = false;
    var counter = new InstanceCounter(cache, field);
    if (counter.getCount() <= maxCount)
      allPassed = true;
    results.put(header, RuleCheckingOutput.create(counter.isNA(), allPassed));
  }
}
