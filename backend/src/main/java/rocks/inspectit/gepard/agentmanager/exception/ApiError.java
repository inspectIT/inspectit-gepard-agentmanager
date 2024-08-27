/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.exception;

import java.time.LocalDateTime;
import java.util.List;

/** Represents an error that occurred during an API request. */
public record ApiError(String path, List<String> errors, int statusCode, LocalDateTime timestamp) {}
