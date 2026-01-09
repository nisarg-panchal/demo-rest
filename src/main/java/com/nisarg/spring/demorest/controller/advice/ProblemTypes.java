package com.nisarg.spring.demorest.controller.advice;

import java.net.URI;

public final class ProblemTypes {
  private ProblemTypes() {}

  public static final URI VALIDATION = URI.create("https://example.com/problems/validation");
  public static final URI BAD_REQUEST = URI.create("https://example.com/problems/bad-request");
  public static final URI NOT_FOUND = URI.create("https://example.com/problems/not-found");
  public static final URI METHOD_NOT_ALLOWED = URI.create("https://example.com/problems/method-not-allowed");
  public static final URI UNSUPPORTED_MEDIA_TYPE = URI.create("https://example.com/problems/unsupported-media-type");
  public static final URI NOT_ACCEPTABLE = URI.create("https://example.com/problems/not-acceptable");
}