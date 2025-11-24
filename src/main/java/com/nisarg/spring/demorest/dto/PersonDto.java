package com.nisarg.spring.demorest.dto;

import com.nisarg.spring.demorest.entity.Person;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link Person}
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PersonDto implements Serializable {

  UUID id;
  String name;
  private String email;
}