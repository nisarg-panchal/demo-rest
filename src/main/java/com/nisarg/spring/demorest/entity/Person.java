package com.nisarg.spring.demorest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.cache.annotation.Cacheable;

@Cacheable(value = "persons")
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
public class Person {

  @Id
  @UuidGenerator
  private UUID id;

  private String name;

  private String email;
}
