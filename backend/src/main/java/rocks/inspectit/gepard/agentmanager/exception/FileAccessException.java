/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileAccessException extends RuntimeException {

  private final HttpStatus httpStatus;

  public FileAccessException(String message, Throwable cause, HttpStatus httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }
}
