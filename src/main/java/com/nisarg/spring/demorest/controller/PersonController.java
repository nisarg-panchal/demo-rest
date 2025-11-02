package com.nisarg.spring.demorest.controller;

import com.nisarg.spring.demorest.entity.Person;
import com.nisarg.spring.demorest.repository.PersonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private final PersonRepository personRepository;

  @GetMapping("/echo")
  public String echo() {
    return "Hello from Person Controller";
  }

  @GetMapping("/{id}")
  public Person findById(@PathVariable UUID id) {
    log.info("Finding Person with id {}", id);
    return personRepository.findById(id).orElse(null);
  }

  @GetMapping
  public List<Person> findAll() {
    log.info("Finding all Persons");
    return personRepository.findAll();
  }

  @PostMapping
  public Person save(@RequestBody Person person) {
    log.info("Saving Person: {}", person);
    return personRepository.save(person);
  }
}
