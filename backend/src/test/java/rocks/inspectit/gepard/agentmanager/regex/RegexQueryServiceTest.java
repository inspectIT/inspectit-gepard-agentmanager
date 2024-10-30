/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.regex;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rocks.inspectit.gepard.agentmanager.connection.validation.RegexQueryService;

class RegexQueryServiceTest {

  private RegexQueryService service;

  @BeforeEach
  void setUp() {
    service = new RegexQueryService();
  }

  @Nested
  class Matches {

    @Test
    void matchesReturnsTrueIfPatternIsNullAndValueIsNotNull() {
      assertTrue(service.matches(null, "value"));
    }

    @Test
    void matchesReturnsTrueIfPatternIsNotNullAndValueIsNull() {
      assertTrue(service.matches("pattern", null));
    }

    @Test
    void matchesReturnsTrueIfPatternAndValueAreNull() {
      assertTrue(service.matches(null, null));
    }

    @Test
    void matchesReturnsTrueIfPatternAndValueAreEmpty() {
      assertTrue(service.matches("", ""));
    }

    @Test
    void matchesReturnsTrueIfPatternAndValueAreEqual() {
      assertTrue(service.matches("pattern", "pattern"));
    }

    @Test
    void matchesReturnsTrueIfPatternMatchesValue() {
      assertTrue(service.matches("example-service", "regex:.*-service"));
    }

    @Test
    void matchesReturnsFalseIfPatternDoesNotMatchValue() {
      assertFalse(service.matches("example-server", "regex:.*-service"));
    }
  }

  @Nested
  class MatchesLong {
    @Test
    void matchesLongReturnsTrueIfPatternIsNullAndValueIsNotNull() {
      assertTrue(service.matchesLong(null, "1L"));
    }

    @Test
    void matchesLongReturnsTrueIfPatternIsNotNullAndValueIsNull() {
      assertTrue(service.matchesLong(1L, null));
    }

    @Test
    void matchesLongReturnsTrueIfPatternAndValueAreNull() {
      assertTrue(service.matchesLong(null, null));
    }

    @Test
    void matchesLongReturnsTrueIfPatternAndValueAreEqual() {
      assertTrue(service.matchesLong(1L, "1"));
    }

    @Test
    void matchesLongReturnsTrueIfPatternMatchesValue() {
      assertTrue(service.matchesLong(123L, "regex:12.*"));
    }

    @Test
    void matchesLongReturnsFalseIfPatternDoesNotMatchValue() {
      assertFalse(service.matchesLong(123L, "regex:45.*"));
    }
  }

  @Nested
  class MatchesInstant {
    @Test
    void matchesInstantReturnsTrueIfPatternIsNullAndValueIsNotNull() {
      assertTrue(service.matchesInstant(null, "2024-01-01T00:00:00Z"));
    }

    @Test
    void matchesInstantReturnsTrueIfPatternIsNotNullAndValueIsNull() {
      assertTrue(service.matchesInstant(Instant.parse("2024-01-01T00:00:00Z"), null));
    }

    @Test
    void matchesInstantReturnsTrueIfPatternAndValueAreNull() {
      assertTrue(service.matchesInstant(null, null));
    }

    @Test
    void matchesInstantReturnsTrueIfPatternAndValueAreEqual() {
      assertTrue(
          service.matchesInstant(Instant.parse("2024-01-01T00:00:00Z"), "2024-01-01T00:00:00Z"));
    }

    @Test
    void matchesInstantReturnsTrueIfPatternMatchesValue() {
      assertTrue(
          service.matchesInstant(Instant.parse("2024-01-01T00:00:00Z"), "regex:2024-01-01T.*"));
    }

    @Test
    void matchesInstantReturnsFalseIfPatternDoesNotMatchValue() {
      assertFalse(
          service.matchesInstant(Instant.parse("2024-01-01T00:00:00Z"), "regex:2025-01-01T.*"));
    }

    @Test
    void matchesInstantThrowsIllegalArgumentExceptionIfPatternIsInvalid() {
      assertThrows(
          IllegalArgumentException.class,
          () -> service.matchesInstant(Instant.parse("2024-01-01T00:00:00Z"), "invalid-pattern"));
    }
  }
}
