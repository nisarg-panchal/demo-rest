package com.nisarg.spring.demorest.repository;

import com.nisarg.spring.demorest.entity.Person;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

}
