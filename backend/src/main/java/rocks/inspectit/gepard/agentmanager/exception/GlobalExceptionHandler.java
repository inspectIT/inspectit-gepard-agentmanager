/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.PatternSyntaxException;
import org.eclipse.jgit.errors.InvalidPatternException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Global exception handler for the application. */
@ControllerAdvice
public class GlobalExceptionHandler {

  /** Handles MethodArgumentNotValidException (e.g. if a NotNull-Field isn´t present). */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationErrors(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

    ApiError apiError =
        new ApiError(
            request.getRequestURI(), errors, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  /** Handles HttpMessageNotReadableException (e.g. if no request body is provided). */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleBadRequestError(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles MethodArgumentTypeMismatchException (e.g. if a path variable is not of the correct
   * type).
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  /** Handles NoSuchElementException (e.g. if an entity is not found). */
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiError> handleNotFoundError(
      NoSuchElementException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  /** Handles FileAccessException (e.g. if a file could not be read). */
  @ExceptionHandler(FileAccessException.class)
  public ResponseEntity<ApiError> handleFileAccessException(
      FileAccessException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            ex.getHttpStatus().value(),
            LocalDateTime.now());
    return new ResponseEntity<>(apiError, ex.getHttpStatus());
  }

  /**
   * Handles GitOperationException (e.g. if the initialization of the local repository fails or if
   * it fails to open the local repository)
   */
  @ExceptionHandler(GitOperationException.class)
  public ResponseEntity<ApiError> handleGitOperationException(
      GitOperationException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /** Handles JsonParseException (e.g. if (de-) serialization of JSON fails */
  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<ApiError> handleJsonParseError(
      JsonParseException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now());
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UnrecognizedPropertyException.class)
  public ResponseEntity<ApiError> handleUnrecognizedProperty(
      UnrecognizedPropertyException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidPatternException.class)
  public ResponseEntity<ApiError> handleInvalidPattern(
      InvalidPatternException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PatternSyntaxException.class)
  public ResponseEntity<ApiError> handleInvalidPatternSyntax(
      PatternSyntaxException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingHeaderException.class)
  public ResponseEntity<ApiError> handleMissingHeaderException(
      MissingHeaderException ex, HttpServletRequest request) {
    ApiError apiError =
        new ApiError(
            request.getRequestURI(),
            List.of(ex.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now());

    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }
}
