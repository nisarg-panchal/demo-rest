package com.nisarg.spring.demorest.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalApiExceptionHandler {

  // -----------------------------
  // 400 - Bean Validation Errors
  // -----------------------------
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    ProblemDetail pd = ApiProblemDetailFactory.build(
        HttpStatus.BAD_REQUEST,
        ProblemTypes.VALIDATION,
        "Validation failed",
        "One or more request fields are invalid.",
        request
    );

    List<Map<String, Object>> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(this::toFieldError)
        .toList();

    pd.setProperty("errors", errors);
    return pd;
  }

  private Map<String, Object> toFieldError(FieldError fe) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("field", fe.getField());
    m.put("message",
        StringUtils.hasText(fe.getDefaultMessage()) ? fe.getDefaultMessage() : "Invalid value");
    if (fe.getRejectedValue() != null) {
      // Consider masking sensitive values in production (passwords, tokens, etc.)
      m.put("rejectedValue", fe.getRejectedValue());
    }
    return m;
  }

  // -----------------------------
  // 400 - Bad JSON / unreadable body
  // -----------------------------
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleUnreadableBody(HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    return ApiProblemDetailFactory.build(
        HttpStatus.BAD_REQUEST,
        ProblemTypes.BAD_REQUEST,
        "Malformed request body",
        "Request body is missing or invalid JSON.",
        request
    );
  }

  // -----------------------------
  // 400 - Missing required query param
  // -----------------------------
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex,
      HttpServletRequest request) {
    ProblemDetail pd = ApiProblemDetailFactory.build(
        HttpStatus.BAD_REQUEST,
        ProblemTypes.BAD_REQUEST,
        "Missing request parameter",
        "Required parameter is missing: " + ex.getParameterName(),
        request
    );
    pd.setProperty("parameter", ex.getParameterName());
    return pd;
  }

  // -----------------------------
  // 400 - Wrong type for path/query parameter
  // -----------------------------
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex,
      HttpServletRequest request) {
    String expected =
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
    ProblemDetail pd = ApiProblemDetailFactory.build(
        HttpStatus.BAD_REQUEST,
        ProblemTypes.BAD_REQUEST,
        "Invalid parameter",
        "Parameter '" + ex.getName() + "' must be a valid " + expected + ".",
        request
    );
    pd.setProperty("parameter", ex.getName());
    pd.setProperty("expectedType", expected);
    return pd;
  }

  // -----------------------------
  // 404 - Resource not found (if you throw ResponseStatusException 404)
  // -----------------------------
  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatus(ResponseStatusException ex,
      HttpServletRequest request) {
    // Only normalize 4XX here; let 5XX be handled elsewhere if you have a 500 handler
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null || !status.is4xxClientError()) {
      // fallback to a generic 400 to avoid leaking server error details in this 4xx-only handler
      status = HttpStatus.BAD_REQUEST;
    }

    String detail = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();

    return ApiProblemDetailFactory.build(
        status,
        status == HttpStatus.NOT_FOUND ? ProblemTypes.NOT_FOUND : ProblemTypes.BAD_REQUEST,
        status.getReasonPhrase(),
        detail,
        request
    );
  }

  // -----------------------------
  // 405 - Wrong HTTP method
  // -----------------------------
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
      HttpServletRequest request) {
    ProblemDetail pd = ApiProblemDetailFactory.build(
        HttpStatus.METHOD_NOT_ALLOWED,
        ProblemTypes.METHOD_NOT_ALLOWED,
        "Method not allowed",
        ex.getMessage(),
        request
    );
    if (ex.getSupportedHttpMethods() != null) {
      pd.setProperty("supportedMethods",
          ex.getSupportedHttpMethods().stream().map(HttpMethod::name).toList());
    }
    return pd;
  }

  // -----------------------------
  // 415 - Unsupported Content-Type
  // -----------------------------
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ProblemDetail handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex,
      HttpServletRequest request) {
    ProblemDetail pd = ApiProblemDetailFactory.build(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        ProblemTypes.UNSUPPORTED_MEDIA_TYPE,
        "Unsupported media type",
        ex.getMessage(),
        request
    );
    pd.setProperty("supportedMediaTypes",
        ex.getSupportedMediaTypes().stream().map(Object::toString).toList());
    return pd;
  }

  // -----------------------------
  // 406 - Not Acceptable (Accept header mismatch)
  // -----------------------------
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ProblemDetail handleNotAcceptable(HttpMediaTypeNotAcceptableException ex,
      HttpServletRequest request) {
    return ApiProblemDetailFactory.build(
        HttpStatus.NOT_ACCEPTABLE,
        ProblemTypes.NOT_ACCEPTABLE,
        "Not acceptable",
        ex.getMessage(),
        request
    );
  }

  // -----------------------------
  // Optional: Normalize other Spring 4XX "ErrorResponseException" types
  // (e.g., missing headers, etc.)
  // -----------------------------
  @ExceptionHandler(ErrorResponseException.class)
  public ProblemDetail handleErrorResponseException(ErrorResponseException ex,
      HttpServletRequest request) {
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null) {
      status = HttpStatus.BAD_REQUEST;
    }

    // Only 4XX here
    if (!status.is4xxClientError()) {
      status = HttpStatus.BAD_REQUEST;
    }

    return ApiProblemDetailFactory.build(
        status,
        ProblemTypes.BAD_REQUEST,
        status.getReasonPhrase(),
        ex.getBody().getDetail() != null
            ? ex.getBody().getDetail()
            : status.getReasonPhrase(),
        request
    );
  }
}
