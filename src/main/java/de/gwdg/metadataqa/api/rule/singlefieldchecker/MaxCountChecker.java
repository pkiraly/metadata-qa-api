package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.InstanceCounter;

public class MaxCountChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 3259638493041988749L;
  public static final String PREFIX = "maxCount";
  private boolean allowEmptyInstances = false;
  protected Integer maxCount;

  public MaxCountChecker(DataElement field, int maxCount) {
    this(field, field.getLabel(), maxCount);
  }

  public MaxCountChecker(DataElement field, int maxCount, boolean allowEmptyInstances) {
    this(field, field.getLabel(), maxCount);
    this.allowEmptyInstances = allowEmptyInstances;
  }


  public MaxCountChecker(DataElement field, String header, int maxCount) {
    super(field, header + ":" + PREFIX);
    this.maxCount = maxCount;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = false;
    var counter = new InstanceCounter(cache, field, allowEmptyInstances);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") value: " + counter.getCount());
    if (counter.getCount() <= maxCount)
      allPassed = true;

    addOutput(results, counter.isNA(), allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(counter.isNA(), allPassed));
  }
}
