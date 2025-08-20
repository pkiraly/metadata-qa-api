package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.InstanceCounter;

import java.util.logging.Logger;

public class MaxCountChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 3259638493041988749L;
  public static final String PREFIX = "maxCount";

  private static final Logger LOGGER = Logger.getLogger(MaxCountChecker.class.getCanonicalName());

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
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = false;
    var counter = new InstanceCounter(selector, field, allowEmptyInstances);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") value: " + counter.getCount());
    if (counter.getCount() <= maxCount)
      allPassed = true;

    if (counter.isNA() && maxCount == 0)
      counter.ignoreNA();

    addOutput(results, counter.isNA(), allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(counter.isNA(), allPassed, isMandatory()));
  }
}
