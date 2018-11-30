package de.gwdg.metadataqa.api.util;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class StringDuplicationDetector {

	private static boolean isLineBreakInTheMiddle(String middlePart) {
		return middlePart.substring(1, 2).matches("\n");
	}

	private static boolean isCommaInTheMiddle(String middlePart) {
		return middlePart.substring(1, 2).matches("[,;]");
	}

	private static boolean isSpaceCommaSpace(String middlePart) {
		return middlePart.matches(" [,;] ");
	}

	public static boolean isDuplicated(String input) {
		int len = input.length();
		if (len % 2 == 0) {
			int half = len / 2;
			if (input.substring(0, half).equals(input.substring(half))) {
				return true;
			}
			if (input.substring(half, half + 1).equals(" ")
			    && input.substring(half - 1, half).matches("[;,]")) {
				return input.substring(0, half - 1).equals(input.substring(half + 1));
			}
		} else {
			int half = len / 2;
			String middlePart = input.substring(half - 1, half + 2);
			if (isSpaceCommaSpace(middlePart)) {
				return input.substring(0, half - 1).equals(input.substring(half + 2));
			} else if (isCommaInTheMiddle(middlePart)
					  || isLineBreakInTheMiddle(middlePart)) {
				return input.substring(0, half).equals(input.substring(half + 1));
			}
		}
		return false;
	}
}
