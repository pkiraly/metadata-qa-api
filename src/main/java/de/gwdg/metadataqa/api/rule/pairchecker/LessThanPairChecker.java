package de.gwdg.metadataqa.api.rule.pairchecker;

import de.gwdg.metadataqa.api.counter.FieldCounter;
import de.gwdg.metadataqa.api.json.JsonBranch;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.model.pathcache.PathCache;
import de.gwdg.metadataqa.api.rule.RuleCheckerOutput;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputStatus;
import de.gwdg.metadataqa.api.rule.RuleCheckingOutputType;

import java.util.List;
import java.util.regex.Pattern;

public class LessThanPairChecker extends PropertyPairChecker {

  private static final long serialVersionUID = 2271685635591166409L;

  private final TYPE type;

  public enum TYPE {
    LESS_THAN("lessThan"),
    LESS_THAN_OR_EQUALS("lessThanOrEquals")
    ;

    private final String prefix;

    TYPE(String prefix) {
      this.prefix = prefix;
    }

    public String getPrefix() {
      return prefix;
    }
  }
  private static final Pattern isNumericPattern = Pattern.compile("^\\d+(\\.\\d+)?$");

  public LessThanPairChecker(JsonBranch field1, JsonBranch field2, TYPE type) {
    super(field1, field2, type.prefix);
    this.type = type;
  }

  @Override
  public void update(PathCache cache, FieldCounter<RuleCheckerOutput> results, RuleCheckingOutputType outputType) {
    if (isDebug())
      LOGGER.info(this.getClass().getSimpleName() + " " + this.id);

    var allPassed = true;
    var isNA = false;
    List<XmlFieldInstance> instances1 = cache.get(field1.getAbsoluteJsonPath().replace("[*]", ""));
    List<XmlFieldInstance> instances2 = cache.get(field2.getAbsoluteJsonPath().replace("[*]", ""));
    if (instances1 != null && !instances1.isEmpty() && instances2 != null && !instances2.isEmpty()) {
      for (XmlFieldInstance instance1 : instances1) {
        if (instance1.hasValue()) {
          for (XmlFieldInstance instance2 : instances2) {
            if (instance2.hasValue()) {
              isNA = false;
              allPassed = checkValues(instance1.getValue(), instance2.getValue());
              if (!allPassed)
                break;
            }
          }
        }
      }
    }
    addOutput(results, isNA, allPassed, outputType);
    if (isDebug())
      LOGGER.info("result: " + RuleCheckingOutputStatus.create(isNA, allPassed));
  }

  private boolean checkValues(String value1, String value2) {
    var allPassed = false;
    if (type == TYPE.LESS_THAN) {
      allPassed = lessThan(value1, value2);
    } else if (type == TYPE.LESS_THAN_OR_EQUALS) {
      allPassed = lessThanOrEquals(value1, value2);
    }
    return allPassed;
  }

  public static boolean lessThan(String value1, String value2) {
    if (isNumeric(value1) && isNumeric(value2))
      return Double.parseDouble(value1) < Double.parseDouble(value2);
    return value1.compareTo(value2) < 0;
  }

  public static boolean lessThanOrEquals(String value1, String value2) {
    if (isNumeric(value1) && isNumeric(value2))
      return Double.parseDouble(value1) <= Double.parseDouble(value2);
    return value1.compareTo(value2) <= 0;
  }

  public static boolean isNumeric(String value) {
    return isNumericPattern.matcher(value).matches();
  }
}
