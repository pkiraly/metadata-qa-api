package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.configuration.schema.ApplicationScope;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleChecker;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;
import de.gwdg.metadataqa.api.uniqueness.UniquenessExtractor;

import java.util.List;

public class LanguageTagChecker extends SingleFieldChecker {
  private static final long serialVersionUID = 7236047216814906713L;
  public static final String PREFIX = "languageTag";
  private ApplicationScope scope = ApplicationScope.anyOf;

  public LanguageTagChecker(DataElement field) {
    this(field, field.getLabel());
  }

  public LanguageTagChecker(DataElement field, String header) {
    super(field, header);
  }

  @Override
  public void update(Selector cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass() + " " + this.id);
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasLanguage()) {
          isNA = false;
          if (isDebug())
            LOGGER.info("language tag: " + instance.hasLanguage());
          break;
        }
      }
    }

    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed));

  }

  public RuleChecker withScope(ApplicationScope hasLanguageTag) {
    this.scope = scope;
    return this;
  }
}
