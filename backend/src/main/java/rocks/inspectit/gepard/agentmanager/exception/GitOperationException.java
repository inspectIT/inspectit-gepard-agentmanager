/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.exception;

public class GitOperationException extends RuntimeException {
  public GitOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
