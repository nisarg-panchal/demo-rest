package com.nisarg.spring.demorest.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Slf4j
public final class ApiProblemDetailFactory {

  private ApiProblemDetailFactory() {}

  public static ProblemDetail build(
      HttpStatus status,
      URI type,
      String title,
      String detail,
      HttpServletRequest request
  ) {
    ProblemDetail pd = ProblemDetail.forStatus(status);
    pd.setType(type);
    pd.setTitle(title);
    pd.setDetail(detail);
    pd.setInstance(URI.create(request.getRequestURI()));

    // Common enrichment fields (optional but useful)
    pd.setProperty("timestamp", OffsetDateTime.now().toString());

    // Correlation id propagation (if you have one); adjust header name to your standard
    String traceId = request.getHeader("X-Trace-Id");
    if (traceId != null && !traceId.isBlank()) {
      pd.setProperty("traceId", traceId);
    }

    return pd;
  }
}
