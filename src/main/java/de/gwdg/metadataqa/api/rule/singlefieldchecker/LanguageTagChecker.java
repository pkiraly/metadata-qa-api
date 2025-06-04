package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.configuration.schema.ApplicationScope;
import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.DataElement;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.selector.Selector;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

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
  public void update(Selector selector, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass() + " " + this.id + ", scope: " + scope);
    var allPassed = true;
    var isNA = true;
    int counter = 0;
    List<XmlFieldInstance> instances = selector.get(field);
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        isNA = false;
        if (instance.hasLanguage()) {
          counter++;
          if (isDebug())
            LOGGER.info("language tag: " + instance.getLanguage());
          if (scope.equals(ApplicationScope.anyOf)) {
            break;
          }
        } else if (scope.equals(ApplicationScope.allOf)) {
          allPassed = false;
          break;
        }
      }
    }

    if (!isNA && counter == 0) {
      allPassed = false;
    } else if (scope.equals(ApplicationScope.oneOf) && counter != 1) {
      allPassed = false;
    }

    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id + ") result: " + RuleCheckingOutputStatus.create(isNA, allPassed, isMandatory()));

  }

  public LanguageTagChecker withScope(ApplicationScope scope) {
    this.scope = scope;
    return this;
  }
}
