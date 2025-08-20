package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.util.InstanceCounter;

import java.util.logging.Logger;

public class MinCountChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 2298498693779624776L;
  public static final String PREFIX = "minCount";

  private static final Logger LOGGER = Logger.getLogger(MinCountChecker.class.getCanonicalName());

  protected Integer minCount;
  protected boolean allowEmptyInstances = false;

  public MinCountChecker(DataElement field, Integer minCount) {
    this(field, field.getLabel(), minCount);
  }

  public MinCountChecker(DataElement field, Integer minCount, boolean allowEmptyInstances) {
    this(field, field.getLabel(), minCount);
    this.allowEmptyInstances = allowEmptyInstances;
  }

  public MinCountChecker(DataElement field, String header, Integer minCount) {
    super(field, header + ":" + PREFIX);
    this.minCount = minCount;
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    // System.err.println(selector);
    var allPassed = false;
    var counter = new InstanceCounter(selector, field, allowEmptyInstances);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") number of instances: " + counter.getCount());
    if (counter.getCount() >= minCount)
      allPassed = true;
    else if (counter.isNA() && minCount > 0)
      counter.ignoreNA();

    addOutput(results, counter.isNA(), allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(counter.isNA(), allPassed, isMandatory()));
  }

  public void setAllowEmptyInstances(boolean allowEmptyInstances) {
    this.allowEmptyInstances = allowEmptyInstances;
  }

  public Integer getMinCount() {
    return minCount;
  }

  public boolean isEmptyInstancesAllowed() {
    return allowEmptyInstances;
  }
}
