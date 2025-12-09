package com.nisarg.spring.demorest.controller;

import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ai-chat")
public class AIChatController {

  @GetMapping("/echo")
  public String echo() {
    return "Hello from AI Chat Controller";
  }

  @PostMapping
  public String chat(@RequestBody File image) {
    String result = "Image analysis not implemented.";
    if (image != null) {

      return "You sent an image with name: " + image.getName();
    }
    return result;
  }

  @GetMapping("/ask")
  public String askAI(@RequestParam String question) {
    log.info("Received question: {}", question);
    String response = "This is a placeholder response from the AI model.";
    log.info("Response: {}", response);
    return "You asked: " + question + "\n\nAI Response: " + response;
  }
}
