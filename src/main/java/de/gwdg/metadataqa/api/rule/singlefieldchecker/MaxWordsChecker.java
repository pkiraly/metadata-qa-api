package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class MaxWordsChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 3259638493041988749L;
  public static final String PREFIX = "maxWords";

  private static final Logger LOGGER = Logger.getLogger(MaxWordsChecker.class.getCanonicalName());

  protected Integer maxWords;

  public MaxWordsChecker(DataElement field, int maxWords) {
    this(field, field.getLabel(), maxWords);
  }

  public MaxWordsChecker(DataElement field, String header, int maxWords) {
    super(field, header + ":" + PREFIX);
    this.maxWords = maxWords;
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var isNA = true;
    boolean hasFailed = false;
    int passCount = 0;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {
          isNA = false;
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
          if (countWords(instance.getValue()) > maxWords) {
            hasFailed = true;
            if (scopeIsAllOf()) {
              break;
            }
          } else {
            passCount++;
          }
        }
      }
    }
    boolean allPassed = isPassed(passCount, hasFailed);
    addOutput(results, isNA, allPassed, outputType);
  }

  private int countWords(String value) {
    return new StringTokenizer(value).countTokens();
  }
}
