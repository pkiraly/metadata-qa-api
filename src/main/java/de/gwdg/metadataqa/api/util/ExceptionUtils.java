package de.gwdg.metadataqa.api.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility method for exceptions
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public final class ExceptionUtils {

  private ExceptionUtils() {
  }

  public static String extractRelevantPath(Exception e) {
    List<String> relevantPaths = new ArrayList<>();
    for (StackTraceElement element : e.getStackTrace()) {
      if (element.getClassName().startsWith("de.gwdg")) {
        relevantPaths.add(element.toString());
      }
    }
    return StringUtils.join(relevantPaths, "\n");
  }

}
