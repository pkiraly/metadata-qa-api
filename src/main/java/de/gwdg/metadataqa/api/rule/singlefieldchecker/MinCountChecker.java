package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutput;
import de.gwdg.metadataqa.api.util.InstanceCounter;

public class MinCountChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 2298498693779624776L;
  public static final String PREFIX = "minCount";
  protected Integer minCount;

  public MinCountChecker(JsonBranch field, Integer minCount) {
    this(field, field.getLabel(), minCount);
  }

  public MinCountChecker(JsonBranch field, String header, Integer minCount) {
    super(field, PREFIX + ":" + header);
    this.minCount = minCount;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckingOutput> results) {
    var allPassed = false;
    var counter = new InstanceCounter(cache, field);
    if (counter.getCount() >= minCount)
      allPassed = true;
    results.put(header, RuleCheckingOutput.create(counter.isNA(), allPassed));
  }
}
