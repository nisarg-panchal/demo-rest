package com.nisarg.spring.demorest.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handler for all controllers.
 * Returns consistent JSON responses with correlationId.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle Bean Validation errors (@Valid)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, Object> body = baseErrorBody(HttpStatus.BAD_REQUEST, "Validation failed");

    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    body.put("errors", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  /**
   * Handle JSR-303 validation errors thrown manually
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, Object> body = baseErrorBody(HttpStatus.BAD_REQUEST, "Constraint violation");
    body.put("errors", ex.getConstraintViolations()
        .stream()
        .collect(HashMap::new,
            (m, v) -> m.put(v.getPropertyPath().toString(), v.getMessage()),
            HashMap::putAll));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  /*@ExceptionHandler(BusinessException.class)
  public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
    Map<String, Object> body = baseErrorBody(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
  }*/

  /**
   * Handle all unexpected exceptions
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAll(Exception ex, WebRequest request) {
    Map<String, Object> body = baseErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  /**
   * Helper to construct consistent response body
   */
  private Map<String, Object> baseErrorBody(HttpStatus status, String message) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", OffsetDateTime.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("correlationId", MDC.get(CorrelationIdFilter.MDC_KEY));
    return body;
  }
}
