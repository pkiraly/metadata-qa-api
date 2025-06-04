package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultilingualChecker extends SingleFieldChecker {
  private static final long serialVersionUID = 3911175767180059821L;
  public static final String PREFIX = "multilingual";

  public MultilingualChecker(DataElement field) {
    this(field, field.getLabel());
  }

  public MultilingualChecker(DataElement field, String header) {
    super(field, header);
  }

  @Override
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass() + " " + this.id);
    var allPassed = true;
    var isNA = true;
    Set<String> languages = new HashSet<>();
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasLanguage()) {
          languages.add(instance.getLanguage());
          isNA = false;
        }
      }
    }
    if (languages.size() < 2)
      allPassed = false;

    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));
  }
}
