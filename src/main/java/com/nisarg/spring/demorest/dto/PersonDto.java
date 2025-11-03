package com.nisarg.spring.demorest.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * DTO for {@link com.nisarg.spring.demorest.entity.Person}
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Value
public class PersonDto implements Serializable {

  UUID id;
  String name;
  String email;
}