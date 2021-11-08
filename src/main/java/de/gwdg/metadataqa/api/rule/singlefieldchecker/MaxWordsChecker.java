package de.gwdg.metadataqa.api.rule.singlefieldchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;

import java.util.List;
import java.util.StringTokenizer;

public class MaxWordsChecker extends SingleFieldChecker {

  private static final long serialVersionUID = 3259638493041988749L;
  public static final String PREFIX = "maxWords";
  protected Integer maxWords;

  public MaxWordsChecker(JsonBranch field, int maxWords) {
    this(field, field.getLabel(), maxWords);
  }

  public MaxWordsChecker(JsonBranch field, String header, int maxWords) {
    super(field, header + ":" + PREFIX);
    this.maxWords = maxWords;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results) {
    var allPassed = true;
    var isNA = true;
    List<XmlFieldInstance> instances = cache.get(field.getJsonPath());
    if (instances != null && !instances.isEmpty()) {
      for (XmlFieldInstance instance : instances) {
        if (instance.hasValue()) {

          isNA = false;
          if (countWords(instance.getValue()) > maxWords) {
            allPassed = false;
            break;
          }
        }
      }
    }
    results.put(getHeader(), new RuleCheckerOutput(this, isNA, allPassed));
  }

  private int countWords(String value) {
    return new StringTokenizer(value).countTokens();
  }
}
