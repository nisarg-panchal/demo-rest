package com.nisarg.spring.demorest.controller;

import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.service.PersonService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

  private final PersonService personService;

  @GetMapping("/echo")
  public String echo() {
    return "Hello from Person Controller";
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonDto> findById(@PathVariable UUID id) {
    log.info("Finding Person with id {}", id);
    return ResponseEntity.ok(personService.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<PersonDto>> findAll() {
    log.info("Finding all Persons");
    return ResponseEntity.ok(personService.findAll());
  }

  @PostMapping
  public ResponseEntity<PersonDto> save(@RequestBody PersonDto person) {
    log.info("Saving Person: {}", person);
    PersonDto savedPerson = personService.save(person);
    return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
  }
}
