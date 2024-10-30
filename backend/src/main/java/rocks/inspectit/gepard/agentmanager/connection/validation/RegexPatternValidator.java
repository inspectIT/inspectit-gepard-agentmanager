/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.validation;

import static rocks.inspectit.gepard.agentmanager.connection.validation.RegexQueryService.REGEX_INDICATOR;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Validates that a given string is a valid regex pattern.
 *
 * <p>Used in conjunction with the {@link ValidRegexPattern} annotation.
 */
public class RegexPatternValidator implements ConstraintValidator<ValidRegexPattern, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    if (value.startsWith(REGEX_INDICATOR)) {
      try {
        Pattern.compile(value.substring(REGEX_INDICATOR.length()));
        return true;
      } catch (PatternSyntaxException e) {
        return false;
      }
    }
    return true;
  }
}
