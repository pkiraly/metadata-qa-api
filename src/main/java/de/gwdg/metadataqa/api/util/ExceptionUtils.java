package de.gwdg.metadataqa.api.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class ExceptionUtils {

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
