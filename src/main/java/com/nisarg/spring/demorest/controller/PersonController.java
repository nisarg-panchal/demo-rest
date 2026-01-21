package com.nisarg.spring.demorest.controller;

import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Person Management", description = "APIs for managing persons in the system")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

  private final PersonService personService;

  @Operation(
      summary = "Echo service",
      description = "Simple health check endpoint that returns a greeting string."
  )
  @ApiResponse(responseCode = "200", description = "Successfully returned greeting")
  @GetMapping("/echo")
  public String echo() {
    return "Hello from Person Controller";
  }

  @Operation(
      summary = "Find person by ID",
      description = "Retrieves a single person's details based on their unique UUID identifier."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Found the person",
          content = @Content(schema = @Schema(implementation = PersonDto.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Person not found with the provided ID",
          content = @Content
      )
  })
  @GetMapping("/{id}")
  public ResponseEntity<PersonDto> findById(
      @Parameter(description = "UUID of the person to be retrieved", required = true)
      @PathVariable UUID id) {
    log.info("Finding Person with id {}", id);
    return ResponseEntity.ok(personService.findById(id));
  }

  @Operation(
      summary = "List all persons",
      description = "Returns a complete list of all persons registered in the system."
  )
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved list of persons",
      content = @Content(schema = @Schema(implementation = PersonDto.class))
  )
  @GetMapping
  public ResponseEntity<List<PersonDto>> findAll() {
    log.info("Finding all Persons");
    return ResponseEntity.ok(personService.findAll());
  }

  @Operation(
      summary = "Create a new person",
      description = "Registers a new person in the system with the provided information."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "Person successfully created",
          content = @Content(schema = @Schema(implementation = PersonDto.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid request payload provided",
          content = @Content
      )
  })
  @PostMapping
  public ResponseEntity<PersonDto> save(
      @Parameter(description = "Details of the person to be created", required = true)
      @Valid @RequestBody PersonDto person) {
    log.info("Saving Person: {}", person);
    PersonDto savedPerson = personService.save(person);
    return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update an existing person",
      description = "Updates the information of an existing person identified by their UUID."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Person successfully updated",
          content = @Content(schema = @Schema(implementation = PersonDto.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Person not found with the provided ID",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid request payload provided",
          content = @Content
      )
  })
  @PutMapping("/{id}")
  public ResponseEntity<PersonDto> update(
      @Parameter(description = "UUID of the person to be updated", required = true)
      @PathVariable UUID id,
      @Parameter(description = "Updated person information", required = true)
      @Valid @RequestBody PersonDto person) {
    log.info("Updating Person with id {} and data: {}", id, person);
    PersonDto updatedPerson = personService.update(id, person);
    return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
  }
}
