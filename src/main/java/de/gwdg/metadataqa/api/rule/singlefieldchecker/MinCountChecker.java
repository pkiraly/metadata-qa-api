package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.InstanceCounter;

public class MinCountChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 2298498693779624776L;
  public static final String PREFIX = "minCount";
  protected Integer minCount;

  public MinCountChecker(JsonBranch field, Integer minCount) {
    this(field, field.getLabel(), minCount);
  }

  public MinCountChecker(JsonBranch field, String header, Integer minCount) {
    super(field, header + ":" + PREFIX);
    this.minCount = minCount;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = false;
    var counter = new InstanceCounter(cache, field);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") value: " + counter.getCount());
    if (counter.getCount() >= minCount)
      allPassed = true;

    addOutput(results, counter.isNA(), allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(counter.isNA(), allPassed));
  }
}
