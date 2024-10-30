/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.validation;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Service for querying values against regex patterns.
 *
 * <p>Used for filtering entities based on regex patterns.
 */
@Service
@Validated
public class RegexQueryService {

  public static final String REGEX_INDICATOR = "regex:";

  /**
   * Checks if the given value matches the given pattern. If the pattern starts with "regex:", the
   * pattern is treated as a regex pattern. Otherwise, the pattern is treated as an exact match. If
   * the pattern or value is null, the method returns true, because this means: No filtering.
   *
   * @param value the value to match
   * @param pattern the pattern to match against
   * @return true if the value matches the pattern or is an exact match, false otherwise
   */
  public boolean matches(String value, String pattern) {

    if (pattern == null || value == null) {
      return true;
    }

    if (pattern.startsWith(REGEX_INDICATOR)) {
      String regexPattern = pattern.substring(REGEX_INDICATOR.length());
      return Pattern.compile(regexPattern).matcher(value).matches();
    } else {
      return value.equals(pattern);
    }
  }

  /**
   * Checks if the given Long matches the given pattern. If the pattern starts with "regex:", the
   * pattern is treated as a regex pattern. Otherwise, the pattern is treated as an exact match. If
   * the pattern or value is null, the method returns true, because this means: No filtering.
   *
   * @param value the value to match
   * @param pattern the pattern to match against
   * @return true if the value matches the pattern or is an exact match, false otherwise
   */
  public boolean matchesLong(Long value, String pattern) {
    if (pattern == null || value == null) {
      return true;
    }

    if (pattern.startsWith(REGEX_INDICATOR)) {
      String regexPattern = pattern.substring(REGEX_INDICATOR.length());
      return Pattern.compile(regexPattern).matcher(value.toString()).matches();
    } else {
      return value.toString().equals(pattern);
    }
  }

  /**
   * Checks if the given Instant matches the given pattern. If the pattern starts with "regex:", the
   * pattern is treated as a regex pattern. Otherwise, the pattern is treated as an exact match. If
   * the pattern or value is null, the method returns true, because this means: No filtering.
   *
   * @param value The value to match
   * @param pattern The pattern to match against
   * @return true if the value matches the pattern or is an exact match, false otherwise
   */
  public boolean matchesInstant(Instant value, String pattern) {
    if (pattern == null || value == null) {
      return true;
    }

    if (pattern.startsWith(REGEX_INDICATOR)) {
      String regexPattern = pattern.substring(REGEX_INDICATOR.length());
      // Convert Instant to ISO-8601 string for regex matching
      String instantString = value.truncatedTo(ChronoUnit.MILLIS).toString();
      return Pattern.compile(regexPattern).matcher(instantString).matches();
    } else {
      try {
        // For exact matching, parse the pattern as Instant and compare
        Instant patternInstant = Instant.parse(pattern);
        return value
            .truncatedTo(ChronoUnit.MILLIS)
            .equals(patternInstant.truncatedTo(ChronoUnit.MILLIS));
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException(
            "Invalid timestamp format. Expected ISO-8601 format (e.g., 2024-10-29T10:15:30Z)", e);
      }
    }
  }
}
