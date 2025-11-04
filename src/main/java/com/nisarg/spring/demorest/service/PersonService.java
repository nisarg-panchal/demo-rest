package com.nisarg.spring.demorest.service;

import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.mapper.PersonMapper;
import com.nisarg.spring.demorest.repository.PersonRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepository personRepository;
  private final PersonMapper personMapper;

  @CachePut(value = "persons", key = "#result.id")
  public PersonDto save(PersonDto personDto) {
    MDC.put("SavePerson", personDto.getName() + "-" + personDto.getEmail());
    var person = personRepository.save(personMapper.toEntity(personDto));
    log.info("Saved person with id: {}", person.getId());
    MDC.clear();
    return personMapper.toDto(person);
  }

  @CachePut(value = "persons", key = "#id")
  public PersonDto findById(UUID id) {
    MDC.put("id", id.toString());
    var person = personRepository.findById(id);
    log.info("Found person with id: {}", id);
    MDC.clear();
    return person.map(personMapper::toDto).orElse(null);
  }

  @CachePut(value = "persons")
  public List<PersonDto> findAll() {
    MDC.put("findAll", UUID.randomUUID().toString());
    var persons = personRepository.findAll();
    log.info("Found {} persons", persons.size());
    MDC.clear();
    return persons.stream()
        .map(personMapper::toDto)
        .toList();
  }

  @CacheEvict(value = "persons", key = "#id")
  public void deleteById(UUID id) {
    MDC.put("id", id.toString());
    log.info("Deleting person with id: {}", id);
    personRepository.deleteById(id);
    MDC.clear();
  }

  @CacheEvict(value = "persons", allEntries = true)
  public void deleteAll() {
    log.info("Deleting all persons");
    personRepository.deleteAll();
  }

  public long count() {
    long count = personRepository.count();
    log.info("Total persons count: {}", count);
    return count;
  }

  public boolean existsById(UUID id) {
    boolean exists = personRepository.existsById(id);
    log.info("Person with id {} exists: {}", id, exists);
    return exists;
  }

  public PersonDto update(UUID id, PersonDto personDto) {
    if (!personRepository.existsById(id)) {
      log.warn("Person with id {} not found for update", id);
      return null;
    }
    MDC.put("updatePerson", personDto.getName() + "-" + personDto.getEmail());
    personDto.setId(id);
    var updatedPerson = personRepository.save(personMapper.toEntity(personDto));
    log.info("Updated person with id: {}", id);
    MDC.clear();
    return personMapper.toDto(updatedPerson);
  }
}
