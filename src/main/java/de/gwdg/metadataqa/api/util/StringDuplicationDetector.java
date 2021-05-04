package de.gwdg.metadataqa.api.util;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public final class StringDuplicationDetector {

  private StringDuplicationDetector() {
  }

  /**
   * Checks if is there a line break in a string.
   *
   * @param middlePart The middle part of the input string.
   * @return
   *   True if there is a line break inside.
   */
  private static boolean isLineBreakInTheMiddle(final String middlePart) {
    return middlePart.substring(1, 2).matches("\n");
  }

  /**
   * Checks is the string has a comma.
   *
   * @param middlePart The middle part of the input string.
   * @return
   *   True if there is a comma inside.
   */
  private static boolean isCommaInTheMiddle(final String middlePart) {
    return middlePart.substring(1, 2).matches("[,;]");
  }

  /**
   * Checks if the string has space - comma - space sequence.
   *
   * @param middlePart The middle part of the input string.
   * @return
   *   True if there is a space - comma - space inside.
   */
  private static boolean isSpaceCommaSpace(final String middlePart) {
    return middlePart.matches(" [,;] ");
  }

  /**
   * Checks if the string is a duplication is a substring.
   *
   * @param input The input string
   * @return
   *   True is the string is a duplication.
   */
  public static boolean isDuplicated(final String input) {
    var len = input.length();
    if (isEven(len)) {
      var half = len / 2;
      if (hasEqualParts(input, half, half)) {
        return true;
      }
      if (input.substring(half, half + 1).equals(" ")
          && input.substring(half - 1, half).matches("[;,]")) {
        return hasEqualParts(input, half - 1, half + 1);
      }
    } else {
      var half = len / 2;
      var middlePart = input.substring(half - 1, half + 2);
      if (isSpaceCommaSpace(middlePart)) {
        return hasEqualParts(input, half - 1, half + 2);
      } else if (isCommaInTheMiddle(middlePart)
                 || isLineBreakInTheMiddle(middlePart)) {
        return hasEqualParts(input, half, half + 1);
      }
    }
    return false;
  }

  private static boolean isEven(int len) {
    return len % 2 == 0;
  }

  /**
   * Checks if a string's beginning and end are equal.
   *
   * @param input The input string
   * @param beginTo The beginning string ends at this point.
   * @param endFrom The ending string starts from this point.
   * @return
   *   True if the two parts are equals, otherwise false.
   */
  private static boolean hasEqualParts(final String input,
                                       final int beginTo,
                                       final int endFrom) {
    var begin = input.substring(0, beginTo);
    var end = input.substring(endFrom);
    return begin.equals(end);
  }
}
