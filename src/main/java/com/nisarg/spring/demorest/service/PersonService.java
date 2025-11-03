package com.nisarg.spring.demorest.service;

import com.nisarg.spring.demorest.dto.PersonDto;
import com.nisarg.spring.demorest.mapper.PersonMapper;
import com.nisarg.spring.demorest.repository.PersonRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PersonService {
  private final PersonRepository personRepository;
  private final PersonMapper personMapper;

  public PersonDto save(PersonDto personDto) {
    var person = personRepository.save(personMapper.toEntity(personDto));
    log.info("Saved person with id: {}", person.getId());
    return personMapper.toDto(person);
  }

  public PersonDto findById(java.util.UUID id) {
    var person = personRepository.findById(id);
    return person.map(personMapper::toDto).orElse(null);
  }

  public List<PersonDto> findAll() {
    var persons = personRepository.findAll();
    return persons.stream()
        .map(personMapper::toDto)
        .toList();
  }
}
