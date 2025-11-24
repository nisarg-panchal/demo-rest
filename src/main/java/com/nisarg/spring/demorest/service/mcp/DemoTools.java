package com.nisarg.spring.demorest.service.mcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoTools {

  @Tool(description = "Say hello to someone")
  public String hello(String name) {
    log.info("hello to someone");
    return "Hello " + name + " from demo-rest MCP!";
  }
}