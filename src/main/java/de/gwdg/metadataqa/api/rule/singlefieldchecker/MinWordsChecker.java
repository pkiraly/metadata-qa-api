package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;
import java.util.StringTokenizer;

public class MinWordsChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 3259638493041988749L;
  public static final String PREFIX = "minWords";
  protected Integer minWords;

  public MinWordsChecker(DataElement field, int minWords) {
    this(field, field.getLabel(), minWords);
  }

  public MinWordsChecker(DataElement field, String header, int minWords) {
    super(field, header + ":" + PREFIX);
    this.minWords = minWords;
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {

          isNA = false;
          if (isDebug())
            LOGGER.info("value: " + instance.getValue());
          LOGGER.info("countWords: " + countWords(instance.getValue()));
          if (countWords(instance.getValue()) < minWords) {
            allPassed = false;
            break;
          }
        }
      }
    }
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }

  private int countWords(String value) {
    return new StringTokenizer(value).countTokens();
  }
}
