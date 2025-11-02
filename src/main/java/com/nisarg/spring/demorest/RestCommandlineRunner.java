package com.nisarg.spring.demorest;

import com.nisarg.spring.demorest.entity.Person;
import com.nisarg.spring.demorest.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestCommandlineRunner implements CommandLineRunner {

  private final PersonRepository personRepository;

  @Override
  public void run(String... args) {
    //Add some sample data
    if (personRepository.count() > 0) {
      log.info("Sample data already exists. Skipping data initialization.");
      return;
    }
    personRepository.saveAll(
        java.util.List.of(
            new Person(null, "John Doe", "john.doe@meet.com"),
            new Person(null, "Jane Smith",
                "jane.smith@meetme.com"),
            new Person(null, "Alice Johnson",
                "alice.smith@wonderland.com")
        )
    );
  }
}
