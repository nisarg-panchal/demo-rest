package com.nisarg.spring.demorest.service.mcp;

import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.service.PersonService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonMcpService {

  private final PersonService personService;

  @Autowired
  public PersonMcpService(PersonService personService) {
    this.personService = personService;
  }

  @McpTool(description = "Find a person by UUID")
  public PersonDto findById(
      @McpToolParam(description = "UUID of the person", required = true) UUID id) {
    return personService.findById(id);
  }

  @McpTool(description = "List all persons")
  public List<PersonDto> findAll() {
    return personService.findAll();
  }

  @McpTool(description = "Save a new person")
  public PersonDto save(
      @McpToolParam(description = "Person data to save", required = true) PersonDto personDto) {
    return personService.save(personDto);
  }

  @McpTool(description = "Update an existing person")
  public PersonDto update(
      @McpToolParam(description = "UUID of the person to update", required = true) UUID id,
      @McpToolParam(description = "Updated person data", required = true) PersonDto personDto) {
    return personService.update(id, personDto);
  }

  @McpTool(description = "Delete a person by UUID")
  public void deleteById(
      @McpToolParam(description = "UUID of the person to delete", required = true) UUID id) {
    personService.deleteById(id);
  }

  @McpTool(description = "Delete all persons")
  public void deleteAll() {
    personService.deleteAll();
  }

  @McpTool(description = "Count total number of persons")
  public long count() {
    return personService.count();
  }

  @McpTool(description = "Check if a person exists by UUID")
  public boolean existsById(
      @McpToolParam(description = "UUID of the person to check", required = true) UUID id) {
    return personService.existsById(id);
  }
}
